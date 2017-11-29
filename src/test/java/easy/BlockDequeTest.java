/**
 * 
 */
package easy;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import queue.LinkedBlockDeque;
import queue.LinkedBlockDeque;

/**
 * @author mujjiang
 * 
 */
public class BlockDequeTest {

	public static void main(String[] args) {

		Thread threads[] = new Thread[10];

		final LinkedBlockDeque<String> queues = new LinkedBlockDeque<String>(10);
		
	/*	queues.offerFirst("a");
		
		queues.offerFirst("b");
		
		queues.offerFirst("c");
		
		queues.offerFirst("d");
		
		queues.offerFirst("e");
		
		queues.offerLast("z");
		
		queues.offerLast("y");
		
		queues.offerLast("x");*/
		
		
		/*queues.putFirst("a");
		
		queues.putFirst("b");
		
		queues.putFirst("c");
		
		queues.putFirst("d");
		
		queues.putFirst("e");
		
		queues.putLast("z");
		
		queues.putLast("y");
		
		queues.putLast("x");*/
		
	/*	queues.putFirst("a");
		
		queues.putFirst("b");
		
		queues.putFirst("c");
		
		queues.putFirst("d");
		
		queues.putFirst("e");
		
		queues.putLast("z");
		
		queues.putLast("y");
		
		queues.putLast("x");
		
		String taskFirst = queues.taskFirst();
		
		System.out.println("取出了taskfirst:"+taskFirst);
		
		taskFirst = queues.taskLast();
		
		System.out.println("取出了taskfirst:"+taskFirst);
		
		queues.printAll();*/
		
		

		final AtomicInteger m = new AtomicInteger(0);

		for (int i = 0; i < 5; i++) {
			threads[i] = new Thread(new Runnable() {

				public void run() {

					while (m.get()<20) {
							try {
								TimeUnit.SECONDS.sleep(ThreadLocalRandom
										.current().nextInt(2));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							int andIncrement = m.getAndIncrement();
							String str = "str" + andIncrement;
							queues.offerLast(str);
					}

				}
			}, "线程" + i);
		}

		for (int i = 5; i < 10; i++) {
			threads[i] = new Thread(new Runnable() {

				public void run() {
					while (true) {
						try {
							TimeUnit.SECONDS.sleep(2);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						String pollLast = queues.pollLast();
						System.out.println(Thread.currentThread().getName()+"线程取出:"+pollLast);
					}
				}
			}, "线程" + i);
		}

		for (int i = 0; i < 10; i++) {

			threads[i].start();

		}
		
		for(int i =0 ;i<5;i++){
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
