package ru.ibs.intern.entity.dictionaries;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvRecurse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Cities")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class City {

    @Id
    @JsonProperty("id")
    private Long cityId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "areaId")
    @CsvRecurse
    private Area area;

    @CsvBindByName
    private String cityName;

}
