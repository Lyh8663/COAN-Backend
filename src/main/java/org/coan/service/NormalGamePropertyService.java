package org.coan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.coan.mapper.NormalGamePropertyMapper;
import org.coan.pojo.NormalGameProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Nebula
 * @Description 普通游戏资产操作Service层
 * @date 2023/5/21 18:08
 */
@Service
public class NormalGamePropertyService {
    @Autowired
    NormalGamePropertyMapper normalGamePropertyMapper;

    @Transactional
    public Page<NormalGameProperty> getNormalGamePropertiesByType(Integer propertyId,int pageNum, int pageSize){
        return normalGamePropertyMapper.selectPropertyByType(propertyId,new Page<>(pageNum,pageSize));
    }

    @Transactional
    public int updateNormalGamePropertiesOwner(Long ownerId,Integer id){
        return normalGamePropertyMapper.updateOwner(ownerId, id);
    }

    @Transactional
    public boolean exchangeNormalGameProperty(Integer gamePropertyId1,Integer gamePropertyId2){
        NormalGameProperty normalGameProperty1 = normalGamePropertyMapper.selectPropertyById(gamePropertyId1);
        NormalGameProperty normalGameProperty2 = normalGamePropertyMapper.selectPropertyById(gamePropertyId2);
        int res = 0;
        res+=updateNormalGamePropertiesOwner(normalGameProperty1.getOwnerId(), normalGameProperty2.getId());
        res+=updateNormalGamePropertiesOwner(normalGameProperty2.getOwnerId(), normalGameProperty1.getId());
        return res==2?true:false;
    }
}
