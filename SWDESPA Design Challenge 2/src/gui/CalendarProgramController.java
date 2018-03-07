package gui;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import backend.*;
import backend.Date;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CalendarProgramController implements Initializable {

	@FXML
	private Tab dayPane, agendaPane;
	@FXML
	private Pane mainPane;
	@FXML
	private ListView<String> agendaListView;
	@FXML
	private GridPane calendarGrid;
	@FXML
	private Label monthLabel, yearLabel, dateTodayLabel;
	@FXML
	private Button createButton, nextButton, prevButton, doneButton;
	@FXML
	private CheckBox eventCheck, taskCheck;

	@FXML
	private TableView<Showing> dayTable;
	@FXML
	private TableColumn<Showing, String> dayTime;
	@FXML
	private TableColumn<Showing, String> dayEvent;

	private Date date = new Date();
	String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

	public void initialize(URL url, ResourceBundle rb) {
		dateTodayLabel.setText("Today is " + months[date.getMonthToday()] + " " + date.getDayToday() + ", " + date.getYearToday());
		eventCheck.setSelected(true);
		taskCheck.setSelected(true);	
		doneButton.setVisible(false);
		refreshCalendar(date.getMonthBound(), date.getYearBound());
	}

	public void refreshCalendar(int month, int year) {
		int nod, som, i;

		monthLabel.setText(months[month]);
		yearLabel.setText("" + year);

		calendarGrid.getChildren().clear();

		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get(GregorianCalendar.DAY_OF_WEEK);

		for (i = 1; i <= nod; i++) {
			Label label = new Label("" + i);
			int column = (i + som - 2) % 7;
			int row = new Integer((i + som - 2) / 7);
			calendarGrid.add(label, column, row);
			GridPane.setHalignment(label, HPos.CENTER);

			if (date.getDayToday() == i) {
				refreshAgendaPane(row, column);
				refreshDayPane(row, column);
			}
		}
	}

	public void refreshAgendaPane(int row, int col) {
		Node result = null;
		ObservableList<Node> childrens = calendarGrid.getChildren();

		for (Node node : childrens) {
			if (calendarGrid.getRowIndex(node) == row && calendarGrid.getColumnIndex(node) == col) {
				result = node;
				break;
			}
		}

		String nodestring = result.toString();
		String num[] = nodestring.split("'");
		int day = Integer.parseInt(num[1]);

		ObservableList<String> itemsForTheDay = FXCollections.observableArrayList();

		CalendarItems items = new CalendarItems();
		for (int x = 0; x < items.getItemsSize(); x++) {
			CalendarItem item = CalendarItems.getItems().get(x);

			if (item.getYear() == Integer.parseInt(yearLabel.getText())) {

				int month = 0;
				for (int m = 0; m < months.length; m++) {
					if (monthLabel.getText().compareTo(months[m]) == 0)
						month = m + 1;
				}

				if (item.getMonth() == month) {
					if (item.getDay() == day) {
						if (item instanceof Event) {
							itemsForTheDay.add((item.getStartHour() < 10 ? "0" : "") + item.getStartHour() + ":" + (item.getStartMinute() < 10 ? "0" : "")
									+ item.getStartMinute() + " - " + (item.getEndHour() < 10 ? "0" : "") + item.getEndHour() + ":" + (item.getEndMinute() < 10 ? "0" : "")
									+ item.getEndMinute() + "    " + item.getTitle());
						} else {
							itemsForTheDay.add((item.getStartHour() < 10 ? "0" : "") + item.getStartHour() + ":" + (item.getStartMinute() < 10 ? "0" : "")
									+ item.getStartMinute() + "    " + item.getTitle());
						}
					}
				}
				
			}
		}
		agendaListView.setItems(itemsForTheDay);
		agendaListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> stringListView) {

		        final ListCell<String> cell = new ListCell<String>() {
					@Override
					protected void updateItem(String s, boolean b) {
						super.updateItem(s,b);
						if (s == null) {
							setText(null);
							setGraphic(null);
						} else {
							if (s.contains("-")) {
								this.setText(s);
								this.setTextFill(Color.BLUE);
								this.setStyle("-fx-font-size: 15;");
								
							} else {
								this.setText(s);
								this.setTextFill(Color.GREEN);
								this.setStyle("-fx-font-size: 15;");
							}
							for (int i =0; i<CalendarItems.getItemsSize(); i++) {
								CalendarItem item =CalendarItems.getItems().get(i);
								if (s.split("    ")[1].compareTo(item.getTitle()) == 0) {
									if (item.isDone() == true) {
										this.getStylesheets().add(getClass().getResource("strikethrough.css").toExternalForm());
									}
								}
							}
						}
					}
					
				};

            	EventHandler<ActionEvent> markAsDone = new EventHandler<ActionEvent>() {
            	    @Override
            	    public void handle(ActionEvent event) {

            	    	for (int i =0; i<CalendarItems.getItemsSize(); i++) {
							CalendarItem item =CalendarItems.getItems().get(i);
							if (ItemSelected.getSelected().split("    ")[1].compareTo(item.getTitle()) == 0) {
								if (item.isDone() == false) {
									cell.getStylesheets().add(getClass().getResource("strikethrough.css").toExternalForm());
									item.setIsDone(true);
								}
							}
						}
            	    }
            	};
            	
		        cell.setOnMousePressed( new EventHandler<MouseEvent>() {
		            @Override
		            public void handle( MouseEvent event )
		            {
		                if ( cell.getItem() != null ) {
		                    ItemSelected.setSelected(cell.getItem().toString());
		                	doneButton.setVisible(true);
		                	doneButton.setOnAction(markAsDone);
		                }
		            }
		        } );
				return cell;
			}
		});
	}


	public void refreshDayPane(int row, int col) {
		Node result = null;
		ObservableList<Node> childrens = calendarGrid.getChildren();

		for (Node node : childrens) {
			if (calendarGrid.getRowIndex(node) == row && calendarGrid.getColumnIndex(node) == col) {
				result = node;
				break;
			}
		}

		String nodestring = result.toString();
		String num[] = nodestring.split("'");
		int day = Integer.parseInt(num[1]);
		
		ObservableList<Showing> data = initializedDayView(day);
		dayTime.setCellValueFactory(new PropertyValueFactory<Showing, String>("time"));
		dayEvent.setCellValueFactory(new PropertyValueFactory<Showing, String>("event"));
		dayEvent.setCellFactory(column -> {
			return new TableCell<Showing, String>() {
				@Override
				protected void updateItem(String event, boolean empty) {
					super.updateItem(event, empty);

					if (event == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(event);
						Showing currentItem = getTableView().getItems().get(getIndex());
						if (currentItem.getColor() != null) {
							setTextFill(Color.WHITE);
							if (currentItem.getColor() == Color.BLUE)
								setStyle("-fx-background-color: blue");
							else
								setStyle("-fx-background-color: green");
						} else {
							setTextFill(Color.BLACK);
							setStyle("");
						}
					}
				}
			};
		});

		dayTable.setItems(data);
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
			refreshAgendaPane(rowIndex.intValue(), colIndex.intValue());
			refreshDayPane(rowIndex.intValue(), colIndex.intValue());

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
		if (date.getMonthToday() == 0) {
			date.setMonthToday(11);
			date.setYearToday(date.getYearToday() - 1);
		} else
			date.setMonthToday(date.getMonthToday() - 1);

		refreshCalendar(date.getMonthToday(), date.getYearToday());
	}

	@FXML
	private void nextMonth(ActionEvent event) throws IOException {
		if (date.getMonthToday() == 11) {
			date.setMonthToday(0);
			date.setYearToday(date.getYearToday() + 1);
		} else
			date.setMonthToday(date.getMonthToday() + 1);

		refreshCalendar(date.getMonthToday(), date.getYearToday());
	}
	
	public ObservableList<Showing> initializedDayView(int dayToDisplay) {
		ArrayList<CalendarItem> items = CalendarItems.getItems();
		ArrayList<CalendarItem> itemsToDisplay = new ArrayList<CalendarItem>();

		ArrayList<Showing> toTableItems = new ArrayList<>();

		int month = 0;

		for (int m = 0; m < months.length; m++) {
			if (monthLabel.getText().compareTo(months[m]) == 0)
				month = m + 1;
		}

		for (int hour = 0; hour <= 23; hour++)
			for (int min = 0; min <= 30; min+=30) {

				if (min < 30) {
					toTableItems.add(new Showing(hour + ":" + String.format("%02d", min), ""));
					toTableItems.get(toTableItems.size()-1).setValueStartHour(hour);
					toTableItems.get(toTableItems.size()-1).setValueStartMin(min);
					toTableItems.get(toTableItems.size()-1).setValueEndHour(hour);
					toTableItems.get(toTableItems.size()-1).setValueEndMin(min+29);
				} else {
					toTableItems.add(new Showing("", ""));
					toTableItems.get(toTableItems.size() - 1).setValueStartHour(hour);
					toTableItems.get(toTableItems.size() - 1).setValueStartMin(min);
					toTableItems.get(toTableItems.size() - 1).setValueEndHour(hour);
					toTableItems.get(toTableItems.size() - 1).setValueEndMin(59);
				}
			}

		for (CalendarItem item : items) {
			if (item.getMonth() == month && item.getDay() == dayToDisplay && item.getYear() == Integer.parseInt(yearLabel.getText()))
				itemsToDisplay.add(item);
		}

		itemsToDisplay.sort(Comparator.comparingInt(CalendarItem::getStartHour)
				.thenComparingInt(CalendarItem::getStartMinute));

		for (CalendarItem item: itemsToDisplay) {
			int startHour = item.getStartHour();
			int startMin = item.getStartMinute();
			int endHour;
			int endMin;

			if (startHour == item.getEndHour() && startMin == item.getEndMinute()) {
				endHour = item.getEndHour();

				if (item.getEndMinute() == 0)
					endMin = 29;
				else
					endMin = 59;
			} else if (item.getEndMinute() == 00) {
				endHour = item.getEndHour() - 1;
				endMin = 59;
			} else if (item.getEndMinute() == 30) {
				endHour = item.getEndHour();
				endMin = 29;
			} else {
				endHour = item.getEndHour();
				endMin = item.getEndMinute();
			}

			for (Showing displayTime: toTableItems) {
				if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin &&
						displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {
					displayTime.setEvent(item.getTitle());

					if (item instanceof Event)
						displayTime.setColor(Color.BLUE);
					else
						displayTime.setColor(Color.GREEN);

					break;
				} else if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin) {
					displayTime.setEvent(item.getTitle());

					if (item instanceof Event)
						displayTime.setColor(Color.BLUE);
					else
						displayTime.setColor(Color.GREEN);
				} else {
					if (displayTime.getValueStartHour() >= startHour && displayTime.getValueEndHour() <= endHour)
						if (displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {
							displayTime.setEvent(" ");

							if (item instanceof Event)
								displayTime.setColor(Color.BLUE);
							else
								displayTime.setColor(Color.GREEN);
							break;
						} else {
							displayTime.setEvent(" ");

							if (item instanceof Event)
								displayTime.setColor(Color.BLUE);
							else
								displayTime.setColor(Color.GREEN);
						}
				}
			}
		}
		return FXCollections.observableArrayList(toTableItems);
	}
}
