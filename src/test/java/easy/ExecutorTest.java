package easy;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ExecutorTest {

	public static void main(String[] args) {
		
		TestNum testNum = new TestNum();

		DefaultThreadPool defaultThreadPool = new DefaultThreadPool(5, 30);

		Task delete = null;

		for (int i = 0; i < 10; i++) {
			final int j = i;
			defaultThreadPool.execute(new AddTask(testNum));

			Task t = new Task(j) {

				public void run() {

					try {
						int nextInt = ThreadLocalRandom.current().nextInt(5);
						System.out.println(Thread.currentThread().getName() + "執行task" + j + "\t" + nextInt + "秒");
						TimeUnit.SECONDS.sleep(nextInt);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					System.out.println(Thread.currentThread().getName() + "執行task" + j + "完毕");
				}
			};

			if (i == 0) {
				delete = t;
			}

			defaultThreadPool.execute(t);
		}
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int i = testNum.getI();
		
		System.out.println("最终计算结果："+i);

		System.out.println("---------------------------------------------------");

		defaultThreadPool.tryRemoveTask(delete);

		defaultThreadPool.shutdown();

	}

}
