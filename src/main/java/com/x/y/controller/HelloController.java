package com.x.y.controller;

import com.x.y.domain.User;
import com.x.y.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/hello")
@Log4j2
public class HelloController {
    private final UserRepository userRepository;

    @Autowired
    public HelloController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/say")
    public String say() {
        return "welcome";
    }

    @RequestMapping("saveUser")
    @ResponseBody
    public void saveUser() {
        User u = new User();
        u.setUserName("老板");
        u.setAddress("天堂");
        u.setBirthDay(new Date());
        u.setSex("应该是男");
        userRepository.save(u);
    }

    @RequestMapping("getUserByName")
    @ResponseBody
    public User getUserByName() {
        log.info("aaaaaaaaa11111111111");
        return userRepository.findUserByName("老板");
    }
}