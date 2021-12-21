package ru.ibs.intern.entity.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BarChartByCompOneArea {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @JsonIgnore
    private String areaName;

    /*private String cityName;

    private String companyName;*/

    private String ordinate;

    @JsonIgnore
    private long numberOfVacancies;

    private long maxSalary;

    private long minSalary;

    private long avgMediana;

}
