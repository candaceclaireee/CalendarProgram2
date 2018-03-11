package model;

import java.util.ArrayList;

public abstract class DataParser {

	protected ArrayList<String[]> eventlines = new ArrayList<String[]>();
	protected ArrayList<String[]> tasklines = new ArrayList<String[]>();
	protected ArrayList<String[]> donetasklines = new ArrayList<String[]>();
	
	protected ArrayList<String[]> eventlinesNew = new ArrayList<String[]>();
	protected ArrayList<String[]> tasklinesNew = new ArrayList<String[]>();
	protected ArrayList<String[]> donetasklinesNew = new ArrayList<String[]>();
	
	
	public void parseData() {
		readData(); 
		processData();
	}
	
	public abstract void readData();
	public abstract void processData();
	public abstract void writeData();
}
