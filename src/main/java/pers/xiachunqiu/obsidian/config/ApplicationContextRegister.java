package pers.xiachunqiu.obsidian.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * 设置全局上下文，便于全局使用
 *
 * @author Hugh
 * @since 2018-12-10 09:05:47
 */
@Component
@Lazy(false)
@Log4j2
public class ApplicationContextRegister implements ApplicationContextAware {
    private static ApplicationContext APPLICATION_CONTEXT;

    @Override
    @ParametersAreNonnullByDefault
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }
}