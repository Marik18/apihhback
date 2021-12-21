package ru.ibs.intern.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.ibs.intern.entity.dictionaries.Area;
import ru.ibs.intern.entity.dictionaries.City;
import ru.ibs.intern.entity.dictionaries.Currency;
import ru.ibs.intern.entity.dictionaries.Experience;
import ru.ibs.intern.repositories.AreasRepo;
import ru.ibs.intern.repositories.CitiesRepo;
import ru.ibs.intern.repositories.CurrenciesRepo;
import ru.ibs.intern.repositories.ExperienceRepo;
import ru.ibs.intern.service.interfaces.DictionariesService;

import java.util.List;
import java.util.Objects;

@Service
public class DictionariesServiceImpl implements DictionariesService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CurrenciesRepo currenciesRepo;

    @Autowired
    private AreasRepo areasRepo;

    @Autowired
    private CitiesRepo citiesRepo;

    @Autowired
    private ExperienceRepo experienceRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String hhApiDictionaries = "https://api.hh.ru/dictionaries";
    private static final String hhApiAreas = "https://api.hh.ru/areas";

    public void getCurrentRate() throws JsonProcessingException {

        ResponseEntity<JsonNode> response = restTemplate.exchange(hhApiDictionaries, HttpMethod.GET,
                null, JsonNode.class);
        JsonNode currency = Objects.requireNonNull(response.getBody()).get("currency");

        for (int i = 0; i < currency.size(); i++) {
            currenciesRepo.save(objectMapper.treeToValue(currency.get(i), Currency.class));
        }
    }

    public void getAreas() throws JsonProcessingException {

        ResponseEntity<JsonNode> response = restTemplate.exchange(hhApiAreas, HttpMethod.GET,
                null, JsonNode.class);

        JsonNode areas = Objects.requireNonNull(response.getBody()).get(0).get("areas");

        for (int i = 0; i < areas.size(); i++) {
            areasRepo.save(objectMapper.treeToValue(areas.get(i), Area.class));
        }
    }


    public void getExperience() throws JsonProcessingException {
        ResponseEntity<JsonNode> response = restTemplate.exchange(hhApiDictionaries, HttpMethod.GET,
                null, JsonNode.class);
        JsonNode experience = Objects.requireNonNull(response.getBody()).get("experience");

        for (int i = 0; i < experience.size(); i++) {
            experienceRepo.save(objectMapper.treeToValue(experience.get(i), Experience.class));
        }
    }

    public void getCities() {
        ResponseEntity<JsonNode> response = restTemplate.exchange(hhApiAreas, HttpMethod.GET,
                null, JsonNode.class);
        JsonNode russiaAreas = Objects.requireNonNull(response.getBody()).get(0).get("areas");

        for (int i = 0; i < russiaAreas.size(); i++) {
            JsonNode currentArea = russiaAreas.get(i).get("areas");
            for (int j = 0; j < currentArea.size(); j++) {
                City city = new City(currentArea.get(j).get("id").asLong()
                        , areasRepo.findByAreaId(currentArea.get(j).get("parent_id").asLong())
                        , currentArea.get(j).get("name").asText());
                citiesRepo.save(city);
                //citiesRepo.save(objectMapper.treeToValue(currentArea.get(j), City.class));
            }
        }
        citiesRepo.save(new City(1L, areasRepo.findByAreaId(1L), "Москва"));
        citiesRepo.save(new City(2L, areasRepo.findByAreaId(2L), "Санкт-Петербург"));
    }

    public void createDictionaries() throws JsonProcessingException {
        getCurrentRate();
        getAreas();
        getExperience();
        getCities();
    }

    public List<Area> sendAreas() {
        return areasRepo.findAll();
    }


}
