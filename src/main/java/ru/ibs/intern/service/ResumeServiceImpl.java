package ru.ibs.intern.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.ibs.intern.entity.SearchParameters;
import ru.ibs.intern.entity.resumes.Contacts;
import ru.ibs.intern.entity.resumes.DesiredSalary;
import ru.ibs.intern.entity.resumes.Resume;
import ru.ibs.intern.entity.vacancies.Vacancy;
import ru.ibs.intern.repositories.CitiesRepo;
import ru.ibs.intern.repositories.CurrenciesRepo;
import ru.ibs.intern.repositories.ResumesRepo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.time.temporal.ChronoUnit;


@Service
public class ResumeServiceImpl {

    private final String HH_RESUME_URL = "https://api.hh.ru/resumes";

    private final String MOCK_RESUME_URL = "https://42ae178b-dc1f-40ed-a589-6349b8385425.mock.pstmn.io/resumes";

    private final String FAKE_RESUME_URL = "http://localhost:8090/fakeApiHH/resumes";

    @Autowired
    ResumesRepo resumesRepo;

    @Autowired
    CitiesRepo citiesRepo;

    @Autowired
    CurrenciesRepo currenciesRepo;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestTemplate restTemplate;

    public void getResumesFromHH(int requiredResumesNumber, String searchURL, String token, SearchParameters params) {

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setBearerAuth(token);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);

        int count = 0;

        int pages = Objects.requireNonNull(restTemplate.exchange(searchURL, HttpMethod.GET
                , requestEntity, JsonNode.class).getBody()).get("pages").asInt();

        int found = Objects.requireNonNull(restTemplate.exchange(searchURL, HttpMethod.GET
                , requestEntity, JsonNode.class).getBody()).get("found").asInt();

