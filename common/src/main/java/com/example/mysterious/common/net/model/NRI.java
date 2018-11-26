package com.example.mysterious.common.net.model;

import java.util.List;

public class NRI {
    /**
     * 任务奖励
     */
    public List<MissionReward> tasks;


    public NRI setTasks(List<MissionReward> tasks) {
        this.tasks = tasks;
        return this;
    }


}
