package com.x.y.controller;

import com.x.y.dto.PagerDTO;
import com.x.y.entity.User;
import com.x.y.dto.PagerRtnDTO;
import com.x.y.service.CommonService;
import com.x.y.util.MD5Util;
import com.x.y.util.StringUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Getter
@Controller
public class BaseController {
    private CommonService commonService;

    @Autowired
    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    <T> PagerRtnDTO<T> getPagerRtn(List<T> list, PagerDTO pagerDTO) {
        PagerRtnDTO<T> pagerRtnDTO = new PagerRtnDTO<>();
        pagerRtnDTO.setList(list).setPagerDTO(pagerDTO);
        return pagerRtnDTO;
    }

    User getUserByName(String username) {
        User user = new User();
        user.setUserName(username);
        List<User> userList = commonService.getEntityList(user, new PagerDTO(1, 1, 1));
        return userList.size() > 0 ? userList.get(0) : null;
    }

    User getRequestUer(HttpServletRequest request) {
        return (User) request.getAttribute("user");
    }

    static String getPassword(String pwd) {
        if (StringUtil.isNull(pwd)) {
            pwd = "000000";
        }
        return MD5Util.encryptByMD5(pwd);
    }
}