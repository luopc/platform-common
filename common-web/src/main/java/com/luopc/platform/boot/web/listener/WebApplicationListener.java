package com.luopc.platform.boot.web.listener;


import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Robin
 */
@Slf4j
@Component
@Order(value = 1000)
public class WebApplicationListener implements ApplicationListener<WebServerInitializedEvent> {

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        WebServer server = webServerInitializedEvent.getWebServer();
        WebServerApplicationContext context = webServerInitializedEvent.getApplicationContext();
        Environment env = context.getEnvironment();

        String ip = NetUtil.getLocalhost().getHostAddress();
        Integer port = server.getPort();

        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null) {
            contextPath = "";
        }
        String projectName = Optional.ofNullable(env.getProperty("spring.application.name")).orElse(env.getProperty("application.name"));
        String profile = env.getProperty("spring.profiles.active");
        String title = String.format("--------------- ACTIVATION COMPLETED for [%s%s] ---------------", projectName, StringUtils.isNotBlank(profile) ? "(" + profile + ")" : "");
        String localhostUrl = URLUtil.normalize(String.format("http://localhost:%d%s", port, contextPath), false, true);
        String externalUrl = URLUtil.normalize(String.format("http://%s:%d%s", ip, port, contextPath), false, true);
        String monitorUrl = URLUtil.normalize(String.format("http://localhost:%d%s/actuator", port, contextPath), false, true);
        String prometheusUrl = URLUtil.normalize(String.format("http://localhost:%d%s/actuator/prometheus", port, contextPath), false, true);
        String apiUrl = URLUtil.normalize(String.format("http://localhost:%d%s/swagger-ui/index.html", port, contextPath), false, true);
        log.info("\n{}\n" +
                        "\tAPI:\t\t{}" +
                        "\n\tLocal:\t\t{}" +
                        "\n\tExternal:\t{}" +
                        "\n\tMonitor:\t{}" +
                        "\n\tPrometheus:\t{}" +
                        "\n-------------------------------------------------------------------------------------\n",
                title, apiUrl, localhostUrl, externalUrl, monitorUrl, prometheusUrl);
    }
}
