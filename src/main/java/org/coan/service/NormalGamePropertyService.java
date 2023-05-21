package org.coan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.coan.mapper.NormalGamePropertyMapper;
import org.coan.pojo.NormalGameProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Nebula
 * @Description TODO
 * @date 2023/5/21 18:08
 */
@Service
public class NormalGamePropertyService {
    @Autowired
    NormalGamePropertyMapper normalGamePropertyMapper;

    public Page<NormalGameProperty> getNormalGamePropertiesByType(Integer propertyId,int pageNum, int pageSize){
        return normalGamePropertyMapper.selectPropertyByType(propertyId,new Page<>(pageNum,pageSize));
    }
}
