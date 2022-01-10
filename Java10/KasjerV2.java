import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

class Kasjer implements KasjerInterface{
    RozmieniaczInterface MojRozmieniacz;
    Map<Integer, List<Pieniadz>> Kasa_Sklepowa = new HashMap<>();
    List<Integer> Nominaly = new ArrayList<>(Arrays.asList(1, 2, 5, 10, 20, 50, 100, 200, 500));
    @Override
    public List<Pieniadz> rozlicz(int cena, List<Pieniadz> pieniadze) {
        InicjacjaKasy();
        List<Pieniadz> Reszta = new ArrayList<>();
        if (!CzyWszystkieNR(pieniadze)){
            if (IleDalKlient(pieniadze) == cena){
                DodajDoKasy(pieniadze);
                return Reszta;
            }
            else{
                return WydajReszteKlientowi(pieniadze, cena);
            }
        }
        else{
            if (IleDalKlient(pieniadze) == cena){
                DodajDoKasy(pieniadze);
                return Reszta;
            }
            else{
                return WydajReszteKlientowi(pieniadze, cena);
            }
        }
    }

    
    
    
    @Override
    public List<Pieniadz> stanKasy() {
        List<Pieniadz> Stan_Kasy = new ArrayList<>();
        for (var entry : Kasa_Sklepowa.values()){
            for(int i = 0; i < entry.size(); i++){
                Stan_Kasy.add(entry.get(i));
            }
        }
        return Stan_Kasy;
    }
    
    @Override
    public void dostępDoRozmieniacza(RozmieniaczInterface rozmieniacz) {
        MojRozmieniacz = rozmieniacz;
    }
    
    @Override
    public void dostępDoPoczątkowegoStanuKasy(Supplier<Pieniadz> dostawca) {
        InicjacjaKasy();
        Pieniadz banknot = dostawca.get();
        while (banknot != null){
            Kasa_Sklepowa.get(banknot.wartosc()).add(banknot);
            banknot = dostawca.get();
        }
    }
    
    private void InicjacjaKasy() {
        for (int i = 0; i < Nominaly.size(); i++){
            if (Kasa_Sklepowa.get(Nominaly.get(i)) == null){
                Kasa_Sklepowa.put(Nominaly.get(i), new ArrayList<Pieniadz>());
            }
        }
    }
    
    private void DodajDoKasy(List<Pieniadz> pieniadze) {
        for (int i = 0; i < pieniadze.size(); i++){
            Kasa_Sklepowa.get(pieniadze.get(i).wartosc()).add(pieniadze.get(i));
        }
    }

    private boolean CzyWszystkieNR (List<Pieniadz> kasa){
        
        for (int i = 0; i < kasa.size(); i++){
            if (kasa.get(i).czyMozeBycRozmieniony()){
                return false;
            }
        }
        return true;
    }
    
    private int IleDalKlient (List<Pieniadz> kasa){
        int suma = 0;
        for (int i = 0; i < kasa.size(); i++){
            suma += kasa.get(i).wartosc();
        }
        return suma;
    }
    
    private List<Pieniadz> WydajReszteKlientowi(List<Pieniadz> kasa, int cena) {
        if (!CzyWszystkieNR(kasa)){
            List<Pieniadz> kasaKopia = new ArrayList<>(kasa);
            for (int i = 0; i < kasa.size(); i++){
                if (!kasa.get(i).equals(NajwiekszyRozmienialny(kasa))){
                    Kasa_Sklepowa.get(kasa.get(i).wartosc()).add(kasa.get(i));
                }
            }
            return ZwrotPieniedzy(NajwiekszyRozmienialny(kasaKopia), IleDalKlient(kasaKopia) - cena);
        }
        else{
            return ZwrocZKasy(kasa, IleDalKlient(kasa) - cena);
        }
        
    }

    private Pieniadz NajmniejszyNierozmienialny (List<Pieniadz> kasa){
        Pieniadz banknot = kasa.get(0);
        for (int i = 0; i < kasa.size(); i++){
            if (banknot.czyMozeBycRozmieniony()){
                banknot = kasa.get(i);
            }
            if((banknot.wartosc() > kasa.get(i).wartosc()) && !kasa.get(i).czyMozeBycRozmieniony()){
                banknot = kasa.get(i);
            }
        }
        return banknot;
    }
    
