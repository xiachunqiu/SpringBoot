package com.x.y.controller;

import com.x.y.domain.User;
import com.x.y.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello")
@Log4j2
public class HelloController {
    private final UserService userService;

    @Autowired
    public HelloController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/say")
    public String say() {
        return "welcome";
    }

    @RequestMapping("getUserByName")
    @ResponseBody
    public User getUserByName() {
        User user = new User();
        user.setUserName("hhhh");
        userService.addUser(user);
        return userService.findUserByName("老板");
    }
}