/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.event;/*
 * Create Author  : chao.ji
 * Create  Time   : 14/12/5 下午6:15
 * Project        : untitled
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

import com.ovfintech.arch.common.event.multi.Dividable;
import org.springframework.stereotype.Service;

@Service
public class TaskChannel implements Dividable<TaskEvent>
{
    public static final String NAME = "BackgroundTaskChannel";

    @Override
    public String channel(TaskEvent channel)
    {
        return NAME;
    }
}
