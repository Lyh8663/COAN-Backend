package org.coan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.coan.mapper.CoanUserMapper;
import org.coan.mapper.ExchangeRequestMapper;
import org.coan.mapper.MetaverseGamePropertyMapper;
import org.coan.pojo.CoanUser;
import org.coan.pojo.ExchangeRequest;
import org.coan.pojo.MetaverseGameProperty;
import org.coan.pojo.NormalGameProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;


/**
 * @author Nebula
 * @Description 元宇宙游戏资产操作Service层
 * @date 2023/5/21 18:08
 */
@Service
public class MetaverseGamePropertyService {
    @Autowired
    MetaverseGamePropertyMapper metaverseGamePropertyMapper;
    @Autowired
    CoanUserMapper coanUserMapper;
    @Autowired
    ExchangeRequestMapper exchangeRequestMapper;
    @Autowired(required = false)
    private JavaMailSender sender;

    @Transactional
    public Page<MetaverseGameProperty> getNormalGamePropertiesByType(Integer propertyId, int pageNum, int pageSize) {
        return metaverseGamePropertyMapper.selectPropertyByType(propertyId, new Page<>(pageNum, pageSize));
    }

    @Transactional
    public NormalGameProperty selectPropertyWithName(Integer id){
        return metaverseGamePropertyMapper.selectPropertyWithName(id);
    }

    @Transactional
    public MetaverseGameProperty getNormalGamePropertyById(Integer id){
        return metaverseGamePropertyMapper.selectPropertyById(id);
    }

    @Transactional
    public int updateNormalGamePropertiesOwner(Long ownerId, Integer id) {
        return metaverseGamePropertyMapper.updateOwner(ownerId, id);
    }

    @Transactional
    public boolean exchangeMetaverseGameProperty(Integer id) {
        ExchangeRequest exchangeRequest = exchangeRequestMapper.selectById(id);
        int res=0;
        res+=updateNormalGamePropertiesOwner(exchangeRequest.getReceiverId(),exchangeRequest.getPropertyIdSend());
        res+=updateNormalGamePropertiesOwner(exchangeRequest.getSenderId(),exchangeRequest.getPropertyIdReceive());
        exchangeRequest.setIsSuccess(1);
        res+=exchangeRequestMapper.updateById(exchangeRequest);
        return res == 3 ? true : false;
    }

    @Transactional
    public boolean refuseExchange(Integer id) {
        ExchangeRequest exchangeRequest = exchangeRequestMapper.selectById(id);
        exchangeRequest.setIsSuccess(-1);
        int res=exchangeRequestMapper.updateById(exchangeRequest);
        return res == 1 ? true : false;
    }

    @Transactional
    public boolean requestExchange(Integer id1, Integer id2,String comment) {

        MetaverseGameProperty receiverNormalGameProperty = metaverseGamePropertyMapper.selectPropertyById(id1);
        MetaverseGameProperty senderNormalGameProperty = metaverseGamePropertyMapper.selectPropertyById(id2);
        String receiverPropertyName = metaverseGamePropertyMapper.queryPropertyName(id1);
        String senderPropertyName = metaverseGamePropertyMapper.queryPropertyName(id2);
        CoanUser coanUser = coanUserMapper.selectById(receiverNormalGameProperty.getOwnerId());
        try {
            ExchangeRequest exchangeRequest = new ExchangeRequest(id2,id1, senderNormalGameProperty.getOwnerId(), receiverNormalGameProperty.getOwnerId(), 2,0,comment);
            exchangeRequestMapper.insert(exchangeRequest);
            int id = exchangeRequest.getId();
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,true);
            mimeMessageHelper.setSubject("COAN平台游戏资产交换请求");//发送邮件的标题
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("有人向您发出资产交换申请啦！\n");
            stringBuffer.append("对方愿意用“"+senderPropertyName+"”交换您的“"+receiverPropertyName+"”！\n");
            stringBuffer.append("点击链接同意请求！(非本人请忽略)");
            stringBuffer.append("http://localhost:8100/metaverseGameProperty/exchange/"+id+"\n");
            stringBuffer.append("点此拒绝请求");
            stringBuffer.append("http://localhost:8100/metaverseGameProperty/refuse/"+id+"\n");
            mimeMessageHelper.setText(stringBuffer.toString());
            mimeMessageHelper.setTo(coanUser.getEmail());//收件人
            mimeMessageHelper.setFrom("435851735@qq.com");//寄件人
            sender.send(message);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
