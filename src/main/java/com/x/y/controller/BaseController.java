package com.x.y.controller;

import com.x.y.domain.User;
import com.x.y.dto.Pager;
import com.x.y.dto.PagerRtn;
import com.x.y.service.CommonService;
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

    <T> PagerRtn<T> getPagerRtn(List<T> list, Pager pager) {
        PagerRtn<T> pagerRtn = new PagerRtn<>();
        pagerRtn.setList(list);
        pagerRtn.setPager(pager);
        return pagerRtn;
    }

    User getRequestUer(HttpServletRequest request){
        return (User) request.getAttribute("user");
    }
}