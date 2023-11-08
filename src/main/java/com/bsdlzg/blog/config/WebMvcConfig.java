package com.bsdlzg.blog.config;

import com.bsdlzg.blog.config.intercept.AccessLimitIntercept;
import com.bsdlzg.blog.config.intercept.PageableInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author bsdlzg
 * @date 2022/3/10
 * @apiNote
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-folder}")
    private String UPLOAD_FOLDER;

    /**
     * 这里需要先将限流拦截器入住，不然无法获取到拦截器中的redistemplate
     *
     * @return
     */
    @Bean
    public AccessLimitIntercept getAccessLimitIntercept() {
        return new AccessLimitIntercept();
    }

    /**
     * 注册sa-token的拦截器，打开注解式鉴权功能 (如果您不需要此功能，可以删除此类)
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //分页拦截器
        registry.addInterceptor(new PageableInterceptor());
        //IP限流拦截器
        registry.addInterceptor(getAccessLimitIntercept()).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/img/**").addResourceLocations("file:" + UPLOAD_FOLDER);
    }
}
