package ru.ibs.intern.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ibs.intern.entity.dictionaries.Experience;

@Repository
public interface ExperienceRepo extends JpaRepository<Experience, Long> {

Experience findByExperienceId(String id);

}
