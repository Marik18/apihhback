package ru.ibs.intern.entity.vacancies;

import com.opencsv.bean.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ibs.intern.entity.dictionaries.City;
import ru.ibs.intern.entity.dictionaries.Experience;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "vacancies")
public class Vacancy {

    @Id
    @CsvBindByName
    private Long vacancyId;

    @CsvBindByName
    private String vacancyName;

    @ManyToOne
    @JoinColumn(name = "cityId")
    @CsvRecurse
    private City city;

    @CsvBindByName
    private String companyName;

    @OneToOne(cascade = CascadeType.ALL)
    @CsvRecurse
    private Salary salary;

    @ManyToOne(cascade = CascadeType.ALL)
    @CsvRecurse
    private Experience experience;

}
