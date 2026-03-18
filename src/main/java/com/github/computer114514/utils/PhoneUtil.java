package com.github.computer114514.utils;

/**
 * 验证手机号工具类
 */
public class PhoneUtil {
    private static final String CHINA_PHONE_REGEX="^1[3-9]\\d{9}$";

    public static boolean isPhoneRight(String phone){
        if(phone==null){
            return false;
        }
        return phone.matches(CHINA_PHONE_REGEX);
    }
}
