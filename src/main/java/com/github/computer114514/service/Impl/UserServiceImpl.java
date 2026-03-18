package com.github.computer114514.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.computer114514.constant.UserConstant;
import com.github.computer114514.domain.dto.UserDTO;
import com.github.computer114514.domain.dto.loginFromDTO;
import com.github.computer114514.domain.enity.Cat;
import com.github.computer114514.domain.enity.User;
import com.github.computer114514.domain.vo.LoginVO;
import com.github.computer114514.mapper.UserMapper;
import com.github.computer114514.domain.enity.Result;
import com.github.computer114514.service.UserService;
import com.github.computer114514.utils.PhoneUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.github.computer114514.constant.UserConstant.REDIS_USER_LOGIN_TOKEN_DDL;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService  {
    @Autowired
    UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    /**
     * 通过用户名找user
     */
    @Override
    public User findUserByUsername(String username) {
       return  userMapper.findUserByUsername(username);
    }

    /**
     * 发射验证码方法
     */
    @Override
    public Result<Void> sendCode(@RequestParam String phone) {
        //1，检查手机号格式。
        boolean phoneRight = PhoneUtil.isPhoneRight(phone);
        //不对返回
        if(!phoneRight){
            return Result.error("手机号格式不对");
        }
        //2,生成验证码。
        String code = RandomUtil.randomNumbers(6);
        //3,保存到redis。
        stringRedisTemplate.opsForValue().set(phone,code);
        //4,发送验证码。
        log.info("验证码是{}", code);

        return Result.success();
    }

    /**
     * 通过验证码登录方法
     */

    @Override
    public Result<LoginVO> codeLogin(loginFromDTO loginFromDTO) {
        //前端给你发送了phone和code。
        String phone = loginFromDTO.getPhone();
        String code = loginFromDTO.getCode();
        //0,校验手机号正确性
        boolean phoneRight = PhoneUtil.isPhoneRight(phone);
        //不对返回
        if(!phoneRight){
            return Result.error("手机号格式不对");
        }
        //1,在redis当中查找有没有对应手机号的验证码。
        String trueCode = stringRedisTemplate.opsForValue().get(phone);
        //2,有就验证是否正确。
        if(trueCode==null||trueCode.isEmpty()||!trueCode.equals(code)){
            return Result.error("验证码错误!");
        }
        //3,验证码正确就检查在mysql有没有这个用户。
        User user = this.getUserByPhone(phone);
        //4,没有就创建用户，并且生成token到redis中作为凭证。
        if(user==null){
            boolean save = this.save(user);
            if(save){
                user=this.getUserByPhone(phone);
            }
        }
        //5,生成对应token并且保存到redis作为登录凭证。
        String token = UUID.randomUUID().toString().replace("-","");
        String key= UserConstant.REDIS_USER_LOGIN_TOKEN_KEY +token;

        //5.1user转换为不敏感的userDTO才能存进去(hash)。(里面不用放token)
        UserDTO userDTO = new UserDTO();
        if(user!=null){
            BeanUtils.copyProperties(user,userDTO);
        }else{
            return Result.error("用户数据异常");
        }
        //5.2还要转一下map?
        // 5.3存入
        // 5.4设立有效期。
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
        CopyOptions
                .create()
                .setIgnoreNullValue(true)
                .setFieldValueEditor((fieldName,FieldValue)-> FieldValue.toString()));
        stringRedisTemplate.opsForHash().putAll(key,userMap);
        stringRedisTemplate.expire(key,REDIS_USER_LOGIN_TOKEN_DDL,TimeUnit.MINUTES);
        //5.5删除存的验证码。
        stringRedisTemplate.opsForValue().getAndDelete(phone);
        //6,凭借loginVO,返回token。
        LoginVO loginVO = new LoginVO(token);
        return Result.success(loginVO);
    }
    public User getUserByPhone(String phone){
        //创建wrapper包装器。
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        //2,构造条件:phone字段等于传入的phone
        //2,要知道这个phone对应那个字段，才可以查。会识别phone列
        wrapper.eq(User::getPhone,phone);
        //构建条件;eq,还有大于小于等于。。。。。

        //3,执行查询(slelectone 返回一条)
        return this.getOne(wrapper);
        //用这个构造器查一个
    }
    public Result<LoginVO> passwordLogin(loginFromDTO loginFromDTO){
        //1,密码登录,前端给了账号密码。
        String username = loginFromDTO.getUsername();
        String password = loginFromDTO.getPassword();
        //2,根据username查询用户
        User user = findUserByUsername(username);
        if(user==null){
            return Result.error("账号不存在!");
        }
        //3,存在的话就保存到redis
        String token = UUID.randomUUID().toString().replace("-","");
        String key= UserConstant.REDIS_USER_LOGIN_TOKEN_KEY +token;
        UserDTO userDTO = new UserDTO();
        BeanUtil.copyProperties(user,userDTO);
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions
                        .create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName,FieldValue)-> FieldValue.toString()));
        stringRedisTemplate.opsForHash().putAll(key,userMap);
        stringRedisTemplate.expire(key,REDIS_USER_LOGIN_TOKEN_DDL,TimeUnit.MINUTES);
        //4,凭借loginVO,返回token。
        LoginVO loginVO = new LoginVO(token);
        return Result.success(loginVO);
    }

}
