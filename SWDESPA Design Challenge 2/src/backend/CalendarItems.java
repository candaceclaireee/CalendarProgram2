package backend;

import java.util.ArrayList;

public class CalendarItems {

	private static ArrayList<CalendarItem> items = new ArrayList<CalendarItem>();

	public void addEvent(CalendarItem i){
		items.add(i);
	}

	public ArrayList<CalendarItem> getItems(){
		return items;
	}

	public int getItemsSize() {
		return items.size();
	}

	public int getIndex(CalendarItem i) {
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

