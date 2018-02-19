import java.util.ArrayList;

public interface Context {
	public Context currentContext();
	public void addAction(Action a);
	public ArrayList<Action> getActions();
}
