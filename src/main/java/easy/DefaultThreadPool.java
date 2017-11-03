/**
 * 
 */
package easy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * @author mymai
 * 
 */
public class DefaultThreadPool implements Executor {

	private static Logger logger = Logger.getLogger("DefaultThreadPool");

	// 任务的容器
	private LinkedBlockingDeque<Task> queureTask = new LinkedBlockingDeque<Task>();

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

	public DefaultThreadPool(int poolSize, int maxPoolSize) {
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
			workThread.add(new WorkThread(threadname));
		}

		for (int i = 0; i < workThread.size(); i++) {
			WorkThread workThread2 = workThread.get(i);
			logger.info("启动线程:" + workThread2.getName());
			workThread2.start();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see easy.Executor#execute(java.lang.Runnable)
	 */
	public void execute(Task run) {
		logger.info("添加任务：" + run);
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
					pollLast.run();
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

		while (queureTask.size() > 0) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		running = false;

		workThread = null;
	}

	public int getTaskNum() {
		return tasknum.get();
	}

}
