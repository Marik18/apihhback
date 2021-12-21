package ru.ibs.intern.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import ru.ibs.intern.entity.dictionaries.Area;


@Repository
public interface AreasRepo extends JpaRepository<Area, Long> {

    Area findByAreaName(String name);

    Area findByAreaId(Long id);

}
