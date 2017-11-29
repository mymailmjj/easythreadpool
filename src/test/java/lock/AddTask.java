/**
 * 
 */
package lock;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import easy.Task;

/**
 * @author mujjiang
 *
 */
public class AddTask extends Task {
	
	private Lock lock = new CLHLock();
	
//	private java.util.concurrent.locks.Lock lock = new ReentrantLock();
	
	private volatile int i;
	
	public AddTask(int i) {
		super(i);
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
		
		if(i==7){
			int j = 2/0;
			
			/*try {
				FileReader reader = new FileReader(new File("c://logs"));
			} catch (FileNotFoundException e) {
				throw e;
			}*/
		}
		
		if(i < 20){
			System.out.println(Thread.currentThread().getName()+"\ti+=:"+i);
			add();
		}else{
			System.out.println(Thread.currentThread().getName()+"\ti-=:"+i);
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
