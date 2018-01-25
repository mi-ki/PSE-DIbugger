package test.debuglogic.debugger;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import dibugger.debuglogic.debugger.DebugControl;
import dibugger.debuglogic.debugger.DebugLogicFacade;
import dibugger.debuglogic.debugger.ProgramInput;
import dibugger.debuglogic.exceptions.DIbuggerLogicException;

public class DebugControlTest {
    
    @Test
    public void steppingTest() throws DIbuggerLogicException{
        DebugLogicFacade facade = new DebugLogicFacade();
        ProgramInput in = new ProgramInput("void main() { \n"
                + "int i = 2 \n;"
                + "int x = 3+i; \n"
                + "boolean b = true; \n"
                + "while (x<80) { \n"
                + " i = i+2; \n"
                + " x = x +1; \n"
                + " } \n"
                + "}", new ArrayList<String>(), 0, "A");
        List<ProgramInput> l_in = new ArrayList<ProgramInput>();
        l_in.add(in);
        facade.setStepSize(0, 1);
        facade.launchRun(l_in);
        for(int i=0;i<5;++i){
            facade.step(DebugControl.STEP_NORMAL);
        }
        System.out.println("All Variables: "+facade.getAllVariables("A"));
        for(String s : facade.getAllVariables("A")){
            System.out.println(s+" = "+facade.getValueOf("A", s));
        }
        facade.continueDebug();
    }
    
}
