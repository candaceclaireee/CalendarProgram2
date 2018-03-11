package model;

import java.io.*;

public class CSVDataParser extends DataParser {
	
	public void readData(){
		try {
			File f = new File("src\\sample_files\\Events.csv");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
		
			String curLine = br.readLine();
			
			while(curLine != null) {
				
				String line[] = curLine.split(",");
				eventlines.add(line);
			
				curLine = br.readLine();
			}

			f = new File("src\\sample_files\\Tasks.csv");
			fr = new FileReader(f);
			br = new BufferedReader(fr);

			curLine = br.readLine();

			while(curLine != null) {

				String line[] = curLine.split(",");
				tasklines.add(line);

				curLine = br.readLine();
			}
			
			f = new File("src\\sample_files\\DoneTasks.csv");
			fr = new FileReader(f);
			br = new BufferedReader(fr);

			curLine = br.readLine();

			while(curLine != null) {

				String line[] = curLine.split(",");
				donetasklines.add(line);

				curLine = br.readLine();
			}

			br.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processData() {
		CalendarItems items = new CalendarItems();
		
		for(int i = 0; i < eventlines.size(); i++) {
			for(int j = 0; j < eventlines.get(i).length; j=j+4) {
								
				Event e = new Event();

				String line = eventlines.get(i)[j];
				String date[] = line.split("/");
				e.setDate(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));

				line = eventlines.get(i)[j+1];
				String starttime[] = line.split(":");
				e.setStartTime(Integer.parseInt(starttime[0]), Integer.parseInt(starttime[1]));
				
				line = eventlines.get(i)[j+2];
				String endtime[] = line.split(":");
				e.setEndTime(Integer.parseInt(endtime[0]), Integer.parseInt(endtime[1]));
				
				e.setTitle(eventlines.get(i)[j+3]);
				
				items.addEvent(e);
			}
		}
		
		for(int i = 0; i < tasklines.size(); i++) {
			for(int j = 0; j < tasklines.get(i).length; j=j+3) {
								
				Task t = new Task();

				String line = tasklines.get(i)[j];
				String date[] = line.split("/");
				t.setDate(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));

				line = tasklines.get(i)[j+1];
				String starttime[] = line.split(":");
				t.setStartTime(Integer.parseInt(starttime[0]), Integer.parseInt(starttime[1]));
								
				t.setTitle(tasklines.get(i)[j+2]);
				t.setIsDone(false);
				items.addTask(t);
			}
		}

		for(int i = 0; i < donetasklines.size(); i++) {
			for(int j = 0; j < donetasklines.get(i).length; j=j+3) {
								
				Task t = new Task();

				String line = donetasklines.get(i)[j];
				String date[] = line.split("/");
				t.setDate(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));

				line = donetasklines.get(i)[j+1];
				String starttime[] = line.split(":");
				t.setStartTime(Integer.parseInt(starttime[0]), Integer.parseInt(starttime[1]));
								
				t.setTitle(donetasklines.get(i)[j+2]);
				t.setIsDone(true);
				items.addTask(t);
			}
		}
		
