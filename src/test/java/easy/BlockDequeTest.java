/**
 * 
 */
package easy;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import queue.LinkedBlockDeque;

/**
 * @author mujjiang
 * 
 */
public class BlockDequeTest {

	public static void main(String[] args) {

		Thread threads[] = new Thread[10];

		final LinkedBlockDeque<String> queues = new LinkedBlockDeque<String>(10);

		final AtomicInteger m = new AtomicInteger(0);

		for (int i = 0; i < 5; i++) {
			threads[i] = new Thread(new Runnable() {

				public void run() {

					while (true) {

						synchronized (queues) {
							try {
								TimeUnit.SECONDS.sleep(ThreadLocalRandom
										.current().nextInt(2));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							int andIncrement = m.getAndIncrement();
							String str = "str" + andIncrement;
							queues.addLast(str);
						}
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
						queues.pollLast();
					}
				}
			}, "线程" + i);
		}

		for (int i = 0; i < 10; i++) {

			threads[i].start();

		}

	}

}
