package pers.xiachunqiu.obsidian.dto;

import pers.xiachunqiu.obsidian.entity.LeftMenu;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class LeftMenuDTO implements Serializable {
    private Integer id;
    private String icon;
    private String name;
    private Integer parentId;
    private String url;
    /**
     * 图标类型，可选值为：
     * layui-icon,iconfont
     */
    private String iconType;
    /**
     * 顶部一级菜单的ID值
     */
    private String topMenuId;
    /**
     * 排序ID ，越小越在最前
     */
    private Integer orderId;
    private Boolean isPage;
    /**
     * 该菜单的子列表用于传值到前台
     */
    private List<LeftMenuDTO> childList = new LinkedList<>();

    public LeftMenu convert() {
        LeftMenu leftMenu = new LeftMenu();
        BeanUtils.copyProperties(this, leftMenu);
        return leftMenu;
    }

    public static LeftMenuDTO convert(LeftMenu leftMenu) {
        LeftMenuDTO leftMenuDTO = new LeftMenuDTO();
        BeanUtils.copyProperties(leftMenu, leftMenuDTO);
        return leftMenuDTO;
    }
}