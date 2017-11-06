/**
 * 
 */
package lock;

import easy.Executor;
import easy.Executors;

/**
 * @author mujjiang
 * 
 */
public class Main {

	public static void main(String[] args) {

		
		Executor newFixedPoolExecutors = Executors.newFixedPoolExecutors(5);

		AddTask addTask = new AddTask(0);

		for (int i = 0; i < 40; i++) {
			newFixedPoolExecutors.execute(addTask);
		}

		int i = addTask.get();

		System.out.println(i);

		newFixedPoolExecutors.shutdown();
		
		
		
		
	/*	ExecutorService newFixedThreadPool = java.util.concurrent.Executors.newFixedThreadPool(2);

		AddTask addTask = new AddTask(0);

		for (int i = 0; i < 5; i++) {
			newFixedThreadPool.execute(addTask);
		}
		
		try {
			newFixedThreadPool.awaitTermination(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int i = addTask.get();

		System.out.println("结果:"+i);

		newFixedThreadPool.shutdown();*/
		

	}

}
