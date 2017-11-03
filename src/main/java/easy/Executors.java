/**
 * 
 */
package easy;

/**
 * @author mujjiang
 *
 */
public class Executors {
	
	/**
	 * 创建一个固定大小的线程池
	 * @param fixThread
	 * @return
	 */
	public static Executor newFixedPoolExecutors(int fixThread){
		return new DefaultThreadPool(fixThread, fixThread);
	}

}
