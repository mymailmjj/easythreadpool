package easy;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ExecutorTest {

	public static void main(String[] args) {
		DefaultThreadPool defaultThreadPool = new DefaultThreadPool(5,30);
		for(int i = 0; i < 100;i++){
			final int j = i;
			defaultThreadPool.execute(new Task() {
				
				public void run() {
					
					try {
						int nextInt = ThreadLocalRandom.current().nextInt(5);
						System.out.println(Thread.currentThread().getName()+"執行task"+j+"\t"+nextInt+"秒");
						TimeUnit.SECONDS.sleep(nextInt);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					System.out.println(Thread.currentThread().getName()+"執行task"+j+"完毕");
				}
			});
		}
		
		
		defaultThreadPool.shutdown();
		
	}

}
