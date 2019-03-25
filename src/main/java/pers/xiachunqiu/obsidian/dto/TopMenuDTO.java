package pers.xiachunqiu.obsidian.dto;

import pers.xiachunqiu.obsidian.entity.TopMenu;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class TopMenuDTO implements Serializable {
    private Integer id;
    private String icon;
    /**
     * 图标类型，可选值为：
     * layui-icon,iconfont
     */
    private String iconType;
    /**
     * 显示的导航名称
     */
    private String name;
    private Integer parentId;
    private String url;
    /**
     * 排序ID ，越小越在最前
     */
    private Integer orderId;
    /**
     * 该菜单的子列表用于传值到前台
     */
    private List<TopMenuDTO> childList = new LinkedList<>();

    public TopMenu convert() {
        TopMenu topMenu = new TopMenu();
        BeanUtils.copyProperties(this, topMenu);
        return topMenu;
    }

    public static TopMenuDTO convert(TopMenu topMenu) {
        TopMenuDTO topMenuDTO = new TopMenuDTO();
        BeanUtils.copyProperties(topMenu, topMenuDTO);
        return topMenuDTO;
    }
}