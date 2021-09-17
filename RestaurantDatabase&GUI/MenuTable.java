import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MenuTable extends AbstractTableModel {
	private static final int ID_COL = 0;
	private static final int NAME_COL = 1;
	private static final int PRICE_COL = 2;
	
	
	private String[] columnNames = {"ID", "Name", "Price"};
	
	private List<Product> products;
	
	public MenuTable(List<Product> inputProducts) {
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
		case PRICE_COL:
			return temp.getPrice();
		default:
			return temp.getName();
		}
		
	}
	
	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
	
}
