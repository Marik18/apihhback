package ru.ibs.intern.entity.resumes;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvRecurse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ibs.intern.entity.dictionaries.City;
import ru.ibs.intern.entity.vacancies.Salary;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "resumes")
public class Resume {

    @Id
    @CsvBindByName
    private String resumeId;

    @CsvBindByName
    private String title;

    @CsvBindByName
    private String firstName;

    @CsvBindByName
    private String lastName;

    @CsvBindByName
    private String middleName;

    @ManyToOne
    @CsvRecurse
    private City city;

    @OneToOne(cascade = CascadeType.ALL)
    @CsvRecurse
    private DesiredSalary desiredSalary;

    @ManyToOne(cascade=CascadeType.ALL)
    private Contacts contacts;

    private long experience;

}
