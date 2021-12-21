package ru.ibs.intern.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ibs.intern.entity.dictionaries.City;


@Repository
public interface CitiesRepo extends JpaRepository<City, Long> {

    City findByCityId(Long id);

}
