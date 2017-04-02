import javafx.application.Application;
import javafx.stage.Stage;
import java.io.*;


public class DataManagerMain extends Application{

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Model model = new Model();
        View view = new View(model);
    }

}
