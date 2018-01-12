package dibugger.DebugLogic.Exceptions;

/**
 * Exception that is thrown if a program code of the user does not correspsond to the format specified by the grammar WLang.
 * @author scheler
 *
 */
public class InvalidProgramException extends DIbuggerLogicException {

	/**
	 * Default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	public InvalidProgramException(String occurrence) {
		super(occurrence, "invalidProgramException");
	}


}
