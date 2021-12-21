package ru.ibs.intern.entity.dictionaries;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Currency {

    @Id
    @Column
    @JsonProperty("code")
    private String currencyCode;

    @JsonProperty("rate")
    private Double currencyRate;

    @JsonProperty("name")
    @CsvBindByName
    private String currencyName;

}
