import java.io.Serializable;
import java.util.ArrayList;

import static java.lang.Math.exp;

public class Neuron implements Serializable {
    private double wyjscie_neuronu;
    private double suma_wazona_wejsc;
    private double blad;
    private int ilosc_wag;

    private boolean czy_bias;
    private double[] wagi_neuronu;
    private double[] poprzednie_wagi_neuronu;
    private double[] poprzednie_wagi_neuronu2;

    public Neuron(int ilosc_wag, boolean czy_bias){
        this.czy_bias = czy_bias;
        this.ilosc_wag = ilosc_wag;

        if (czy_bias){
           wagi_neuronu = new double[ilosc_wag + 1];
           poprzednie_wagi_neuronu = new double[ilosc_wag + 1];
           poprzednie_wagi_neuronu2 = new double[ilosc_wag + 1];
        } else {
           wagi_neuronu = new double[ilosc_wag];
           poprzednie_wagi_neuronu = new double[ilosc_wag];
           poprzednie_wagi_neuronu2 = new double[ilosc_wag];
        }
    }

    // Funkcja ta losuje wagi z przedziału 0;1
    // Round zaorągla liczbę, random losuje
    public void losuj_wagi () {
        for (int i = 0; i < wagi_neuronu.length; i++){
            wagi_neuronu[i] = (Math.round((Math.random() - 0.5) * 100.00) / 100.00);
            System.out.println(wagi_neuronu[i]);
        }
    }

    // Funkcja aktywacji - sigmoid
    public double sigmoid(double x){
        return 1 / (1 + exp(-x)); // sigm = 1 / 1 + e^-x
    }

    // Funkcja służąca do oblczania sumy wagowej
    public void oblicz_sume(ArrayList<Neuron> poprzednia_warstwa){
        double suma = 0.0;
        for (int i = 0; i < poprzednia_warstwa.size(); i++){
            suma += poprzednia_warstwa.get(i).wyjscie_neuronu * wagi_neuronu[i];
        }
        if (czy_bias) {
            suma += wagi_neuronu[wagi_neuronu.length - 1]; // redukujemy -1 wejść
        }
        suma_wazona_wejsc = suma;
    }

    // Akson - po obliczeniu sumy oraz funkcji aktywacji, przesyłamy dalej wartości
    public void akson (ArrayList<Neuron> poprzednia_warstwa){
        oblicz_sume(poprzednia_warstwa);
        wyjscie_neuronu = sigmoid(suma_wazona_wejsc);
    }

    // Zmiana wagi
    public void setWage_neuronu (double nowa_waga, int index){
        wagi_neuronu[index] = nowa_waga;
    }

    // Gettery settery
    public double getWyjscie_neuronu() {
        return wyjscie_neuronu;
    }

    public void setWyjscie_neuronu(double wyjscie_neuronu) {
        this.wyjscie_neuronu = wyjscie_neuronu;
    }

    public double getSuma_wazona_wejsc() {
        return suma_wazona_wejsc;
    }

    public void setSuma_wazona_wejsc(double suma_wazona_wejsc) {
        this.suma_wazona_wejsc = suma_wazona_wejsc;
    }

    public double getBlad() {
        return blad;
    }

    public void setBlad(double blad) {
        this.blad = blad;
    }

    public int getIlosc_wag() {
        return ilosc_wag;
    }

    public void setIlosc_wag(int ilosc_wag) {
        this.ilosc_wag = ilosc_wag;
    }

    public boolean isCzy_bias() {
        return czy_bias;
    }

    public void setCzy_bias(boolean czy_bias) {
        this.czy_bias = czy_bias;
    }

    public double[] getWagi_neuronu() {
        return wagi_neuronu;
    }

    public void setWagi_neuronu(double[] wagi_neuronu) {
        this.wagi_neuronu = wagi_neuronu;
    }

    public double[] getPoprzednie_wagi_neuronu() {
        return poprzednie_wagi_neuronu;
    }

    public void setPoprzednie_wagi_neuronu(double[] poprzednie_wagi_neuronu) {
        this.poprzednie_wagi_neuronu = poprzednie_wagi_neuronu;
    }

    public double[] getPoprzednie_wagi_neuronu2() {
        return poprzednie_wagi_neuronu2;
    }

    public void setPoprzednie_wagi_neuronu2(double[] poprzednie_wagi_neuronu2) {
        this.poprzednie_wagi_neuronu2 = poprzednie_wagi_neuronu2;
    }

}
