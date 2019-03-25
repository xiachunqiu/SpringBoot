package pers.xiachunqiu.obsidian.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class PagerRtnDTO<T> implements Serializable {
    private PagerDTO pagerDTO;
    private List<T> list;
}