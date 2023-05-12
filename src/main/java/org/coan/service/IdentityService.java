package org.coan.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.coan.mapper.CoanUserMapper;
import org.coan.mapper.IdentityMapper;
import org.coan.pojo.Identity;
import org.coan.pojo.vo.IdentityVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import static org.coan.enumeration.IdentityStatusEum.*;

@Service
public class IdentityService {
    @Autowired
    private IdentityMapper mapper;

    @Autowired
    private CoanUserMapper userMapper;

    public Identity getIdentityById(long id) {
        LambdaQueryWrapper<Identity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Identity::getId, id).eq(Identity::getStatus, 1);
        List<Identity> res = mapper.selectList(lqw);
        if (res.isEmpty()) {
            return null;
        } else if (res.size() > 1) {
            throw new RuntimeException("多数字身份");
        }
        return res.get(0);
    }

    public IdentityVo getIdentityVoById(long id) {
        return mapper.getIdentityVoById(id);
    }

    public Boolean createIdentity(Identity identity) {
        LambdaQueryWrapper<Identity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Identity::getId, identity.getId());
        if (mapper.selectCount(lqw) > 0) {
            return false;
        }
        if(userMapper.selectById(identity.getId()) == null) {
            return false;
        }
        identity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        identity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        identity.setStatus(NORMAL.getType());
        mapper.insert(identity);
        return true;
    }

    public boolean deIdentityById(long id) {
        Identity identity = mapper.selectById(id);
        identity.setStatus(LOGOUT.getType());
        return mapper.updateById(identity) > 0;
    }

    public boolean banIdentity(long id) {
        Identity identity = mapper.selectById(id);
        identity.setStatus(BANNED.getType());
        return mapper.updateById(identity) > 0;
    }

    public Boolean delIdentity(long id) {
        Identity identity = mapper.selectById(id);
        if (identity == null || identity.getStatus() != 1) {
            return false;
        }
        mapper.deleteById(identity);
        return true;
    }

    public boolean updateIdentity(long id, Identity identity) {
        Identity oldIdentity = mapper.selectById(id);
        if(identity.getCredit() != oldIdentity.getCredit()) {
            return false;
        }
        BeanUtils.copyProperties(identity, oldIdentity);
        oldIdentity.setId(id);
        oldIdentity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return mapper.updateById(oldIdentity) > 0;
    }

    public boolean changeCreditById(Long id, Double change) {
        Identity credit = getIdentityById(id);
        Double curCredit = credit.getCredit();
        credit.setCredit(change + curCredit);
        return mapper.updateById(credit) > 0;
    }

    public boolean isIdentityExist(long id) {
        Identity identity = mapper.selectById(id);
        return !Objects.isNull(identity);

    }

    public boolean isIdentityStateNormal(long id) {
        Identity identity = mapper.selectById(id);
        if(Objects.isNull(identity)) {
            return false;
        }
        return identity.getStatus() == NORMAL.getType();
    }

    public List<Identity> getAllIdentity() {
        LambdaQueryWrapper<Identity> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Identity::getUpdateTime);
        return mapper.selectList(lqw);
    }


    public boolean checkQuota(Identity identity, double quota) {
        // TODO: check identity quota and save else return false
        if(identity.getCurrentQuota()+quota<= identity.getHistoryQuota()){
            identity.setCurrentQuota((int)(identity.getCurrentQuota()+quota));
            mapper.updateById(identity);
            return true;
        }
        return false;
    }

    public boolean rechargeCash(long id, double cash) {
        Identity identity = mapper.selectById(id);
        if(identity == null) {
            return false;
        }
        identity.setCash(identity.getCash() + cash);
        return mapper.updateById(identity) > 0;

    }
}
