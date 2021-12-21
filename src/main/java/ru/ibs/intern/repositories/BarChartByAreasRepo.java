package ru.ibs.intern.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ibs.intern.entity.reports.BarChartByAreas;

import java.util.List;

@Repository
public interface BarChartByAreasRepo extends JpaRepository<BarChartByAreas, Long> {

    @Query(value = "select tbl1.areaname, numberOfVacancies, minSalary, maxSalary, mediana from\n" +
            "(select areaname, count(vacancyname) as numberOfVacancies,\n" +
            "       MIN((coalesce (NULLIF(salaryfrom, 0), salaryto) / currencyrate)) as minSalary,\n" +
            "       MAX((coalesce (NULLIF(salaryto, 0), salaryfrom) / currencyrate)) as maxSalary\n" +
            "from vacancies join cities c on c.cityid = vacancies.cityid\n" +
            "               join salary s on s.id = vacancies.salary_id\n" +
            "               join currency c2 on c2.currencycode = s.currencycode\n" +
            "               join areas a on a.areaid = c.areaid\n" +
            "group by areaname) as tbl1 join\n" +
            "\n" +
            "(select areaname, percentile_cont(0.5) within group ( order by data) as mediana\n" +
            "from\n" +
            "    (select salaryfrom as data, areaname\n" +
            "     from vacancies join cities c on c.cityid = vacancies.cityid\n" +
            "                    join areas a on a.areaid = c.areaid\n" +
            "                    join salary s on s.id = vacancies.salary_id\n" +
            "     where salaryfrom != 0\n" +
            "     union all\n" +
            "     select salaryto, areaname\n" +
            "     from vacancies join cities c on c.cityid = vacancies.cityid\n" +
            "                    join areas a on a.areaid = c.areaid\n" +
            "                    join salary s on s.id = vacancies.salary_id\n" +
            "     where salaryto != 0) as bvc\n" +
            "group by areaname) as tbl2 on tbl1.areaname=tbl2.areaname", nativeQuery = true)
    List<BarChartByAreas> byAllAreas();
}
