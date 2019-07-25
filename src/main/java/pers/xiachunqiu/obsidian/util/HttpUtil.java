package pers.xiachunqiu.obsidian.util;

import lombok.extern.log4j.Log4j2;
import org.apache.catalina.manager.Constants;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
public class HttpUtil {
    private HttpUtil() {
    }

    /**
     * 发送一个 GET 请求
     */
    public static String getService(String path, Map<String, String> map) throws Exception {
        String result = null;
        String param = "";
        if (map != null) {
            for (String key : map.keySet()) {
                Object v = map.get(key);
                if ("".equals(param)) {
                    param = key + "=" + (v == null ? "" : URLEncoder.encode(v.toString(), Constants.CHARSET));
                } else {
                    param += "&" + key + "=" + (v == null ? "" : URLEncoder.encode(v.toString(), Constants.CHARSET));
                }
            }
        }
        String getURL = path + (path.contains("?") ? "&" : "?") + param;
        HttpGet httpGet = new HttpGet(getURL);
        CloseableHttpClient client = initClient(path);
        try {
            httpGet.setConfig(getCommonRequestConfig());
            HttpResponse response = client.execute(httpGet);
            result = IOUtils.toString(response.getEntity().getContent(), Constants.CHARSET);
            httpGet.releaseConnection();
        } catch (Exception e) {
            return result;
        } finally {
            if (getURL.startsWith("https") && client != null) {
                client.close();
            }
        }
        return result;
    }

    /**
     * 发送post请求
     */
    public static String postService(String path, Map<String, String> map) {
        String result = null;
        try {
            HttpPost httpPost = new HttpPost(path);
            if (map != null) {
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                for (String key : map.keySet()) {
                    nameValuePairs.add(new BasicNameValuePair(key, map.get(key)));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8));
            }
            httpPost.setConfig(getCommonRequestConfig());
            CloseableHttpClient client = initClient(path);
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, Constants.CHARSET);
                EntityUtils.consume(entity);
            }
            response.close();
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    private static CloseableHttpClient initClient(String path) throws Exception {
        return path.startsWith("https") ? createSSLInsecureClient() : HttpClients.createDefault();
    }

    private static RequestConfig getCommonRequestConfig() {
        return RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build();
    }

    private static CloseableHttpClient createSSLInsecureClient() throws Exception {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }

            @Override
            public void verify(String host, SSLSocket ssl) {
            }

            @Override
            public void verify(String host, X509Certificate cert) {
            }

            @Override
            public void verify(String host, String[] cns, String[] subjectAlts) {
            }
        });
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }
}