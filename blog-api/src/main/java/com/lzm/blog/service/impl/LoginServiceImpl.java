package com.lzm.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.lzm.blog.dao.pojo.SysUser;
import com.lzm.blog.service.LoginService;
import com.lzm.blog.service.SysUserService;
import com.lzm.blog.utils.JWTUtils;
import com.lzm.blog.vo.ErrorCode;
import com.lzm.blog.vo.Result;
import com.lzm.blog.vo.params.LoginPrams;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Transactional
@Service
public class LoginServiceImpl implements LoginService {

    public static final String salt = "mszlu!@#";

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 1. check if parameters are legal
     * 2. select sysUser by account and password
     * 3. if user not exists, fail
     * 4. else create token and return
     * 5. put token into redis
     * @param loginPrams
     * @return JWT Token
     */
    @Override
    public Result login(LoginPrams loginPrams) {
        String account = loginPrams.getAccount();
        String password = loginPrams.getPassword();

        if (StringUtils.isBlank(account) || StringUtils.isBlank(password) ) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }

        String pwd = DigestUtils.md5Hex(password + salt);
        SysUser sysUser = sysUserService.findUser(account, pwd);
        if (sysUser == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("Token_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null) {
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("Token_" + token);
        if (StringUtils.isBlank(userJson)) {
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("Token_" + token);

        return Result.success(null);
    }


    /**
     * 1. params valid?
     * 2. account already exist?
     * 3. register
     * 4. create token
     * 5. store in redis and return
     * 6. add transaction if anything wrong callback
     * @param loginPrams
     * @return
     */
    @Override
    public Result register(LoginPrams loginPrams) {
        String account = loginPrams.getAccount();
        String password = loginPrams.getPassword();
        String nickname = loginPrams.getNickname();

        if (StringUtils.isBlank(account)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(nickname)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser = sysUserService.findUserByAccount(account);
        if (sysUser != null) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+salt));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        sysUserService.save(sysUser);
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("Token_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
        return Result.success(token);
    }
}
