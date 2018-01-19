package dibugger.debuglogic.interpreter;
/**
 * 
 * @author pascal
 *
 */
public class DoubleValue extends TermValue {

	private double value;
	
	public DoubleValue(double value) {
		super(Type.DOUBLE);
		this.value = value;
	}

	@Override
	public TermValue add(TermValue operand) {	
		if(operand.getType()==Type.FLOAT){
			return new DoubleValue(value+((FloatValue)operand).getValue());
		}
		else if(operand.getType()==Type.DOUBLE){
			return new DoubleValue(value+((DoubleValue)operand).getValue());
		}
		else if(operand.getType()==Type.INT){
			return new DoubleValue(value+((IntValue)operand).getValue());
		}
		else if(operand.getType()==Type.LONG){
			return new DoubleValue(value+((LongValue)operand).getValue());
		}
		else if(operand.getType()==Type.CHAR){
			return new DoubleValue(value+((CharValue)operand).getValue());
		}
		//TODO throw exception
		return null;
	}
	@Override
	public TermValue mul(TermValue operand) {
		if(operand.getType()==Type.FLOAT){
			return new DoubleValue(value*((FloatValue)operand).getValue());
		}
		else if(operand.getType()==Type.DOUBLE){
			return new DoubleValue(value*((DoubleValue)operand).getValue());
		}
		else if(operand.getType()==Type.INT){
			return new DoubleValue(value*((IntValue)operand).getValue());
		}
		else if(operand.getType()==Type.LONG){
			return new DoubleValue(value*((LongValue)operand).getValue());
		}
		else if(operand.getType()==Type.CHAR){
			return new DoubleValue(value*((CharValue)operand).getValue());
		}
		return null;
	}

	@Override
	public TermValue div(TermValue operand) {
		if(operand.getType()==Type.FLOAT){
			return new DoubleValue(value/((FloatValue)operand).getValue());
		}
		else if(operand.getType()==Type.DOUBLE){
			return new DoubleValue(value/((DoubleValue)operand).getValue());
		}
		else if(operand.getType()==Type.INT){
			return new DoubleValue(value/((IntValue)operand).getValue());
		}
		else if(operand.getType()==Type.LONG){
			return new DoubleValue(value/((LongValue)operand).getValue());
		}
		else if(operand.getType()==Type.CHAR){
			return new DoubleValue(value/((CharValue)operand).getValue());
		}
		return null;
	}

	@Override
	public TermValue mod(TermValue operand) {
		if(operand.getType()==Type.FLOAT){
			return new DoubleValue(value%((FloatValue)operand).getValue());
		}
		else if(operand.getType()==Type.DOUBLE){
			return new DoubleValue(value%((DoubleValue)operand).getValue());
		}
		else if(operand.getType()==Type.INT){
			return new DoubleValue(value%((IntValue)operand).getValue());
		}
		else if(operand.getType()==Type.LONG){
			return new DoubleValue(value%((LongValue)operand).getValue());
		}
		else if(operand.getType()==Type.CHAR){
			return new DoubleValue(value%((CharValue)operand).getValue());
		}
		return null;
	}

	@Override
	public TermValue sub(TermValue operand) {
		if(operand.getType()==Type.FLOAT){
			return new DoubleValue(value-((FloatValue)operand).getValue());
		}
		else if(operand.getType()==Type.DOUBLE){
			return new DoubleValue(value-((DoubleValue)operand).getValue());
		}
		else if(operand.getType()==Type.INT){
			return new DoubleValue(value-((IntValue)operand).getValue());
		}
		else if(operand.getType()==Type.LONG){
			return new DoubleValue(value-((LongValue)operand).getValue());
		}
		else if(operand.getType()==Type.CHAR){
			return new DoubleValue(value-((CharValue)operand).getValue());
		}
		return null;
	}

//	@Override
//	public TermValue or(TermValue operand) {	
//		return null;
//	}
//	
//	@Override
//	public TermValue not(TermValue operand) {
//		return null;
//	}
//	
//	@Override
//	public TermValue and(TermValue operand) {
//		return null;
//	}
	
	@Override
	public boolean greaterEqual(TermValue operand) {
		if(operand.getType()==Type.FLOAT){
			return value>=((FloatValue)operand).getValue();
		}
		else if(operand.getType()==Type.DOUBLE){
			return value>=((DoubleValue)operand).getValue();
		}
		else if(operand.getType()==Type.INT){
			return value>=((IntValue)operand).getValue();
		}
		else if(operand.getType()==Type.LONG){
			return value>=((LongValue)operand).getValue();
		}
		else if(operand.getType()==Type.CHAR){
			return value>=((CharValue)operand).getValue();
		}
		return false;
	}

	@Override
	public boolean greaterThan(TermValue operand) {
		if(operand.getType()==Type.FLOAT){
			return value>((FloatValue)operand).getValue();
		}
		else if(operand.getType()==Type.DOUBLE){
			return value>((DoubleValue)operand).getValue();
		}
		else if(operand.getType()==Type.INT){
			return value>((IntValue)operand).getValue();
		}
		else if(operand.getType()==Type.LONG){
			return value>((LongValue)operand).getValue();
		}
		else if(operand.getType()==Type.CHAR){
			return value>((CharValue)operand).getValue();
		}
		return false;
	}

	@Override
	public boolean lessEqual(TermValue operand) {
		if(operand.getType()==Type.FLOAT){
			return value<=((FloatValue)operand).getValue();
		}
		else if(operand.getType()==Type.DOUBLE){
			return value<=((DoubleValue)operand).getValue();
		}
		else if(operand.getType()==Type.INT){
			return value<=((IntValue)operand).getValue();
		}
		else if(operand.getType()==Type.LONG){
			return value<=((LongValue)operand).getValue();
		}
		else if(operand.getType()==Type.CHAR){
			return value<=((CharValue)operand).getValue();
		}
		return false;
	}

	@Override
	public boolean lessThan(TermValue operand) {
		if(operand.getType()==Type.FLOAT){
			return value<((FloatValue)operand).getValue();
		}
		else if(operand.getType()==Type.DOUBLE){
			return value<((DoubleValue)operand).getValue();
		}
		else if(operand.getType()==Type.INT){
			return value<((IntValue)operand).getValue();
		}
		else if(operand.getType()==Type.LONG){
			return value<((LongValue)operand).getValue();
		}
		else if(operand.getType()==Type.CHAR){
			return value<((CharValue)operand).getValue();
		}
		return false;
	}

	@Override
	public boolean equal(TermValue operand) {
		if(operand.getType()==Type.FLOAT){
			return value==((FloatValue)operand).getValue();
		}
		else if(operand.getType()==Type.DOUBLE){
			return value==((DoubleValue)operand).getValue();
		}
		else if(operand.getType()==Type.INT){
			return value==((IntValue)operand).getValue();
		}
		else if(operand.getType()==Type.LONG){
			return value==((LongValue)operand).getValue();
		}
		else if(operand.getType()==Type.CHAR){
			return value==((CharValue)operand).getValue();
		}
		return false;
	}

	@Override
	public boolean or(TermValue operand) {
		return false;
	}
	
	@Override
	public boolean and(TermValue operand) {
		return false;
	}
	
	@Override
	public boolean not() {
		return false;
	}
	
	@Override
	public String toString() {
		return ""+this.value;
	}

	public double getValue() {
		return value;
	}

	
}