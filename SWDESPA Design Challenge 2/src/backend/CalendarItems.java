package backend;

import java.util.ArrayList;

public class CalendarItems {

	private static ArrayList<CalendarItem> items = new ArrayList<CalendarItem>();

	public static void addEvent(Event e){
		items.add(e);
	}
	
	public static void addTask(Task t){
		items.add(t);
	}

	public static ArrayList<CalendarItem> getItems(){
		return items;
	}

	public static int getItemsSize() {
		return items.size();
	}

	public static int getIndex(CalendarItem i) {
		return items.indexOf(i);
	}

	public void printEvents() {
		for (int i = 0; i<items.size(); i++){
			System.out.println(i + " " + items.get(i).getTitle() + " " + items.get(i).getColor());
			System.out.println("ON: "+ items.get(i).getMonth()+ "/"+  items.get(i).getDay()+ "/" +items.get(i).getYear());
			System.out.println("AT: " + items.get(i).getStartHour() +":"+ items.get(i).getStartMinute()+ " TO " + items.get(i).getEndHour() + ":"+ items.get(i).getEndMinute() );
		}
	}
}

