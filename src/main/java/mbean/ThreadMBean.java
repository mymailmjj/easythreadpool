/**
 * 
 */
package mbean;

/**
 * 
 * 使线程池具有被管理的特性
 * @author mujjiang
 *
 */
public interface ThreadMBean {
	
	public int getCoreSize();
	
	public void updateCoreSize();
	
	public int getMaxSize();
	
	public void updateMaxSize();
	
	

}
