package dibugger.DebugLogic.Exceptions;

/**
 * Exception thrown if a wrong number of arguments is passed to a method.
 * @author scheler
 *
 */
public class WrongNumberArgumentException extends WrongArgumentException {

	/**
	 * Default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for a WrongNumberArgumentException.
	 * @param occurrence the line of occurrence of the exception
	 */
	public WrongNumberArgumentException(String occurrence) {
		super(occurrence, "wrongNumberArgumentException");
	}

}