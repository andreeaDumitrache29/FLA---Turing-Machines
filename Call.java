import java.util.ArrayList;

public class Call implements Action {
	String input;
	TuringMachine mt;
	int cursor;
	String input_mt;
	Alphabet alphabet;
	Context context;
	
	public Call() {
		alphabet = new Alphabet();
	}
	
	public Call(TuringMachine mt) {
		this.mt = mt;
		this.input = "";
		alphabet = new Alphabet();
	}
	
	public Call(String input) {
		this.input = input;
		alphabet = new Alphabet();
	}
	
	public Call(String input, TuringMachine mt) {
		this.input = input;
		this.mt = mt;
		alphabet = new Alphabet();
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	@Override
	public String toString() {
		return "[" + mt.toString() + input + "]";
	}
	
	public int getCursor() {
		return cursor;
	}
	
	@Override
	public String execute() {
		alphabet = Main.alphabet;
		String res = input_mt;
		switch (mt.getName()) {
		case "L":
			if(input.contentEquals("")) {
				if(cursor > 1) {
					cursor = cursor - 1;
				}else {
					String aux = "#";
					aux += res;
					res = aux;
				}
			}else if(input.contains("!")) {
				char c = res.charAt(cursor);
				if(cursor > 1) {
					cursor = cursor - 1;
				}else {
					String aux = "#";
					aux += res;
					res = aux;
				}
				while(res.charAt(cursor) == c) {
					if(cursor > 1) {
						cursor = cursor - 1;
					}else {
						String aux = "#";
						aux += res;
						res = aux;
					}
				}
			}else {
				if(cursor > 1) {
					cursor = cursor - 1;
				}else {
					String aux = "#";
					aux += res;
					res = aux;
				}
				while(res.charAt(cursor) != input.charAt(0)) {
					if(cursor > 1) {
						cursor = cursor - 1;
					}else {
						String aux = "#";
						aux += res;
						res = aux;
					}
				}
			}
			break;
		case "R":
			if(input.contentEquals("")) {
				if(cursor < res.length() - 2) {
					cursor = cursor + 1;
				}else {
					res += "#";
					cursor = cursor + 1; 
				}
			}else if(input.contains("!")) {
				char c = res.charAt(cursor);
				if(cursor < res.length() - 2) {
					cursor = cursor + 1;
				}else {
					res += "#";
					cursor = cursor + 1; 
				}
				while(res.charAt(cursor) == c) {
					if(cursor < res.length() - 2) {
						cursor = cursor + 1;
					}else {
						res += "#";
						cursor = cursor + 1; 
					}
				}
			}else {
				if(cursor < res.length() - 1) {
					cursor = cursor + 1;
				}else {
					res += "#";
					cursor = cursor + 1; 
				}
				while(res.charAt(cursor) != input.charAt(0)) {
					if(cursor < res.length() - 2) {
						cursor = cursor + 1;
					}else {
						res += "#";
						cursor = cursor + 1; 
					}
				}
			}
			break;
		default:
			if(alphabet.contains(mt.getName().charAt(0)) && mt.getName().length() == 1) {
				String aux = res.substring(0, cursor);
				aux += mt.getName();
				aux += res.substring(cursor + 1, res.length());
				res = aux;
			}else {
				for(TuringMachine m : Main.machines) {
					if(m.getName().equals(mt.getName())) {
						m.setBand(input_mt);
						m.setCursor(cursor);
						res = m.execute();
					}
				}
			}
			break;
		}
		return res;
	}
	
	@Override
	public Context currentContext() {
		return context;
	}
	
	public ArrayList<Action> getActions() {
		return null;
	}

	@Override
	public void addAction(Action a) {
		return;
	}

}
