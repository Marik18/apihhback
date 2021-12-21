package ru.ibs.intern.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.ibs.intern.entity.vacancies.Salary;
import ru.ibs.intern.entity.vacancies.Vacancy;
import ru.ibs.intern.repositories.CitiesRepo;
import ru.ibs.intern.repositories.CurrenciesRepo;
import ru.ibs.intern.repositories.ExperienceRepo;
import ru.ibs.intern.repositories.VacanciesRepo;
import ru.ibs.intern.service.interfaces.VacanciesService;

import java.util.List;
import java.util.Objects;


@Service
public class VacanciesServiceImpl implements VacanciesService {

    private final String VACANCIES_URL = "https://api.hh.ru/vacancies";

    @Autowired
    private VacanciesRepo vacanciesRepo;

    @Autowired
    private CitiesRepo citiesRepo;

    @Autowired
    private ExperienceRepo experienceRepo;

    @Autowired
    private CurrenciesRepo currenciesRepo;

    @Autowired
    ObjectMapper objectMapper;

    private final static RestTemplate restTemplate = new RestTemplate();

    public void getVacanciesFromHH(int requiredVacanciesNumber, String searchURL) {

        int count = 0;
        int pages = Objects.requireNonNull(restTemplate.exchange(searchURL, HttpMethod.GET
                , null, JsonNode.class).getBody()).get("pages").asInt();
        int found = Objects.requireNonNull(restTemplate.exchange(searchURL, HttpMethod.GET
                , null, JsonNode.class).getBody()).get("found").asInt();
        for (int i = 0; i < pages; i++) {
            JsonNode currentVacanciesList = restTemplate.exchange(searchURL + "&page=" + i, HttpMethod.GET
                    , null, JsonNode.class).getBody();
            for (int j = 0; j < 20; j++) {
                Vacancy vacancy = new Vacancy();
                assert currentVacanciesList != null;
                writeData(currentVacanciesList.get("items").get(j), vacancy);
                vacanciesRepo.save(vacancy);
                count++;
                if(count == requiredVacanciesNumber || count == found) break;
            }
            if(count == requiredVacanciesNumber || count == found) break;
        }
        System.out.println("adding exp");
        addExperience();
    }

    private void writeData(JsonNode currentHHVacancy, Vacancy vacancy) {

        if (currentHHVacancy.has("id"))
            vacancy.setVacancyId(currentHHVacancy.get("id").asLong());

        if (currentHHVacancy.has("name")){
            vacancy.setVacancyName(currentHHVacancy.get("name").asText());
        }

        if (currentHHVacancy.has("area")) {
            if (currentHHVacancy.get("area").has("id")) {
                vacancy.setCity(citiesRepo.findByCityId(currentHHVacancy.get("area").get("id").asLong()));
            }
        }

        if (currentHHVacancy.has("employer")) {
            if (currentHHVacancy.get("employer").has("name")) {
                vacancy.setCompanyName(currentHHVacancy.get("employer").get("name").asText());
            }
        }

        addSalary(currentHHVacancy, vacancy);
    }

    private void addSalary(JsonNode currentHHVacancy, Vacancy vacancy) {

        if (currentHHVacancy.has("salary")) {
            Salary salary = new Salary();
            if (currentHHVacancy.get("salary").has("from")) {
                salary.setSalaryFrom(currentHHVacancy.get("salary").get("from").asLong());
            }
            if (currentHHVacancy.get("salary").has("to")) {
                salary.setSalaryTo(currentHHVacancy.get("salary").get("to").asLong());
            }
            if (currentHHVacancy.get("salary").has("currency")) {
                salary.setCurrency(currenciesRepo.findByCurrencyCode(currentHHVacancy.get("salary").get("currency")
                                                                                    .asText()));
            }
            vacancy.setSalary(salary);
        }
    }

    private void addExperience(){

        List<Vacancy> vacancyList = vacanciesRepo.findAll();

        for (Vacancy vacancy: vacancyList) {
            JsonNode currentVacancyExp = Objects.requireNonNull(restTemplate.exchange(VACANCIES_URL + "/"
                            + vacancy.getVacancyId(), HttpMethod.GET, null, JsonNode.class)
                            .getBody()).get("experience");
            if(currentVacancyExp.has("id")) {
                vacancy.setExperience(experienceRepo.findByExperienceId(currentVacancyExp.get("id").asText()));
            }
            vacanciesRepo.save(vacancy);
        }
    }

    public void truncateVacTable() {
        vacanciesRepo.deleteAll();
    }


}
