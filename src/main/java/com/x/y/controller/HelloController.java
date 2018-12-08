package com.x.y.controller;

import com.x.y.common.Constants;
import com.x.y.domain.User;
import com.x.y.dto.Pager;
import com.x.y.dto.PagerRtn;
import com.x.y.dto.ResponseData;
import com.x.y.utils.DateUtils;
import com.x.y.utils.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/hello")
@Log4j2
public class HelloController extends BaseController {
    @RequestMapping("/hi")
    public ModelAndView hi() {
        return new ModelAndView("page/auth/index");
    }

    @RequestMapping("/user")
    public ModelAndView user() {
        return new ModelAndView("page/user/userList");
    }

    @RequestMapping("getUserList")
    public ResponseData getUserList(User user, @RequestParam(defaultValue = "1", value = "pageNo") Integer pageNo) {
        try {
            int count = super.getCommonService().findCountForSearch(user);
            Pager pager = new Pager(count, Constants.PAGE_SIZE, pageNo);
            List<User> list = super.getCommonService().findListForSearch(user, pager);
            list.forEach(l -> l.setAddDateStr(DateUtils.format(l.getAddDate())));
            PagerRtn<User> pagerRtn = super.getPagerRtn(list, pager);
            return ResponseData.okData(pagerRtn);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseData.fail();
        }
    }

    @RequestMapping(value = "addUser", method = RequestMethod.POST)
    public ResponseData addUser(User user) {
        try {
            Assert.isTrue(StringUtils.isNotNull(user.getUserName()), "User name cannot be empty");
            user.setAddDate(new Date());
            super.getCommonService().add(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseData.fail(e.getMessage());
        }
        return ResponseData.ok();
    }

    @RequestMapping(value = "updateUser", method = RequestMethod.POST)
    public ResponseData updateUser(User user) {
        try {
            Assert.isTrue(StringUtils.isNotNull(user.getUserName()), "User name cannot be empty");
            Assert.isTrue(StringUtils.isNotNull(user.getSex()), "Sex cannot be empty");
            super.getCommonService().merge(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseData.fail(e.getMessage());
        }
        return ResponseData.ok();
    }
}