		//items.printEvents(); //test
	}	

	public void writeData() {
		String[] lineToWrite;
		String filepathE = "src\\sample_files\\Events.csv";
		String filepathT = "src\\sample_files\\Tasks.csv"; 
		String filepathDT = "src\\sample_files\\DoneTasks.csv";
		
		try {
			for (int i=0; i<CalendarItems.getItemsSize(); i++) {
				CalendarItem item = CalendarItems.getItems().get(i);
				if (item instanceof Event) {
					lineToWrite = new String[4];
					lineToWrite[0] = item.getMonth() + "/" + item.getDay() + "/" + item.getYear();
					lineToWrite[1] = (item.getStartHour() < 10 ? "0" : "") +item.getStartHour()+ ":"+(item.getStartMinute() < 10 ? "0" : "") +item.getStartMinute();
					lineToWrite[2] = (item.getEndHour() < 10 ? "0" : "") +item.getEndHour()+ ":"+(item.getEndMinute() < 10 ? "0" : "") +item.getEndMinute();
					lineToWrite[3] = item.getTitle();
					eventlinesNew.add(lineToWrite);
				}
				else {
					lineToWrite = new String[3];
					if (item.isDone() == false) {
						lineToWrite[0] = item.getMonth() + "/" + item.getDay() + "/" + item.getYear();
						lineToWrite[1] = (item.getStartHour() < 10 ? "0" : "") +item.getStartHour()+ ":"+(item.getStartMinute() < 10 ? "0" : "") +item.getStartMinute();
						lineToWrite[2] = item.getTitle();
						tasklinesNew.add(lineToWrite);
					}
					else if (item.isDone() == true) {
						lineToWrite[0] = item.getMonth() + "/" + item.getDay() + "/" + item.getYear();
						lineToWrite[1] = (item.getStartHour() < 10 ? "0" : "") +item.getStartHour()+ ":"+(item.getStartMinute() < 10 ? "0" : "") +item.getStartMinute();
						lineToWrite[2] = item.getTitle();
						donetasklinesNew.add(lineToWrite);
					}
				}
			}
			
			StringBuilder temp = new StringBuilder();
			StringBuilder temp2 = new StringBuilder();
			StringBuilder temp3 = new StringBuilder();
			for(int i = 0; i < eventlinesNew.size(); i++) {
				for(int j = 0; j < eventlinesNew.get(i).length; j++) {
					temp.append(eventlinesNew.get(i)[j]);
					if (j!= eventlinesNew.get(i).length-1)
						temp.append(",");
					eventlinesNew.get(i)[j] = temp.toString();
					temp.setLength(0);
				}
			}
			for(int i = 0; i < tasklinesNew.size(); i++) {
				for(int j = 0; j < tasklinesNew.get(i).length; j++) {
					temp2.append(tasklinesNew.get(i)[j]);
					if (j!= tasklinesNew.get(i).length-1)
						temp2.append(",");
					tasklinesNew.get(i)[j] = temp2.toString();
					temp2.setLength(0);
				}
			}
			for(int i = 0; i < donetasklinesNew.size(); i++) {
				for(int j = 0; j < donetasklinesNew.get(i).length; j++) {
					temp3.append(donetasklinesNew.get(i)[j]);
					if (j!= donetasklinesNew.get(i).length-1)
						temp3.append(",");
					donetasklinesNew.get(i)[j] = temp3.toString();
					temp3.setLength(0);
				}
			}
			
			FileWriter fw = new FileWriter(filepathE);
			BufferedWriter bw = new BufferedWriter(fw);
			String line = null;
			for(int i = 0; i < eventlinesNew.size(); i++) {
				for(int j = 0; j < eventlinesNew.get(i).length; j++) {
					line = eventlinesNew.get(i)[j];
					bw.write(line);
				}
				bw.write(System.getProperty("line.separator"));
			}
			bw.close();
			
			FileWriter fw2 = new FileWriter(filepathT);
			BufferedWriter bw2 = new BufferedWriter(fw2);
			String line2 = null;
			for(int i = 0; i < tasklinesNew.size(); i++) {
				for(int j = 0; j < tasklinesNew.get(i).length; j++) {
					line2 = tasklinesNew.get(i)[j];
					bw2.write(line2);
				}
				bw2.write(System.getProperty("line.separator"));
			}
			bw2.close();
			
			FileWriter fw3 = new FileWriter(filepathDT);
			BufferedWriter bw3 = new BufferedWriter(fw3);
			String line3 = null;
			for(int i = 0; i < donetasklinesNew.size(); i++) {
				for(int j = 0; j < donetasklinesNew.get(i).length; j++) {
					line3 = donetasklinesNew.get(i)[j];
					bw3.write(line3);
				}
				bw3.write(System.getProperty("line.separator"));
			}
			bw3.close();
			
		} catch(IOException e) {
			e.printStackTrace();

		}
	}
	
}
