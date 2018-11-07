package com.x.y.controller;

import com.x.y.domain.User;
import com.x.y.service.CommonService;
import com.x.y.utils.StringUtils;
import com.x.y.utils.UUIDUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

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

    @RequestMapping("/xiachunqiu")
    public String xiachunqiu() {
        return "page/xiachunqiu";
    }

    @RequestMapping("getUserByName")
    @ResponseBody
    public int getUserByName() {
        User user = new User();
        return commonService.findCountByObj(user);
    }

    @RequestMapping("addUser")
    @ResponseBody
    public String addUser() {
        User user = new User();
        user.setUserName("1");
        commonService.add(user);
        log.info("成功");
        return "成功";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            Assert.isTrue(!file.isEmpty(), "请选择上传文件");
            String fileName = file.getOriginalFilename();
            String suffix = StringUtils.isNotNull(fileName) ? fileName.substring(fileName.lastIndexOf(".")) : "";
            String saveFileName = UUIDUtils.getUUID();
            String path = request.getSession().getServletContext().getRealPath("/upload/");
            File saveFile = new File(path + saveFileName + suffix);
            if (!saveFile.getParentFile().exists()) {
                Assert.isTrue(saveFile.getParentFile().mkdirs(), "创建上传文件夹失败，路径为：" + saveFile.getParentFile().getAbsolutePath());
                log.info("创建存储的目录为：" + path);
            }
            file.transferTo(saveFile);
            log.info("上传的文件为：" + saveFile.getAbsolutePath());
            return "成功";
        } catch (Exception e) {
            log.error(e);
            return e.getMessage();
        }
    }
}