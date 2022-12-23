package com.hmdp.service.impl;

import cn.hutool.Hutool;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public Result sendCode(String phone, HttpSession session) {
        if(RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail("手机格式不合法");
        }
        String code = RandomUtil.randomNumbers(6);
        //存入session
        session.setAttribute("code"+phone,code);
        //发送code
        log.info("生成code{}",code);
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        String code = (String) session.getAttribute("code"+loginForm.getCode());
        if(!loginForm.getCode().equals(code)) {
            return Result.fail("验证码不正确");
        }
        //查询手机账号
        User user = query().eq("phone", loginForm.getPhone()).one();
        if(user == null) {
            //创建用户
        }
        session.setAttribute("user",user);
        //用户存在
        return Result.ok();
    }
}
