import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Math;

class Compression implements CompressionInterface{

	List<String> ToCompress = new ArrayList<>();
	Map<String, Integer> HowManyTimes = new HashMap<>();
    Map <String, String> CompressionKey = new HashMap<>();
    Map <String, String> DeCompressionKey = new HashMap<>();
    List <String> ToCalculate = new ArrayList<>();
    List <String> KeySet = new ArrayList<>();
	int KeySize = 1;
	int BestKeySize = 0;
	
	@Override
	public void addWord(String word) {
		if (!HowManyTimes.containsKey(word)) {
			HowManyTimes.put(word, 1);
		}
		else {
			int i = HowManyTimes.get(word);
			HowManyTimes.remove(word);
			HowManyTimes.put(word,  i+1 );
		}
		ToCompress.add(word);
		
	}

	@Override
	public void compress() {
        CompressionKey.clear();
        DeCompressionKey.clear();
        ToCalculate.clear();
        KeySet.clear();
		CountForAndAgainst();
		HowManyTimes.clear();
	}

	@Override
	public Map<String, String> getHeader() {
		return CompressionKey;
	}

	@Override
	public String getWord() {
		String ToReturn = new String(ToCompress.get(0));
		
		if (DeCompressionKey.size() == 0){

		}
        else if (DeCompressionKey.get(ToReturn) != null) {
            ToReturn = DeCompressionKey.get(ToReturn);
        }
        else {
            ToReturn = "1" + ToReturn;
        }
        ToCompress.remove(0);
		return ToReturn;
	}

	private void CountForAndAgainst() {
		List <String> CurrentBest = new ArrayList<>();
		int Best = 0;
		int CurrentCompressed = 0;
		//int KeySize = 1;
		Map<String, Integer> HowManyTimesCopy = new HashMap<>(HowManyTimes);
		while (HowManyTimesCopy.size() != 0) {
			ToCalculate.add(findMostAppear(HowManyTimesCopy));
			CurrentCompressed = ToCalculate.size();
			if (CurrentCompressed > Math.pow(2.0, KeySize - 1)){
				KeySize++;
			}
            if (calculateForAndAgainst(Best, CurrentCompressed, HowManyTimes) > Best){
                CurrentBest = new ArrayList<>(ToCalculate);
                Best = calculateForAndAgainst (Best, CurrentCompressed, HowManyTimes);
				BestKeySize = KeySize;
            }
			HowManyTimesCopy.remove(findMostAppear(HowManyTimesCopy));
		}

        if (CurrentBest.size() != 0) doCompression (CurrentBest);

	}

    private int calculateForAndAgainst(int best, int cc, Map<String, Integer> howManyTimesCopy) {
        int Gained = 0;
        int Lost = 0;
        //int KeySize = 1;

        for (var entry : howManyTimesCopy.entrySet()){
            if (ToCalculate.contains(entry.getKey())){
                Gained += (entry.getKey().length() - KeySize) * entry.getValue();
                Lost += KeySize + entry.getKey().length();
            }
            else {
                Lost += entry.getValue();
            }
        }
        return (Gained - Lost);
    }

    private String findMostAppear(Map<String, Integer> howManyTimesCopy) {
		Integer current = 0;
		String Key = "";
		for (var entry : howManyTimesCopy.entrySet()) {
		    if (entry.getValue() > current) {
		    	current = entry.getValue();
		    	Key = entry.getKey();
		    }
		}
		return Key;
	}

    private void doCompression(List<String> Compress) {

        setKeyList(BestKeySize);

        for (int i = 0; i < Compress.size(); i++){
            DeCompressionKey.put(Compress.get(i), KeySet.get(i));
            CompressionKey.put(KeySet.get(i), Compress.get(i));
        }
    }


    public void setKeyList(int keySize) {
		int b = (int)(Math.pow(2.0, keySize - 1));
		System.out.println(b);
		for (int a = 0; a < b; a++){
			String result = Integer.toBinaryString(a);
			System.out.println(result);
			String Intstr = String.valueOf(keySize);
			String format = "%" + Intstr + "s";
			String resultWithPadding = String.format(format, result).replaceAll(" ", "0");
			System.out.println(resultWithPadding);
			KeySet.add(resultWithPadding);
		}
        
   }

   
   public static void main(String[] args) {
	   Compression k = new Compression();
	   k.setKeyList(3);
	   System.out.println(k.KeySet);
   }

}