package ru.ibs.intern.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.ibs.intern.entity.SearchParameters;
import ru.ibs.intern.service.AuthService;
import ru.ibs.intern.service.CsvService;
import ru.ibs.intern.service.ResumeServiceImpl;
import ru.ibs.intern.service.SearchServiceImpl;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/resumes", consumes = {MediaType.ALL_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ResumeController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ResumeServiceImpl resumeService;

    @Autowired
    SearchServiceImpl searchService;

    @Autowired
    AuthService authService;

    @Autowired
    CsvService csvService;

    private String token="sdsdsd";

    @PostConstruct
    public void authorization() {
        //token = authService.getAuthToken();
    }

    @PostMapping(value = "searchByParams", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String getResumes(@RequestBody SearchParameters params) {
        resumeService.truncateResumeTable();
        String searchURL = searchService.getResumeSearchURL(params);
        resumeService.getResumesFromHH(100, searchURL, token, params);
        return "Got it.";
    }

    @GetMapping(value ="csv", produces = "text/csv")
    public void createCsv(HttpServletResponse response)
            throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        csvService.getCsv(response, "resumes");
    }

}
