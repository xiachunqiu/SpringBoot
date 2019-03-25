package pers.xiachunqiu.obsidian.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.Assert;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class PagerDTO implements Serializable {
    private int pageCount;
    private int pageSize;
    private int recordCount;
    private int pageNo = 1;

    public PagerDTO(int recordCount, int pageSize, int pageNo) {
        Assert.isTrue(pageSize > 0, "pageSize is 0");
        this.setRecordCount(recordCount).setPageSize(pageSize).setPageNo(pageNo)
                .setPageCount(recordCount / pageSize + (recordCount % pageSize == 0 ? 0 : 1));
    }

    public int getStartPos() {
        return (this.getPageNo() - 1) * this.getPageSize();
    }
}