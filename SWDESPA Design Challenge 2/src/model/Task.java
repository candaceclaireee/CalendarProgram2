package model;

public class Task extends CalendarItem{
	
	public Task() {
		
		color = "Green"; 
		title = ""; 
		doneornot = false;
	}

	public void setStartTime(int h, int m) {
		starthour = h;
		startminute = m;
		
		if (startminute == 0) {				//example 4:00
			endhour = starthour;			//end: 4:30
			endminute = startminute + 30;
		}
		else if (startminute == 30) {		//example 5:30
			endhour = starthour + 1;		//end: 6:00
			endminute = 0;
		}
	}
}

