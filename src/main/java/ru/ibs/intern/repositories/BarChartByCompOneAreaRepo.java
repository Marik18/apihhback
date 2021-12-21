package ru.ibs.intern.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ibs.intern.entity.reports.BarChartByCompOneArea;

import java.util.List;

@Repository
public interface BarChartByCompOneAreaRepo extends JpaRepository<BarChartByCompOneArea, Long> {

    @Query(value = "select row_number() OVER() as id, areaname, companyname ||' г.'|| cityname as ordinate,\n" +
            "\n" +
            "            count(vacancyname) as numberOfVacancies,\n" +
            "\n" +
            "            + MAX((coalesce (NULLIF(salaryto, 0), salaryfrom) / currencyrate)) as MaxSalary,\n" +
            "\n" +
            "            + MIN((coalesce (NULLIF(salaryfrom, 0), salaryto) / currencyrate)) as MinSalary,\n" +
            "\n" +
            "            percentile_cont(0.5) within group (order by\n" +
            "            (coalesce(salaryfrom / currencyrate, salaryto / currencyrate)\n" +
            "            + coalesce(salaryto / currencyrate, salaryfrom / currencyrate))/2) as avgMediana\n" +
            "\n" +
            "            from areas a join cities on a.areaid = cities.areaid\n" +
            "            join vacancies v on cities.cityid = v.cityid\n" +
            "            join salary s on s.id = v.salary_id\n" +
            "            join currency c on s.currencycode = c.currencycode\n" +
            "\n" +
            "            where areaname = :area\n" +
            "            group by companyname, areaname, cityname\n" +
            "            order by avgMediana", nativeQuery = true)
    List<BarChartByCompOneArea> byOneArea(@Param("area") String area);

}
