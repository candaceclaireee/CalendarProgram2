package initialize;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CreateCalendarItemController implements Initializable{

	 @FXML private Tab newItemTab; 
	 @FXML private Pane newItemPane, mainPane;
	 @FXML private GridPane calendarGrid;
	 @FXML private Label monthLabel, yearLabel;
	 @FXML private Button createButton, nextButton, prevButton, discardButton, saveButton;
	
	 public void initialize(URL location, ResourceBundle resources) {
			
	 }
	 
	 @FXML
	 private void goBack(ActionEvent event) throws IOException {
	    Parent newload_parent = FXMLLoader.load(getClass().getResource("CalendarProgram.fxml"));
	    Scene newload_scene = new Scene(newload_parent);
	    Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    app_stage.setScene(newload_scene);
	    app_stage.show();
	}
	
}
