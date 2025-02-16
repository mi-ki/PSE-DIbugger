package dibugger.debuglogic.interpreter;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import dibugger.debuglogic.antlrparser.WlangBaseVisitor;
import dibugger.debuglogic.antlrparser.WlangParser.ArglistContext;
import dibugger.debuglogic.antlrparser.WlangParser.ArgumentContext;
import dibugger.debuglogic.antlrparser.WlangParser.ArrayDeclarationOneDimContext;
import dibugger.debuglogic.antlrparser.WlangParser.ArrayDeclarationThreeDimContext;
import dibugger.debuglogic.antlrparser.WlangParser.ArrayDeclarationTwoDimContext;
import dibugger.debuglogic.antlrparser.WlangParser.ArrayDeclareAssignContext;
import dibugger.debuglogic.antlrparser.WlangParser.ArrayElementAssignOneDimContext;
import dibugger.debuglogic.antlrparser.WlangParser.ArrayElementAssignThreeDimContext;
import dibugger.debuglogic.antlrparser.WlangParser.ArrayElementAssignTwoDimContext;
import dibugger.debuglogic.antlrparser.WlangParser.BlockContext;
import dibugger.debuglogic.antlrparser.WlangParser.CallingAssignContext;
import dibugger.debuglogic.antlrparser.WlangParser.DeclarationContext;
import dibugger.debuglogic.antlrparser.WlangParser.DeclareAssignContext;
import dibugger.debuglogic.antlrparser.WlangParser.FilledArglistContext;
import dibugger.debuglogic.antlrparser.WlangParser.FilledArgumentContext;
import dibugger.debuglogic.antlrparser.WlangParser.FuncCallContext;
import dibugger.debuglogic.antlrparser.WlangParser.FunctionHeadContext;
import dibugger.debuglogic.antlrparser.WlangParser.IfWithBlockContext;
import dibugger.debuglogic.antlrparser.WlangParser.IfWithBlockElseWithBlockContext;
import dibugger.debuglogic.antlrparser.WlangParser.IfWithBlockElseWithSingleContext;
import dibugger.debuglogic.antlrparser.WlangParser.IfWithSingleContext;
import dibugger.debuglogic.antlrparser.WlangParser.IfWithSingleElseWithBlockContext;
import dibugger.debuglogic.antlrparser.WlangParser.IfWithSingleElseWithSingleContext;
import dibugger.debuglogic.antlrparser.WlangParser.MainFunctionHeadContext;
import dibugger.debuglogic.antlrparser.WlangParser.MainProcedureHeadContext;
import dibugger.debuglogic.antlrparser.WlangParser.MainRoutineContext;
import dibugger.debuglogic.antlrparser.WlangParser.ProcedureHeadContext;
import dibugger.debuglogic.antlrparser.WlangParser.PureAssignContext;
import dibugger.debuglogic.antlrparser.WlangParser.ReturnStateContext;
import dibugger.debuglogic.antlrparser.WlangParser.RoutineContext;
import dibugger.debuglogic.antlrparser.WlangParser.StatementContext;
import dibugger.debuglogic.antlrparser.WlangParser.StatementsContext;
import dibugger.debuglogic.antlrparser.WlangParser.WhileWithBlockContext;
import dibugger.debuglogic.antlrparser.WlangParser.WhileWithSingleContext;

public class CommandGenerationVisitor extends WlangBaseVisitor<Command> {
    private GenerationController controller;
    private TermGenerationVisitor termGenVisitor;

    public CommandGenerationVisitor(GenerationController controller) {
        this.controller = controller;
        this.termGenVisitor = new TermGenerationVisitor();
    }

