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
	
	protected int id;    //taskid each task has the only one id
	
	protected String name;   //taskname each task has the only name
	
	public Task(int id) {  //support three constructor
		this.id = id;
	}
	
	public Task(String name) {
		this.name = name;
	}
	
	public Task(int id, String name) {
		this.id = id;
		this.name = name;
	}

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

	/**
	 * 
	 * letsub task to implement
	 */
	protected void beforeRun(){
	}
	
	/**
	 * letsub task to implement
	 * 
	 */
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

	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int hashCode() {
		if(this.name!=null){
			int sum = 0;
			char[] charArray = this.name.toCharArray();
			for (int i = 0; i < charArray.length; i++) {
				char c = charArray[i];
				sum += c;
			}
		}
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null) return false;
		if(obj==this) return true;
		if(obj instanceof Task){
			Task o = (Task)obj;
			if(this.name!=null){
				return o.name.equals(this.name);
			}else{
				return o.id == this.id;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Task [status=" + status + ", id=" + id + ", name=" + name + "]";
	}

	
}
