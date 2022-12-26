package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
class RedisUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private RedisTemplate redisTemplate;

    public RedisUserServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Result sendCode(String phone, HttpSession session) {
        String code = RandomUtil.randomNumbers(6);
        redisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY+phone,code,RedisConstants.LOGIN_CODE_TTL);
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {

        String code = (String) redisTemplate.opsForValue().get(loginForm.getPhone());;
        if(!loginForm.getCode().equals(code)) {
            return Result.fail("验证码不正确");
        }
        //查询手机账号
        User user = query().eq("phone", loginForm.getPhone()).one();
        if(user == null) {
            //创建用户
        }
        //1生成token 2转换成hash存储 3保存 4返回
        String token = UUID.randomUUID().toString().replace("-","");
        Map<String, Object> userMap = BeanUtil.beanToMap(user);
        redisTemplate.opsForHash().putAll(RedisConstants.LOGIN_USER_KEY+token,userMap);
        //token 续写
        redisTemplate.expire(RedisConstants.LOGIN_USER_KEY+token,RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
        return Result.ok(token);
    }
}
