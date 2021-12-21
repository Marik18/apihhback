package ru.ibs.intern.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ibs.intern.entity.vacancies.Vacancy;

import java.util.List;

@Repository
public interface VacanciesRepo extends JpaRepository<Vacancy, Long> {

    List<Vacancy> findVacanciesByCity_CityName(String cityName);

}