        for (int i = 0; i < pages; i++) {

            JsonNode currentResumesList;

            if (searchURL.contains("fakeApiHH")) {
                currentResumesList = restTemplate.exchange(searchURL + "?page=" + i, HttpMethod.GET
                        , requestEntity, JsonNode.class).getBody();
            } else if (searchURL.contains("mock")) {
                currentResumesList = restTemplate.exchange(searchURL, HttpMethod.GET
                    , requestEntity, JsonNode.class).getBody();
            } else {
                currentResumesList = restTemplate.exchange(searchURL + "&page=" + i, HttpMethod.GET
                        , requestEntity, JsonNode.class).getBody();
            }

            for (int j = 0; j < 20; j++) {
                Resume resume = new Resume();
                assert currentResumesList != null;
                writeData(currentResumesList.get("items").get(j), resume);
                resumesRepo.save(resume);
                count++;
                if(count == requiredResumesNumber || count == found) break;
            }
            if(count == requiredResumesNumber || count == found) break;
        }
        addExperience(token, params, searchURL);
    }

    private void writeData(JsonNode currentHHResume, Resume resume) {

        if (currentHHResume.has("title"))
            resume.setTitle(currentHHResume.get("title").asText());

        if (currentHHResume.has("id"))
            resume.setResumeId(currentHHResume.get("id").asText());

        if (currentHHResume.has("first_name")){
            resume.setFirstName(currentHHResume.get("first_name").asText());
        }

        if (currentHHResume.has("last_name")){
            resume.setLastName(currentHHResume.get("last_name").asText());
        }

        if (currentHHResume.has("middle_name")){
            resume.setMiddleName(currentHHResume.get("middle_name").asText());
        }

        if (currentHHResume.has("area")) {
            if (currentHHResume.get("area").has("id")) {
                resume.setCity(citiesRepo.findByCityId(currentHHResume.get("area").get("id").asLong()));
            }
        }

        if (currentHHResume.has("salary")) {
            if (currentHHResume.get("salary").has("amount")) {
                resume.setDesiredSalary(new DesiredSalary(currentHHResume.get("salary").get("amount").asLong()
                        , currenciesRepo.findByCurrencyCode(currentHHResume.get("salary").get("currency").asText())));
            }
        }
        addContacts(currentHHResume, resume);
    }

    private void addExperience(String token, SearchParameters params, String searchURL) {

        List<Resume> resumeList = resumesRepo.findAll();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setBearerAuth(token);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);

        for(Resume resume: resumeList) {
            JsonNode currentHHResume;

            if (searchURL.contains("fakeApiHH")) {
                currentHHResume = Objects.requireNonNull(restTemplate.exchange(FAKE_RESUME_URL + "/"
                        + resume.getResumeId(), HttpMethod.GET, requestEntity, JsonNode.class)
                        .getBody());
            } else if (searchURL.contains("mock")) {
                currentHHResume = Objects.requireNonNull(restTemplate.exchange(MOCK_RESUME_URL + "/"
                        + resume.getResumeId(), HttpMethod.GET, requestEntity, JsonNode.class)
                        .getBody());
            } else {
                currentHHResume = Objects.requireNonNull(restTemplate.exchange(HH_RESUME_URL + "/"
                        + resume.getResumeId(), HttpMethod.GET, requestEntity, JsonNode.class)
                        .getBody());
            }

            if (currentHHResume.has("experience") && !currentHHResume.get("experience").isNull()) {
                String[] keyWords = params.getKeyWords();
                long experience = 0;
                for (int i = 0; i < currentHHResume.get("experience").size(); i++) {
                    for(String keyWord : keyWords) {
                        JsonNode exp = currentHHResume.get("experience").get(i);
                        boolean cond1 = false;
                        if(exp.has("position")) {
                            cond1= exp.get("position").asText().toLowerCase().contains(keyWord.toLowerCase());
                        }
                        boolean cond2 = false;
                        if (exp.has("description")) {
                            cond2 = exp.get("description").asText().toLowerCase().contains(keyWord.toLowerCase());
                        }
                        if (cond1 || cond2) {
                            String dateStartStr = exp.get("start").asText();
                            LocalDate dateStart = LocalDate.parse(dateStartStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            LocalDate dateEnd;
                            if (!exp.get("end").isNull()) {
                                String dateEndStr = exp.get("end").asText();
                                dateEnd = LocalDate.parse(dateEndStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            } else {
                                dateEnd = LocalDate.now();
                            }
                            experience += ChronoUnit.MONTHS.between(dateStart, dateEnd);
                        }
                    }
                }
                resume.setExperience(experience);
            }
            resumesRepo.save(resume);
        }
    }

    /*
    В резюме обязательно должен быть указан email (и он может быть только один).
    В резюме должен быть указан хотя бы один телефон, причём можно указывать только один телефон каждого типа.
    Комментарий можно указывать только для телефонов, для email комментарий не сохранится.
    В контакте типа 'email' value — это email, а для телефонов — объект.
        Обязательно указать либо телефон полностью в поле formatted,
    либо все три части телефона по отдельности в полях country, city и number.
    Если указано и то, и то, используются данные из разделенного телефона.
    В поле formatted допустимо указание дополнительных символов: пробелов, скобок, дефисов.
    В раздельных полях допустимы только цифры.
    */

    private void addContacts(JsonNode currentHHResume, Resume resume) {
        Contacts contacts = new Contacts();
        if(currentHHResume.has("contact")){
            for (int i = 0; i < currentHHResume.get("contact").size(); i++) {
                JsonNode currentContact = currentHHResume.get("contact").get(i);
                if (currentContact.get("type").get("id").asText().equals("email")) {
                    contacts.setEmail(currentContact.get("value").asText());
                } else {
                    addPhoneNumbers(currentContact, contacts);
                }
            }
        }
        resume.setContacts(contacts);
    }

    private void addPhoneNumbers(JsonNode currentContact, Contacts contacts) {
        String phoneNumber = createPhoneNumber(currentContact);
        switch (currentContact.get("type").get("id").asText()) {
            case "cell":
                contacts.setMobilePhoneNumber(phoneNumber);
                break;
            case "work":
                contacts.setWorkPhoneNumber(phoneNumber);
                break;
            case "home":
                contacts.setHomePhoneNumber(phoneNumber);
                break;
        }
    }

    private String createPhoneNumber(JsonNode currentContact) {

        if (currentContact.get("value").has("formatted")) {
            return currentContact.get("value").get("formatted").asText();
        } else {
            String numberPart;
            if(currentContact.get("value").get("number").asText().contains("-")) {
                numberPart = currentContact.get("value").get("number").asText();
            } else {
                StringBuilder sb = new StringBuilder(currentContact.get("value").get("number").asText());
                sb.insert(3,'-').insert(6, '-');
                numberPart = sb.toString();
            }

            return currentContact.get("value").get("country").asText() + "("
                    + currentContact.get("value").get("city").asText() + ")" + numberPart;
        }
    }

    public void truncateResumeTable() {
        resumesRepo.deleteAll();
    }

}
