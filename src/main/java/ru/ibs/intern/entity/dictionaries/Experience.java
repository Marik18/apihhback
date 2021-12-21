package ru.ibs.intern.entity.dictionaries;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
public class Experience {

    @Id
    @CsvIgnore
    @JsonProperty("id")
    private String experienceId;

    @CsvBindByName
    @JsonProperty("name")
    private String experienceType;

}
