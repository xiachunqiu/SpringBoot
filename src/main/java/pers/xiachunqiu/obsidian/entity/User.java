package pers.xiachunqiu.obsidian.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
@Accessors(chain = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String password;
    private Date createdTime;
    private Date modifiedTime;
    private Boolean deletedFlag = false;
    private String gender;
    private String address;
}