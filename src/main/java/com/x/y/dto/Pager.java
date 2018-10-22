package com.x.y.dto;

import java.io.Serializable;

public class Pager implements Serializable {
    private int pageCount;
    private int pageSize;
    private int recordCount;
    private boolean haveNextPage;
    private boolean havePrePage;
    private int pageNo;

    public Pager(int recordCount, int pageSize) {
        if (pageSize == 0) {
            pageSize = 1;
        }
        this.setPageSize(pageSize);
        this.setRecordCount(recordCount);
        if (recordCount % pageSize == 0) {
            this.pageCount = recordCount / pageSize;
        } else {
            this.pageCount = recordCount / pageSize + 1;
        }
        this.pageCount = this.pageCount == 0 ? 1 : this.pageCount;
        this.isHaveNextPage();
        this.isHavePrePage();
    }

    public int getPrePageNo() {
        int prePageNoinner = this.getPageNo() - 1;
        if (prePageNoinner < 1) {
            prePageNoinner = 1;
        }
        return prePageNoinner;
    }

    public int getNextPageNo() {
        int prePageNoinner = this.getPageNo() + 1;
        if (prePageNoinner > this.getPageCount()) {
            prePageNoinner = this.getPageCount();
        }
        return prePageNoinner;
    }

    public void setHaveNextPage(boolean haveNextPage) {
        this.haveNextPage = haveNextPage;
    }

    public void setHavePrePage(boolean havePrePage) {
        this.havePrePage = havePrePage;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getRecordCount() {
        return this.recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getStartPos() {
        int pageNoTemp = this.getPageNo();
        if (pageNoTemp == 0) {
            pageNoTemp = 1;
        }
        if (pageNoTemp >= this.pageCount) {
            pageNoTemp = this.pageCount;
        }
        return (pageNoTemp - 1) * this.pageSize;
    }

    public int getStartPos(int pageNo) {
        if (pageNo == 0) {
            pageNo = 1;
        }

        if (pageNo >= this.pageCount) {
            pageNo = this.pageCount;
        }

        return (pageNo - 1) * this.pageSize;
    }

    public int getEndPos() {
        int pageNoTemp = this.getPageNo();
        if (pageNoTemp == 0) {
            pageNoTemp = 1;
        }
        if (pageNoTemp >= this.pageCount) {
            pageNoTemp = this.pageCount;
        }
        return pageNoTemp * this.pageSize;
    }

    public int getPageSize(int pageNo) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        if (pageNo >= this.pageCount) {
            pageNo = this.pageCount;
        }
        return pageNo * this.pageSize > this.recordCount ? this.recordCount - (pageNo - 1) * this.pageSize : this.pageSize;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
        this.haveNextPage = this.isHaveNextPage();
        this.havePrePage = this.isHavePrePage();
    }

    public int getPageNo() {
        if (this.pageNo > this.pageCount && this.pageCount >= 1) {
            this.pageNo = this.pageCount;
        }
        if (this.pageNo < 1) {
            this.pageNo = 1;
        }
        return this.pageNo;
    }

    public boolean isHaveNextPage() {
        this.haveNextPage = this.getPageNo() < this.getPageCount();
        return this.haveNextPage;
    }

    public boolean isHavePrePage() {
        this.havePrePage = this.getPageNo() > 1;
        return this.havePrePage;
    }
}