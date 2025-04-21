package com.ezhixuan.xuanblog_backend.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class ThumbSyncTask {

    public static String CURRENT_TIME = "currentTime";

    @PostConstruct
    public void init() {
        updateCurrentTimeTask();
    }

    /**
     * 定时更新时间以便任务统计
     * @author Ezhixuan
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void updateCurrentTimeTask() {
        LocalDate localDate = LocalDate.now();
        CURRENT_TIME = localDate.format(DateTimeFormatter.ofPattern("MM:dd"));
    }


}
