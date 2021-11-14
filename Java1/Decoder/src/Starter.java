
public class Starter {
	public static void main (String[] args) {
		Decode obiekt = new Decode();
		obiekt.input(1);
		obiekt.input(1);
		obiekt.input(0);
		obiekt.input(0);
		obiekt.input(0);
		obiekt.input(1);
		obiekt.input(1);
		obiekt.input(1);
		obiekt.input(1);
		obiekt.input(1);
		obiekt.input(1);
		obiekt.input(1);
		obiekt.input(1);
		obiekt.input(1);
		obiekt.input(1);
		obiekt.input(1);
		obiekt.input(1);
		obiekt.input(0);
		obiekt.input(0);
		System.out.println(obiekt.output());
		obiekt.reset();
		obiekt.input(0);
		obiekt.input(0);
		obiekt.input(0);
		obiekt.input(1);
		obiekt.input(0);
		obiekt.input(0);
		obiekt.input(1);
		obiekt.input(1);
		obiekt.input(1);
		obiekt.input(0);
		System.out.println(obiekt.output());
		
	}
}
