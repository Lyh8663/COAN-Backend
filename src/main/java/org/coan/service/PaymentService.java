package org.coan.service;

import org.coan.mapper.CoinMapper;
import org.coan.mapper.PaymentMapper;
import org.coan.pojo.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.coan.enumeration.ItemTypeEnum.CRYPTO;

@Service
public class PaymentService {
    @Autowired
    private PaymentMapper mapper;

    @Autowired
    private CoinMapper coinMapper;

    public double getQuota(String currency,double amount,int type) {
        if (type == CRYPTO.getType()) {
            double price = coinMapper.findProjectValue(currency);
            return price * amount;
        } else {
            return 10000;
        }
    }
}
