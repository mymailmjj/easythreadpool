package easy;

/**
 * 自定义的任务类型
 * @author mujjiang
 *
 */
public abstract class Task implements Runnable{

	/**
	 * 这里先设置任务的状态定义
	 * 暂时定义三种状态
	 * 0001  准备就绪
	 * 0010 开始执行
	 * 0100 执行中
	 * 1000 执行完毕
	 */
	
	private int status;
	
	private final static int READY = 1;      //0001
	
	private final static int START = 1 << 1;  //0010
	
	private final static int RUNNING = 1 << 2;  //0100
	
	private final static int STOP = 1 << 4;  //1000
	
	protected int id;
	
	public void init(){
		status = READY;
	}
	
	public int getStatus() {
		return status;
	}
	
	public boolean isRunning(){
		return this.status > START&&this.status < STOP;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	protected void beforeRun(){
		status = START;
	}
	
	protected void afterRun(){
		
	}
	
	/**
	 * 封装run的方法
	 */
	protected void runTask(){
		setStatus(START);
		beforeRun();
		setStatus(RUNNING);
		run();
		afterRun();
		setStatus(STOP);
	}
	
}
