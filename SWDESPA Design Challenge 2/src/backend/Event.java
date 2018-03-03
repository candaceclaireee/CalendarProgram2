package backend;

public class Event extends CalendarItem{
		
	public Event(){
		
		color = "Blue"; 
		title = ""; 
	}

	public void setStartTime(int h, int m) {
		starthour = h;
		startminute = m;
	}
	
	public void setEndTime(int h, int m) {
		endhour = h;
		endminute = m;
	}
	
	public void setEndHour(int h) {
		endhour = h;
	}

	public void setEndMinute(int m) {
		endminute = m;
	}
	
}
