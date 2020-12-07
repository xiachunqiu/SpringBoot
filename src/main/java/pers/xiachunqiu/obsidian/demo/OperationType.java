package pers.xiachunqiu.obsidian.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationType {
    SCAN_QA_CODE("scan_qa_code"),
    FOLLOW("follow");
    private String value;
}
