package ru.ibs.intern.service;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ibs.intern.entity.SearchParameters;
import ru.ibs.intern.repositories.AreasRepo;

@Service
public class SearchServiceImpl {

    @Autowired
    AreasRepo areasRepo;

    public String getVacSearchURL (SearchParameters params) {

        String vacanciesUrl = "https://api.hh.ru/vacancies";
        StringBuilder sb = new StringBuilder(vacanciesUrl);

        if (ArrayUtils.isNotEmpty(params.getKeyWords())) {
            sb.append("?text=");
            sb.append(String.join("+OR+", params.getKeyWords()).replace(' ', '+'));
            sb.append("&search_field=name");
        }

        if (params.getAreaName().equals("all")) {
            sb.append("&area=113");
        } else if(params.getAreaName() != null) {
            sb.append("&area=").append(areasRepo.findByAreaName(params.getAreaName()).getAreaId());
        }

        if (params.isOnlyWithSalary())
            sb.append("&only_with_salary=true");

        sb.append("&order_by=relevance");

        if (params.getSalaryFrom() != null)
            sb.append("&********=").append(params.getAreaName()); /// для расширения

        if (params.getSalaryTo() != null)
            sb.append("&&********==").append(params.getSalaryTo()); /// для расширения

        return sb.toString();

    }

    public String getResumeSearchURL(SearchParameters params) {
        //String resumesUrl = "https://api.hh.ru/resumes";
        String resumesFakeUrl = "http://localhost:8090/fakeApiHH/resumes";
        String resumesUrl = "https://42ae178b-dc1f-40ed-a589-6349b8385425.mock.pstmn.io/resumes";


        StringBuilder sb = new StringBuilder(resumesUrl);

        if (ArrayUtils.isNotEmpty(params.getKeyWords())) {
            sb.append("?text=");
            sb.append(String.join("+OR+", params.getKeyWords()).replace(' ', '+'));
            sb.append("&search_field=name");
        }

        if (params.getAreaName().equals("all")) {
            sb.append("&area=113");
        } else if(params.getAreaName() != null) {
            sb.append("&area=").append(areasRepo.findByAreaName(params.getAreaName()).getAreaId());
        }

        sb.append("&order_by=relevance").append("&resume_search_label=only_with_salary");
        sb.append("&experience=between1And3&experience=between3And6&experience=moreThan6");
        return sb.toString();
        //return resumesFakeUrl;
    }


}
