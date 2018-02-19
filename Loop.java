import java.util.ArrayList;

public class Loop implements Action {
	String label;
	Context context;
	
	public Loop() {
		
	}
	
	public Loop(String label) {
		this.label = label;
	}
	
	@Override
	public String execute() {
		return label;
	}
	
	@Override
	public String toString() {
		return "&" + label;
	}

	@Override
	public Context currentContext() {
		return context;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public void addAction(Action a) {
		return;
	}
	
	public ArrayList<Action> getActions() {
		return null;
	}
}
