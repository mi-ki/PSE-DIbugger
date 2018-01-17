package dibugger.DebugLogic.Interpreter;

import java.util.List;

import dibugger.DebugLogic.Exceptions.DIbuggerLogicException;

/**
 * @author wagner
 * Represents a runnalbe command in a WLANG program
 */
public abstract class Command {
    protected GenerationController controller;
    protected int linenumber;
    public Command(GenerationController controller, int linenumber) {
	this.controller = controller;
	this.linenumber = linenumber;
    }
    /**
     * Runs the command
     * @return the states, in which the program was during the execution of the command
     */
    public abstract List<TraceState> run() throws DIbuggerLogicException;
}