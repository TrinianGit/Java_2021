import java.util.HashMap;
import java.util.Map;

public class Test03 {

  public static Map<Character, Character> stringToMap(String s) {
    Map<Character, Character> myMap = new HashMap<Character, Character>();
    String[] pairs = s.split(", ");
    for (int i = 0; i < pairs.length; i++) {
      String pair = pairs[i];
      String[] keyValue = pair.split("=");
      myMap.put(keyValue[0].toCharArray()[0], keyValue[1].toCharArray()[0]);
    }
    return myMap;
  }

  public static String decryptText(Map<Character, Character> decode, String text) {
    StringBuilder result = new StringBuilder();

    for (Character ch : text.toCharArray()) {
      Character decodedChar = decode.get(ch);
      if (decodedChar != null)
        result.append(decodedChar);
      else
        result.append(ch);
    }

    return result.toString();
  }

  public static Map<Character, Character> flipMap(Map<Character, Character> original) {
    Map<Character, Character> flipped = new HashMap<>();
    for (Map.Entry<Character, Character> entry : original.entrySet()) {
      flipped.put(entry.getValue(), entry.getKey());
    }
    return flipped;
  }

  public static void main(String[] args) throws Exception {
    int testCount = 0;
    String res;
    Decrypter dec = new Decrypter();
    Map<Character, Character> resultMap;
    Map<Character, Character> expectedMap;

    // ==== 1-1 test ====
    dec.setInputText(
        "Wydział   Fizyki, 	Astronomii \n    i\n    Informatyki 			Stosowanej\nogłasza, że i tak wszystkich na numerkach wypierdoli \nxD");
    resultMap = dec.getCode();
    expectedMap = stringToMap(
        "a=a, A=A, ł=ł, d=d, e=e, F=F, f=f, i=i, I=I, j=j, k=k, m=m, n=n, o=o, r=r, s=s, S=S, t=t, W=W, w=w, y=y, z=z");
    if (!resultMap.equals(expectedMap))
      throw new Exception("test 1 failed, result: " + resultMap.toString());
    testCount++;

    // ==== test oramusa (code) ====
    String test2 = "Dxb80 1*9c3ó8\n1023*4Ł f*30{*, 5678x!xW**\n*\n}!#x8W470{* s7x6xw4!9i";
    dec.setInputText(test2);
    resultMap = dec.getCode();
    expectedMap = stringToMap(
        "a=4, A=5, ł=Ł, d=2, e=9, F=f, f=#, i=*, I=}, j=i, k={, m=W, n=!, o=x, r=8, s=6, S=s, t=7, W=1, w=w, y=0, z=3");
    if (!resultMap.equals(expectedMap))
      throw new Exception("test oramusa (code) failed: " + resultMap.toString());
    testCount++;

    // ==== test oramusa (original string match) ====
    String decodedStr = decryptText(dec.getDecode(), test2);
    if (!decodedStr.equals("Dobry Wieczór\nWydział Fizyki, Astronomii\ni\nInformatyki Stosowanej"))
      throw new Exception("test oramusa (original string match) failed: " + decodedStr);
    testCount++;

    // ==== exact input test ====
    dec.setInputText("Wydział Fizyki, Astronomii i Informatyki Stosowanej");
    resultMap = dec.getCode();
    expectedMap = stringToMap(
        "a=a, A=A, ł=ł, d=d, e=e, F=F, f=f, i=i, I=I, j=j, k=k, m=m, n=n, o=o, r=r, s=s, S=S, t=t, W=W, w=w, y=y, z=z");
    if (!resultMap.equals(expectedMap))
      throw new Exception("exact input test failed: " + resultMap.toString());
    testCount++;

    // ==== pattern in the middle test ====
    dec.setInputText(
        "dupa Wydział Przyrodniczy xDD aaa Wydział Fizyki, Astronomii i Informatyki Stosowanej oadjsod aj alsd jals jasdl jasl djas l lsajd ;lajslaskdjalds ");
    resultMap = dec.getCode();
    expectedMap = stringToMap(
        "a=a, A=A, ł=ł, d=d, e=e, F=F, f=f, i=i, I=I, j=j, k=k, m=m, n=n, o=o, r=r, s=s, S=S, t=t, W=W, w=w, y=y, z=z");
    if (!resultMap.equals(expectedMap))
      throw new Exception("pattern in the middle test failed: " + resultMap.toString());
    testCount++;

    // ==== pattern in the end ====
    dec.setInputText(
        "dupa Wydział Przyrodniczy xDD aaa  oadjsod aj alsd jals jasdl jasl djas l lsajd ;lajslaskdjalds. I tak was wszystkich upierdolimy, podpisano Wydział Fizyki, Astronomii i Informatyki Stosowanej");
    resultMap = dec.getCode();
    expectedMap = stringToMap(
        "a=a, A=A, ł=ł, d=d, e=e, F=F, f=f, i=i, I=I, j=j, k=k, m=m, n=n, o=o, r=r, s=s, S=S, t=t, W=W, w=w, y=y, z=z");
    if (!resultMap.equals(expectedMap))
      throw new Exception("pattern in the end failed: " + resultMap.toString());
    testCount++;

    // ==== pattern in the middle with another fake pattern ====
    dec.setInputText(
        "dupa Wydział Przyrodniczy Wydzaał Fizyki, Astronomii i Informatyki Stosowanej xDD aaa Wydział Fizyki, Astronomii i Informatyki Stosowanej oadjsod aj alsd jals jasdl jasl djas l lsajd ;lajslaskdjalds ");
    resultMap = dec.getCode();
    expectedMap = stringToMap(
        "a=a, A=A, ł=ł, d=d, e=e, F=F, f=f, i=i, I=I, j=j, k=k, m=m, n=n, o=o, r=r, s=s, S=S, t=t, W=W, w=w, y=y, z=z");
    if (!resultMap.equals(expectedMap))
      throw new Exception("pattern in the middle with another fake pattern test failed: " + resultMap.toString());
    testCount++;

    // ==== matching occurences, mismatching count pattern test ====
    dec.setInputText(
        "dupa Wydział Przyrodniczy Wadział Fizaki, Astronomii i Informataki Stosowanej xDD aa oadjsod aj alsd jals jasdl jasl djas l lsajd ;lajslaskdjalds ");
    res = dec.getCode().toString();
    if (!res.equals("{}"))
      throw new Exception("matching occurences, mismatching count pattern test failed: " + resultMap.toString());
    testCount++;

    // ==== caesar's cipher ROT10 map generation ====
    String rot10 = "Ginjskł Psjius, Kcdbyxywss s Sxpybwkdius Cdycygkxot yqłkcjk, żo s dku gcjicdusmr xk xewobukmr gizsobnyvs hN";
    dec.setInputText(rot10);
    resultMap = dec.getCode();
    expectedMap = stringToMap(
        "a=k, A=K, ł=ł, d=n, e=o, F=P, f=p, i=s, I=S, j=t, k=u, m=w, n=x, o=y, r=b, s=c, S=C, t=d, W=G, w=g, y=i, z=j");
    if (!resultMap.equals(expectedMap))
      throw new Exception("caesar's cipher map generation failed, result: " + resultMap.toString());
    testCount++;

    // ==== caesar's cipher ROT10 decryption test ====
    decodedStr = decryptText(dec.getDecode(), rot10);
    if (!decodedStr.equals(
        "Wydział Fizyki, Astronomii i Informatyki Stosowanej oqłasza, że i tak wszystkimr na nemerkamr wyzierdovi hN"))
      throw new Exception("caesar's cipher decryption failed, result: " + decodedStr);
    testCount++;

    // ==== caesar's cipher ROT10 key flip test ====
    // checks if `code` is equal to `decode` with flipped keys and values
    Map<Character, Character> code = dec.getCode();
    if (!flipMap(code).equals(dec.getDecode()))
      throw new Exception("flipped map test failed");
    testCount++;

    // ==== null input test ====
    dec.setInputText(null);
    if (dec.getCode().size() != 0 || dec.getDecode().size() != 0)
      throw new Exception("null input test failed");
    testCount++;

    // ==== empty input test ====
    dec.setInputText("");
    if (dec.getCode().size() != 0 || dec.getDecode().size() != 0)
      throw new Exception("empty input test failed");
    testCount++;

    // ==== invalid input test ====
    dec.setInputText("Dupa");
    if (dec.getCode().size() != 0 || dec.getDecode().size() != 0)
      throw new Exception("invalid input test failed");
    testCount++;

    // ==== random cipher test ====
    String ciphered = "qF d VI Ddp JęKIG fgĄFłdNUO ŹOGÓę Kę VNDKNŃ g DdNrdN gŹIVNDg DFrdN DJŻIłp g KNRF fF ŻFrdDg?\nGIDg Kę łFRaśN ŻFgOG d RFŹÓFŃc fgĄFłdNUI? ąI ÓdN łdNG IśN ĆIĄFDÓę KęJNU g fdNrdN,\nfPęrI ÓdN JFGęŃśIĄND ÓIłNK fF ŻFrdDg d UFRF FrŻIĆIDg, GFĆNDg DFrdN FrŻIĆIf KęfP fF ÓI KF gIDĄOĆęśd DFrdN IśN ÓdN ÓIDgNRF JIJdNĆI JFśIUI ÓIDgNRF ŻFŹIUI łdNśUh FDFrp ,\nćęŹgdIĄ ydgęUd, sDKŻFÓFGdd d óÓAFŻGIKęUd vKFDFłIÓNN\nd KIU łęVhKUFłI d łIĆÓh rF KF ÓdN VNDK UKFŃ KIG KIUd DFrdN ĆN GFĆNDg RF DFrdN łęŃGdIc rF KIU fd Ddp JFŹFrI ÓdN łdNG ł VIUdNV Kę Ddp łęfPFłIĄND ŻFŹgdÓdN\nIśN fPęrI Kę ÓdN łdNG ÓdN ŻFgOGdNDg fF KF VNDK łdIŻI .\nćIŹgdIĄ ydgIUd, sDKŻFÓFGdd d óÓAFŻGIKIUd vKFDFłIÓNV FŻIg ćęŹgdIĄ ydgęUdC sDKŻFÓFGdd d óÓAFŻGIKęUd vKFDFłIÓNV FŻIg ćęŹgdIĄ ydgęUd, sęKŻFÓFGdd d óÓAFŻGIKęUd vęFDFłIÓNV\n\tćęŹgdIĄ ydgęUd,   sDKŻFÓFGdd d\tóÓAFŻGIKęUd vKFDFłIÓNV\nłdŹIf JFVpfdI F KęG UdG ręĄ JIJdNĆ VIÓ JIłNĄ2 VIU ÓdN VNDKNŃfdN ł JNĄÓd ŻFgłdÓdpKN\nOGęDĄFłF KF Ddp ÓdN gIrdNŻIVfdN gI KIUh FDFrp VIU FVfdNf DłdpKę rF KF ŃłdIŹfgę F KęG ĆN ÓdN GIfdN\nfPęrI ł ŹFGO UŻgęĆI IÓd VNŹÓNRF FrŻIgI ŃłdpKNRF ÓdN fPFŹgd KOKIV F UFŃfdaĄ GÓdN IśN łFRaśN FRaśÓdN F gIDIŹę łdIŻę ĆNrę GdNc VIUhD\nRFŹÓFŃc rF JIJdNĆ ÓdUFRF ÓdN FrŻIĆIĄ I Kę gI fF RF FrŻIĆIDg fF? ";
    dec.setInputText(ciphered);
    resultMap = dec.getCode();
    expectedMap = stringToMap(
        "a=I, A=s, ł=Ą, d=Ź, e=N, F=y, f=A, i=d, I=ó, j=V, k=U, m=G, n=Ó, o=F, r=Ż, s=D, S=v, t=K, W=ć, w=ł, y=ę, z=g");
    if (!resultMap.equals(expectedMap))
      throw new Exception("random cipher test failed: " + resultMap.toString());
    testCount++;
    
    // ==== comma test ====
    String cip = "Wydział Fizyki Astronomii i Informatyki Stosowanej Wydzqał Fqzykq, Astronomqq q Informatykq Stosowanej";
    dec.setInputText(cip);
    resultMap = dec.getCode();
    expectedMap = stringToMap(
    		"a=a, A=A, ł=ł, d=d, e=e, F=F, f=f, i=q, I=I, j=j, k=k, m=m, n=n, o=o, r=r, s=s, S=S, t=t, W=W, w=w, y=y, z=z");
    if (!resultMap.equals(expectedMap))
        throw new Exception("random cipher test failed: " + resultMap.toString());
    testCount++;

    System.out.println("All " + testCount + " tests passed successfully");
  }
}
