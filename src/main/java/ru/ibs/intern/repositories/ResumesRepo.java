package ru.ibs.intern.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ibs.intern.entity.resumes.Resume;

public interface ResumesRepo extends JpaRepository<Resume, Long> {
}
