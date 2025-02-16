package dibugger.debuglogic.antlrparser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class ActuallyHelpfulErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
            String msg, RecognitionException e) throws ActuallyHelpfulSyntaxException {
        throw new ActuallyHelpfulSyntaxException(e.getMessage());
    }
}
