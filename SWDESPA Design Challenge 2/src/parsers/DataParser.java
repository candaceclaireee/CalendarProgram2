package parsers;

import java.util.ArrayList;

public abstract class DataParser {

	protected ArrayList<String[]> eventlines = new ArrayList<String[]>();
	protected ArrayList<String[]> tasklines = new ArrayList<String[]>();
	
	public void parseData() {
		readData(); 
		processData();
	}
	
	public abstract void readData();
	public abstract void processData();

}
