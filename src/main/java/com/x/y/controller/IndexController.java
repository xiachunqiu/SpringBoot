package com.x.y.controller;

import com.x.y.common.CacheTime;
import com.x.y.common.Constants;
import com.x.y.entity.User;
import com.x.y.dto.ResponseDataDTO;
import com.x.y.gt.GtConfig;
import com.x.y.gt.GtLib;
import com.x.y.util.MD5Util;
import com.x.y.util.StringUtil;
import com.x.y.util.XMemCachedUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@Controller
@RequestMapping("/index")
@Log4j2
public class IndexController extends BaseController {
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDataDTO login(String userName, String password, HttpServletRequest request) {
        try {
            Assert.isTrue(StringUtil.isNotNull(userName) && StringUtil.isNotNull(password), "User name or password cannot be empty");
            String key = StringUtil.getIpAddress(request) + "." + userName;
            Object errorCountCache = XMemCachedUtil.get(key);
            int errorCount = errorCountCache == null ? 0 : (int) errorCountCache;
            Assert.isTrue(errorCount < 5, "Your account is locked, please try again later!");
            validatorCode(request);
            User user = super.getUserByName(userName);
            Assert.isTrue(user != null && MD5Util.encryptByMD5(password).equals(user.getPassword()), getLoginErrorDes(key, errorCount));
            request.getSession().setAttribute(Constants.USER_SESSION_KEY, user);
            return ResponseDataDTO.success(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataDTO.fail(e.getMessage());
        }
    }

    private void validatorCode(HttpServletRequest request) {
        String clientId = StringUtil.getParameter(request, "client-Id", "");
        Assert.isTrue(StringUtil.isNotNull(clientId), "client-Id is null");
        GtLib gtSdk = new GtLib(GtConfig.gtId, GtConfig.gtKey, GtConfig.newFailBack);
        String challenge = request.getParameter(GtLib.fn_gt_challenge);
        String validate = request.getParameter(GtLib.fn_gt_validate);
        String secCode = request.getParameter(GtLib.fn_gt_sec_code);
        Object gtServerStatus = XMemCachedUtil.get(clientId + gtSdk.gtServerStatusSessionKey);
        Assert.isTrue(gtServerStatus != null, "Verification code has expired, please refresh the page and try again!");
        int gtServerStatusCode = (int) gtServerStatus;
        Object clientIdCacheObject = XMemCachedUtil.get(clientId);
        Assert.isTrue(clientIdCacheObject != null, "Verification code has expired, please refresh the page and try again!");
        String clientIdCache = clientIdCacheObject.toString();
        HashMap<String, String> param = getGtHashMap(request, clientIdCache);
        int gtResult = gtServerStatusCode == 1 ? gtSdk.enhancedValidateRequest(challenge, validate, secCode, param)
                : gtSdk.failBackValidateRequest(challenge, validate, secCode);
        Assert.isTrue(gtResult == 1, "Verification code has expired, please refresh the page and try again!");
    }

    private String getLoginErrorDes(String key, int errorCount) {
        String des = "Username or password incorrect";
        XMemCachedUtil.set(key, CacheTime.LOGIN_ERROR_LOCK_TIME, ++errorCount);
        int leftCount = 5 - errorCount;
        des += leftCount > 0 ? ",You can also try it" + leftCount + "times and then lock it up for five minutes."
                : ",Your account is locked, please try again later!";
        return des;
    }

    @RequestMapping("/gtRegister")
    public void start(HttpServletRequest request, HttpServletResponse response) {
        GtLib gtSdk = new GtLib(GtConfig.gtId, GtConfig.gtKey, GtConfig.newFailBack);
        String clientId = StringUtil.getParameter(request, "client-Id", "");
        if (StringUtil.isNotNull(clientId)) {
            HashMap<String, String> param = getGtHashMap(request, clientId);
            int gtServerStatus = gtSdk.preProcess(param);
            XMemCachedUtil.set(clientId + gtSdk.gtServerStatusSessionKey, CacheTime.COMMON_CACHE_TIME, gtServerStatus);
            XMemCachedUtil.set(clientId, CacheTime.COMMON_CACHE_TIME, clientId);
            String resStr = gtSdk.getResponseStr();
            try {
                PrintWriter out = response.getWriter();
                out.println(resStr);
            } catch (IOException e) {
                log.error(e);
            }
        }
    }

    private HashMap<String, String> getGtHashMap(HttpServletRequest request, String clientIdCache) {
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", clientIdCache);
        param.put("client_type", "web");
        param.put("ip_address", StringUtil.getIpAddress(request));
        return param;
    }
}