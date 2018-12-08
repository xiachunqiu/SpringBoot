package com.x.y.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private Date addDate;
    private String sex;
    private String address;
    @Transient
    private  String addDateStr;
}