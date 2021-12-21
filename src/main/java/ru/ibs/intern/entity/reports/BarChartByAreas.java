package ru.ibs.intern.entity.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BarChartByAreas {

    @Id
    private String areaName;

    @JsonIgnore
    private long numberOfVacancies;

    private long minSalary;

    private long maxSalary;

    private long mediana;

}
