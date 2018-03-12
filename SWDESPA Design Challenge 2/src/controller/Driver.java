package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*; 

public class Driver extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/CalendarProgram.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Productivity Tool");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {

        DataParser parser = new CSVDataParser();
        parser.parseData();
            	
        launch(args);
    }
    
}
