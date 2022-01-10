import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

class PasswordCracker implements PasswordCrackerInterface {
    String Schemat = "";
    Integer[] SchematIndex;
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
            Line = Czytnik.readLine();
            PW.println("Program");
            PW.flush();
            Line = Czytnik.readLine();
            Line = Czytnik.readLine();
            String[] Split = Line.split(" ");
            Schemat = Split[2];
            SchematIndex = new Integer[Schemat.length()];
            for (int i = 0; i < Schemat.length(); i++){
                SchematIndex[i] = 0;
            }
            Line = Czytnik.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Integer Znalezione = 0;
        Integer Poprzednie = 0;
        Integer Obecny = 0;
        boolean First = true;
        boolean Try = false;
        boolean New = true;
        while (true){
            try{
                String Result = "";
                if (!New){
                    if (Znalezione == Poprzednie || Try == true){
                        if (First != true){
                            Integer[] NewSchematIndex = new Integer[SchematIndex.length];
                            for (int i = 0; i < SchematIndex.length; i++){
                                if (i == Obecny){
                                    NewSchematIndex[i] = SchematIndex[i] + 1;
                                }
                                else{
                                    NewSchematIndex[i] = SchematIndex[i];
                                }
                            }
                            SchematIndex = NewSchematIndex;
                        }
                        else{
                            First = false;
                        }
                        Try = false;
                    }
                    else{
                        Integer[] NewSchematIndex = new Integer[SchematIndex.length];
                        for (int i = 0; i < SchematIndex.length; i++){
                            if (i == Obecny){
                                NewSchematIndex[i] = SchematIndex[i] + 1;
                            }
                            else{
                                NewSchematIndex[i] = SchematIndex[i];
                            }
                        }
                        SchematIndex = NewSchematIndex;
                    }
                }
                for (int i = 0; i < Schemat.length(); i++){
                    if (Schemat.charAt(i) == 'l'){
                        Result += PasswordComponents.lowercaseLetters.get(SchematIndex[i]);
                    }
                    else if (Schemat.charAt(i) == 'u'){
                        Result += PasswordComponents.uppercaseLetters.get(SchematIndex[i]);
                    }
                    else if (Schemat.charAt(i) == 'n'){
                        Result += PasswordComponents.numbers.get(SchematIndex[i]);
                    }
                    else if(Schemat.charAt(i) == 's'){
                        Result += PasswordComponents.symbols.get(SchematIndex[i]);
                    }
                }
                PW.println(Result);
                PW.flush();
                Line = Czytnik.readLine();
                //System.out.println(Result);
                String[] split = Line.split(" ");
                if (Line.equals("+OK")){
                    try{
                        Serwer.close();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    return Result;
                }
                else if (split.length > 4){
                    Poprzednie = Znalezione;
                    Znalezione = Integer.valueOf(split[4]);
                    New = false;
                    if (Znalezione >= Poprzednie && Znalezione != Schemat.length()) {
                        if (Schemat.charAt(Obecny) == 'l'){
                            if(SchematIndex[Obecny] == PasswordComponents.lowercaseLetters.size() - 1){
                                Obecny++;
                                Try = false;
                                New = true;
                            }
                            else{
                                Try = true;
                            }
                        }
                        else if (Schemat.charAt(Obecny) == 'u'){
                            if(SchematIndex[Obecny] == PasswordComponents.uppercaseLetters.size() - 1){
                                Obecny++;
                                Try = false;
                                New = true;
                            }
                            else{
                                Try = true;
                            }
                        }
                        else if (Schemat.charAt(Obecny) == 'n'){
                            if(SchematIndex[Obecny] == PasswordComponents.numbers.size() - 1){
                                Obecny++;
                                Try = false;
                                New = true;
                            }
                            else{
                                Try = true;
                            }
                        }
                        else if(Schemat.charAt(Obecny) == 's'){
                            if(SchematIndex[Obecny] == PasswordComponents.symbols.size() - 1){
                                Obecny++;
                                Try = false;
                                New = true;
                            }
                            else{
                                Try = true;
                            }
                        }
                    }
                    else if (Znalezione != Poprzednie){
                        SchematIndex[Obecny] -= 1;
                        Obecny++;
                        Try = false;
                        New = true;
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
                return "null";
            }
        }
    }

}
