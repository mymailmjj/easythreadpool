/**
 * 
 */
package easy;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import exception.IllegalStatusException;
import queue.LinkedBlockDeque;
import queue.Queue;

/**
 * 目前存在的问题
 * 1.线程运行不均
 * 3.精细化操作  已经添加动态删除
 * 
 * 线程调度算法
 * 
 * 
 * @author mujianjiang
 * 
 */
public class DefaultThreadPool implements Executor {
	
	private static Logger logger = Logger.getLogger("DefaultThreadPool");
	/**
	 *默认的异常处理器
	 * @author mujjiang
	 *
	 */
	private class DefaultExecpetionHandler implements UncaughtExceptionHandler{

		public void uncaughtException(Thread t, Throwable e) {
			logger.warning("线程t"+t.getName()+"发生异常,抛出的异常:"+e.getMessage());
		}
		
	}
	
	/**
	 * 默认的异常处理器
	 */
	private UncaughtExceptionHandler uncaughtExceptionHandler = new DefaultExecpetionHandler();
	
	
	// 任务的容器
	private Queue<Task> queureTask;

	private CopyOnWriteArraySet<WorkThread> workThreads;

	// 线程的状态
	private volatile boolean running = false;

	// 线程的容器
	private final static int DEFAULT_POOL_SIZE = 5;

	private List<WorkThread> workThread = null;

	private AtomicInteger tasknum = new AtomicInteger();

	private int poolSize = 0;

	private int maxPoolSize = 0;

	private Lock lock = new ReentrantLock();

	public DefaultThreadPool() {
		this(DEFAULT_POOL_SIZE, DEFAULT_POOL_SIZE);
	}
	
	public DefaultThreadPool(int poolSize, int maxPoolSize){
		this(poolSize, maxPoolSize,new LinkedBlockDeque<Task>());
	}
	
	public DefaultThreadPool(int poolSize, int maxPoolSize,Queue<Task> queue) {
		if (poolSize < 0) {
			poolSize = DEFAULT_POOL_SIZE;
		}

		if (maxPoolSize < 0) {
			maxPoolSize = DEFAULT_POOL_SIZE;
		}

		this.poolSize = poolSize;

		this.maxPoolSize = maxPoolSize;

		workThread = new ArrayList<WorkThread>();

		workThreads = new CopyOnWriteArraySet<WorkThread>();
		
		this.queureTask = queue;

		initPool();

	}

	private void enlargeThreadPool() {
		logger.info("enlargeThreadPool");
		int enlargeSize = maxPoolSize - poolSize;
		for (int i = 0; i < enlargeSize; i++) {
			WorkThread workThread2 = new WorkThread("线程" + (this.poolSize + i));
			workThread.add(workThread2);
			workThread2.start();
		}

		this.poolSize = this.maxPoolSize;

	}

	private void initPool() {

		logger.info("开始初始化线程");

		for (int i = 0; i < poolSize; i++) {
			String threadname = "线程" + i;
			logger.info("添加线程:" + threadname);
			WorkThread workThread2 = new WorkThread(threadname);
			workThread.add(workThread2);
			workThread2.setUncaughtExceptionHandler(uncaughtExceptionHandler);
		}

		for (int i = 0; i < workThread.size(); i++) {
			WorkThread workThread2 = workThread.get(i);
			logger.info("启动线程:" + workThread2.getName());
			workThread2.start();
		}

	}

	public UncaughtExceptionHandler getUncaughtExceptionHandler() {
		return uncaughtExceptionHandler;
	}

	public void setUncaughtExceptionHandler(
			UncaughtExceptionHandler uncaughtExceptionHandler) {
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
	}

	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see easy.Executor#execute(java.lang.Runnable)
	 */
	public void execute(Task run) {
		logger.info("添加任务：" + run);
		run.init();  //初始化任务 
		queureTask.addFirst(run);
		tasknum.incrementAndGet();
	}

	/**
	 * run里面的算法还是有问题，需要修改
	 * 
	 * @author mujjiang
	 * 
	 */
	private class WorkThread extends Thread {

		public WorkThread(String name) {
			super(name);
		}

		@Override
		public void run() {
			if (!running) {
				running = true;
			}

			while (running) {

				lock.lock();
				if (!queureTask.isEmpty()) {
					Task pollLast = queureTask.pollLast();
					logger.info(Thread.currentThread().getName()
							+ "获得锁，执行task:" + pollLast);
					pollLast.runTask();
					tasknum.decrementAndGet();
				}

				lock.unlock();

				// 这里放大线程数量
				if ((getTaskNum() > poolSize) && (poolSize < maxPoolSize)) {
					enlargeThreadPool();
				}

			}
		}

	}
	
	

	public void shutdown() {

		while (!queureTask.isEmpty()) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		running = false;

		workThread = null;
	}
	
	/**
	 * 尝试删除某个任务
	 * @param t
	 */
	public void tryRemoveTask(Task t){
		checkTaskStatus(t);
		
		
		
		
		
		
	}
	
	/**
	 * 检查任务的当前执行状态
	 * 在运行，返回true
	 * 否则，返回false
	 * @param t
	 */
	private void checkTaskStatus(Task t){
		boolean r = t.isRunning();
		if(r){
			throw new IllegalStatusException();
		}
	}

	/*
	 * 获取当前的线程数量
	 *  (non-Javadoc)
	 * @see easy.Executor#getTaskNum()
	 */
	public int getTaskNum() {
		return tasknum.get();
	}

}
