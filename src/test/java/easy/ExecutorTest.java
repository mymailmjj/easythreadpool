package easy;

import java.util.concurrent.TimeUnit;

public class ExecutorTest {

	public static void main(String[] args) {
		
		TestNum testNum = new TestNum();

		DefaultThreadPool defaultThreadPool = new DefaultThreadPool(5, 30);

		Task delete = null;

		for (int i = 0; i < 100; i++) {
			defaultThreadPool.execute(new AddTask(testNum));
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
