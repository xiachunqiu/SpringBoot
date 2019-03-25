package pers.xiachunqiu.obsidian.controller;

import com.google.common.collect.Lists;
import pers.xiachunqiu.obsidian.dto.LeftMenuDTO;
import pers.xiachunqiu.obsidian.dto.TopMenuDTO;
import pers.xiachunqiu.obsidian.entity.LeftMenu;
import pers.xiachunqiu.obsidian.entity.TopMenu;
import pers.xiachunqiu.obsidian.entity.User;
import pers.xiachunqiu.obsidian.dto.ResponseDataDTO;
import pers.xiachunqiu.obsidian.util.StringUtils;
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
    public ResponseDataDTO getTopMenus() {
        try {
            List<TopMenu> list = super.getCommonService().getEntityList(new TopMenu(), null);
            List<TopMenu> topNavList = Lists.newLinkedList();
            list.forEach(t -> {
                if (t.getParentId() == null) {
                    topNavList.add(t);

                }
            });
            List<TopMenuDTO> topMenuDTOList = Lists.newArrayList();
            topNavList.forEach(tl -> {
                list.forEach(t -> {
                    if (t.getId().equals(tl.getParentId())) {
                        tl.getChildList().add(t);
                    }
                });
                topMenuDTOList.add(TopMenuDTO.convert(tl));
            });
            return ResponseDataDTO.success(topMenuDTOList);
        } catch (Exception e) {
            log.error(e);
            return ResponseDataDTO.fail(e.getMessage());
        }
    }

    /**
     * 获取左侧菜单列表,用于加载middle-left.html部分
     *
     * @author Hugh
     * @since 2018-12-8 18:12:45
     */
    @RequestMapping("/getLeftMenus")
    public ResponseDataDTO getLeftMenus(LeftMenuDTO leftMenuDTO) {
        try {
            LeftMenu leftMenu = leftMenuDTO.convert();
            Assert.isTrue(StringUtils.isNotNull(leftMenu.getTopMenuId()), "topMenuId is null");
            List<LeftMenu> leftMenuList = super.getCommonService().getEntityList(leftMenu, null, "order by orderId");
            List<LeftMenuDTO> leftMenuDTOList = Lists.newArrayList();
            leftMenuList.forEach(l -> leftMenuDTOList.add(LeftMenuDTO.convert(l)));
            return ResponseDataDTO.success(leftMenuDTOList);
        } catch (Exception e) {
            log.error(e);
            return ResponseDataDTO.fail(e.getMessage());
        }
    }

    /**
     * 修改密码
     *
     * @author Hugh
     * @since 2018-12-8 18:12:45
     */
    @RequestMapping("/changePassword")
    public ResponseDataDTO changePassword(HttpServletRequest request, String oldPassWord, String newPassword) {
        try {
            User user = super.getRequestUer(request);
            User dbUser = super.getCommonService().getEntityById(user.getId(), User.class);
            Assert.isTrue(getPassword(oldPassWord).equalsIgnoreCase(dbUser.getPassword()), "Old password error");
            Assert.isTrue(StringUtils.isNotNull(newPassword) && newPassword.length() > 5, "Minimum 6 bits of new password length");
            user.setPassword(getPassword(newPassword));
            super.getCommonService().update(user);
            return ResponseDataDTO.success();
        } catch (Exception e) {
            log.error(e);
            return ResponseDataDTO.fail(e.getMessage());
        }
    }
}