import java.io.*;
import java.net.NetPermission;
import java.util.ArrayList;

public class OperacjePliki {
    public static class daneWyjsciowe{
        int x;
        double error;
    }

    public static ArrayList<ArrayList<Integer>> czytajDane (String text){
        BufferedReader reader;
        ArrayList<ArrayList<Integer>> plik = new ArrayList<ArrayList<Integer>>();
        try {
            reader = new BufferedReader(new FileReader(text));
            String line = reader.readLine();
            while (line != null){
                String[] tmp = line.split(" ");
                ArrayList<Integer> tmpArray = new ArrayList<Integer>();
                for(String i:tmp){
                    tmpArray.add(Integer.parseInt(i));
                }
                plik.add(tmpArray);
                line = reader.readLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return plik;
    }

    public static void zapiszTabliceDoCSV(ArrayList<daneWyjsciowe> errors, String text){
        try {
            FileWriter writer = new FileWriter(text);
            for ( daneWyjsciowe i:errors){
                writer.write(i.x+";"+i.error+"\r\n");
            }
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void zapiszDane (Siec siec, String text){
        try {
            FileWriter myWriter = new FileWriter(text);
            myWriter.write("Wzorzec wejściowy\r\n");
            for(Integer it: siec.getWejscie())
            {
                myWriter.write(it.toString()+" "); //wzorzec wejściowy
            }
            myWriter.write("\r\n");
            myWriter.write("Błąd sieci: "+siec.getBlad_sieci()+"\r\n"); // błąd sieci
            myWriter.write("Pożądany wzorzec odpowiedzi\r\n");
            for(Integer it: siec.getOczekiwane_wyjscie())
            {
                myWriter.write(it.toString()+" "); //wzorzec wejściowy
            }
            myWriter.write("\r\n");
            myWriter.write("Błędy na wyjściach sieci\r\n");
            for(Neuron it: siec.getSiec_neuronowa().get(siec.getSiec_neuronowa().size()-1))
            {
                myWriter.write(Double.toString(it.getBlad())+" ");
            }
            myWriter.write("\r\n");
            myWriter.write("Wyjścia sieci\r\n");
            for(Neuron it: siec.getSiec_neuronowa().get(siec.getSiec_neuronowa().size()-1))
            {
                myWriter.write(Double.toString(it.getWyjscie_neuronu())+" ");
            }
            myWriter.write("\r\n");
            myWriter.write("Wagi neuronów wyjściowych\r\n");
            for(Neuron it: siec.getSiec_neuronowa().get(siec.getSiec_neuronowa().size()-1))
            {
                for(int i=0;i<it.getWagi_neuronu().length;i++)
                {
                    myWriter.write(Double.toString(it.getWagi_neuronu()[i])+" ");
                }
                myWriter.write("\r\n");
            }
            myWriter.write("\r\n");
            myWriter.write("Warstwy ukryte\r\n");
            myWriter.write("Wagi\r\n");
            for(int i= siec.getSiec_neuronowa().size()-2;i>0;i--)
            {
                for(Neuron it: siec.getSiec_neuronowa().get(i))
                {
                    for(int j=0;j<it.getWagi_neuronu().length;j++)
                    {
                        myWriter.write(Double.toString(it.getWagi_neuronu()[j])+" ");
                    }
                    myWriter.write("\r\n");
                }
            }
            myWriter.write("Wyjścia\r\n");
            for(int i= siec.getSiec_neuronowa().size()-2;i>0;i--)
            {
                for(Neuron it: siec.getSiec_neuronowa().get(i))
                {
                    myWriter.write(it.getWyjscie_neuronu()+" ");
                }
                myWriter.write("\r\n");
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportSieci (Siec siec) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("siec.bin"));
        objectOutputStream.writeObject(siec);
        objectOutputStream.close();
    }

    public Siec importSieci (String text) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(text));
        Siec siec = (Siec) objectInputStream.readObject();
        objectInputStream.close();
        return siec;
    }
}