    private Pieniadz NajwiekszyRozmienialny (List<Pieniadz> kasa){
        Pieniadz banknot = kasa.get(0);
        for (int i = 0; i < kasa.size(); i++){
            if (!banknot.czyMozeBycRozmieniony()){
                banknot = kasa.get(i);
            }
            if((banknot.wartosc() < kasa.get(i).wartosc()) && kasa.get(i).czyMozeBycRozmieniony()){
                banknot = kasa.get(i);
            }
        }
        return banknot;
    }


    private List<Pieniadz> ZwrotPieniedzy(Pieniadz najwiekszyRozmienialny, int iledoZwrotu) {
        List<Pieniadz> PoRozmianie = MojRozmieniacz.rozmien(najwiekszyRozmienialny);
        List<Pieniadz> DoZwrotu = new ArrayList<>();
        for (int i = 0; i < PoRozmianie.size(); i++){
            if (PoRozmianie.get(i).wartosc() == iledoZwrotu){
                DoZwrotu.add(PoRozmianie.get(i));
                iledoZwrotu -= PoRozmianie.get(i).wartosc();
                PoRozmianie.remove(i);
                break;
            }
        }
        if (DoZwrotu.size() == 0){
            for (int i = 0; i < PoRozmianie.size(); i++){
                if (iledoZwrotu - PoRozmianie.get(i).wartosc() >= 0){
                    DoZwrotu.add(PoRozmianie.get(i));
                    PoRozmianie.remove(i);
                    iledoZwrotu -= DoZwrotu.get(i).wartosc();
                }
            }
        }
        if (iledoZwrotu == 0){
            for (int i = 0; i < PoRozmianie.size(); i++){
                Kasa_Sklepowa.get(PoRozmianie.get(i).wartosc()).add(PoRozmianie.get(i));
            }
            PoRozmianie.clear();
        }
        else{
            while (iledoZwrotu != 0){
                List<Pieniadz> RozmianaRozmiany = new ArrayList<>();
                for (int i = 0; i < PoRozmianie.size(); i++){
                    if (PoRozmianie.get(i).wartosc() != 1){
                        RozmianaRozmiany = MojRozmieniacz.rozmien(PoRozmianie.get(i));
                        PoRozmianie.remove(i);
                        break;
                    }
                }
                for (int i = 0; i < RozmianaRozmiany.size(); i++){
                    PoRozmianie.add(RozmianaRozmiany.get(i));
                }
                for (int i = 0; i < PoRozmianie.size(); i++){
                    if ((iledoZwrotu - PoRozmianie.get(i).wartosc()) >= 0){
                        DoZwrotu.add(PoRozmianie.get(i));
                        iledoZwrotu -= PoRozmianie.get(i).wartosc();
                        PoRozmianie.remove(i);
                    }
                }
            }
        }
        for (int i = 0; i < PoRozmianie.size(); i++){
            Kasa_Sklepowa.get(PoRozmianie.get(i).wartosc()).add(PoRozmianie.get(i));
        }
        PoRozmianie.clear();
        return DoZwrotu;
    }
    
