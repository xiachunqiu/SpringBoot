package pers.xiachunqiu.obsidian.dto;

import pers.xiachunqiu.obsidian.global.ResponseCode;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data(staticConstructor="of")
@Accessors(chain = true)
public class ResponseDataDTO implements Serializable {
    @NonNull
    private String code;
    private Object data;
    private String msg;

    public static ResponseDataDTO success() {
        return ResponseDataDTO.of(ResponseCode.SUCCESS).setMsg("SUCCESS");
    }

    public static ResponseDataDTO success(Object data) {
        return ResponseDataDTO.of(ResponseCode.SUCCESS).setData(data).setMsg("SUCCESS");
    }

    public static ResponseDataDTO success(String msg, Object data) {
        return ResponseDataDTO.of(ResponseCode.SUCCESS).setData(data).setMsg(msg);
    }

    public static ResponseDataDTO fail() {
        return ResponseDataDTO.of(ResponseCode.FAIL).setMsg("FAIL");
    }

    public static ResponseDataDTO fail(String msg) {
        return ResponseDataDTO.of(ResponseCode.FAIL).setMsg(msg);
    }

    public static ResponseDataDTO fail(String msg, String code) {
        return ResponseDataDTO.of(code).setMsg(msg);
    }
}