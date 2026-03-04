package com.luopc.platform.web.common.core.config;

import org.springframework.context.event.EventListener;

import java.util.Properties;
/**
 * <a href="https://mp.weixin.qq.com/s/vzsTzWDPA6W149FXs5oEGQ">利用观察者模式实现动态刷新Bean中的配置</a>
 * <a href="https://mp.weixin.qq.com/s/W0o1_FkNg_uUxXCn7OekyA">SpringBoot中10种动态修改配置的方法</a>
 */
public class BeanFactoryDemo {

    //@Bean
//    public SomeServiceClient someServiceClient(
//        final ResourceLoader resourceLoader,
//        final EncryptionDecryptionService encryptionDecryptionService
//            ) {
//        final Properties properties = ResourcesUtils.loadProperties(
//                            resourceLoader,
//                            ResourcesUtils.CLASSPATH_META_INF + "/some-service.properties"
//                        );
//
//        final PropertyLoader propertyLoader = new PropertyLoader(encryptionDecryptionService, properties);
//
//        return new SomeServiceClient(propertyLoader);
//    }
//
//    @EventListener
//    public void handleConfigRefreshEvent(final ConfigRefreshEvent event) {
//        if (event.getProjects().contains("my-config-project")) {
//            ConfigRefreshObserverNotifier.notifyObservers();
//        }
//    }
}
