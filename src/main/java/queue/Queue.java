package queue;

public interface Queue<U> {
	
	/**
	 * 头部加入的接口
	 * 
	 * @param u
	 */
	public void addFirst(U u);
	
	/**
	 * 尾部加入的接口
	 * 
	 * @param u
	 */
	public void addLast(U u);

	
	/**
	 * 拉出头部元素 
	 * 
	 * @return
	 */
	public U pollFirst();
	
	
	/**
	 * 
	 * 拉出最尾部元素
	 * @return
	 */
	public U pollLast();
	
	
	/**
	 * 是否为空
	 * @return
	 */
	public boolean isEmpty();
	
}
