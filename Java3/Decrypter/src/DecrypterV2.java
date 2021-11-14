import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

class Decrypter implements DecrypterInterface{

	private String encrypted = "";
	private Vector<Integer> Sus = new Vector<Integer>();
	private String[] pattern = {"Wydzia\u0142", "Fizyki,", "Astronomii", "i", "Informatyki", "Stosowanej"};
	private Map <Character, Character> result = new HashMap<>();
	
	@Override
	public void setInputText(String encryptedDocument) {
		encrypted = encryptedDocument;
	}

	@Override
	public Map<Character, Character> getCode() {
		result.clear();
		if (DecryptMachine(encrypted)) return result;
		result.clear();
		return result;
	}

	@Override
	public Map<Character, Character> getDecode() {
		result.clear();
		if (DecryptMachine(encrypted)) {
			Map<Character, Character> reversed_result = new HashMap<>();
			for(Map.Entry<Character, Character> entry : result.entrySet()){
			    reversed_result.put(entry.getValue(), entry.getKey());
			}
			return reversed_result;
		}
		result.clear();
		return result;
	}
	
	private boolean DecryptMachine(String Eted) {
		Sus.clear();
		if (Eted == null) return false;
		else {
			Eted = WhiteCharacterDestroy(Eted);
			if (!NumericCheck(Eted)) return false;
			else {
				int i;
				for (i = 0; i < Sus.size(); i++) {
					if (ToMap(Eted, Sus.get(i))) break;
					result.clear();
				}
				if (i == Sus.size()) {
					return false;
				}
				return true;
			}
		}
	}
	
	private boolean NumericCheck(String ToCheck) {
		if (ToCheck.length() < 51) return false;
		else {
			String[] Checking = ToCheck.split(" ");
			int first = 0;
			while (first < Checking.length) {
				int count = 0;
				int i = first;				
				for (i = first; i < Checking.length && count != pattern.length; i++) {
					if (Checking[i].length() == pattern[count].length()) {
						count++;
						if (count == 1) {
							first = i;
						}
					}
					else {
						count = 0;
						i = first++;
					}
					
				}
				if (count == pattern.length) {
					Sus.add(first);
				}
				first++;
			}
		}
		if (Sus.size() != 0) return true;
		return false;
	}
	
	private boolean ToMap(String MappingString, int FromWhere) {
		String[] Mapping = MappingString.split(" ");
		String ToKey;
		String ToValue;
		for (int i = FromWhere; i < (FromWhere + 6); i++) {
			ToKey = pattern[i-FromWhere];
			ToValue = Mapping[i];
			for (int j = 0; j < ToKey.length(); j++) {
				if (ToKey.charAt(j) == ',');
				else if (ToValue.charAt(j) == ',') return false;
				else if (result.containsValue(ToValue.charAt(j)) && result.get(ToKey.charAt(j)) == null) return false;
				else if (!result.containsKey(ToKey.charAt(j))) result.put(ToKey.charAt(j), ToValue.charAt(j));
				else {
					if (result.get(ToKey.charAt(j)) == ToValue.charAt(j));
					else {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private String WhiteCharacterDestroy(String wsString) {
		wsString = wsString.replace("\t", " ");
		wsString = wsString.replace("\n", " ");
		while (wsString.replace("  ", " ") != wsString) {
			wsString = wsString.replace("  ", " ");
		}
		return wsString;
	}

}

