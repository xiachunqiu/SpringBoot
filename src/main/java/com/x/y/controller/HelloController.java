package com.x.y.controller;

import com.x.y.domain.User;
import com.x.y.service.CommonService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/hello")
@Log4j2
public class HelloController {
    private final CommonService commonService;

    @Autowired
    public HelloController(CommonService commonService) {
        this.commonService = commonService;
    }

    @RequestMapping("/hi")
    public String say() {
        return "welcome";
    }

    @RequestMapping("getUserByName")
    @ResponseBody
    public List<User> getUserByName() {
        User user = new User();
        user.setUserName("老板");
        return commonService.findListByObj(user, null);
    }
}