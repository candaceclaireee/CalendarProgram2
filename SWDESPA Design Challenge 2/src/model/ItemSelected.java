package model;

public class ItemSelected {
	public static String itemSelected; 
	public static int row, col; 
	
	public static void setSelected(String s) {
		itemSelected = s; 
	}
	
	public static String getSelected() {
		return itemSelected;
	}
	
	public static void setRowCol(int r, int c) {
		row = r; 
		col = c;
	}
	
	public static void setRow(int r) {
		row = r;
	}
	
	public static void setCol(int c) {
		col = c; 
	}

	public static int getRow() {
		return row;
	}
	
	public static int getCol() {
		return col;
	}
}
