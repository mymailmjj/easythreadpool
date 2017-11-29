package easy;

import java.util.concurrent.TimeUnit;

public class ExecutorTest {

	public static void main(String[] args) {
		
		TestNum testNum = new TestNum();

		DefaultThreadPool defaultThreadPool = new DefaultThreadPool(5, 30);

		for (int i = 0; i < 10; i++) {
			
			if(i < 5){
				defaultThreadPool.execute(new AddTask(testNum));
			}else{
				defaultThreadPool.execute(new SubTask(testNum));
			}
		}
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int i = testNum.getI();
		
		System.out.println("最终计算结果："+i);

		defaultThreadPool.shutdown();

	}

}
