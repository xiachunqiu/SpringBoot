package pers.xiachunqiu.obsidian.controller;

import pers.xiachunqiu.obsidian.enums.CacheTime;
import pers.xiachunqiu.obsidian.global.Constants;
import pers.xiachunqiu.obsidian.entity.User;
import pers.xiachunqiu.obsidian.dto.ResponseDataDTO;
import pers.xiachunqiu.obsidian.util.MD5Util;
import pers.xiachunqiu.obsidian.util.StringUtils;
import pers.xiachunqiu.obsidian.util.XMemCachedUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/index")
@Log4j2
public class IndexController extends BaseController {
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDataDTO login(String userName, String password, String clientId, HttpServletRequest request) {
        try {
            Assert.isTrue(StringUtils.isNotNull(clientId), "ClientId cannot be empty");
            Object client = XMemCachedUtil.get(clientId);
            Assert.isTrue(client == null || !(boolean) client, "Please complete verification");
            Assert.isTrue(StringUtils.isNotNull(userName) && StringUtils.isNotNull(password), "User name or password cannot be empty");
            String key = StringUtils.getIpAddress(request) + "." + userName;
            Object errorCountCache = XMemCachedUtil.get(key);
            int errorCount = errorCountCache == null ? 0 : (int) errorCountCache;
            Assert.isTrue(errorCount < 50, "Your account is locked, please try again later!");
            User user = super.getUserByName(userName);
            Assert.isTrue(user != null && MD5Util.encryptByMD5(password).equals(user.getPassword()), getLoginErrorDes(key, errorCount));
            request.getSession().setAttribute(Constants.USER_SESSION_KEY, user);
            return ResponseDataDTO.success(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataDTO.fail(e.getMessage());
        }
    }

    private String getLoginErrorDes(String key, int errorCount) {
        String des = "Username or password incorrect";
        XMemCachedUtil.set(key, CacheTime.LOGIN_ERROR_LOCK_TIME.getValue(), ++errorCount);
        int leftCount = 5 - errorCount;
        des += leftCount > 0 ? ",You can also try it" + leftCount + "times and then lock it up for five minutes."
                : ",Your account is locked, please try again later!";
        return des;
    }

    @RequestMapping(value = "verificationRegister", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDataDTO verificationRegister(String clientId) {
        try {
            Assert.isTrue(StringUtils.isNotNull(clientId), "ClientId cannot be empty");
            Object client = XMemCachedUtil.get(clientId);
            XMemCachedUtil.set("clientId", CacheTime.COMMON_CACHE_TIME.getValue(), client != null);
            return ResponseDataDTO.success();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataDTO.fail(e.getMessage());
        }
    }
}