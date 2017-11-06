/**
 * 
 */
package lock;

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

/**
 * 模拟java api使用CLH队列的lock锁
 * 
 * @author mujjiang
 * 
 */
public class CLHLock implements Lock {
	
	private static Logger logger = Logger.getLogger("lock.CLHLock");

	AtomicReference<QNode> atomicReference = null;

	ThreadLocal<QNode> mynode = null; // 当前结点

	ThreadLocal<QNode> mypred = null; // 前一个结点的链接T

	public CLHLock() {
		logger.info("初始化锁");
		atomicReference = new AtomicReference<CLHLock.QNode>(new QNode(false));
		mynode = new ThreadLocal<CLHLock.QNode>();
		QNode qnode = new QNode();
		mynode.set(qnode);
		mypred = new ThreadLocal<CLHLock.QNode>();
		mypred.set(new QNode(false));
		QNode qNode2 = atomicReference.get();
		if(qNode2==null){
			mypred.set(qNode2);
		}
	}

	public void lock() {
		
		Thread currentThread = Thread.currentThread();
		logger.info(currentThread.getName()+"开始获得锁");
		mynode.set(new QNode(true));
	
		//先检查一次前面有没有锁，如果没有，则直接设置好当前去执行
		QNode pred = atomicReference.get();
		
		if(pred==null||!pred.locked) {
			atomicReference.set(mynode.get());
			logger.info(currentThread.getName()+"获得锁成功");
			return;
		}
		
		if (pred != null) {
			QNode temp = pred;
			mypred.set(pred);
			while(true){
				temp = atomicReference.get();
				if(!temp.locked){
					atomicReference.getAndSet(mynode.get());
					break;		
				}
			}
			
		}
		
		logger.info(currentThread.getName()+"获得锁成功");
		
	}

	public void unlock() {
		Thread currentThread = Thread.currentThread();
		logger.info(currentThread.getName()+"开始解锁");
		QNode qNode = mynode.get();
		qNode.locked = false;
		mynode.set(mypred.get());
		logger.info(currentThread.getName()+"解锁成功");

	}

	private class QNode {

		volatile boolean locked = false;

		public QNode() {

		}

		public QNode(boolean locked) {
			this.locked = locked;
		}

		@Override
		public String toString() {
			return "QNode [locked=" + locked + "]";
		}

	}

}
