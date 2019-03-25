package pers.xiachunqiu.obsidian.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table
@Data
@Accessors(chain = true)
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
    private Date createdTime;
    private Date modifiedTime;
    private Boolean deletedFlag = false;
    @Transient
    private String sqlString;
    /**
     * 该菜单的子列表用于传值到前台
     */
    @Transient
    private List<TopMenu> childList = new LinkedList<>();
}