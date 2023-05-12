package org.coan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.coan.mapper.SectorMapper;
import org.coan.pojo.Sector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SectorService {
    @Autowired
    private SectorMapper mapper;

    public List<String> getSectorList() {
        QueryWrapper<Sector> qw = new QueryWrapper<>();
        qw.select("distinct sector_name");
        List<String> res = new ArrayList<>();
        for (Sector sector : mapper.selectList(qw)) {
            res.add(sector.getSectorName());
        }
        return res;
    }

}
