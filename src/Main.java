import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        OperacjePliki plik = new OperacjePliki();
        Siec siec = null;
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- MENU ---\n1.Stwórz nową sieć\n2.Wczytaj sieć");
        switch (scanner.nextInt()) {
            case 1:{
                siec = Siec.stworzSiec();
                System.out.println("Podaj współczynnik nauki:");
                //siec.setLearningRate(Double.parseDouble(scanner.next()));
                siec.setLearningRate(0.6);
                System.out.println("Podaj współczynnik momentum:");
//                siec.setMomentum(Double.parseDouble(scanner.next()));
                siec.setMomentum(0);
                break;
            }
            case 2:
                siec = plik.importSieci("siec.bin");
                break;
        }
        while (true){
            System.out.println("-- MENU --\n1.Tryb nauki\n2.Tryb testowania\n3.Zapisz sieć\n4.Zamknij program");
            switch (scanner.nextInt()){
                case 1:
                    System.out.println("Podaj ilość epok:");
//                    int epoki = scanner.nextInt();
                    int epoki = 2000;
                    siec = Siec.uczSiec(siec, plik.czytajDane("in.txt"), epoki);
                case 2:
                    ArrayList<ArrayList<Integer>> wejscie = plik.czytajDane("in.txt");
                    for(int i=0;i<wejscie.size();i++)
                    {
                        siec.testujSiec(siec, wejscie.get(i), "Plik"+i+".txt");
                    }
                    break;
                case 3:
                    plik.exportSieci(siec);
                    break;
                case 4:
                    System.exit(0);
            }
        }

    }


}
