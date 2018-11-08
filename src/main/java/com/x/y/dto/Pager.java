package com.x.y.dto;

import lombok.Data;
import org.springframework.util.Assert;

import java.io.Serializable;

@Data
public class Pager implements Serializable {
    private int pageCount;
    private int pageSize;
    private int recordCount;
    private int pageNo = 1;

    public Pager(int recordCount, int pageSize) {
        Assert.isTrue(recordCount > 0, "recordCount is 0");
        Assert.isTrue(pageSize > 0, "pageSize is 0");
        this.setRecordCount(recordCount);
        this.setPageSize(pageSize);
        this.setPageCount(recordCount / pageSize + (recordCount % pageSize == 0 ? 0 : 1));
    }

    public int getStartPos() {
        return (this.getPageNo() - 1) * this.getPageSize();
    }
}