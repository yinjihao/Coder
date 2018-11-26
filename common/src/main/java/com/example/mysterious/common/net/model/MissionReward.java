package com.example.mysterious.common.net.model;

import java.io.Serializable;

/**
 * @author hanbo1@yiche.com
 * @ClassName MissionReward
 * @Description 任务奖励窗口
 * @date 2015-3-23 下午2:22:45
 */
public class MissionReward implements Serializable {


    private static final long serialVersionUID = 2314261483730579113L;
    /*id	    int	任务ID
	taskType	int	任务类型(1.新手任务 2.每日任务)	 
	taskName	string 任务名称	 
	repeatCount	int 任务操作需要执行的次数	 
	reward	    int	完成任务的奖励	 
	progress	int	任务完成进度	 
	done	    bool 任务是否完成	 
	award	    bool 任务是否领取奖励	 
	firstDone	bool 是否是第一完成任务，只有在任务关联操作刚好达到完成次数时，返回true	*/

    public int id;
    public int taskType;
    public String taskName;
    public int repeatCount;
    public int reward;
    public int progress;
    public boolean done;
    public boolean award;
    public boolean firstDone;


    @Override
    public String toString() {
        return "MissionReward [id=" + id + ", taskType=" + taskType
                + ", taskName=" + taskName + ", repeatCount=" + repeatCount
                + ", reward=" + reward + ", progress=" + progress + ", done="
                + done + ", bool=" + award + ", firstDone=" + firstDone + "]";
    }


}
