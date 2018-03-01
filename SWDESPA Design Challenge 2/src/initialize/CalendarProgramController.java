package initialize;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CalendarProgramController implements Initializable{

	 @FXML private Tab dayPane, agendaPane; 
	 @FXML private Pane agendaContentPane, mainPane;
	 @FXML private GridPane calendarGrid;
	 @FXML private Label monthLabel, yearLabel;
	 @FXML private Button createButton, nextButton, prevButton;
	 @FXML private CheckBox eventCheck, taskCheck;
	
	 private Date date = new Date();
	 
    public void initialize(URL url, ResourceBundle rb) {
  
	     refreshCalendar(date.getMonthBound(), date.getYearBound());
    }    
	
    public void refreshCalendar(int month, int year){
    	
		String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		int nod, som, i, j;
			
		monthLabel.setText(months[month]);
		yearLabel.setText(""+year);

		calendarGrid.getChildren().clear();
		
		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get(GregorianCalendar.DAY_OF_WEEK);
	
		for (i = 1; i <= nod; i++) {
		    Label label = new Label(" "+ i);
			int row  =  (i+som-2)%7;
			int column = new Integer((i+som-2)/7);
			calendarGrid.add(label, row, column);
		}
    }
    
    @FXML
    private void createItem(ActionEvent event) throws IOException {
    	Parent newload_parent = FXMLLoader.load(getClass().getResource("CreateCalendarItem.fxml"));
        Scene newload_scene = new Scene(newload_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(newload_scene);
        app_stage.show();
    }
    
    @FXML
    private void prevMonth(ActionEvent event) throws IOException {
		if (date.getMonthToday() == 0){
			date.setMonthToday(11);
			date.setYearToday(date.getYearToday() - 1);
		}
		else
			date.setMonthToday(date.getMonthToday() - 1);
		
		refreshCalendar(date.getMonthToday(), date.getYearToday());
    }
    
    @FXML
    private void nextMonth(ActionEvent event) throws IOException {
    	if (date.getMonthToday() == 11){
			date.setMonthToday(0);
			date.setYearToday(date.getYearToday() + 1);
		}
		else
			date.setMonthToday(date.getMonthToday() + 1);
			
		refreshCalendar(date.getMonthToday(), date.getYearToday());
    }
    
    
}
