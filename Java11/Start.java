public class Start {
    
    public static void main(String[] args) {
        PasswordCracker Pass = new PasswordCracker();
        for (int i = 0; i < 1000; i++){
            System.out.println("Iteracja " + i + ": " + Pass.getPassword("localhost", 8080));
        }
    }
}
