package com.x.y.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PagerRtn<T> implements Serializable {
    private Pager pager;
    private List<T> list;
}