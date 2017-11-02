/**
 * 
 */
package queue;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mujjiang
 * 
 */
public class LinkedBlockDeque<U> {

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
			return "Node [" + (u != null ? "u=" + u + ", " : "") + "]";
		}

	}

	/**
	 * 内部方法 头部添加
	 * 
	 * @param node
	 */
	private void addHead(Node<U> node) {

		// 如果头结点为空
		if (this.head == null) {
			this.head = node;
			this.tail = node;
			return;
		}

		node.next = this.head;
		this.head.prev = node;
		this.head = node;
	}

	/**
	 * 内部方法 尾部添加
	 * 
	 * @param node
	 */
	private void addlast(Node<U> node) {

		if (this.tail == null) {
			this.tail = node;
			this.head = node;
			return;
		}

		node.prev = this.tail;
		this.tail.next = node;
		this.tail = node;
	}

	/**
	 * 头部加入
	 * 
	 * @param u
	 */
	public void addFirst(U u) {

		lock.lock();

		while (this.size >= capacity) {
			try {
				fullCondition.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// 判断容量是否超限制

		checkArg(u);
		// 新建要插入的结点

		Node<U> node = new Node<U>(u);

		addHead(node);

		this.size++;

		emptyCondition.signalAll();

		lock.unlock();

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
	public void addLast(U u) {

		lock.lock();

		while (this.size > capacity) {
			try {
				emptyCondition.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		checkArg(u);
		Node<U> node = new Node<U>(u);
		addlast(node);

		this.size++;

		emptyCondition.signal();

		lock.unlock();

	}

	/**
	 * 打印遍历所有的变量
	 */
	private void printAll() {

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

		while (size <= 0) {
			try {
				emptyCondition.awaitNanos(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		Node<U> deleteNode = this.head;

		if (this.head.next != null) {
			// 删除head，重新置
			this.head.next.prev = null;

			this.head = this.head.next;
		} else {
			this.head = null;
		}

		size--;

		fullCondition.signal();
		lock.unlock();

		return deleteNode.u;
	}

	/**
	 * 从尾部取出并删除尾部元素
	 * 
	 * @return
	 */
	public U pollLast() {

		lock.lock();

		while (size <= 0) {
			try {
				emptyCondition.awaitNanos(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		Node<U> deleteNode = this.tail;

		if (this.tail.prev != null) {
			// 删除head，重新置
			this.tail.prev.next = null;

			this.tail = this.tail.prev;
		}

		size--;

		fullCondition.signal(); // 唤醒其他线程

		lock.unlock();

		return deleteNode.u;
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
	
}
