import java.util.ArrayList;

public class TuringMachine implements Context{
	protected String name;
	protected String band;
	protected int cursor;
	protected ArrayList<Action> actions;
	Context context;
	
	public TuringMachine() {
		this.actions = new ArrayList<Action>();
	}
	
	public TuringMachine(String name) {
		this.name = name;
		this.actions = new ArrayList<Action>();
	}
	
	public void setCursor(int c) {
		this.cursor = c;
	}
	
	public int getCursor() {
		return cursor;
	}
	
	public void setBand(String b) {
		band = b;
	}
	
	public String getBand() {
		return band;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Action> getActions() {
		return actions;
	}
	
	
	public void addAction(Action a) {
		actions.add(a);
	}
	
	public void listActions() {
		for(Action a : actions) {
			System.out.println(a.toString());
		}
	}
	
	public String toString() {
		return name;
	}

	@Override
	public Context currentContext() {
		return this;
	}
	
	public String execute() {
		String res = "";
		for(Action a : actions) {
			if(a instanceof Call) {
				((Call) a).cursor = cursor;
				((Call) a).input_mt = band;
			}
			if(a instanceof Label) {
				((Label) a).cursor = cursor;
				((Label) a).input = band;
			}
			if(a instanceof Transition) {
				((Transition) a).cursor = cursor;
				((Transition) a).input = band;
			}
			a.setContext(this);
			res = a.execute();
			band = res;
			if(a instanceof Call) {
				cursor = ((Call) a).getCursor();
			}
			if(a instanceof Label) {
				cursor = ((Label) a).cursor;
			}
			if(a instanceof Transition) {
				cursor = ((Transition) a).cursor;
			}
		}
		return res;
	}
}
