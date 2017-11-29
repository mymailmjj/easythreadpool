/**
 * 
 */
package easy;

/**
 * @author mujjiang
 * 
 */
public class AddTask extends Task {

	private TestNum testNum;

	public AddTask(TestNum testNum) {
		this.testNum = testNum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()s
	 */
	public void run() {
		testNum.addNum();
		System.out.println(Thread.currentThread().getName()+"线程增加1:"+testNum.getI());
	}

}
