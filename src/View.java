import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.function.UnaryOperator;


public class View extends Stage {

    private Model model;

    //create Valuelabels (accessed in the listener of the textfield)
    Label nameV = new Label();
    Label populationV = new Label();
    Label areaV = new Label();
    Label densityV = new Label();

    public View(Model model){
        this.model = model;

        this.setScene(new Scene(createVbox(),225,250));
        this.setTitle("Countries");
        this.show();
    }

    private VBox createVbox(){
        return new VBox(
                createTextArea(),
                createTextField(),
                createGridPane()
        );
    }

    private TextArea createTextArea(){
        TextArea textArea = new TextArea();
        for(Country country : model.getCountryList()){
            textArea.appendText(country.getIndex() + ": " + country.getName()+"\n");
        }
        textArea.setEditable(false);
        return textArea;
    }


    private TextField createTextField(){
        TextField textField = new TextField();
        textField.setPromptText("Enter #number");
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    nameV.setText(model.getCountryList().get(Integer.parseInt(newValue)).getName());
                    populationV.setText(String.valueOf(model.getCountryList().get(Integer.parseInt(newValue)).getPopulation()));
                    areaV.setText(String.valueOf(model.getCountryList().get(Integer.parseInt(newValue)).getArea()));
                    densityV.setText(String.valueOf(model.getCountryList().get(Integer.parseInt(newValue)).getDensity()));
                } catch (IndexOutOfBoundsException IOOB){
                    //Can be ignored since if the user enters a index that does not exist the view wont change and previous
                    //country will be shown
                }
            }
        });

        //create TextFormatter to only let numbers be entered in the textfield
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textField.setTextFormatter(textFormatter);

        return textField;
    }

    private GridPane createGridPane(){
        GridPane gridPane = new GridPane();

        //create topic labels
        Label nameT = new Label("Country (* =a ll):\t");
        Label populationT = new Label("Population:\t");
        Label areaT = new Label("Area:\t");
        Label densityT = new Label("Population Density:\t");


        gridPane.addRow(0, nameT,nameV);
        gridPane.addRow(1, populationT,populationV);
        gridPane.addRow(2, areaT, areaV);
        gridPane.addRow(3,densityT,densityV);

        gridPane.setPadding(new Insets(5.0,5.0,5.0,5.0));

        return gridPane;
    }


}
