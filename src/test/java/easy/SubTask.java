/**
 * 
 */
package easy;

/**
 * @author mujjiang
 * 
 */
public class SubTask extends Task {

	private TestNum testNum;

	public SubTask(TestNum testNum) {
		this.testNum = testNum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		testNum.subNum();
		System.out.println(Thread.currentThread().getName()+"线程减少1:"+testNum.getI());
	}

}
