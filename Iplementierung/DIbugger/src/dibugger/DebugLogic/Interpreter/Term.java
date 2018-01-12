package dibugger.DebugLogic.Interpreter;

import java.util.List;
/**
 * Represents a Term, which can be evaluated.
 * @author wagner
 *
 */
public abstract class Term {
    	/**
    	 * Evaluates the Term replacing the variables with information from the given TraceStates
    	 * @param states
    	 * @return Value of the Term
    	 */
	public abstract TermValue evaluate(List<TraceState> states);
	/**
    	 * Evaluates the Term replacing the variables with information from the given Scope
    	 * @param states
    	 * @return Value of the Term
    	 */
	public abstract TermValue evaluate(Scope currentScope);
	/**
	 * Evaluates the Term replacing the variables with information from the given TraceStates
	 * and returns a String
	 * @param states
	 * @return the value as a string
	 */
	public String evaluateToString(List<TraceState> states){
		return this.evaluate(states).toString();
	}
}