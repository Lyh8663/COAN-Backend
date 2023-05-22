package org.coan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.coan.mapper.CoanUserMapper;
import org.coan.mapper.NormalGamePropertyMapper;
import org.coan.pojo.CoanUser;
import org.coan.pojo.NormalGameProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    @Autowired
    CoanUserMapper coanUserMapper;
    @Autowired(required = false)
    private JavaMailSender sender;

    @Transactional
    public Page<NormalGameProperty> getNormalGamePropertiesByType(Integer propertyId, int pageNum, int pageSize) {
        return normalGamePropertyMapper.selectPropertyByType(propertyId, new Page<>(pageNum, pageSize));
    }

    @Transactional
    public NormalGameProperty getNormalGamePropertyById(Integer id){
        return normalGamePropertyMapper.selectPropertyById(id);
    }

    @Transactional
    public int updateNormalGamePropertiesOwner(Long ownerId, Integer id) {
        return normalGamePropertyMapper.updateOwner(ownerId, id);
    }

    @Transactional
    public boolean exchangeNormalGameProperty(Integer gamePropertyId1, Integer gamePropertyId2) {
        NormalGameProperty normalGameProperty1 = normalGamePropertyMapper.selectPropertyById(gamePropertyId1);
        NormalGameProperty normalGameProperty2 = normalGamePropertyMapper.selectPropertyById(gamePropertyId2);
        int res = 0;
        res += updateNormalGamePropertiesOwner(normalGameProperty1.getOwnerId(), normalGameProperty2.getId());
        res += updateNormalGamePropertiesOwner(normalGameProperty2.getOwnerId(), normalGameProperty1.getId());
        return res == 2 ? true : false;
    }

    public boolean sendEmailToExchanger(Integer id) {

        NormalGameProperty normalGameProperty = normalGamePropertyMapper.selectPropertyById(id);
        CoanUser coanUser = coanUserMapper.selectById(normalGameProperty.getOwnerId());

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("COAN平台游戏资产交换请求");//发送邮件的标题
            message.setText("有人向您发出资产交换申请啦！登录COAN平台查看吧！"+"http://101.34.23.125/coan-front-end/index.html");
            message.setTo(coanUser.getEmail());//收件人
            message.setFrom("435851735@qq.com");//寄件人
            sender.send(message);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
