package queue;

public interface Queue<U> {

	/**
	 * 头部加入的接口
	 * 
	 * @param u
	 */
	public void offerFirst(U u);

	/**
	 * 尾部加入的接口
	 * 
	 * @param u
	 */
	public boolean offerLast(U u);

	/**
	 * 删除头部元素
	 * 
	 * @return
	 */
	public U pollFirst();

	/**
	 * 
	 * 删除尾部元素
	 * 
	 * @return
	 */
	public U pollLast();

	/**
	 * 阻塞方法，在头部插入元素
	 * 
	 * @param u
	 */
	public void putFirst(U u);

	/**
	 * 阻塞方法，在尾部插入元素
	 * 
	 * @param u
	 */
	public void putLast(U u);

	/**
	 * 阻塞方法，取出头部元素
	 * 
	 */
	public U taskFirst();

	/**
	 * 阻塞方法，取出尾部元素
	 * 
	 * @return
	 */
	public U taskLast();

	/**
	 * 是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty();

	/**
	 * 删除某个任务
	 * 
	 * @param u
	 * @return
	 */
	public boolean remove(U u);

}
