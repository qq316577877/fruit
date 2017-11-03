package com.fruit.sys.admin.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.mtc.threadpool.MtContextExecutors;
import com.ovfintech.concurrent.TaskProcess;
import com.ovfintech.concurrent.TaskProcessFactory;
import com.ovfintech.concurrent.TaskProcessManager;

public class TaskProcessUtil {

	/**
	 * 核心线程数
	 */
	private static int DEFAULT_CORE_SIZE = Runtime.getRuntime().availableProcessors();

	/**
	 * 线程池大小
	 */
	private static int DEFAULT_POOL_SIZE = DEFAULT_CORE_SIZE * 2;

	/**
	 * 初始始化线程工厂
	 */
	private static TaskProcessFactory mtTaskProcessFactory = new TaskProcessFactory() {
		@Override
		public TaskProcess createTaskProcess(String domain) {
			// 有界队列
			BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(1);
			ExecutorService executor = new ThreadPoolExecutor(DEFAULT_CORE_SIZE, DEFAULT_POOL_SIZE, 60,
					TimeUnit.SECONDS, queue, new DefaultThreadFactory(domain),
					new ThreadPoolExecutor.CallerRunsPolicy());
			ExecutorService mtExecutor = MtContextExecutors.getMtcExecutorService(executor);
			TaskProcess tp = new TaskProcess(mtExecutor);
			return tp;
		}

	};

	/**
	 * 取的多线程的任务处理
	 * 
	 * @return
	 */
	public static TaskProcess getMtTaskProcess() {
		return TaskProcessManager.getTaskProcess(TaskProcessUtil.class.getName(), mtTaskProcessFactory);
	}

	/**
	 * The default thread factory
	 */
	static class DefaultThreadFactory implements ThreadFactory {
		static final AtomicInteger poolNumber = new AtomicInteger(1);

		final ThreadGroup group;

		final AtomicInteger threadNumber = new AtomicInteger(1);

		final String namePrefix;

		DefaultThreadFactory(String domain) {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			namePrefix = domain + "-TaskProcess-" + poolNumber.getAndIncrement() + "-thread-";
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			if (t.isDaemon())
				t.setDaemon(false);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
	}
}
