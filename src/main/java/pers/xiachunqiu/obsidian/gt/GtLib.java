package pers.xiachunqiu.obsidian.gt;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@Log4j2
public class GtLib {
    private final String apiUrl = "https://api.geetest.com";

    private final String json_format = "1";

    public static final String fn_gt_challenge = "geetest_challenge";

    public static final String fn_gt_validate = "geetest_validate";

    public static final String fn_gt_sec_code = "geetest_seccode";

    private String captchaId;

    private String privateKey;

    private boolean newFailBack;

    private String responseStr = "";

    public String gtServerStatusSessionKey = "gt_server_status";

    public GtLib(String captchaId, String privateKey, boolean newFailBack) {
        this.captchaId = captchaId;
        this.privateKey = privateKey;
        this.newFailBack = newFailBack;
    }

    public String getResponseStr() {
        return responseStr;
    }

    private String getFailPreProcessRes() {
        Long rnd1 = Math.round(Math.random() * 100);
        Long rnd2 = Math.round(Math.random() * 100);
        String md5Str1 = md5Encode(rnd1 + "");
        String md5Str2 = md5Encode(rnd2 + "");
        String challenge = md5Str1 + md5Str2.substring(0, 2);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("SUCCESS", 0);
            jsonObject.put("gt", this.captchaId);
            jsonObject.put("challenge", challenge);
            jsonObject.put("new_captcha", this.newFailBack);
        } catch (Exception e) {
            gtLog("json dumps error");
        }
        return jsonObject.toString();
    }

    private String getSuccessPreProcessRes(String challenge) {
        gtLog("challenge:" + challenge);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("SUCCESS", 1);
            jsonObject.put("gt", this.captchaId);
            jsonObject.put("challenge", challenge);
        } catch (Exception e) {
            gtLog("json dumps error");
        }
        return jsonObject.toString();
    }

    public int preProcess(HashMap<String, String> data) {
        if (registerChallenge(data) != 1) {
            this.responseStr = this.getFailPreProcessRes();
            return 0;
        }
        return 1;
    }

    private int registerChallenge(HashMap<String, String> data) {
        try {
            String userId = data.get("user_id");
            String clientType = data.get("client_type");
            String ipAddress = data.get("ip_address");
            String registerUrl = "/register.php";
            String getUrl = apiUrl + registerUrl + "?";
            String param = "gt=" + this.captchaId + "&json_format=" + this.json_format;
            param = getParamStr(userId, clientType, ipAddress, param);
            gtLog("GET_URL:" + getUrl + param);
            String result_str = readContentFromGet(getUrl + param);
            if (result_str.equals("FAIL")) {
                gtLog("gtServer register challenge failed");
                return 0;
            }
            gtLog("result:" + result_str);
            JSONObject jsonObject = JSONObject.parseObject(result_str);
            String return_challenge = jsonObject.getString("challenge");
            gtLog("return_challenge:" + return_challenge);
            if (return_challenge.length() == 32) {
                this.responseStr = this.getSuccessPreProcessRes(this.md5Encode(return_challenge + this.privateKey));
                return 1;
            } else {
                gtLog("gtServer register challenge error");
                return 0;
            }
        } catch (Exception e) {
            gtLog(e.toString());
            gtLog("exception:register api");
        }
        return 0;
    }

    private String getParamStr(String userId, String clientType, String ipAddress, String param) {
        if (userId != null) {
            param = param + "&user_id=" + userId;
        }
        if (clientType != null) {
            param = param + "&client_type=" + clientType;
        }
        if (ipAddress != null) {
            param = param + "&ip_address=" + ipAddress;
        }
        return param;
    }

    private boolean objIsEmpty(Object gtObj) {
        if (gtObj == null) {
            return true;
        }
        return gtObj.toString().trim().length() == 0;
    }

    private boolean requestIsLegal(String challenge, String validate, String secCode) {
        if (objIsEmpty(challenge)) {
            return true;
        }
        if (objIsEmpty(validate)) {
            return true;
        }
        return objIsEmpty(secCode);
    }

    public int enhancedValidateRequest(String challenge, String validate, String secCode, HashMap<String, String> data) {
        if (requestIsLegal(challenge, validate, secCode)) {
            return 0;
        }
        gtLog("request legitimate");
        String userId = data.get("user_id");
        String clientType = data.get("client_type");
        String ipAddress = data.get("ip_address");
        String validateUrl = "/validate.php";
        String postUrl = this.apiUrl + validateUrl;
        String param = String.format("challenge=%s&validate=%s&seccode=%s&json_format=%s", challenge, validate, secCode, this.json_format);
        param = getParamStr(userId, clientType, ipAddress, param);
        gtLog("param:" + param);
        String response = "";
        try {
            if (validate.length() <= 0) {
                return 0;
            }
            if (!checkResultByPrivate(challenge, validate)) {
                return 0;
            }
            gtLog("checkResultByPrivate");
            response = readContentFromPost(postUrl, param);
            gtLog("response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String returnSecCode;
        try {
            JSONObject return_map = JSONObject.parseObject(response);
            returnSecCode = return_map.getString("seccode");
            gtLog("md5: " + md5Encode(returnSecCode));
            if (returnSecCode.equals(md5Encode(secCode))) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            gtLog("json load error");
            return 0;
        }
    }

    public int failBackValidateRequest(String challenge, String validate, String secCode) {
        gtLog("in FAIL back validate");
        if (requestIsLegal(challenge, validate, secCode)) {
            return 0;
        }
        gtLog("request legitimate");
        return 1;
    }

    private void gtLog(String message) {
        log.info("gtLog: " + message);
    }

    private boolean checkResultByPrivate(String challenge, String validate) {
        String encodeStr = md5Encode(privateKey + "geetest" + challenge);
        return validate.equals(encodeStr);
    }

    private String readContentFromGet(String URL) throws IOException {
        URL getUrl = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        connection.setConnectTimeout(2000);
        connection.setReadTimeout(2000);
        connection.connect();
        return getConnResult(connection);
    }

    private String getConnResult(HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() == 200) {
            StringBuilder sBuffer = new StringBuilder();
            byte[] buf = new byte[1024];
            InputStream inStream = connection.getInputStream();
            for (int n; (n = inStream.read(buf)) != -1; ) {
                sBuffer.append(new String(buf, 0, n, StandardCharsets.UTF_8));
            }
            inStream.close();
            connection.disconnect();
            return sBuffer.toString();
        } else {
            return "FAIL";
        }
    }

    private String readContentFromPost(String URL, String data) throws IOException {
        gtLog(data);
        URL postUrl = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        connection.setConnectTimeout(2000);
        connection.setReadTimeout(2000);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.connect();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
        outputStreamWriter.write(data);
        outputStreamWriter.flush();
        outputStreamWriter.close();
        return getConnResult(connection);
    }

    private String md5Encode(String plainText) {
        String re_md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] b = md.digest();
            int i;
            StringBuilder buf = new StringBuilder();
            for (byte aB : b) {
                i = aB;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            re_md5 = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }
}