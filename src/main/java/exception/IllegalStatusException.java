/**
 * 
 */
package exception;

/**
 * 线程池非法状态异常
 * @author mujjiang
 *
 */
public class IllegalStatusException extends RuntimeException {
	
	private String name;   //exception name
	
	private int code;   //exception code

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IllegalStatusException(String name){
		super(name);
	}

}
