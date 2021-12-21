package ru.ibs.intern.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ibs.intern.entity.reports.BarChartByAreas;
import ru.ibs.intern.entity.reports.BarChartByCompOneArea;
import ru.ibs.intern.repositories.BarChartByAreasRepo;
import ru.ibs.intern.repositories.BarChartByCompOneAreaRepo;

import java.util.List;


@Service
public class BarChartService {

    @Autowired
    BarChartByAreasRepo barChartByAreasRepo;

    @Autowired
    BarChartByCompOneAreaRepo barChartByCompOneAreaRepo;

    public List<BarChartByAreas> byAllAreas() {
        return barChartByAreasRepo.byAllAreas();
    }

    public List<BarChartByCompOneArea> byOneArea(String area) {
        return barChartByCompOneAreaRepo.byOneArea(area);
    }


}
