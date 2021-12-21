package ru.ibs.intern.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchParameters {

    private String areaName;
    private String[] keyWords;
    private Long salaryFrom; //// для расширения,
    private Long salaryTo; //// для расширения,
    private boolean onlyWithSalary = true; // для расширения,
                                           // если нужна будет возможность выбора между
                                            // вакансиями и указанием ЗП и без


}
