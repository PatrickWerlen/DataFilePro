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
        //give each file his .txt file
        this.popDat = manageFiles("worldpopulation");
        this.areaDat = manageFiles("worldarea");

        fillCountryList();
    }

    public List<Country> getCountryList(){
        return countryList;
    }

    //give user a filechooser and give the .dat files to convertFile()
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
        //creat tmp string to then give them to the Country constructor
        String popInput, areaInput, name;
        int area;
        int index = 1;
        long population;

        try (//create a scanner for each file
            Scanner popScan = new Scanner(popDat);
            Scanner areaScan = new Scanner(areaDat);
        ){
            popScan.useDelimiter(" ");
            areaScan.useDelimiter(" ");
            while(popScan.hasNext()){

                //read per line
                popInput = popScan.nextLine();
                areaInput = areaScan.nextLine();

                //split the sting made from the line in 2 strings ([0] = name / [1] = number)
                String partsPop[] = popInput.split("\t");
                name = partsPop[0];

                //try to parse the numbers if number is missing for a country --> ignore and continue
                try {
                    population = Long.parseLong(partsPop[1]);
                }catch (ArrayIndexOutOfBoundsException arrayexception){
                    continue;
                }

                String partsArea[] = areaInput.split("\t");
                try {
                    area = Integer.parseInt(partsArea[1]);
                } catch (ArrayIndexOutOfBoundsException arrayexception){
                    continue;
                }

                //add new countries to the list
                countryList.add(new Country(index++, name, population, area));
            }

            //add the total (index 0) country
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
