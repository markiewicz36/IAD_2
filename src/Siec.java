import java.io.Serializable;
import java.net.NetPermission;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class Siec implements Serializable {
    private ArrayList<ArrayList<Neuron>> siec_neuronowa = new ArrayList<ArrayList<Neuron>>();
    private boolean czy_bias;
    private double blad_sieci;
    private double momentum = 0.0;
    private double learningRate = 0.6;

    ArrayList<Integer> wejscie = new ArrayList<Integer>();
    ArrayList<Integer> oczekiwane_wyjscie = new ArrayList<Integer>();

    public Siec (int warstwy, ArrayList<Integer> liczba_neuronow_w_warstwie, boolean czy_bias){
        this.czy_bias = czy_bias;

        for(int i = 0 ; i < warstwy ; i++){
            ArrayList <Neuron> warstwa = new ArrayList<Neuron>();
            for(int j = 0; j < liczba_neuronow_w_warstwie.get(i); j++){
                if(i < 1) //pierwsza warstwa
                    warstwa.add(new Neuron(0, czy_bias));
                else
                    warstwa.add(new Neuron(liczba_neuronow_w_warstwie.get(i-1), czy_bias));
            }
            siec_neuronowa.add(warstwa);
        }
    }

    // Losowanie wag dla wszystkich neuronów oprócz warstwy wejściowej
    public void losuj_wagi() {
        for (int i = 1; i <siec_neuronowa.size(); i++){
            for (int j = 0; j <siec_neuronowa.get(i).size(); j++){
                siec_neuronowa.get(i).get(j).losuj_wagi();
            }
        }
    }

    public void akson (ArrayList<Integer> wejscie){
        this.wejscie = wejscie;

        for(int i=0; i < siec_neuronowa.size(); i++){
            if( i == 0 ){ //dla pierwszej warstwy pobieramy wartosci z pliku
                for (int j = 0; j < siec_neuronowa.get(0).size(); j++){
                    siec_neuronowa.get(0).get(j).setWyjscie_neuronu(wejscie.get(j));
                }
            }
            else {
                for (int j = 0; j < siec_neuronowa.get(i).size(); j++){
                    siec_neuronowa.get(i).get(j).akson(siec_neuronowa.get(i-1));
                }
            }
        }
    }

    public void oszacowanie_bledu(ArrayList<Integer> oczekiwane_wyjscie){
        this.oczekiwane_wyjscie = oczekiwane_wyjscie;
        double blad = 0.0;
        blad_sieci = 0.0;
        double pochodna;
        double roznica;

        // Obliczanie bledu dla ostatniej warstwy
        for (int i = 0; i < siec_neuronowa.get(siec_neuronowa.size()-1).size(); i++){
            pochodna = siec_neuronowa.get(siec_neuronowa.size()-1).get(i).getWyjscie_neuronu() // f'(x) = 1 * f(x) * (1 - f(x))
                    * (1 - siec_neuronowa.get(siec_neuronowa.size()-1).get(i).getWyjscie_neuronu());
            roznica = oczekiwane_wyjscie.get(i) - siec_neuronowa.get(siec_neuronowa.size()-1).get(i).getWyjscie_neuronu();
            //System.out.println(pochodna + " - " + roznica);
            siec_neuronowa.get(siec_neuronowa.size()-1).get(i).setBlad(pochodna * roznica);
            blad_sieci += Math.pow(roznica, 2);
        }
        blad_sieci = blad_sieci / 2;

        // Obliczanie blad dla pozostalych warstw
        // Iteracja od konca
        for (int j = siec_neuronowa.size() - 1; j > 0; j--){
            // Iteracja po liczbie wag, pobiera ile jest wag i tyle razy petla sie wykona
            for (int k = 0; k < siec_neuronowa.get(j).get(0).getIlosc_wag(); k++){
                // Iteracja po ilosci neuronow
                for (int l = 0; l < siec_neuronowa.get(j).size(); l++) {
                    blad += siec_neuronowa.get(j).get(l).getBlad()
                            * siec_neuronowa.get(j).get(l).getWagi_neuronu()[k];
                }
                pochodna = siec_neuronowa.get(j - 1).get(k).getWyjscie_neuronu()
                        * (1 - siec_neuronowa.get(j - 1).get(k).getWyjscie_neuronu());

                blad *= pochodna;
                siec_neuronowa.get(j - 1).get(k).setBlad(blad);
                blad = 0.0;
            }
        }
    }

    // Propagacja wsteczna
    public void propgagacja_wsteczna() {
        double delta;

        for ( int i = 1; i < siec_neuronowa.size(); i++){

            for ( int j = 0; j < siec_neuronowa.get(i).size(); j++){

                for ( int k = 0; k < siec_neuronowa.get(i).get(j).getIlosc_wag(); k++){

                    delta = learningRate * siec_neuronowa.get(i).get(j).getBlad()
                            * siec_neuronowa.get(i - 1).get(k).getWyjscie_neuronu();

                    // Ustawienie wag dla naurona
                    siec_neuronowa.get(i).get(j).setWage_neuronu(siec_neuronowa.get(i).get(j).getWagi_neuronu()[k]
                            + delta, k);

                    System.out.println(siec_neuronowa.get(i).get(j).getWagi_neuronu()[k]);

                    if (czy_bias && siec_neuronowa.get(i).get(j).getIlosc_wag()-k == 1){
                        delta = learningRate * siec_neuronowa.get(i).get(j).getBlad();
                        siec_neuronowa.get(i).get(j).setWage_neuronu(siec_neuronowa.get(i).get(j).getWagi_neuronu()[k+1]
                                + delta, k+1);
                    }
                }
            }
        }
    }

    public static Siec stworzSiec(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj ilość warstw sieci:");
        //int liczbaWarstw = scanner.nextInt();
        int liczbaWarstw = 3;
        ArrayList<Integer> iloscNeuronow = new ArrayList<Integer>();
        System.out.println("Podaj ilosc neuronów wejściowych:");
        //iloscNeuronow.add(scanner.nextInt());
        iloscNeuronow.add(4);
        for (int i=0; i<liczbaWarstw - 2; i++){
            System.out.println("Podaj ilość neuronów w ukrytej warstwie:");
//            iloscNeuronow.add(scanner.nextInt());
             iloscNeuronow.add(2);
        }
        System.out.println("Podaj ilość neuronów w warstwie wyjściowej:");
//        iloscNeuronow.add(scanner.nextInt());
        iloscNeuronow.add(4);
        System.out.println("Czy siec ma mieć bias? 1 - TAK, 0 - NIE");
        //int czy_bias = scanner.nextInt();
        int czy_bias = 1;
        boolean bias = czy_bias == 1;
        Siec siec = new Siec(liczbaWarstw, iloscNeuronow, bias);
        return siec;
    }

    public static Siec uczSiec (Siec siec, ArrayList<ArrayList<Integer>> dane, int epoki){
        OperacjePliki pliki = new OperacjePliki();
        double bladTreningowy = 0;
        siec.losuj_wagi();
        ArrayList<OperacjePliki.daneWyjsciowe> bledy = new ArrayList<OperacjePliki.daneWyjsciowe>();
        int x = 0;
        while (x < epoki){
            ArrayList<ArrayList<Integer>> wejscie = dane;
            Collections.shuffle(wejscie);
            ArrayList<ArrayList<Integer>> wyjscie = dane;

            for (int i = 0; i < 4; i++){
                siec.akson(wejscie.get(i));
                siec.oszacowanie_bledu(wyjscie.get(i));
                siec.propgagacja_wsteczna();
                bladTreningowy += siec.getBlad_sieci();
            }

            bladTreningowy /= wejscie.size();

            if(x % 50 ==0) {
                OperacjePliki.daneWyjsciowe tmp = new OperacjePliki.daneWyjsciowe();
                tmp.x = x;
                tmp.error = bladTreningowy;
                bledy.add(tmp);
            }
            bladTreningowy = 0;
            x++;
        }
        pliki.zapiszTabliceDoCSV(bledy, "dane.csv");
        return siec;
    }

    public void testujSiec(Siec siec, ArrayList<Integer> wejscie, String text){
        siec.akson(wejscie);
        siec.oszacowanie_bledu(wejscie);
        OperacjePliki plik = new OperacjePliki();
        plik.zapiszDane(siec, text);
    }

    public ArrayList<ArrayList<Neuron>> getSiec_neuronowa() {
        return siec_neuronowa;
    }

    public void setSiec_neuronowa(ArrayList<ArrayList<Neuron>> siec_neuronowa) {
        this.siec_neuronowa = siec_neuronowa;
    }

    public boolean isCzy_bias() {
        return czy_bias;
    }

    public void setCzy_bias(boolean czy_bias) {
        this.czy_bias = czy_bias;
    }

    public double getBlad_sieci() {
        return blad_sieci;
    }

    public void setBlad_sieci(double blad_sieci) {
        this.blad_sieci = blad_sieci;
    }

    public double getMomentum() {
        return momentum;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public ArrayList<Integer> getWejscie() {
        return wejscie;
    }

    public void setWejscie(ArrayList<Integer> wejscie) {
        this.wejscie = wejscie;
    }

    public ArrayList<Integer> getOczekiwane_wyjscie() {
        return oczekiwane_wyjscie;
    }

    public void setOczekiwane_wyjscie(ArrayList<Integer> oczekiwane_wyjscie) {
        this.oczekiwane_wyjscie = oczekiwane_wyjscie;
    }
}
