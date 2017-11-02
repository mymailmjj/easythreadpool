/**
 * 
 */
package lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author mujjiang
 * 
 */
public class Main {

	public static void main(String[] args) {
		
		ExecutorService newCachedThreadPool = Executors.newFixedThreadPool(10);
		
		AddTask addTask = new AddTask(0);
		
		for(int i = 0; i< 40; i++){
			newCachedThreadPool.execute(addTask);
		}
		
		try {
			newCachedThreadPool.awaitTermination(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		newCachedThreadPool.shutdown();

	}

}
