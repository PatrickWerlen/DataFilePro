import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Model {
    private File popDat;
    private File areaDat;

    private List<Country> countryList = new ArrayList<Country>();

    public Model(){
        this.popDat = manageFiles("worldpopulation");
        this.areaDat = manageFiles("worldarea");
        fillCountryList();
    }

    public List<Country> getCountryList(){
        return countryList;
    }

    //give user a filechooser
    private final File manageFiles(String fileName){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please choose the " + fileName +" file");
        fileChooser.setInitialDirectory(new File("."));
        return convertFile(fileChooser.showOpenDialog(new Stage()));
    }

    //convert Files from .dat to .txt -> Replace separator with '\t'
    private final File convertFile(File file) {
        try (
                DataInputStream dis = new DataInputStream(new FileInputStream(file));
                PrintWriter pw = new PrintWriter(file.getName() + ".txt")
        ) {
            while (dis.available() > 0) {
                char character = dis.readChar();
                if (character == '/') {
                    pw.println(String.valueOf('\t') + dis.readInt());
                } else {
                    pw.print(character);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(file.getName()+".txt");
    }

    //reads .txt Files and creates object Country and adds them to the countryList
    private void fillCountryList(){
        String popInput, areaInput, name;
        int area;
        int index = 1;
        long population;

        try (
            Scanner popScan = new Scanner(popDat);
            Scanner areaScan = new Scanner(areaDat);
        ){
            popScan.useDelimiter(" ");
            areaScan.useDelimiter(" ");
            while(popScan.hasNext()){
                popInput = popScan.nextLine();
                areaInput = areaScan.nextLine();

                String partsPop[] = popInput.split("\t");
                name = partsPop[0];
                population = Long.parseLong(partsPop[1]);

                String partsArea[] = areaInput.split("\t");
                area = Integer.parseInt(partsArea[1]);

                countryList.add(new Country(index++, name, population, area));
            }
            addTotalCountry();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    //add "*=all" with index 0
    private void addTotalCountry(){
        long totalPop = 0;
        int totalArea = 0;
        int index = 0;
        String name = "*";

        for(Country county : countryList){
            totalPop += county.getPopulation();
            totalArea += county.getArea();
        }
        countryList.add(0, new Country(index,name,totalPop,totalArea));
    }


}
