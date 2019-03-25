package pers.xiachunqiu.obsidian.interceptor;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import pers.xiachunqiu.obsidian.util.StringUtils;
import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Log4j2
public class LogInterceptor implements HandlerInterceptor {
    private static final int MAX_LOG_LENGTH = 512;
    private static final long LIMIT_TIME = 60;
    /**
     * 每个IP每LIMIT_TIME秒的访问频率限制数
     */
    private static final long PERMIT = 80;
    private static final long MAXSIZE = 100000;
    private final ThreadLocal<Long> beginTimeLocal = new ThreadLocal<>();
    @ParametersAreNonnullByDefault
    private LoadingCache<String, AtomicLong> counter = CacheBuilder.newBuilder().maximumSize(MAXSIZE)
            .expireAfterWrite(LIMIT_TIME, TimeUnit.SECONDS).build(new CacheLoader<String, AtomicLong>() {
                @Override
                public AtomicLong load(String key) {
                    return new AtomicLong(0);
                }
            });

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String key = StringUtils.getIpAddress(request) + "-" + request.getRequestURI();
        long count = counter.get(key).incrementAndGet();
        if (count > PERMIT) {
            log.error("key is :" + key + ". The frequency of access is too fast in a minute. The count is : " + count);
            response.setContentType("text/html;charset=utf-8");
            @Cleanup
            PrintWriter out = response.getWriter();
            out.println("The frequency of access is too fast in a minute");
            return false;
        }
        long beginTime = System.currentTimeMillis();
        beginTimeLocal.set(beginTime);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        StringBuilder sb = new StringBuilder();
        long time = System.currentTimeMillis() - getBeginTimeLocal();
        if (time >= 0) {
            sb.append("time:").append(time).append("ms").append("  ").append(request.getMethod());
        }
        sb.append("  ").append(logRequest(request));
        log.info(sb.toString());
    }

    private long getBeginTimeLocal() {
        return beginTimeLocal.get() == null ? 0L : beginTimeLocal.get();
    }

    private static String logRequest(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        Map map = request.getParameterMap();
        for (Object key : map.keySet()) {
            String name = key.toString();
            String value = request.getParameter(name);
            if (StringUtils.isNull(value) || value.length() <= MAX_LOG_LENGTH) {
                sb.append(name).append("=").append(value).append("&");
            }
        }
        sb.append("  ").append(StringUtils.getIpAddress(request));
        String url = getScheme(request) + "://" + request.getHeader("host") + request.getRequestURI();
        return url + sb.toString();
    }

    private static String getScheme(HttpServletRequest request) {
        String scheme = request.getHeader("X-Forwarded-Scheme");
        return scheme == null ? "http" : scheme;
    }
}