package ru.ibs.intern.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.ibs.intern.entity.dictionaries.Area;
import ru.ibs.intern.entity.SearchParameters;
import ru.ibs.intern.entity.reports.BarChartByAreas;
import ru.ibs.intern.entity.reports.BarChartByCompOneArea;
import ru.ibs.intern.entity.vacancies.Vacancy;
import ru.ibs.intern.repositories.VacanciesRepo;
import ru.ibs.intern.service.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/vacancies", consumes = {MediaType.ALL_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
public class VacancyController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    DictionariesServiceImpl dictionary;

    @Autowired
    VacanciesServiceImpl vacanciesService;

    @Autowired
    SearchServiceImpl searchService;

    @Autowired
    CsvService csvService;

    @Autowired
    BarChartService barChartService;

    @Autowired
    VacanciesRepo vacanciesRepo;

    @PostConstruct
    public void postConstruct() throws JsonProcessingException {
        //dictionary.createDictionaries();
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public List<BarChartByAreas> getVacancies(@RequestBody SearchParameters params) {
        vacanciesService.truncateVacTable();
        String searchURL = searchService.getVacSearchURL(params);
        vacanciesService.getVacanciesFromHH(100, searchURL);
        return barChartService.byAllAreas();
    }

    @GetMapping(value ="csv", produces = "text/csv")
    public void createCsv(HttpServletResponse response)
                                    throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        csvService.getCsv(response, "vacancies");
    }

    // отдать фронту словарь регионов
    @GetMapping(value ="areas", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Area> sendAreas() {
        return dictionary.sendAreas();
    }

    // отдать фронту данные для гистограммы
    @GetMapping(value ="reportAll", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<BarChartByAreas> sendAll() {
        return barChartService.byAllAreas();
    }

    @GetMapping(value ="reportOne", consumes = {MediaType.APPLICATION_JSON_VALUE},
                                       produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<BarChartByCompOneArea> sendOne(@RequestBody JsonNode area) {
        return barChartService.byOneArea(area.get("areaName").asText());
    }

    @GetMapping(value ="csv1", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vacancy> sendCsv() {
        return vacanciesRepo.findAll();
    }

}
