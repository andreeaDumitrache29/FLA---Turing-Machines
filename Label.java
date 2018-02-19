import java.util.ArrayList;

public class Label implements Action {
	String name;
	int cursor;
	ArrayList<Action> actions;
	String input;
	Context context;
	int loop_ready = 0;

	public Label() {
		this.actions = new ArrayList<Action>();
	}

	public Label(String name) {
		this.name = name;
		this.actions = new ArrayList<Action>();
	}

	public void addAction(Action a) {
		actions.add(a);
	}

	@Override
	public String toString() {
		String s = name;
		s += "@";
		for (Action a : actions) {
			s += a.toString();
			s += "\n";
		}
		return s;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public String execute() {
		String res = input;
		String backup = res;
		for (Action a : actions) {
			if (a instanceof Call) {
				((Call) a).cursor = cursor;
				((Call) a).input_mt = res;
			}
			if (a instanceof Transition) {
				((Transition) a).cursor = cursor;
				((Transition) a).input = res;
			}
			if (a instanceof Label) {
				((Label) a).cursor = cursor;
				((Label) a).input = res;
			}
			a.setContext(this);
			backup = res;
			res = a.execute();
			if (a instanceof Call) {
				cursor = ((Call) a).getCursor();
			}
			if (a instanceof Transition) {
				cursor = ((Transition) a).cursor;
			}
			if (a instanceof Label) {
				cursor = ((Label) a).cursor;
			}
			if (a instanceof Loop) {
				Context c = this;
				while (true) {
					if (!(context instanceof Transition)) {
						for (Action action : c.getActions()) {
							if (action instanceof Label && ((Label) action).name.contentEquals(res)) {
								((Label) action).cursor = cursor;
								((Label) action).input = backup;
								backup = res;
								res = action.execute();
								cursor = ((Label) action).cursor;
								loop_ready = 1;
								break;
							}
							if (loop_ready == 1) {
								break;
							}
						}
					} else {
						ArrayList<ArrayList<Action>> actions = ((Transition) c).getAllActions();
						for (ArrayList<Action> act : actions) {
							for (Action action : act) {
								if (action instanceof Label && ((Label) action).name.contentEquals(res)) {
									((Label) action).cursor = cursor;
									((Label) action).input = backup;
									backup = res;
									res = action.execute();
									cursor = ((Label) action).cursor;
									loop_ready = 1;
									break;
								}
							}
							if (loop_ready == 1) {
								break;
							}
						}
					}
					c = c.currentContext();
				}
			}
		}
		loop_ready = 0;
		return res;
	}

	@Override
	public Context currentContext() {
		return context;
	}

	@Override
	public ArrayList<Action> getActions() {
		return actions;
	}

}
