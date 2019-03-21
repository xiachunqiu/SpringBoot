package com.x.y.interceptor;

import com.alibaba.fastjson.JSON;
import com.x.y.common.Constants;
import com.x.y.common.ResponseCode;
import com.x.y.config.ApplicationContextRegister;
import com.x.y.entity.SysLog;
import com.x.y.entity.User;
import com.x.y.dto.ResponseDataDTO;
import com.x.y.service.CommonService;
import com.x.y.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

/**
 * @author Hugh
 * @since 2018-12-10 09:13:45
 */
@Log4j2
@Getter
@Setter
public class AuthInterceptor implements HandlerInterceptor {
    private CommonService commonService = ApplicationContextRegister.getApplicationContext().getBean(CommonService.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            String uri = request.getRequestURI();
            User user = (User) request.getSession().getAttribute(Constants.USER_SESSION_KEY);
            if (user == null) {
                PrintWriter print = response.getWriter();
                print.write(JSON.toJSON(ResponseDataDTO.fail("Please login", ResponseCode.not_logged_in)).toString());
                return false;
            }
            request.setAttribute("user", user);
            saveLog(request, user, uri);
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    /**
     * 保存日志
     */
    private void saveLog(HttpServletRequest request, User user, String uri) {
        try {
            SysLog sysLog = new SysLog();
            sysLog.setCreatedTime(new Date());
            sysLog.setUri(StringUtil.isNotNull(uri) ? uri : null);
            sysLog.setIp(StringUtil.getIpAddress(request));
            sysLog.setUserId(user.getId().toString());
            sysLog.setUserName(user.getUserName());
            sysLog.setContent(logRequestParameter(request));
            commonService.insert(sysLog);
        } catch (Exception e) {
            log.error("insert log error", e);
        }
    }

    private String logRequestParameter(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        Map map = request.getParameterMap();
        for (Object key : map.keySet()) {
            String name = key.toString();
            String value = request.getParameter(name);
            if (StringUtil.isNotNull(value) && value.length() > 5120) {
                continue;
            }
            if (StringUtil.isNull(value) || "_".equalsIgnoreCase(key.toString())) {
                continue;
            }
            sb.append(name).append("=").append(value).append("&");
        }
        String url = request.getRequestURI();
        Assert.isTrue(request.getHeader("host") != null, "host is null");
        return "" + url + sb.toString();
    }
}