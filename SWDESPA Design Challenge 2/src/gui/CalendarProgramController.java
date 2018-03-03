package gui;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import backend.CalendarItems;
import backend.Date;
import backend.Event;
import parsers.CSVDataParser;
import parsers.DataParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CalendarProgramController implements Initializable{

	 @FXML private Tab dayPane, agendaPane; 
	 @FXML private Pane mainPane;
	 @FXML private ListView<String> agendaListView;
	 @FXML private GridPane calendarGrid;
	 @FXML private Label monthLabel, yearLabel;
	 @FXML private Button createButton, nextButton, prevButton;
	 @FXML private CheckBox eventCheck, taskCheck;
	
	 private Date date = new Date();
	 
    public void initialize(URL url, ResourceBundle rb) {
  
    	ArrayList<DataParser> parsers = new ArrayList<DataParser>();
        parsers.add(new CSVDataParser());

         for (DataParser parser: parsers)
            parser.parseData();
        
	     refreshCalendar(date.getMonthBound(), date.getYearBound());

    }    
	
    public void refreshCalendar(int month, int year){
    	
		String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		int nod, som, i;
			
		monthLabel.setText(months[month]);
		yearLabel.setText(""+year);

		calendarGrid.getChildren().clear();
		
		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get(GregorianCalendar.DAY_OF_WEEK);
	
		for (i = 1; i <= nod; i++) {
		    Label label = new Label(""+ i);
			int column  =  (i+som-2)%7;
			int row = new Integer((i+som-2)/7);
			calendarGrid.add(label, column, row);
			GridPane.setHalignment(label, HPos.CENTER);
			
			if (date.getDayToday() == i)
				refreshAgendaPane(row, column);
		}
    }
    
    public void refreshAgendaPane(int row, int col) {
    	String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    	Node result = null;
        ObservableList<Node> childrens = calendarGrid.getChildren();
        
        for (Node node : childrens) {
            if(calendarGrid.getRowIndex(node) == row && calendarGrid.getColumnIndex(node) == col) {
                result = node;
                break;
            }
        }
        
    	String nodestring = result.toString();
		String num[] = nodestring.split("'");
    	int day = Integer.parseInt(num[1]);

    	ObservableList<String> itemsForTheDay = FXCollections.observableArrayList();
    	
    	CalendarItems items = new CalendarItems();
		for (int x = 0 ; x < items.getItemsSize(); x++) {
			if (items.getItems().get(x).getYear() == Integer.parseInt(yearLabel.getText())){
				
				int month = 0;
				for (int m = 0; m<months.length; m++) {
					if (monthLabel.getText().compareTo(months[m]) == 0)
						month = m+1;
				}
				
				if (items.getItems().get(x).getMonth() == month) {
					if (items.getItems().get(x).getDay() == day) {
						if (items.getItems().get(x) instanceof Event) {
							 itemsForTheDay.add((items.getItems().get(x).getStartHour() < 10 ? "0" : "") +items.getItems().get(x).getStartHour()+":"+(items.getItems().get(x).getStartMinute() < 10 ? "0" : "")
							 + items.getItems().get(x).getStartMinute() + " - " + (items.getItems().get(x).getEndHour() < 10 ? "0" : "") +items.getItems().get(x).getEndHour()+":"+(items.getItems().get(x).getEndMinute() < 10 ? "0" : "")
							 + items.getItems().get(x).getEndMinute() + "   "+ items.getItems().get(x).getTitle());
						}
						else {
							itemsForTheDay.add((items.getItems().get(x).getStartHour() < 10 ? "0" : "") +items.getItems().get(x).getStartHour()+":"+(items.getItems().get(x).getStartMinute() < 10 ? "0" : "")
							 + items.getItems().get(x).getStartMinute() + "   "+ items.getItems().get(x).getTitle());
						}
					}
				}
			}
		}
		agendaListView.setItems(itemsForTheDay);     	
    }
    
    
    @FXML
    private void mouseEntered(MouseEvent e) {
    	try {
	    	Node target = (Node) e.getTarget();
	        if (target != calendarGrid) {
	            Node parent;
	            while ((parent = target.getParent()) != calendarGrid) 
	                target = parent;
	        }
	        Integer colIndex = GridPane.getColumnIndex(target);
	        Integer rowIndex = GridPane.getRowIndex(target);
	        System.out.printf("Mouse entered cell [%d, %d]%n", rowIndex.intValue(), colIndex.intValue());
	        refreshAgendaPane(rowIndex.intValue(), colIndex.intValue()); //use the pos to show items in date
	        
    	} catch (NullPointerException ex) {
	    	System.out.println("Null location!");
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
