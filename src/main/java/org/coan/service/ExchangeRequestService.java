package org.coan.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;
import org.coan.mapper.ExchangeRequestMapper;
import org.coan.pojo.ExchangeRequest;
import org.coan.pojo.NormalGameProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Nebula
 * @Description TODO
 * @date 2023/6/1 20:34
 */
@Service
public class ExchangeRequestService {

    @Autowired
    ExchangeRequestMapper exchangeRequestMapper;

    @Transactional
    public Page<ExchangeRequest> selectExchangeRequestBySenderId(Long senderId, int pageNumber,int pageSize){
        return exchangeRequestMapper.selectExchangeRequestByType(senderId,new Page<>(pageNumber,pageSize));
    }

    @Transactional
    public ExchangeRequest queryExchangeRequestById(Integer id){
        return exchangeRequestMapper.queryExchangeRequestById(id);
    }

    @Transactional
    public int insertExchangeRequest(ExchangeRequest exchangeRequest){
        return exchangeRequestMapper.insert(exchangeRequest);
    }
}
