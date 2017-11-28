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
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		testNum.addNum();
	}

}
