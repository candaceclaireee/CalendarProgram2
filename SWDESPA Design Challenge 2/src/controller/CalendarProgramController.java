package controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.*;
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
	private Button createButton, nextButton, prevButton, doneButton, removeButton;
	@FXML
	private CheckBox eventCheck, taskCheck;

	@FXML
	private TableView<DayTableItem> dayTable;
	@FXML
	private TableColumn<DayTableItem, String> dayTime;
	@FXML
	private TableColumn<DayTableItem, String> dayEvent;

	@FXML
	private TableView<WeekTableItem> weekTable;
	@FXML
	private TableColumn<WeekTableItem, String> weekTime;
	@FXML
	private TableColumn<WeekTableItem, String> weekSunEvent;
	@FXML
	private TableColumn<WeekTableItem, String> weekMonEvent;
	@FXML
	private TableColumn<WeekTableItem, String> weekTueEvent;
	@FXML
	private TableColumn<WeekTableItem, String> weekWedEvent;
	@FXML
	private TableColumn<WeekTableItem, String> weekThuEvent;
	@FXML
	private TableColumn<WeekTableItem, String> weekFriEvent;
	@FXML
	private TableColumn<WeekTableItem, String> weekSatEvent;

	private CalendarDate date = new CalendarDate();
	String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	private Model model;
	
	public void initialize(URL url, ResourceBundle rb) {
		
		dateTodayLabel.setText("Today is " + months[date.getMonthToday()] + " " + date.getDayToday() + ", " + date.getYearToday());
		eventCheck.setSelected(true);
		taskCheck.setSelected(true);	
		doneButton.setVisible(false);
		removeButton.setVisible(false);
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
				refreshWeekPane(row, column);
				model.setRowCol(row, column);
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

		for (int x = 0; x < model.getItemsSize(); x++) {
			CalendarItem item = model.getItems().get(x);

			if (item.getYear() == Integer.parseInt(yearLabel.getText())) {

				int month = 0;
				for (int m = 0; m < months.length; m++) {
					if (monthLabel.getText().compareTo(months[m]) == 0)
						month = m + 1;
				}

				if (item.getMonth() == month) {
					if (item.getDay() == day) {
						if (eventCheck.isSelected() == false && taskCheck.isSelected() == true) {
							if (item instanceof Task) {
								itemsForTheDay.add((item.getStartHour() < 10 ? "0" : "") + item.getStartHour() + ":" + (item.getStartMinute() < 10 ? "0" : "")
										+ item.getStartMinute() + "    " + item.getTitle());
							}
						} else if (taskCheck.isSelected() == false && eventCheck.isSelected() == true) {
							if (item instanceof Event) {
								itemsForTheDay.add((item.getStartHour() < 10 ? "0" : "") + item.getStartHour() + ":" + (item.getStartMinute() < 10 ? "0" : "")
										+ item.getStartMinute() + " - " + (item.getEndHour() < 10 ? "0" : "") + item.getEndHour() + ":" + (item.getEndMinute() < 10 ? "0" : "")
										+ item.getEndMinute() + "    " + item.getTitle());
							}
						} else if (taskCheck.isSelected() == true && eventCheck.isSelected() == true) {

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
		}
		
		agendaListView.setItems(itemsForTheDay.sorted());
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
							for (int i =0; i<model.getItemsSize(); i++) {
								CalendarItem item =model.getItems().get(i);
								if (s.split("    ")[1].compareTo(item.getTitle()) == 0 && item instanceof Task) {
									if (item.isDone() == true) {
										this.getStylesheets().add(getClass().getResource("../view/strikethrough.css").toExternalForm());
									}
								}
							}
						}
					}
					
				};

            	EventHandler<ActionEvent> markAsDone = new EventHandler<ActionEvent>() {
            	    @Override
            	    public void handle(ActionEvent event) {

            	    	for (int i =0; i<model.getItemsSize(); i++) {
							CalendarItem item =model.getItems().get(i);
							if (model.getSelected().split("    ")[1].compareTo(item.getTitle()) == 0 && item instanceof Task) {
								if (item.isDone() == false) {
									cell.getStylesheets().add(getClass().getResource("../view/strikethrough.css").toExternalForm());
									item.setIsDone(true);

	            	    			refreshDayPane(model.getRow(), model.getCol());
	            	    			refreshWeekPane(model.getRow(), model.getCol());
									CSVDataParser csv = new CSVDataParser();
									csv.writeData();
								}
							}
						}
            	    }
            	};
            	

            	EventHandler<ActionEvent> removeItem = new EventHandler<ActionEvent>() {
            	    @Override
            	    public void handle(ActionEvent event) {
            	    	for (int i=0; i<model.getItemsSize(); i++) {
            	    		CalendarItem item = model.getItems().get(i);
            	    		if (model.getSelected().split("    ")[1].compareTo(item.getTitle()) == 0) {
            	    			model.removeItem(item);
            	    			itemsForTheDay.remove(itemsForTheDay.remove(model.getSelected()));

            	    		  refreshDayPane(model.getRow(), model.getCol());
            	    		  refreshWeekPane(model.getRow(), model.getCol());
            	    		  CSVDataParser csv = new CSVDataParser();
          					  csv.writeData();
            	    		}
            	    	}
            	    }
            	};
		        cell.setOnMousePressed( new EventHandler<MouseEvent>() {
		            @Override
		            public void handle( MouseEvent event )
		            {
		                if ( cell.getItem() != null ) {
		                	model.setSelected(cell.getItem().toString());
		                	doneButton.setVisible(true);
		                	doneButton.setOnAction(markAsDone);
		                	removeButton.setVisible(true);
		                	removeButton.setOnAction(removeItem);
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
		
		ObservableList<DayTableItem> data = initializeDayView(day);
		dayTime.setCellValueFactory(new PropertyValueFactory<DayTableItem, String>("time"));
		dayEvent.setCellValueFactory(new PropertyValueFactory<DayTableItem, String>("event"));
		dayEvent.setCellFactory(column -> {
			return new TableCell<DayTableItem, String>() {
				@Override
				protected void updateItem(String event, boolean empty) {
					super.updateItem(event, empty);

					if (event == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(event);
						DayTableItem currentItem = getTableView().getItems().get(getIndex());
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
			refreshAgendaPane(rowIndex.intValue(), colIndex.intValue());
			refreshDayPane(rowIndex.intValue(), colIndex.intValue());
			refreshWeekPane(rowIndex.intValue(), colIndex.intValue());
			model.setRowCol(rowIndex.intValue(), colIndex.intValue());
		} catch (NullPointerException ex) {
			System.out.println("Null location!");
		}
	}
	
	@FXML
	private void createItem(ActionEvent event) throws IOException {
		Parent newload_parent = FXMLLoader.load(getClass().getResource("../view/CreateCalendarItem.fxml"));
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

	@FXML
	public void checkBoxAction() {
		refreshAgendaPane(model.getRow(), model.getCol());
		refreshDayPane(model.getRow(), model.getCol());
		refreshWeekPane(model.getRow(), model.getCol());
	}


	public ObservableList<DayTableItem> initializeDayView(int dayToDisplay) {
		ArrayList<CalendarItem> items = model.getItems();
		ArrayList<CalendarItem> itemsToDisplay = new ArrayList<CalendarItem>();

		ArrayList<DayTableItem> toTableItems = new ArrayList<>();

		int month = 0;

		for (int m = 0; m < months.length; m++) {
			if (monthLabel.getText().compareTo(months[m]) == 0)
				month = m + 1;
		}

		for (int hour = 0; hour <= 23; hour++)
			for (int min = 0; min <= 30; min+=30) {

				if (min < 30) {
					toTableItems.add(new DayTableItem(hour + ":" + String.format("%02d", min), ""));
					toTableItems.get(toTableItems.size()-1).setValueStartHour(hour);
					toTableItems.get(toTableItems.size()-1).setValueStartMin(min);
					toTableItems.get(toTableItems.size()-1).setValueEndHour(hour);
					toTableItems.get(toTableItems.size()-1).setValueEndMin(min+29);
				} else {
					toTableItems.add(new DayTableItem("", ""));
					toTableItems.get(toTableItems.size() - 1).setValueStartHour(hour);
					toTableItems.get(toTableItems.size() - 1).setValueStartMin(min);
					toTableItems.get(toTableItems.size() - 1).setValueEndHour(hour);
					toTableItems.get(toTableItems.size() - 1).setValueEndMin(59);
				}
			}

		for (CalendarItem item : items) {
			if (item.getMonth() == month && item.getDay() == dayToDisplay && item.getYear() == Integer.parseInt(yearLabel.getText())) {
				if (eventCheck.isSelected() && item instanceof Event)
					itemsToDisplay.add(item);
				else if (taskCheck.isSelected() && item instanceof Task)
					itemsToDisplay.add(item);
			}
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

			for (DayTableItem displayTime: toTableItems) {
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



	public void refreshWeekPane(int row, int col) {
		Date dateForWeek = new Date();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d");
		SimpleDateFormat monthDeterminer = new SimpleDateFormat("M");

		Node result = null;
		ObservableList<Node> childrens = calendarGrid.getChildren();

		for (Node node : childrens) {
			if (calendarGrid.getRowIndex(node) == row && calendarGrid.getColumnIndex(node) == col) {
				result = node;
				break;
			}
		}

		int month = 0;

		for (int m = 0; m < months.length; m++) {
			if (monthLabel.getText().compareTo(months[m]) == 0)
				month = m + 1;
		}

		String nodestring = result.toString();
		String num[] = nodestring.split("'");
		int day = Integer.parseInt(num[1]);

		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DATE, day);
		cal.set(Calendar.YEAR, Integer.parseInt(yearLabel.getText()));

		int startWeekValue = -(cal.get(Calendar.DAY_OF_WEEK) - 1);

		cal.add(Calendar.DATE, startWeekValue);

		int monthCompare = Integer.parseInt(monthDeterminer.format(cal.getTime()));

		do {
			if (monthCompare != month) {
				cal.add(Calendar.DATE, 1);
				monthCompare = Integer.parseInt(monthDeterminer.format(cal.getTime()));
			}
		} while (monthCompare != month);

		ObservableList<WeekTableItem> data = initializeWeekView(cal);
		weekTime.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("time"));
		weekSunEvent.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("sunEvent"));
		weekMonEvent.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("monEvent"));
		weekTueEvent.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("tueEvent"));
		weekWedEvent.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("wedEvent"));
		weekThuEvent.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("thuEvent"));
		weekFriEvent.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("friEvent"));
		weekSatEvent.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("satEvent"));

		weekSunEvent.setCellFactory(column -> {
			return new TableCell<WeekTableItem, String>() {
				@Override
				protected void updateItem(String sunEvent, boolean empty) {
					super.updateItem(sunEvent, empty);

					if (sunEvent == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(sunEvent);
						WeekTableItem currentItem = getTableView().getItems().get(getIndex());
						if (currentItem.getSunColor() != null) {
							setTextFill(Color.WHITE);
							if (currentItem.getSunColor() == Color.BLUE)
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

		weekMonEvent.setCellFactory(column -> {
			return new TableCell<WeekTableItem, String>() {
				@Override
				protected void updateItem(String monEvent, boolean empty) {
					super.updateItem(monEvent, empty);

					if (monEvent == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(monEvent);
						WeekTableItem currentItem = getTableView().getItems().get(getIndex());
						if (currentItem.getMonColor() != null) {
							setTextFill(Color.WHITE);
							if (currentItem.getMonColor() == Color.BLUE)
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

		weekTueEvent.setCellFactory(column -> {
			return new TableCell<WeekTableItem, String>() {
				@Override
				protected void updateItem(String tueEvent, boolean empty) {
					super.updateItem(tueEvent, empty);

					if (tueEvent == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(tueEvent);
						WeekTableItem currentItem = getTableView().getItems().get(getIndex());
						if (currentItem.getTueColor() != null) {
							setTextFill(Color.WHITE);
							if (currentItem.getTueColor() == Color.BLUE)
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

		weekWedEvent.setCellFactory(column -> {
			return new TableCell<WeekTableItem, String>() {
				@Override
				protected void updateItem(String wedEvent, boolean empty) {
					super.updateItem(wedEvent, empty);

					if (wedEvent == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(wedEvent);
						WeekTableItem currentItem = getTableView().getItems().get(getIndex());
						if (currentItem.getWedColor() != null) {
							setTextFill(Color.WHITE);
							if (currentItem.getWedColor() == Color.BLUE)
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

		weekThuEvent.setCellFactory(column -> {
			return new TableCell<WeekTableItem, String>() {
				@Override
				protected void updateItem(String event, boolean empty) {
					super.updateItem(event, empty);

					if (event == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(event);
						WeekTableItem currentItem = getTableView().getItems().get(getIndex());
						if (currentItem.getThuColor() != null) {
							setTextFill(Color.WHITE);
							if (currentItem.getThuColor() == Color.BLUE)
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

		weekFriEvent.setCellFactory(column -> {
			return new TableCell<WeekTableItem, String>() {
				@Override
				protected void updateItem(String event, boolean empty) {
					super.updateItem(event, empty);

					if (event == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(event);
						WeekTableItem currentItem = getTableView().getItems().get(getIndex());
						if (currentItem.getFriColor() != null) {
							setTextFill(Color.WHITE);
							if (currentItem.getFriColor() == Color.BLUE)
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

		weekSatEvent.setCellFactory(column -> {
			return new TableCell<WeekTableItem, String>() {
				@Override
				protected void updateItem(String satEvent, boolean empty) {
					super.updateItem(satEvent, empty);

					if (satEvent == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(satEvent);
						WeekTableItem currentItem = getTableView().getItems().get(getIndex());
						if (currentItem.getSatColor() != null) {
							setTextFill(Color.WHITE);
							if (currentItem.getSatColor() == Color.BLUE)
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
		weekTable.setItems(data);
	}


	public ObservableList<WeekTableItem> initializeWeekView(Calendar forWeekCalendar) {
		ArrayList<CalendarItem> items = model.getItems();
		ArrayList<CalendarItem> itemsToDisplay = new ArrayList<CalendarItem>();

		ArrayList<WeekTableItem> toTableItems = new ArrayList<>();

		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d");
		SimpleDateFormat monthDeterminer = new SimpleDateFormat("M");

		int month = Integer.parseInt(monthDeterminer.format(forWeekCalendar.getTime()));

		int startDay = forWeekCalendar.get(Calendar.DATE);
		String[] date = sdf.format(forWeekCalendar.getTime()).split("\\s+");
		int endDay = forWeekCalendar.get(Calendar.DATE);

		while (!date[0].equalsIgnoreCase("Sat")) {
			forWeekCalendar.add(Calendar.DATE, 1);
			date = sdf.format(forWeekCalendar.getTime()).split("\\s+");
			endDay = Integer.parseInt(date[2]);
		}

		toTableItems.add(new WeekTableItem(""));

		for (int hour = 0; hour <= 23; hour++)
			for (int min = 0; min <= 30; min+=30) {

				if (min < 30) {
					toTableItems.add(new WeekTableItem(hour + ":" + String.format("%02d", min)));
					toTableItems.get(toTableItems.size()-1).setValueStartHour(hour);
					toTableItems.get(toTableItems.size()-1).setValueStartMin(min);
					toTableItems.get(toTableItems.size()-1).setValueEndHour(hour);
					toTableItems.get(toTableItems.size()-1).setValueEndMin(min+29);
				} else {
					toTableItems.add(new WeekTableItem(""));
					toTableItems.get(toTableItems.size() - 1).setValueStartHour(hour);
					toTableItems.get(toTableItems.size() - 1).setValueStartMin(min);
					toTableItems.get(toTableItems.size() - 1).setValueEndHour(hour);
					toTableItems.get(toTableItems.size() - 1).setValueEndMin(59);
				}
			}

		for (CalendarItem item : items) {
			if (item.getMonth() == month && (item.getDay() >= startDay && item.getDay() <= endDay)
					&& item.getYear() == Integer.parseInt(yearLabel.getText())) {
				if (eventCheck.isSelected() && item instanceof Event)
					itemsToDisplay.add(item);
				else if (taskCheck.isSelected() && item instanceof Task)
					itemsToDisplay.add(item);
			}
		}

		itemsToDisplay.sort(Comparator.comparingInt(CalendarItem::getStartHour)
				.thenComparingInt(CalendarItem::getStartMinute));

		int monDate = 0, tueDate = 0, wedDate = 0, thuDate = 0, friDate = 0, satDate = 0, sunDate = 0;

		forWeekCalendar.set(Integer.parseInt(yearLabel.getText()), month-1, startDay);
		String compareDay = sdf.format(forWeekCalendar.getTime()).substring(0,3);

		do {
			if (month == Integer.parseInt(monthDeterminer.format(forWeekCalendar.getTime()))) {
				switch (compareDay) {
					case "Sun":
						sunDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
						break;
					case "Mon":
						monDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
						break;
					case "Tue":
						tueDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
						break;
					case "Wed":
						wedDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
						break;
					case "Thu":
						thuDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
						break;
					case "Fri":
						friDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
						break;
					case "Sat":
						satDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
						break;
				}

				forWeekCalendar.add(Calendar.DATE, 1);
				compareDay = sdf.format(forWeekCalendar.getTime()).substring(0, 3);
			} else
				break;
		} while (!compareDay.equalsIgnoreCase("Sat"));

		if (month == Integer.parseInt(monthDeterminer.format(forWeekCalendar.getTime())))
			satDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));

		if (sunDate > 0)
			toTableItems.get(0).setEvent(Integer.toString(sunDate), "Sun");
		if (monDate > 0)
			toTableItems.get(0).setEvent(Integer.toString(monDate), "Mon");
		if (tueDate > 0)
			toTableItems.get(0).setEvent(Integer.toString(tueDate), "Tue");
		if (wedDate > 0)
			toTableItems.get(0).setEvent(Integer.toString(wedDate), "Wed");
		if (thuDate > 0)
			toTableItems.get(0).setEvent(Integer.toString(thuDate), "Thu");
		if (friDate > 0)
			toTableItems.get(0).setEvent(Integer.toString(friDate), "Fri");
		if (satDate > 0)
			toTableItems.get(0).setEvent(Integer.toString(satDate), "Sat");

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

			String dayOfItem = null;

			if (sunDate == item.getDay()) {
				dayOfItem = "Sun";
			} else if (monDate == item.getDay()) {
				dayOfItem = "Mon";
			} else if (tueDate == item.getDay()) {
				dayOfItem = "Tue";
			} else if (wedDate == item.getDay()) {
				dayOfItem = "Wed";
			} else if (thuDate == item.getDay()) {
				dayOfItem = "Thu";
			} else if (friDate == item.getDay()) {
				dayOfItem = "Fri";
			} else if (satDate == item.getDay()) {
				dayOfItem = "Sat";
			}

			for (WeekTableItem displayTime: toTableItems) {
				if (displayTime.getTime().equalsIgnoreCase(""))
					continue;
				if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin &&
						displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {

						displayTime.setEvent(item.getTitle(), dayOfItem);

						if (item instanceof Event)
							displayTime.setColor(Color.BLUE, dayOfItem);
						else
							displayTime.setColor(Color.GREEN, dayOfItem);

						break;
				} else if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin) {
						displayTime.setEvent(item.getTitle(), dayOfItem);

						if (item instanceof Event)
							displayTime.setColor(Color.BLUE, dayOfItem);
						else
							displayTime.setColor(Color.GREEN, dayOfItem);
				} else {
					if (displayTime.getValueStartHour() >= startHour && displayTime.getValueEndHour() <= endHour) {
						if (displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {
								displayTime.setEvent(" ", dayOfItem);

								if (item instanceof Event)
									displayTime.setColor(Color.BLUE, dayOfItem);
								else
									displayTime.setColor(Color.GREEN, dayOfItem);
								break;
						} else {
								displayTime.setEvent(" ", dayOfItem);

								if (item instanceof Event)
									displayTime.setColor(Color.BLUE, dayOfItem);
								else
									displayTime.setColor(Color.GREEN, dayOfItem);
						}
					}
				}
			}
		}

		return FXCollections.observableArrayList(toTableItems);
	}

}
