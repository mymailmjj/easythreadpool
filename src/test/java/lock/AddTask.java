/**
 * 
 */
package lock;

import easy.Task;

/**
 * @author mujjiang
 *
 */
public class AddTask implements Task {
	
	private Lock lock = new CLHLock();
	
	private volatile int i;
	
	public AddTask(int i) {
		this.i = i;
	}

	private void add(){
		i++;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		lock.lock();
		
		if(i < 20){
			System.out.println("i+=:"+i);
			add();
		}else{
			System.out.println("i-=:"+i);
			sub();
		}
		
		lock.unlock();
	}
	
	private void sub(){
		i--;
	}
	
	public int get(){
		return i;
	}

}