    // Helper Methods
    private List<Command> collectInBlock(BlockContext block) {
        List<Command> content = new ArrayList<Command>();
        StatementsContext statements = block.statements();
        while (statements != null && statements.getChildCount() > 1) {
            StatementContext statement = statements.statement();
            Command command = this.visit(statement);
            content.add(command);
            // sift down the tree
            statements = statements.statements();
        }
        // gather in the leaf
        if (statements != null) {
            StatementContext statement = statements.statement();
            Command command = this.visit(statement);
            content.add(command);
        }
        return content;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Command visitStatement(StatementContext ctx) {
        return visit(ctx.getChild(0));
    }

    // RoutineCommands
    @Override
    public Command visitMainRoutine(MainRoutineContext ctx) {
        RoutineCommand parent = (RoutineCommand) this.visit(ctx.mainHead());
        List<Command> content = this.collectInBlock(ctx.block());
        for (Command c : content) {
            parent.addChild(c);
        }
        return parent;
    }

    @Override
    public Command visitMainProcedureHead(MainProcedureHeadContext ctx) {
        List<String> paramIdentifiers = new ArrayList<String>();
        List<Type> expectedParamTypes = new ArrayList<Type>();
        // gather in the parameters
        ArglistContext args = ctx.args;
        while (args != null && args.getChildCount() > 1) {
            ArgumentContext argument = args.argument();
            paramIdentifiers.add(argument.id.getText());
            expectedParamTypes.add(Type.fromStringToType(argument.type.getText()));
            // sift down the tree
            args = args.arglist();
        }
        // gather in the leaf
        if (args != null) {
            ArgumentContext argument = args.argument();
            paramIdentifiers.add(argument.id.getText());
            expectedParamTypes.add(Type.fromStringToType(argument.type.getText()));

        }
        return new RoutineCommand(controller, "main", ctx.getStart().getLine(), paramIdentifiers, expectedParamTypes,
                Type.NULL);
    }

    @Override
    public Command visitMainFunctionHead(MainFunctionHeadContext ctx) {
        List<String> paramIdentifiers = new ArrayList<String>();
        List<Type> expectedParamTypes = new ArrayList<Type>();
        // gather in the parameters
        ArglistContext args = ctx.args;
        while (args != null && args.getChildCount() > 1) {
            ArgumentContext argument = args.argument();
            paramIdentifiers.add(argument.id.getText());
            expectedParamTypes.add(Type.fromStringToType(argument.type.getText()));
            // sift down the tree
            args = args.arglist();
        }
        // gather in the leaf
        if (args != null) {
            ArgumentContext argument = args.argument();
            paramIdentifiers.add(argument.id.getText());
            expectedParamTypes.add(Type.fromStringToType(argument.type.getText()));

        }
        return new RoutineCommand(controller, "main", ctx.getStart().getLine(), paramIdentifiers, expectedParamTypes,
                Type.fromStringToType(ctx.returntype.getText()));
    }

    @Override
    public Command visitRoutine(RoutineContext ctx) {
        RoutineCommand parent = (RoutineCommand) this.visit(ctx.routineHead());
        List<Command> content = this.collectInBlock(ctx.block());
        for (Command c : content) {
            parent.addChild(c);
        }
        return parent;
    }

    @Override
    public Command visitFunctionHead(FunctionHeadContext ctx) {
        List<String> paramIdentifiers = new ArrayList<String>();
        List<Type> expectedParamTypes = new ArrayList<Type>();
        // gather in the parameters
        ArglistContext args = ctx.args;
        while (args != null && args.getChildCount() > 1) {
            ArgumentContext argument = args.argument();
            paramIdentifiers.add(argument.id.getText());
            expectedParamTypes.add(Type.fromStringToType(argument.type.getText()));
            // sift down the tree
            args = args.arglist();
        }
        // gather in the leaf
        if (args != null) {
            ArgumentContext argument = args.argument();
            paramIdentifiers.add(argument.id.getText());
            expectedParamTypes.add(Type.fromStringToType(argument.type.getText()));

        }
        return new RoutineCommand(controller, ctx.id.getText(), ctx.getStart().getLine(), paramIdentifiers,
                expectedParamTypes, Type.fromStringToType(ctx.returntype.getText()));
    }

    @Override
    public Command visitProcedureHead(ProcedureHeadContext ctx) {
        List<String> paramIdentifiers = new ArrayList<String>();
        List<Type> expectedParamTypes = new ArrayList<Type>();
        // gather in the parameters
        ArglistContext args = ctx.args;
        while (args != null && args.getChildCount() > 1) {
            ArgumentContext argument = args.argument();
            paramIdentifiers.add(argument.id.getText());
            expectedParamTypes.add(Type.fromStringToType(argument.type.getText()));
            // sift down the tree
            args = args.arglist();
        }
        // gather in the leaf
        if (args != null) {
            ArgumentContext argument = args.argument();
            paramIdentifiers.add(argument.id.getText());
            expectedParamTypes.add(Type.fromStringToType(argument.type.getText()));

        }
        return new RoutineCommand(controller, ctx.id.getText(), ctx.getStart().getLine(), paramIdentifiers,
                expectedParamTypes, Type.NULL);
    }

    @Override
    public Command visitReturnState(ReturnStateContext ctx) {
        Term returnValue = this.termGenVisitor.visit(ctx.returnvalue);
        return new ReturnCommand(controller, ctx.getStart().getLine(), returnValue);
    }

    // Array Commands
    @Override
    public Command visitArrayDeclarationOneDim(ArrayDeclarationOneDimContext ctx) {
        String identifier = ctx.id.getText();
        Term index = this.termGenVisitor.visit(ctx.index);
        return new ArrayDeclaration(this.controller, ctx.id.getLine(), identifier, index);
    }

    @Override
    public Command visitArrayDeclarationTwoDim(ArrayDeclarationTwoDimContext ctx) {
        String identifier = ctx.id.getText();
        Term firstIndex = this.termGenVisitor.visit(ctx.firstIndex);
        Term secondIndex = this.termGenVisitor.visit(ctx.secondIndex);
        return new ArrayDeclaration(this.controller, ctx.id.getLine(), identifier, firstIndex, secondIndex);
    }

    @Override
    public Command visitArrayDeclarationThreeDim(ArrayDeclarationThreeDimContext ctx) {
        String identifier = ctx.id.getText();
        Term firstIndex = this.termGenVisitor.visit(ctx.firstIndex);
        Term secondIndex = this.termGenVisitor.visit(ctx.secondIndex);
        Term thirdIndex = this.termGenVisitor.visit(ctx.thirdIndex);
        return new ArrayDeclaration(this.controller, ctx.id.getLine(), identifier, firstIndex, secondIndex, thirdIndex);
    }

    @Override
    public Command visitArrayDeclareAssign(ArrayDeclareAssignContext ctx) {
        Term values = this.termGenVisitor.visit(ctx.filledArglist());
        Term size = this.termGenVisitor.visit(ctx.term());
        return new ArrayDeclarationAssignment(this.controller, ctx.getStart().getLine(), ctx.id.getText(), size,
                values);
    }

    @Override
    public Command visitArrayElementAssignOneDim(ArrayElementAssignOneDimContext ctx) {
        String identifier = ctx.id.getText();
        Term index = this.termGenVisitor.visit(ctx.index);
        Term value = this.termGenVisitor.visit(ctx.value);
        return new ArrayElementAssignment(this.controller, ctx.id.getLine(), identifier, index, value);
    }

    @Override
    public Command visitArrayElementAssignTwoDim(ArrayElementAssignTwoDimContext ctx) {
        String identifier = ctx.id.getText();
        Term firstIndex = this.termGenVisitor.visit(ctx.firstIndex);
        Term secondIndex = this.termGenVisitor.visit(ctx.secondIndex);
        Term value = this.termGenVisitor.visit(ctx.value);
        return new ArrayElementAssignment(this.controller, ctx.id.getLine(), identifier, firstIndex, secondIndex,
                value);
    }

    @Override
    public Command visitArrayElementAssignThreeDim(ArrayElementAssignThreeDimContext ctx) {
        String identifier = ctx.id.getText();
        Term firstIndex = this.termGenVisitor.visit(ctx.firstIndex);
        Term secondIndex = this.termGenVisitor.visit(ctx.secondIndex);
        Term thirdIndex = this.termGenVisitor.visit(ctx.thirdIndex);
        Term value = this.termGenVisitor.visit(ctx.value);
        return new ArrayElementAssignment(this.controller, ctx.id.getLine(), identifier, firstIndex, secondIndex,
                thirdIndex, value);
    }

    // Assignments and Declaration
    @Override
    public Command visitCallingAssign(CallingAssignContext ctx) {
        Command funcCall = visit(ctx.value);
        return new CallingAssignment(this.controller, ctx.getStart().getLine(), ctx.id.getText(), funcCall);
    }

    @Override
    public Command visitPureAssign(PureAssignContext ctx) {
        Term value = this.termGenVisitor.visit(ctx.value);
        return new Assignment(this.controller, ctx.id.getLine(), ctx.id.getText(), value);
    }

    @Override
    public Command visitDeclaration(DeclarationContext ctx) {
        return new Declaration(this.controller, ctx.getStart().getLine(), ctx.id.getText(),
                Type.fromStringToType(ctx.type.getText()));
    }

    @Override
    public Command visitDeclareAssign(DeclareAssignContext ctx) {
        Term value = this.termGenVisitor.visit(ctx.value);
        return new DeclarationAssignment(this.controller, ctx.getStart().getLine(), ctx.id.getText(),
                Type.fromStringToType(ctx.type.getText()), value);
    }

    // Function Call
    @Override
    public Command visitFuncCall(FuncCallContext ctx) {
        List<Term> arguments = new ArrayList<Term>();
        // collect arguments
        FilledArglistContext args = ctx.args;
        while (args != null && args.getChildCount() > 1) {
            FilledArgumentContext argument = args.filledArgument();
            Term argumentTerm = this.termGenVisitor.visit(argument.term());
            arguments.add(argumentTerm);
            // sift down the tree
            args = args.filledArglist();
        }
        // gather in the leaf
        if (args != null) {
            FilledArgumentContext argument = args.filledArgument();
            Term argumentTerm = this.termGenVisitor.visit(argument.term());
            arguments.add(argumentTerm);
        }
        return new RoutineCall(this.controller, ctx.getStart().getLine(), ctx.functionname.getText(), arguments);
    }

    // Composite Commands
    @Override
    public Command visitWhileWithBlock(WhileWithBlockContext ctx) {
        Term condition = this.termGenVisitor.visit(ctx.condition());
        WhileCommand whileCommand = new WhileCommand(this.controller, ctx.getStart().getLine(), condition);
        // add child commands
        List<Command> content = this.collectInBlock(ctx.content);
        for (Command c : content) {
            whileCommand.addChild(c);
        }
        return whileCommand;
    }

    @Override
    public Command visitWhileWithSingle(WhileWithSingleContext ctx) {
        Term condition = this.termGenVisitor.visit(ctx.condition());
        WhileCommand whileCommand = new WhileCommand(this.controller, ctx.getStart().getLine(), condition);
        // add child command
        whileCommand.addChild(visit(ctx.content));
        return whileCommand;
    }

    @Override
    public Command visitIfWithBlock(IfWithBlockContext ctx) {
        Term condition = this.termGenVisitor.visit(ctx.condition());
        IfCommand ifCommand = new IfCommand(this.controller, ctx.getStart().getLine(), condition);
        // add child commands
        List<Command> content = this.collectInBlock(ctx.content);
        for (Command c : content) {
            ifCommand.addChild(c);
        }
        return ifCommand;
    }

    @Override
    public Command visitIfWithSingle(IfWithSingleContext ctx) {
        Term condition = this.termGenVisitor.visit(ctx.condition());
        IfCommand ifCommand = new IfCommand(this.controller, ctx.getStart().getLine(), condition);
        // add child command
        ifCommand.addChild(visit(ctx.content));
        return ifCommand;
    }

    @Override
    public Command visitIfWithBlockElseWithBlock(IfWithBlockElseWithBlockContext ctx) {
        Term condition = this.termGenVisitor.visit(ctx.condition());
        IfElseCommand ifelse = new IfElseCommand(this.controller, ctx.getStart().getLine(), condition);
        // add if part commands
        List<Command> ifcontent = this.collectInBlock(ctx.ifcontent);
        for (Command c : ifcontent) {
            ifelse.addIfChild(c);
        }
        // add else part commands
        List<Command> elsecontent = this.collectInBlock(ctx.elsecontent);
        for (Command c : elsecontent) {
            ifelse.addElseChild(c);
        }
        return ifelse;
    }

    @Override
    public Command visitIfWithBlockElseWithSingle(IfWithBlockElseWithSingleContext ctx) {
        Term condition = this.termGenVisitor.visit(ctx.condition());
        IfElseCommand ifelse = new IfElseCommand(this.controller, ctx.getStart().getLine(), condition);
        // add if part commands
        List<Command> ifcontent = this.collectInBlock(ctx.ifcontent);
        for (Command c : ifcontent) {
            ifelse.addIfChild(c);
        }
        // add else part command
        ifelse.addElseChild(visit(ctx.elsecontent));
        return ifelse;
    }

    @Override
    public Command visitIfWithSingleElseWithBlock(IfWithSingleElseWithBlockContext ctx) {
        Term condition = this.termGenVisitor.visit(ctx.condition());
        IfElseCommand ifelse = new IfElseCommand(this.controller, ctx.getStart().getLine(), condition);
        // add if part command
        ifelse.addIfChild(visit(ctx.ifcontent));
        // add else part commands
        List<Command> elsecontent = this.collectInBlock(ctx.elsecontent);
        for (Command c : elsecontent) {
            ifelse.addElseChild(c);
        }
        return ifelse;
    }

    @Override
    public Command visitIfWithSingleElseWithSingle(IfWithSingleElseWithSingleContext ctx) {
        Term condition = this.termGenVisitor.visit(ctx.condition());
        IfElseCommand ifelse = new IfElseCommand(this.controller, ctx.getStart().getLine(), condition);
        // add if part command
        ifelse.addIfChild(visit(ctx.ifcontent));
        // add else part command
        ifelse.addElseChild(visit(ctx.elsecontent));
        return ifelse;
    }
}
