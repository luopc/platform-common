package com.luopc.platform.web.util;

import com.google.common.collect.Lists;
import com.luopc.platform.common.core.util.SmartNumberUtil;
import com.luopc.platform.common.core.util.SmartRandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

/**
 * @author Robin
 */
@Slf4j
public class HttpClientPortUtil {

    private static final List<Integer> START_NUM = Lists.newArrayList(40, 42, 62, 43, 63, 44, 64);


    public static int getAvailableTcpPort() {
        // 指定范围10000到65535
        for (int i = 0; i <= 200; i++) {
            int port = randomPort();
            try {
                return getAvailableTcpPort(port);
            } catch (IOException e) {
                // ignore, try next one
                log.warn("Cannot set port to [{}] ", port);
            }
        }
        return -1;
    }

    public static int randomPort() {
        String startNum = String.format("%02d", SmartRandomUtil.randomGet(START_NUM));
        String middleNum = String.format("%02d", SmartNumberUtil.randomNumber(3, 58));
        return Integer.parseInt(startNum + middleNum + 7);
    }


    private static int getAvailableTcpPort(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        int localPort = serverSocket.getLocalPort();
        serverSocket.close();
        return localPort;
    }

}