    private List<Pieniadz> ZwrocZKasy(List<Pieniadz> kasa, int ileDoZwrotu) {
        List<Pieniadz> DoZwrotu = new ArrayList<>();
        for (int i = 0; i < Nominaly.size(); i++){
            if (Nominaly.get(i) > ileDoZwrotu && Kasa_Sklepowa.get(Nominaly.get(i)).size() > 0){
                for (int j = 0; j < Kasa_Sklepowa.get(Nominaly.get(i)).size(); j++){
                    if (Kasa_Sklepowa.get(Nominaly.get(i)).get(j).czyMozeBycRozmieniony()){
                        DoZwrotu = ZwrotPieniedzy(Kasa_Sklepowa.get(Nominaly.get(i)).get(j), ileDoZwrotu);
                        Kasa_Sklepowa.get(Nominaly.get(i)).remove(j);
                        break;
                    }
                }
                if (DoZwrotu.size() > 0){
                    break;
                }
            }
            else if (Nominaly.get(i) == ileDoZwrotu && Kasa_Sklepowa.get(Nominaly.get(i)).size() > 0){
                for (int j = 0; j < Kasa_Sklepowa.get(Nominaly.get(i)).size(); j++){
                    if (Kasa_Sklepowa.get(Nominaly.get(i)).get(j).czyMozeBycRozmieniony()){
                        DoZwrotu.add(Kasa_Sklepowa.get(Nominaly.get(i)).get(j));
                        Kasa_Sklepowa.get(Nominaly.get(i)).remove(j);
                        break;
                    }
                }
                if (PoliczNominalyPoza() < ileDoZwrotu && DoZwrotu.size() == 0){
                    DoZwrotu.add(Kasa_Sklepowa.get(Nominaly.get(i)).get(0));
                    Kasa_Sklepowa.get(Nominaly.get(i)).remove(0);
                }
            }
        }
        if (DoZwrotu.size() == 0){
            if (WKasieSameNR()){
                DoZwrotu = WydajZNR(ileDoZwrotu, new ArrayList<>());
            }
            else{
                List <Pieniadz> Rozmienialne = new ArrayList<>();
                for (int i = 0; Nominaly.get(i) < ileDoZwrotu; i++){
                    for (int j = 0; j < Kasa_Sklepowa.get(Nominaly.get(i)).size(); j++){
                        if (Kasa_Sklepowa.get(Nominaly.get(i)).get(j).czyMozeBycRozmieniony()){
                            Rozmienialne.add(Kasa_Sklepowa.get(Nominaly.get(i)).get(j));
                        }
                    }
                }
                if (RozmienialneStarcza(ileDoZwrotu, Rozmienialne)){
                    DoZwrotu = WydajZMalychRozmienialnych(ileDoZwrotu);
                }
                else{
                    DoZwrotu = WydajHybrydowo(ileDoZwrotu);
                }
            }
        }
        DoZwrotu.add(NajmniejszyNierozmienialny(kasa));
        return DoZwrotu;
    }

    private List<Pieniadz> WydajHybrydowo(int ileDoZwrotu) {

        List <Pieniadz> PieniadzeZKasy = new ArrayList<>();
        List <Pieniadz> PieniadzeZKasy2 = new ArrayList<>();
        List <Pieniadz> DoZwrotu = new ArrayList<>();
        for (int i = 0; Nominaly.get(i) < ileDoZwrotu; i++){
            for (int j = 0; j < Kasa_Sklepowa.get(Nominaly.get(i)).size(); j++){
                if (Kasa_Sklepowa.get(Nominaly.get(i)).get(j).czyMozeBycRozmieniony()){
                    PieniadzeZKasy.add(Kasa_Sklepowa.get(Nominaly.get(i)).get(j));
                }
                else{
                    PieniadzeZKasy2.add(Kasa_Sklepowa.get(Nominaly.get(i)).get(j));
                }
            }
            Kasa_Sklepowa.get(Nominaly.get(i)).clear();
        }
        for (int i = 0; i < PodliczRozmienialne(PieniadzeZKasy); i++){
            DoZwrotu = WydajZNR(ileDoZwrotu - (PodliczRozmienialne(PieniadzeZKasy) - i), PieniadzeZKasy2);
            if (DoZwrotu.size() != 0){
                ileDoZwrotu = PodliczRozmienialne(PieniadzeZKasy) - i;
                break;
            }
        }
        while (ileDoZwrotu != 0){
            List<Pieniadz> Rozmiana = new ArrayList<>();
            for (int i = 0; i < PieniadzeZKasy.size(); i++){
                if (PieniadzeZKasy.get(i).wartosc() != 1 && PieniadzeZKasy.get(i).czyMozeBycRozmieniony()){
                    Rozmiana = MojRozmieniacz.rozmien(PieniadzeZKasy.get(i));
                    PieniadzeZKasy.remove(i);
                    break;
                }
            }
            for (int i = 0; i < Rozmiana.size(); i++){
                PieniadzeZKasy.add(Rozmiana.get(i));
            }
            for (int i = 0; i < PieniadzeZKasy.size(); i++){
                if ((ileDoZwrotu - PieniadzeZKasy.get(i).wartosc()) >= 0){
                    DoZwrotu.add(PieniadzeZKasy.get(i));
                    ileDoZwrotu -= PieniadzeZKasy.get(i).wartosc();
                    PieniadzeZKasy.remove(i);
                }
            }
        }
        for (int i = 0; i < PieniadzeZKasy.size(); i++){
            Kasa_Sklepowa.get(PieniadzeZKasy.get(i).wartosc()).add(PieniadzeZKasy.get(i));
        }

        return DoZwrotu;
    }

