package com.x.y.dto;

import com.x.y.common.ResponseCode;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data(staticConstructor="of")
@Accessors(chain = true)
public class ResponseDataDTO implements Serializable {
    @NonNull
    private boolean success;
    @NonNull
    private String code;
    private Object data;
    private String msg;

    public static ResponseDataDTO success() {
        return ResponseDataDTO.of(true, ResponseCode.success);
    }

    public static ResponseDataDTO success(Object data) {
        return ResponseDataDTO.of(true, ResponseCode.success).setData(data);
    }

    public static ResponseDataDTO success(String msg, Object data) {
        return ResponseDataDTO.of(true, ResponseCode.success).setData(data).setMsg(msg);
    }

    public static ResponseDataDTO fail() {
        return ResponseDataDTO.of(false, ResponseCode.fail).setMsg("fail");
    }

    public static ResponseDataDTO fail(String msg) {
        return ResponseDataDTO.of(false, ResponseCode.fail).setMsg(msg);
    }

    public static ResponseDataDTO fail(String msg, String code) {
        return ResponseDataDTO.of(false, code).setMsg(msg);
    }
}