package com.limbo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码加密
 * @author limbo
 */

public class GeneratePassword {
    public static String generatePassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
    public static void main(String[] args) {
        System.out.println(generatePassword("pwd"));
    }
}
