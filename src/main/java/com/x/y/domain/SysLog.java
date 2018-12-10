package com.x.y.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志
 *
 * @author Hugh
 * @since 2018-12-10 09:32:29
 */
@Data
@Entity
@Table(indexes = {@Index(name = "userId", columnList = ("userId"))})
public class SysLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 系统操作用户ID
     */
    private String userId;
    /**
     * 操作者用户名
     */
    private String userName;
    /**
     * 日志记录日期
     */
    private Date addDate;
    /**
     * 请求的完整URL及所有的参数内容
     */
    private String content;
    /**
     * 操作者对应的IP地址
     */
    private String ip;
    private String uri;
}