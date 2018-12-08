package com.x.y.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table
@Data
public class TopMenu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
    @Transient
    private String sqlString;
    /**
     * 该菜单的子列表用于传值到前台
     */
    @Transient
    private List<TopMenu> childList = new LinkedList<>();
}