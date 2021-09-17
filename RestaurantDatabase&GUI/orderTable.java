import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;
import java.util.List;

public class orderTable extends AbstractTableModel {
	private static final int ID_COL = 0;
	private static final int NAME_COL = 1;
	private static final int ADDON_COL = 2;
	private static final int PRICE_COL = 3;
	
	
	private String[] columnNames = {"ID", "Name", "Add-ons", "Price"};
	
	private List<Product> products = new ArrayList<Product>();
	
	public orderTable(List<Product> inputProducts) {
		products = inputProducts;
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public int getRowCount() {
		return products.size();
	}
	
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		Product temp = products.get(row);
		switch (col) {
		case ID_COL:
			return temp.getId();
		case NAME_COL:
			return temp.getName();
		case ADDON_COL:
			String addons = temp.getAddOns();
			if(addons != null) {
				return temp.getAddOns();
			}
			else {
				return "none";
			}
		case PRICE_COL:
			return temp.getPrice();
		default:
			return temp.getName();
		}
		
	}
	
	public void removeRow(int row) {
		fireTableRowsDeleted(row, row);
	}
	
	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
	
	
}