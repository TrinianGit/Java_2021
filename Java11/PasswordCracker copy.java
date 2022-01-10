import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

class PasswordCracker implements PasswordCrackerInterface {
    String Schemat = "";
    String SchematIndex = "";
    String FinalResult = "";
    @Override
    public String getPassword(String host, int port) {
        Socket Serwer = null;
        BufferedReader Czytnik = null;
        PrintWriter PW = null;
        try{
            Serwer = new Socket(host, port);
            Czytnik = new BufferedReader(new InputStreamReader(Serwer.getInputStream()));
            PW = new PrintWriter(Serwer.getOutputStream());
        }
        catch ( UnknownHostException exc ) {
			exc.printStackTrace();
		}
		catch ( ConnectException exc ) {
			exc.printStackTrace();
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
        String Line;
        try{
            while((Line = Czytnik.readLine()) != null);
            PW.println("Program");
            Line = Czytnik.readLine();
            Line = Czytnik.readLine();
            String[] Split = Line.split(" ");
            Schemat = Split[2];
            for (int i = 0; i < Schemat.length(); i++){
                SchematIndex += "0";
            }
            Line = Czytnik.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Integer Znalezione = 0;
        Integer Poprzednie = 0;
        boolean First = true;
        char[] FinalResult = new char[Schemat.length()];
        while (true){
            try{
                String Result = "";
                if (Znalezione == Poprzednie){
                    if (!First){
                        String NewSchematIndex = "";
                        for (int i = 0; i < SchematIndex.length(); i++){
                            NewSchematIndex += String.valueOf(Integer.valueOf(SchematIndex.charAt(i) + 1));
                        }
                        SchematIndex = NewSchematIndex;

                        for (int i = 0; i < Schemat.length(); i++){
                            if (FinalResult[i] == '\0'){
                                if (Schemat.charAt(i) == 'l'){
                                    Result += PasswordComponents.lowercaseLetters.get(Integer.valueOf(SchematIndex.charAt(i)));
                                }
                                else if (Schemat.charAt(i) == 'u'){
                                    Result += PasswordComponents.uppercaseLetters.get(Integer.valueOf(SchematIndex.charAt(i)));
                                }
                                else if (Schemat.charAt(i) == 'n'){
                                    Result += PasswordComponents.numbers.get(Integer.valueOf(SchematIndex.charAt(i)));
                                }
                                else if(Schemat.charAt(i) == 's'){
                                    Result += PasswordComponents.symbols.get(Integer.valueOf(SchematIndex.charAt(i)));
                                }
                            }
                            else{
                                Result += FinalResult[i];
                            }
                            PW.println(Result);
                            String[] split = Czytnik.readLine().split(" ");
                            Poprzednie = Znalezione;
                            Znalezione = Integer.valueOf(split[4]);
                        }
                    }
                    else{
                        First = false;
                        for (int i = 0; i < Schemat.length(); i++){
                            if (FinalResult[i] != '\0'){
                                if (Schemat.charAt(i) == 'l'){
                                    Result += PasswordComponents.lowercaseLetters.get(Integer.valueOf(SchematIndex.charAt(i)));
                                }
                                else if (Schemat.charAt(i) == 'u'){
                                    Result += PasswordComponents.uppercaseLetters.get(Integer.valueOf(SchematIndex.charAt(i)));
                                }
                                else if (Schemat.charAt(i) == 'n'){
                                    Result += PasswordComponents.numbers.get(Integer.valueOf(SchematIndex.charAt(i)));
                                }
                                else if(Schemat.charAt(i) == 's'){
                                    Result += PasswordComponents.symbols.get(Integer.valueOf(SchematIndex.charAt(i)));
                                }
                            }
                        }
                        PW.println(Result);
                        String[] split = Czytnik.readLine().split(" ");
                        Poprzednie = Znalezione;
                        Znalezione = Integer.valueOf(split[4]);
                    }
                }
                else{
                    for (int j = 0; j < SchematIndex.length(); j++){
                        if (FinalResult[j] == '\0'){
                            String NewSchematIndex = "";
                            for (int k = 0; k < SchematIndex.length(); k++){
                                if (k == j){
                                    NewSchematIndex += String.valueOf(Integer.valueOf(SchematIndex.charAt(k) + 1));
                                }
                                else{
                                    NewSchematIndex += String.valueOf(Integer.valueOf(SchematIndex.charAt(k)));
                                }
                            }
                            SchematIndex = NewSchematIndex;
                        }
                        for (int i = 0; i < Schemat.length(); i++){
                            if (FinalResult[i] != '\0'){
                                if (Schemat.charAt(i) == 'l'){
                                    Result += PasswordComponents.lowercaseLetters.get(Integer.valueOf(SchematIndex.charAt(i)));
                                }
                                else if (Schemat.charAt(i) == 'u'){
                                    Result += PasswordComponents.uppercaseLetters.get(Integer.valueOf(SchematIndex.charAt(i)));
                                }
                                else if (Schemat.charAt(i) == 'n'){
                                    Result += PasswordComponents.numbers.get(Integer.valueOf(SchematIndex.charAt(i)));
                                }
                                else if(Schemat.charAt(i) == 's'){
                                    Result += PasswordComponents.symbols.get(Integer.valueOf(SchematIndex.charAt(i)));
                                }
                            }
                        }
                        PW.println(Result);
                        String[] split = Czytnik.readLine().split(" ");
                        Znalezione = Integer.valueOf(split[4]);
                        if (Poprzednie == Znalezione){
                            
                        }
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            break;
        }

        try{
            Serwer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
