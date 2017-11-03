/**
 * 
 */
package easy;

/**
 * @author mujianjiang
 *总的调度的接口
 *
 */
public interface Executor {
	
	public void execute(Task run);
	
	public void shutdown();
	
	public int getTaskNum();   //获取线程数量

}
