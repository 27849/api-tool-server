package com.limbo.controller;

import com.limbo.common.CommonResult;
import com.limbo.modal.User;
import com.limbo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * test
 *
 * @author limbo
 **/

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserService userService;


    @RequestMapping("hello")
    public CommonResult<?> hello(HttpServletRequest request){
        return CommonResult.success("OK");
    }


    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("123456" ));
    }
}
