/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.event;

import com.ovfintech.arch.common.event.access.AccessEventContext;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2014-12-09
 * Project        : bonus
 * File Name      : TaskEvent.java
 */
public class TaskEvent extends AccessEventContext
{
    private ITask task;

    public TaskEvent(ITask task)
    {
        this.task = task;
    }

    public ITask getTask()
    {
        return task;
    }

    public void setTask(ITask task)
    {
        this.task = task;
    }
}
