package ru.ibs.intern.entity.dictionaries;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Areas")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Area {

    @Id
    @JsonProperty("id")
    private Long areaId;

    @JsonProperty("name")
    @CsvBindByName
    private String areaName;

}
