class Decode extends DecoderInterface {

	private String result = "";
	private int X = 0;
	private int current = 0;
	
	@Override
	public void input(int bit) {
		if (bit == 1) {
			current++;
		}
		if (bit == 0) {
			if (current == 0);
			else if (X == 0) {
				X = current;
				result = "0";
				current = 0;
			}
			else {
				result = result + String.valueOf((current/X)-1);
				current = 0;
			}
		}
		
	}

	@Override
	public String output() {
		return result;
	}

	@Override
	public void reset() {
		result = "";
		X = 0;
		current = 0;
	}
	
}
