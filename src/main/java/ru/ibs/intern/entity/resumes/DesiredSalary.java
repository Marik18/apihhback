package ru.ibs.intern.entity.resumes;

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
public class DesiredSalary {

    @Id
    @GeneratedValue
    private Long id;

    @CsvBindByName
    private Long amount;

    @ManyToOne
    @JoinColumn(name = "currencyCode")
    @CsvRecurse
    private Currency currency;

    public DesiredSalary(Long amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }
}
