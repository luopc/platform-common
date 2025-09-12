package com.luopc.platform.cloud.common.process;

import com.luopc.platform.cloud.common.process.config.ProcessConfig;
import com.luopc.platform.cloud.common.process.util.ProcessUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * @author Robin
 */
@Slf4j
@Component
public class ProcessRunner implements DisposableBean, ApplicationRunner {

    @Resource
    private ProcessConfig processConfig;
    private Process proc;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Going to start {}.", processConfig.getName());

        if (StringUtils.hasLength(processConfig.getExec())) {
            proc = startProcess(processConfig.getExec(), processConfig.getParameters());
            if (proc != null) {
                log.info("setup pid={}", proc.pid());
                processConfig.setRunningPid(proc.pid());
                printLog(proc);
                try {
                    int exitVal = proc.waitFor();
                    if (exitVal == 0) {
                        log.info("{} is shutdown!", processConfig.getName());
                    } else {
                        log.error("{} is shutdown unexpected!", processConfig.getName());
                    }
                    processConfig.setRunningPid(null);
                } catch (InterruptedException e) {
                    log.error("Encounter issue when running {}.", processConfig.getName(), e);
                }

            } else {
                log.error("{} cannot be started!", processConfig.getName());
            }
        } else {
            log.warn("exeFile[{}] cannot be found.", processConfig.getExec());
        }

    }

    private Process startProcess(String exec, Map<String, String> parameters) {
        URL sourcePath = this.getClass().getClassLoader().getResource(exec);
        String exeFile = ProcessUtil.getRealPathByFileName(exec, sourcePath);
        if (StringUtils.hasLength(exeFile)) {
            List<String> cmdAndArgs = new ArrayList<>();

            cmdAndArgs.add(exeFile); // add exec
            //add parameters
            Optional.ofNullable(parameters).orElse(new HashMap<>()).forEach((key, value) -> cmdAndArgs.add(processConfig.getParamPrefix() + ProcessUtil.decode(key) + "=" + value));

            log.info("parameters={}", cmdAndArgs);

            ProcessBuilder builder = new ProcessBuilder();
            builder.redirectErrorStream(true);
            if (StringUtils.hasLength(processConfig.getWorkPath())) {
                builder.directory(new File(processConfig.getWorkPath())); // add workPath
                log.info("workPath={}", processConfig.getWorkPath());
            }

            builder.command(cmdAndArgs.toArray(new String[0]));
            Process process = null;
            try {
                process = builder.start();

                log.info("\n{}\n" +
                                "\tpid:\t{}" +
                                "\n\tname:\t{}" +
                                "\n\texec:\t{}" +
                                "\n\tworkPath:\t{}" +
                                "\n\tparameters:\t{}" +
                                "\n-------------------------------------------------------------------------------------\n",
                        process.info(), process.pid(), processConfig.getName(), exeFile, processConfig.getWorkPath(), cmdAndArgs);
                return process;
            } catch (IOException e) {
                log.error("Encounter issue when running {}.", processConfig.getName(), e);
            }
        }
        return null;
    }

    private void printLog(Process process) {
        //SequenceInputStream是一个串联流，能够把两个流结合起来，通过该对象就可以将 getInputStream方法和getErrorStream方法获取到的流一起进行查看了，当然也可以单独操作
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new SequenceInputStream(process.getInputStream(), process.getErrorStream()), ProcessUtil.getLocalCharSet()))) {
            String line = null;
            while ((line = br.readLine()) != null) {//循环读取缓冲区中的数据
                log.info(line);
            }
        } catch (IOException e) {
            log.error("Encounter issue when running {}.", processConfig.getName(), e);
        }
    }

    @Override
    public void destroy() {
        log.info("Going to shutdown {}, pid = {}.", processConfig.getName(), processConfig.getRunningPid());
        try {
            //shutdown by command
            if (StringUtils.hasLength(processConfig.getStopExec())) {
                Process shutDownProcess = startProcess(processConfig.getStopExec(), processConfig.getStopParameters());
                if (shutDownProcess != null) {
                    printLog(shutDownProcess);
                    int exitVal = shutDownProcess.waitFor();
                    if (exitVal == 0) {
                        log.info("ShutDown process completed !");
                    } else {
                        log.error("ShutDown process is in-completed!");
                    }
                }
                int i = 0;
                while (proc != null && i < 20) {
                    Thread.sleep(1000);
                    i++;
                }
            }
            if (processConfig.getRunningPid() != null) {
                //shutdown by pid: kill -s 15 PID or win Taskkill /pid PID
                String stopCommend = ProcessUtil.getStopCommand(proc.pid());
                log.info("going to shutdown {} by {}", processConfig.getName(), stopCommend);
                Process stopProcess = Runtime.getRuntime().exec(stopCommend);
                if (stopProcess != null) {
                    printLog(stopProcess);
                    int exitVal = stopProcess.waitFor();
                    if (exitVal == 0) {
                        log.info("Stop process completed !");
                    } else {
                        log.error("Stop process is in-completed!");
                    }
                    int i = 0;
                    while (proc != null && i < 20) {
                        Thread.sleep(1000);
                        i++;
                    }
                }


                if (processConfig.getRunningPid() != null) {
                    //kill pid kill -s 9 PID or win  Taskkill /f /pid PID
                    String killCommend = ProcessUtil.getKillCommand(proc.pid());
                    log.info("going to kill {} by {}", processConfig.getName(), killCommend);
                    Process killProcess = Runtime.getRuntime().exec(killCommend);
                    if (killProcess != null) {
                        printLog(killProcess);
                        int exitVal = killProcess.waitFor();
                        if (exitVal == 0) {
                            log.info("Kill process completed !");
                        } else {
                            log.error("Kill process is in-completed!");
                        }
                        int i = 0;
                        while (proc != null && i < 20) {
                            Thread.sleep(1000);
                            i++;
                        }
                    }
                }
            }

            //destroy proc
            if (proc != null) {
                log.info("going to destroy {}: pid = {}, proc = {}", processConfig.getName(), processConfig.getRunningPid(), proc.info());
                proc.destroy();
            }
        } catch (IOException | InterruptedException e) {
            log.error("Encounter issue when shutdown {}.", processConfig.getName(), e);
        }
    }

}
