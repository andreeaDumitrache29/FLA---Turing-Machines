import java.util.ArrayList;

public class Transition implements Action {
	ArrayList<ArrayList<String>> symbols;
	ArrayList<ArrayList<Action>> actions;
	String input;
	int cursor;
	int ok = 0;
	Context context;
	int loop_ready = 0;

	public Transition(ArrayList<ArrayList<Action>> actions, ArrayList<ArrayList<String>> symbols) {
		this.actions = actions;
		this.symbols = symbols;
	}

	public Transition() {
		actions = new ArrayList<ArrayList<Action>>();
		symbols = new ArrayList<ArrayList<String>>();
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public String toString() {
		String res = "(";
		for (int i = 0; i < symbols.size(); i++) {
			for (String s : symbols.get(i)) {
				res += s;
				res += ", ";
			}
			res += " -> ";
			for (Action a : actions.get(i)) {
				res += a.toString();
			}
			res += "\n";
		}
		res += ")";
		return res;
	}

	@Override
	public String execute() {
		String res = input;
		String backup = res;
		for (int i = 0; i < symbols.size(); i++) {
			if (ok == 1) {
				break;
			}
			for (String s : symbols.get(i)) {
				if (ok == 1) {
					break;
				}
				//System.out.println(res);
				if (s.charAt(0) == res.charAt(cursor)) {
					for (Action a : actions.get(i)) {
						if (a instanceof Call) {
							((Call) a).cursor = cursor;
							((Call) a).input_mt = res;
						}
						if (a instanceof Label) {
							((Label) a).cursor = cursor;
							((Label) a).input = res;
						}
						if (a instanceof Transition) {
							((Transition) a).cursor = cursor;
							((Transition) a).input = res;
						}
						a.setContext(this);
						backup = res;
						res = a.execute();
						if (a instanceof Call) {
							cursor = ((Call) a).getCursor();
						}
						if (a instanceof Label) {
							cursor = ((Label) a).cursor;
						}
						if (a instanceof Transition) {
							cursor = ((Transition) a).cursor;
						}
						if (a instanceof Loop) {
							Context c = this;
							while (true) {
								if (!(c instanceof Transition)) {
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
								if (loop_ready == 1) {
									break;
								}

								c = c.currentContext();
							}
						}
					}
					ok = 1;
					break;
				}
			}
		}
		input = res;
		loop_ready = 0;
		ok = 0;
		return input;

	}

	@Override
	public Context currentContext() {
		return context;
	}

	@Override
	public void addAction(Action a) {
		return;
	}

	public void addAction(ArrayList<Action> a) {
		ArrayList<Action> aux = new ArrayList<>();
		aux.addAll(a);
		actions.add(aux);
	}

	public int search(char c) {
		for (int i = 0; i < symbols.size(); i++) {
			ArrayList<String> s = symbols.get(i);
			for (int j = 0; j < s.size(); j++) {
				String str = s.get(j);
				System.out.println("test " + str.charAt(0));
				if (str.charAt(0) == c) {
					return i;
				}
			}
		}
		return -1;
	}

	public ArrayList<ArrayList<Action>> getAllActions() {
		return actions;
	}

	public void addSymbols(ArrayList<String> ar) {
		ArrayList<String> aux = new ArrayList<>();
		aux.addAll(ar);
		symbols.add(aux);
	}

	@Override
	public ArrayList<Action> getActions() {
		return actions.get(0);
	}

	public ArrayList<Action> getActions(int idx) {
		return actions.get(idx);
	}

}