    private List<Pieniadz> WydajZMalychRozmienialnych(int ileDoZwrotu) {
        
        List <Pieniadz> PieniadzeZKasy = new ArrayList<>();
        List <Pieniadz> PieniadzeZKasy2 = new ArrayList<>();
        List <Pieniadz> DoZwrotu = new ArrayList<>();
        for (int i = 0; Nominaly.get(i) < ileDoZwrotu; i++){
            for (int j = 0; j < Kasa_Sklepowa.get(Nominaly.get(i)).size(); j++){
                if (Kasa_Sklepowa.get(Nominaly.get(i)).get(j).czyMozeBycRozmieniony()){
                    PieniadzeZKasy.add(Kasa_Sklepowa.get(Nominaly.get(i)).get(j));
                }
                else{
                    PieniadzeZKasy2.add(Kasa_Sklepowa.get(Nominaly.get(i)).get(j));
                }
            }
            Kasa_Sklepowa.get(Nominaly.get(i)).clear();
        }
        
        while (ileDoZwrotu != 0){
            List<Pieniadz> Rozmiana = new ArrayList<>();
            for (int i = 0; i < PieniadzeZKasy.size(); i++){
                if (PieniadzeZKasy.get(i).wartosc() != 1){
                    Rozmiana = MojRozmieniacz.rozmien(PieniadzeZKasy.get(i));
                    PieniadzeZKasy.remove(i);
                    break;
                }
            }
            for (int i = 0; i < Rozmiana.size(); i++){
                PieniadzeZKasy.add(Rozmiana.get(i));
            }
            for (int i = 0; i < PieniadzeZKasy.size(); i++){
                if ((ileDoZwrotu - PieniadzeZKasy.get(i).wartosc()) >= 0){
                    DoZwrotu.add(PieniadzeZKasy.get(i));
                    ileDoZwrotu -= PieniadzeZKasy.get(i).wartosc();
                    PieniadzeZKasy.remove(i);
                }
            }
        }
        for (int i = 0; i < PieniadzeZKasy.size(); i++){
            Kasa_Sklepowa.get(PieniadzeZKasy.get(i).wartosc()).add(PieniadzeZKasy.get(i));
        }
        for (int i = 0; i < PieniadzeZKasy2.size(); i++){
            Kasa_Sklepowa.get(PieniadzeZKasy2.get(i).wartosc()).add(PieniadzeZKasy2.get(i));
        }
        return DoZwrotu;
    }
    
    
    private List<Pieniadz> WydajZNR(int ileDoZwrotu, List<Pieniadz> kasaPieniadze) {
        List <Pieniadz> PieniadzeZKasy = new ArrayList<>(kasaPieniadze);
        List <Pieniadz> Pierwotna_Lista;
        List <Pieniadz> DoZwrotu = new ArrayList<>();
        int licznik = 0;
        if (PieniadzeZKasy.size() == 0){
            for (int i = 0; Nominaly.get(i) < ileDoZwrotu; i++){
                for (int j = 0; j < Kasa_Sklepowa.get(Nominaly.get(i)).size(); j++){
                    if (!Kasa_Sklepowa.get(Nominaly.get(i)).get(j).czyMozeBycRozmieniony()){
                        PieniadzeZKasy.add(Kasa_Sklepowa.get(Nominaly.get(i)).get(j));
                    }
                }
                Kasa_Sklepowa.get(Nominaly.get(i)).clear();
            }
        }
        Pierwotna_Lista = new ArrayList<>(PieniadzeZKasy);
        while (ileDoZwrotu != 0 && !ListaCheck(Pierwotna_Lista, DoZwrotu)){
            if (PieniadzeZKasy.size() != 0){
                if (ileDoZwrotu - PieniadzeZKasy.get(0).wartosc() >= 0){
                    DoZwrotu.add(PieniadzeZKasy.get(0));
                    ileDoZwrotu -= PieniadzeZKasy.get(0).wartosc(); 
                    PieniadzeZKasy.remove(0);
                }
                else{
                    if (DoZwrotu.size() != 0){
                        PieniadzeZKasy.add(DoZwrotu.get(0));
                        ileDoZwrotu += DoZwrotu.get(0).wartosc();
                        DoZwrotu.remove(0);
                    }
                    else{
                        Pieniadz hajs = PieniadzeZKasy.get(0);
                        PieniadzeZKasy.remove(0);
                        PieniadzeZKasy.add(hajs);
                    }
                }
            }
            if (licznik == 0){
                Pierwotna_Lista = new ArrayList<>(DoZwrotu);
                if (PieniadzeZKasy.size() != 0){
                    if (ileDoZwrotu - PieniadzeZKasy.get(0).wartosc() >= 0){
                        DoZwrotu.add(PieniadzeZKasy.get(0));
                        ileDoZwrotu -= PieniadzeZKasy.get(0).wartosc(); 
                        PieniadzeZKasy.remove(0);
                    }
                    else{
                        if (DoZwrotu.size() != 0){
                            PieniadzeZKasy.add(DoZwrotu.get(0));
                            ileDoZwrotu += DoZwrotu.get(0).wartosc();
                            DoZwrotu.remove(0);
                        }
                    }
                }
            }
            licznik++;
        }
        if (ileDoZwrotu != 0) {
            return new ArrayList<>();
        }
        else{
            for (int i = 0; i < PieniadzeZKasy.size(); i++){
                Kasa_Sklepowa.get(PieniadzeZKasy.get(i).wartosc()).add(PieniadzeZKasy.get(i));
            }
            return DoZwrotu;
        }
    }
    
    
    private boolean ListaCheck(List<Pieniadz> pierwotna_Lista, List<Pieniadz> doZwrotu) {
        if (doZwrotu.size() != pierwotna_Lista.size()){
            return false;
        }
        for (int i = 0; i < doZwrotu.size(); i++){
            boolean found = false;
            for (int j = 0; j < pierwotna_Lista.size(); j++){
                if (pierwotna_Lista.get(j).numerSeryjny() == doZwrotu.get(i).numerSeryjny()){
                    found = true;
                }
            }
            if (found == false){
                return false;
            }
        }
        return true;
    }




