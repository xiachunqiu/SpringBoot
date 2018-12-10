package com.x.y.controller;

import com.x.y.domain.LeftMenu;
import com.x.y.domain.TopMenu;
import com.x.y.domain.User;
import com.x.y.dto.ResponseData;
import com.x.y.utils.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 系统管理相关接口
 *
 * @author Hugh
 * @since 2018-12-8 18:15:11
 */
@RestController
@RequestMapping("/sysManager")
@Log4j2
public class SysManagerController extends BaseController {
    /**
     * 获取顶部菜单列表,用于加载Header.html部分
     *
     * @author Hugh
     * @since 2018-12-8 18:12:11
     */
    @RequestMapping("/getTopMenus")
    public ResponseData getTopMenus() {
        try {
            List<TopMenu> list = super.getCommonService().findListByObj(new TopMenu(), null);
            List<TopMenu> topNavList = new LinkedList<>();
            list.forEach(t -> {
                if (t.getParentId() == null) {
                    topNavList.add(t);

                }
            });
            topNavList.forEach(tl -> list.forEach(t -> {
                if (t.getId().equals(tl.getParentId())) {
                    tl.getChildList().add(t);
                }
            }));
            return ResponseData.okData(topNavList);
        } catch (Exception e) {
            log.error(e);
            return ResponseData.fail(e.getMessage());
        }
    }

    /**
     * 获取左侧菜单列表,用于加载middle-left.html部分
     *
     * @author Hugh
     * @since 2018-12-8 18:12:45
     */
    @RequestMapping("/getLeftMenus")
    public ResponseData getLeftMenus(LeftMenu leftMenu) {
        try {
            Assert.isTrue(StringUtils.isNotNull(leftMenu.getTopMenuId()), "topMenuId is null");
            List<LeftMenu> list = super.getCommonService().findListByObj(new LeftMenu(), null, "order by orderId");
            return ResponseData.okData(list);
        } catch (Exception e) {
            log.error(e);
            return ResponseData.fail(e.getMessage());
        }
    }

    /**
     * 修改密码
     *
     * @author Hugh
     * @since 2018-12-8 18:12:45
     */
    @RequestMapping("/changePassword")
    public ResponseData changePassword(HttpServletRequest request, String oldPassWord, String newPassword) {
        try {
            User user = super.getRequestUer(request);
            User dbUser = super.getCommonService().findById(user.getId(), User.class);
            Assert.isTrue(getPassword(oldPassWord).equalsIgnoreCase(dbUser.getPassword()), "Original password error");
            Assert.isTrue(StringUtils.isNotNull(newPassword) && newPassword.length() > 5, "Minimum 6 bits of new password length");
            user.setPassword(getPassword(newPassword));
            super.getCommonService().merge(user);
            return ResponseData.ok();
        } catch (Exception e) {
            log.error(e);
            return ResponseData.fail(e.getMessage());
        }
    }
}