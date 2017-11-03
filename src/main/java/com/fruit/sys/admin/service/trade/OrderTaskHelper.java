package com.fruit.sys.admin.service.trade;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class OrderTaskHelper
{
    private ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(3, 3, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.DiscardPolicy());

    public void submitRunnable(Runnable runnable)
    {
        this.threadPoolExecutor.execute(runnable);
    }

}