    private int PoliczNominalyPoza() {
        int ToReturn = 0;
        for (int i = 0; i < Nominaly.size(); i++){
            for (int j = 0; j < Kasa_Sklepowa.get(Nominaly.get(i)).size(); j++){
                if (Kasa_Sklepowa.get(Nominaly.get(i)).get(j).czyMozeBycRozmieniony()){
                    ToReturn += Kasa_Sklepowa.get(Nominaly.get(i)).get(j).wartosc();
                }
            }
        }
        return ToReturn;
    }
    
    private boolean WKasieSameNR() {
        for (int i = 0; i < Nominaly.size(); i++){
            for (int j = 0; j < Kasa_Sklepowa.get(Nominaly.get(i)).size(); j++){
                if (Kasa_Sklepowa.get(Nominaly.get(i)).get(j).czyMozeBycRozmieniony()){
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean RozmienialneStarcza(int ileDoZwrotu, List<Pieniadz> pieniadzeZKasy) {
        int ToReturn = 0;
        for (int i = 0; i < pieniadzeZKasy.size(); i++){
            ToReturn += pieniadzeZKasy.get(i).wartosc();
        }
        return ToReturn >= ileDoZwrotu ? true : false;
    }
    
    private int PodliczRozmienialne(List<Pieniadz> pieniadzeZKasy) {
        int ToReturn = 0;
        for (int i = 0; i < pieniadzeZKasy.size(); i++){
            ToReturn += pieniadzeZKasy.get(i).wartosc();
        }
        return ToReturn;
    }
    
}