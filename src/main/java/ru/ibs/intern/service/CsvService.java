package ru.ibs.intern.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import ru.ibs.intern.entity.resumes.Resume;
import ru.ibs.intern.entity.vacancies.Vacancy;
import ru.ibs.intern.repositories.ResumesRepo;
import ru.ibs.intern.repositories.VacanciesRepo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class CsvService {

    @Autowired
    VacanciesRepo vacanciesRepo;

    @Autowired
    ResumesRepo resumesRepo;

    public void getCsv(HttpServletResponse response, String type) throws CsvDataTypeMismatchException
                                         , CsvRequiredFieldEmptyException, IOException {
        String csvFileName;
        if ("vacancies".equals(type)) {
            csvFileName = "vacancies.csv";
        } else {
            csvFileName = "resumes.csv";
        }

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + csvFileName + "\"");

        if ("vacancies".equals(type)) {
            StatefulBeanToCsv<Vacancy> writer = new StatefulBeanToCsvBuilder<Vacancy>(response.getWriter())
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator('|')
                    .withOrderedResults(false)
                    .build();
            writer.write(vacanciesRepo.findAll());
        } else {
            StatefulBeanToCsv<Resume> writer = new StatefulBeanToCsvBuilder<Resume>(response.getWriter())
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator('|')
                    .withOrderedResults(false)
                    .build();
            writer.write(resumesRepo.findAll());
        }

    }

}
