import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	public static Alphabet alphabet;
	public static ArrayList<TuringMachine> machines;
	
	public static void main(String[] args) {
		BufferedReader br;
		FileReader f;
		try {
			f = new FileReader(args[0]);
			String mt_to_rule = args[1];
			String input = args[2];
			String res = "";
			br = new BufferedReader(f);
			Flexer scanner = new Flexer(br);
			scanner.yylex();
			alphabet = scanner.getAlphabet();
			machines = scanner.getMachines();
			int cursor = 0;
			for (int i = 0; i < input.length(); i++) {
				if (input.charAt(i) == '>') {
					cursor = i;
					break;
				}
			}
			String s1 = input.substring(0, cursor);
			String s2 = input.substring(cursor + 1, input.length());
			s1 += s2;
			ArrayList<TuringMachine> machines_known = new ArrayList<>();
			TuringMachine m;
			for (int i = 0; i < machines.size(); i++) {
				if (i > 0) {
					machines_known.add(machines.get(i - 1));
				}
				m = machines.get(i);
				if (m.getName().contentEquals(mt_to_rule)) {
					m.setBand(s1);
					m.setCursor(cursor);
					res = m.execute();
					cursor = m.getCursor();
					break;
				}
			}
			s1 = res.substring(0, cursor);
			s1 += ">";
			s1 += res.substring(cursor, res.length());
			s1 = s1.substring(cursor - 1);
			StringBuilder builder = new StringBuilder(s1);
			s2 = builder.reverse().toString();
			s2 = s2.replaceFirst("#+", "#");
			StringBuilder builder2 = new StringBuilder(s2);
			s1 = builder2.reverse().toString();
			if(s1.charAt(s1.length() - 1) != '#') {
				s1 += "#";
			}
			System.out.println(s1);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
