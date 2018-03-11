package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import backend.*;
import parsers.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CreateCalendarItemController implements Initializable{

	 @FXML private Tab newItemTab; 
	 @FXML private Pane newItemPane, mainPane;
	 @FXML private TextField nameLabel, dateLabel, starttimeLabel, endtimeLabel;
	 @FXML private Label dateTodayLabel, toLabel, errorLabel;
	 @FXML private CheckBox eventCheck, taskCheck;
	 @FXML private Button discardButton, saveButton;
	 
	 private CalendarDate date = new CalendarDate(); //
	 String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
			 
	 public void initialize(URL location, ResourceBundle resources) {
		 dateTodayLabel.setText("Today is "+months[date.getMonthToday()]+" "+date.getDayToday()+", "+date.getYearToday());
	     dateLabel.setText(date.getMonthToday()+1+"/"+date.getDayToday()+"/"+date.getYearToday());
	 }
	 
	 @FXML
	 private void newCalendarItem(ActionEvent event) throws IOException {
		 
		  
		  if (!eventCheck.isSelected() && !taskCheck.isSelected()) 
			  errorLabel.setText("Check at least one of the boxes above");

		  else if (eventCheck.isSelected()) {
			   if(nameLabel.getText().isEmpty() || dateLabel.getText().isEmpty() || starttimeLabel.getText().isEmpty() || endtimeLabel.getText().isEmpty())
				   errorLabel.setText("Make sure all fields have values");
			   else {

					  String title = nameLabel.getText();
					  
					  String dateInString = dateLabel.getText();
					  String dateVars[] = dateInString.split("/");
					  int month = Integer.parseInt(dateVars[0]);
					  int day = Integer.parseInt(dateVars[1]);
					  int year = Integer.parseInt(dateVars[2]);
					  
					  String starttime = starttimeLabel.getText();
					  String sTimeVars[] = starttime.split(":");
					  int starthour = Integer.parseInt(sTimeVars[0]);
					  int startminute = Integer.parseInt(sTimeVars[1]);
					  
					  String endttime = endtimeLabel.getText();
					  String eTimeVars[] = endttime.split(":");
					  int endhour = Integer.parseInt(eTimeVars[0]);
					  int endminute = Integer.parseInt(eTimeVars[1]);
					  
					  Event item = new Event();
					  item.setDate(month, day, year);
					  item.setTitle(title);
					  item.setStartTime(starthour, startminute);
					  item.setEndTime(endhour, endminute);
					  CalendarItems.addEvent(item);

					  CSVDataParser csv = new CSVDataParser();
					  csv.writeData();
					  
					  Parent newload_parent = FXMLLoader.load(getClass().getResource("CalendarProgram.fxml"));
					  Scene newload_scene = new Scene(newload_parent);
					  Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					  app_stage.setScene(newload_scene);
					  app_stage.show();
			   }
		  }	
		  else if (taskCheck.isSelected()) {
			   if(nameLabel.getText().isEmpty() || dateLabel.getText().isEmpty() || starttimeLabel.getText().isEmpty() )
				   errorLabel.setText("Make sure all fields have values");
			   else {


					  String title = nameLabel.getText();
					  
					  String dateInString = dateLabel.getText();
					  String dateVars[] = dateInString.split("/");
					  int month = Integer.parseInt(dateVars[0]);
					  int day = Integer.parseInt(dateVars[1]);
					  int year = Integer.parseInt(dateVars[2]);
					  
					  String starttime = starttimeLabel.getText();
					  String sTimeVars[] = starttime.split(":");
					  int starthour = Integer.parseInt(sTimeVars[0]);
					  int startminute = Integer.parseInt(sTimeVars[1]);
					  
					  Task item = new Task();
					  item.setDate(month, day, year);
					  item.setTitle(title);
					  item.setStartTime(starthour, startminute);
					  CalendarItems.addTask(item);

					  CSVDataParser csv = new CSVDataParser();
					  csv.writeData();

					  Parent newload_parent = FXMLLoader.load(getClass().getResource("CalendarProgram.fxml"));
					  Scene newload_scene = new Scene(newload_parent);
					  Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					  app_stage.setScene(newload_scene);
					  app_stage.show();
			   }
		  }  
	 }
	 
	 @FXML
	 private void eventChosen(ActionEvent event) throws IOException {
		  taskCheck.setSelected(false);
		  toLabel.setText("To");
		  endtimeLabel.setVisible(true);
	}
	 
	 @FXML
	 private void taskChosen(ActionEvent event) throws IOException {
		  eventCheck.setSelected(false);
		  toLabel.setText("(Tasks last 30 minutes from the start time)");
		  endtimeLabel.setVisible(false);
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
