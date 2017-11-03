/**
 * 
 */
package lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 模拟java api使用CLH队列的lock锁
 * @author mujjiang
 * 
 */
public class CLHLock implements Lock {

	AtomicReference<QNode> atomicReference = null;

	ThreadLocal<QNode> mynode = null; // 当前结点

	ThreadLocal<QNode> mypred = null; // 前一个结点的链接T

	public CLHLock() {
		atomicReference = new AtomicReference<CLHLock.QNode>(new QNode());
		mynode = new ThreadLocal<CLHLock.QNode>();
		QNode qnode = new QNode();
		mynode.set(qnode);
		mypred = new ThreadLocal<CLHLock.QNode>();
	}

	public void lock() {
		QNode qNode = mynode.get();
		if(qNode==null){
			qNode = new QNode();
			mynode.set(qNode);
		}
		qNode.locked = true;
		QNode pred = atomicReference.getAndSet(qNode);
		if (pred != null) {
			mypred.set(pred);
			while (pred.locked) {

			}
		}
	}

	public void unlock() {
		QNode qNode = mynode.get();
		qNode.locked = false;
		mynode.set(mypred.get());

	}

	private class QNode {

		boolean locked = false;

	}

}
