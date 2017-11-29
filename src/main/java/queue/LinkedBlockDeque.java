/**
 * 
 */
package queue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * @author mujjiang
 * 
 */
public class LinkedBlockDeque<U> implements Queue<U> {

	private static Logger logger = Logger.getLogger("queue.LinkedBlockDeque");

	private volatile int capacity = 0; // 队列容量,默认为0，如果为0，则表示大小没有限制

	private volatile int size = 0;

	private Node<U> head;

	private Node<U> tail;

	private Lock lock = new ReentrantLock();

	private Condition emptyCondition = lock.newCondition(); // 判断为空的条件

	private Condition fullCondition = lock.newCondition(); // 判断为满的条件

	public LinkedBlockDeque(int capacity) {
		this.capacity = capacity;
	}

	public LinkedBlockDeque() {
		this(Integer.MAX_VALUE);
	}

	final static class Node<U> {

		U u;

		Node<U> next;

		Node<U> prev;

		public Node(U u, Node<U> next, Node<U> prev) {
			this.u = u;
			this.next = next;
			this.prev = prev;
		}

		public Node(U u) {
			this.u = u;
		}

		@Override
		public String toString() {
			return "Node [ u=" + u + ")]";
		}

	}

	/**
	 * 
	 * 核心头部添加的方法 内部方法 头部添加
	 * 
	 * @param node
	 */
	private boolean linkHead(Node<U> node) {

		if (this.size > this.capacity)
			return false;

		// 如果头结点为空
		if (this.head == null) {
			this.head = node;
			this.tail = node;
		} else {
			node.next = this.head;
			this.head.prev = node;
			this.head = node;
		}

		this.size++;

		emptyCondition.signal();

		return Boolean.TRUE;
	}

	/**
	 * 核心尾部添加的方法 内部方法 尾部添加
	 * 
	 * @param node
	 */
	private boolean linkLast(Node<U> node) {

		if (this.size > this.capacity)
			return false;

		if (this.tail == null) {
			this.tail = node;
			this.head = node;
		} else {
			node.prev = this.tail;
			this.tail.next = node;
			this.tail = node;
		}

		this.size++;

		emptyCondition.signal();

		return Boolean.TRUE;
	}

	/**
	 * 核心头部删除逻辑
	 * 
	 * @return
	 */
	private Node<U> unlinkHead() {

		Node<U> deleteNode = this.head;

		if (this.head.next != null) {
			// 删除head，重新置
			this.head.next.prev = null;

			this.head = this.head.next;
		} else {
			this.head = null;
		}

		size--;

		logger.info("LinkedBlockDeque poll first element:" + deleteNode
				+ ",\tblocksize:" + this.size);

		fullCondition.signal();

		return deleteNode;

	}

	/**
	 * 核心尾部删除逻辑
	 * 
	 * @return
	 */
	private Node<U> unlinkLast() {
		
		if(this.size<=0) return null;

		Node<U> deleteNode = this.tail;
		
		if(this.tail==null) return null;

		if (this.tail.prev != null) {
			// 删除head，重新置
			this.tail.prev.next = null;

			this.tail = this.tail.prev;
		}

		logger.info("LinkedBlockDeque poll last element:" + deleteNode
				+ ",\tblocksize:" + this.size);

		size--;

		fullCondition.signal(); // 唤醒其他线程

		return deleteNode;

	}

	/**
	 * 头部加入
	 * 
	 * @param u
	 */
	public void offerFirst(U u) {

		// 判断容量是否超限制

		checkArg(u);
		lock.lock();

		Thread currentThread = Thread.currentThread();

		logger.info("线程名:" + currentThread.getName() + "addFirst加锁");

		// 新建要插入的结点

		Node<U> node = new Node<U>(u);

		linkHead(node);

		lock.unlock();

		logger.info("线程名:" + currentThread.getName() + "addFirst释放锁");

	}

	private void checkArg(U u) {
		if (u == null) {
			throw new IllegalArgumentException("参数不能为空!");
		}
	}

	/**
	 * 尾部加入
	 * 
	 * @param u
	 */
	public boolean offerLast(U u) {
		checkArg(u);

		boolean result = Boolean.FALSE;

		lock.lock();

		Node<U> node = new Node<U>(u);
		result = linkLast(node);

		logger.info("LinkedBlockDeque last add element:" + node
				+ ",\tblocksize:" + this.size);

		lock.unlock();

		return result;

	}

	/**
	 * 打印遍历所有的变量 测试使用
	 */
	public void printAll() {

		if (this.head == null)
			return;

		Node<U> temp = this.head;

		while (temp != null) {
			System.out.println(temp);
			temp = temp.next;
		}

	}

	/**
	 * 从头部取出并删除头部元素
	 * 
	 * @return
	 */
	public U pollFirst() {

		lock.lock();

		Node<U> unlinkHead = unlinkHead();

		lock.unlock();

		return unlinkHead==null?null:unlinkHead.u;
	}

	/**
	 * 从尾部取出并删除尾部元素
	 * 
	 * @return
	 */
	public U pollLast() {

		lock.lock();

		Node<U> deleteNode = unlinkLast();

		lock.unlock();

		return deleteNode==null?null:deleteNode.u;
	}

	/**
	 * 取出第一个元素
	 * 
	 * @return
	 */
	public U peekFirst() {
		return this.head.u;
	}

	/**
	 * 取出最后一个元素
	 * 
	 * @return
	 */
	public U peekLast() {
		return this.tail.u;
	}

	public boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 * 
	 * @param u
	 * @return
	 */
	private Node find(U u) {
		if (this.head == null)
			return null;

		Node<U> temp = this.head;

		while (temp != null) {
			if (temp.u.equals(u)) {
				return temp;
			}
			temp = temp.next;
		}

		return temp;
	}

	/**
	 * 非线程安全，使用前应该检查任务状态 删除某个元素，从队列中
	 */
	public boolean remove(U u) {

		Node node = find(u);

		// 判断首节点的情况
		if (node.prev == null) {
			node.next.prev = null;
			this.head = node.next;
			return true;
		}

		// 判断末尾结点的情况
		if (node.next == null) {
			node.prev.next = null;
			this.tail = node;
			return true;
		}

		node.next.prev = node.prev;

		node.prev.next = node.next;

		return true;
	}

	public void putFirst(U u) {

		lock.lock();

		Node<U> node = new Node<U>(u);

		try {
			while (!linkHead(node)) {
				fullCondition.await();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void putLast(U u) {

		lock.lock();

		try {
			Node<U> node = new Node<U>(u);
			while (!linkHead(node)) {
				emptyCondition.await();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	public U taskFirst() {

		lock.lock();

		try {
			Node<U> deletenode;
			while ((deletenode = unlinkHead()) == null) {
				emptyCondition.await();
			}
			return deletenode.u;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

		return null;
	}

	public U taskLast() {

		lock.lock();

		try {
			Node<U> deletenode;
			while ((deletenode = unlinkLast()) == null) {
				emptyCondition.await();
			}

			return deletenode.u;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return null;
	}

}
