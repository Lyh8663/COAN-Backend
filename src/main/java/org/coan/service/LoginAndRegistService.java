package org.coan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.coan.mapper.CoanUserMapper;
import org.coan.pojo.Auth;
import org.coan.pojo.CoanUser;
import org.coan.util.RedisCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
public class LoginAndRegistService {

    @Autowired
    private CoanUserMapper coanUserMapper;

    //引入Spring mail依赖之后，会自动装配到IOC容器中
    @Autowired(required = false)
    private JavaMailSender sender;

    //判断邮箱是否正确
    public boolean isEmailFormLegal(String email){
        Pattern partern = Pattern.compile("[a-zA-Z0-9]+[\\.]{0,1}[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]+");
        boolean isMatch = partern.matcher(email).matches();
        return isMatch;
    }

    //判断是否被注册过
    public boolean isEmailLegal(String email) {//测试通过
        //邮箱格式正则表达式
        String format = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
        if (!email.matches(format)) {//不符合规范
            return false;
        }
        //执行到这说明邮箱格式正确
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("email", email);
        if (coanUserMapper.selectOne(wrapper) != null) {//注册过了
            return false;
        }
        return true;
    }

    //随机生成6位验证码
    public String createCode() {//测试通过
        String[] beforeShuffle = new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
                "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z"};
        List list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        String result = afterShuffle.substring(3, 9);
        System.out.println(result);
        return result;
    }

    //将生成的邮箱验证码发往指定的邮箱
    public void sendEmailCode(String email, String code) {//测试通过
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("COAN平台注册邮件");//发送邮件的标题
        message.setText("您的验证码为：" + code + ",验证码仅在5分钟内有效。");
        message.setTo(email);//收件人
        message.setFrom("435851735@qq.com");//寄件人
        sender.send(message);
    }

    public Auth authentice(Auth auth) {
        CoanUser user = coanUserMapper.selectById(auth.getUserId());
        if(user == null) {
            return null;
        }
        user.setRealName(auth.getRealName());
        user.setIdCode(auth.getIdCode());
        user.setIdType(auth.getIdType());
        if(coanUserMapper.updateById(user) > 0) {
            return auth;
        }
        return null;
    }

    //将邮箱对应的验证码存入redis缓存中
    public void saveCode2Redis(String email, String code) {//测试通过
        RedisTemplate redisTemplate = RedisCacheUtil.redis;
        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
    }

    //查找邮箱是否存在于redis缓存中
    public boolean isEmailExist(String email) {//测试通过
        RedisTemplate redisTemplate = RedisCacheUtil.redis;
        return redisTemplate.hasKey(email);
    }

    //比对邮箱验证码是否相同
    public boolean isEmailCodeSame(String email, String code) {//测试通过
        RedisTemplate redisTemplate = RedisCacheUtil.redis;
        String codeInRedis = (String) redisTemplate.opsForValue().get(email);
        if (code.equals(codeInRedis)) {
            return true;
        } else {
            return false;
        }
    }

    //查询mysql，判断邮箱是否已经被注册，true表示已被注册，false表示未被注册
    public boolean isEmailRegisted(String email) {//测试通过
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("email", email);
        if (coanUserMapper.selectOne(wrapper) != null) {
            return true;
        }
        return false;
    }

    //邮箱验证第一次登录，创建用户表信息
    public int createUserInfo(String email) {//测试通过
        CoanUser coanUser = new CoanUser();
        coanUser.setEmail(email);
        coanUser.setUsername("e_" + email);
        return coanUserMapper.insert(coanUser);
    }

    //通过邮箱查找用户信息并返回
    public CoanUser selectUserByEmail(String email) {//测试通过
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("email", email);
        return coanUserMapper.selectOne(wrapper);
    }
}
