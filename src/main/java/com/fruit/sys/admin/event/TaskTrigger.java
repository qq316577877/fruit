/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.event;

import com.ovfintech.arch.common.event.EventTrigger;
import com.ovfintech.arch.common.event.access.AccessEventContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service("taskTrigger")
public class TaskTrigger implements EventTrigger<AccessEventContext>
{
    private final static Logger LOGGER = LoggerFactory.getLogger(TaskTrigger.class);

    @Override
    public void process(AccessEventContext accessEventContext)
    {
        TaskEvent taskEvent = (TaskEvent) accessEventContext;
        taskEvent.getTask().doTask();
    }
}