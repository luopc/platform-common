package com.luopc.platform.web.config;

import com.luopc.platform.web.util.HttpClientPortUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Robin
 */
@Slf4j
@Getter
@Setter
@Component
public class BootServerPortConfig implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    @Value("${server.port}")
    private Integer serverPort;

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        WebServer server = factory.getWebServer();
        int port = Math.max(server.getPort(), serverPort);
        log.info("Get config ServerPort [{}]", port);
        if (port <= 0) {
            log.warn("Server port is not valid, try to find a available tcp port");
            port = HttpClientPortUtil.getAvailableTcpPort();
            factory.setPort(port);
        }
        log.info("Reset ServerPort to [{}]", port);
        ServerConfigHolder.SERVER_CONFIG.setServerPort(port);
    }

    private BootServerPortConfig() {
    }

    private static class ServerConfigHolder {
        private static final BootServerPortConfig SERVER_CONFIG = new BootServerPortConfig();
    }

    public static BootServerPortConfig getInstance() {
        return ServerConfigHolder.SERVER_CONFIG;
    }
}
