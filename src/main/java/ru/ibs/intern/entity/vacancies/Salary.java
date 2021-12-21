package ru.ibs.intern.entity.vacancies;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvRecurse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ibs.intern.entity.dictionaries.Currency;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
@Data
public class Salary {

    @Id
    @GeneratedValue
    private Long id;

    @CsvBindByName
    private Long salaryFrom;

    @CsvBindByName
    private Long salaryTo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "currencyCode")
    @CsvRecurse
    private Currency currency;


}
