package ru.ibs.intern.entity.resumes;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
@Data
public class Contacts {

    @Id
    @GeneratedValue
    private Long id;

    @CsvBindByName
    private String email;

    @CsvBindByName
    private String mobilePhoneNumber;

    @CsvBindByName
    private String homePhoneNumber;

    @CsvBindByName
    private String workPhoneNumber;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Resume> resume;

    public Contacts(String email, String mobilePhoneNumber, String homePhoneNumber, String workPhoneNumber, List<Resume> resume) {
        this.email = email;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.homePhoneNumber = homePhoneNumber;
        this.workPhoneNumber = workPhoneNumber;
        this.resume = resume;
    }
}
