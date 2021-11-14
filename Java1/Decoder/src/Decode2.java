class Decode2 extends DecoderInterface {
    private String code = "";
    private String decode = "";

    public void input(int bit){
        String s=String.valueOf(bit);
        code = code + s;
    }

    public java.lang.String output(){       
        int X = 0;  
        int y = 0;  
        int i = 0;

        if(code.equals("")) return decode;
        if(code==null) return decode;
        if(code.length()==0) return decode;
        if(code.isEmpty()) return decode;

        for(; i<code.length(); i++){
            if(code.charAt(i) == '0') {
                if(X>0) break;
                continue;
            }
            else  X++;
        }
        if (code.charAt(i-1) == '1') return decode;
        if(X==code.length()) return decode;
        if(X==0) return decode;

        decode = decode + "0";
        for(;i<code.length(); i++){
            if(code.charAt(i) == '0') {
                if(y>0) {
                    decode = decode + String.valueOf((y/X)-1);
                    y=0;
                };
                continue;
            }
            else y++;
        }
        return decode;
    }

    public void reset(){
        code = "";
        decode = "";
    }
    public static void main(String[] args){   
    	
    }
}