package org.lzx.admin;

import lombok.extern.slf4j.Slf4j;
import org.lzx.common.utils.IpUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@Slf4j
@EnableCaching
@ComponentScan({"org.lzx.admin","org.lzx.frame","org.lzx.common","org.lzx.system"})
@EnableAspectJAutoProxy(exposeProxy = true)
public class AdminApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AdminApplication.class);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AdminApplication.class, args);
        String hostIp = IpUtil.getHostIp();
        String port = context.getEnvironment().getProperty("server.port");
        log.info("Swagger访问地址：http://{}:{}/doc.html", hostIp, port);
    }

}
