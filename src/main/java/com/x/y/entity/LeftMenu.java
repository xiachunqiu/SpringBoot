package com.x.y.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table
@Data
public class LeftMenu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
    private Date createdTime;
    private Date modifiedTime;
    private Boolean deletedFlag = false;
    /**
     * 该菜单的子列表用于传值到前台
     */
    @Transient
    private List<LeftMenu> childList = new LinkedList<>();
}