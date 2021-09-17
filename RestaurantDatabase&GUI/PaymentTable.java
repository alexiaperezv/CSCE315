import java.util.List;
import javax.swing.table.AbstractTableModel;

public class PaymentTable extends AbstractTableModel {
	private static final int ID_COL = 0;
	private static final int CID_COL = 1;
	private static final int NAME_COL = 5;
	
	
	private String[] columnNames = {"ID", "Customer ID",  "Name"};
	
	private List<PaymentInfo> cards;
	
	public PaymentTable(List<PaymentInfo> inputCards) {
		cards = inputCards;
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public int getRowCount() {
		return cards.size();
	}
	
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		PaymentInfo temp = cards.get(row);
		switch (col) {
		case ID_COL:
			return temp.getId();
		case CID_COL:
			return temp.getCustomer_id();
		case NAME_COL:
			return temp.getName();
		default:
			return temp.getName();
		}
		
	}
	
	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
}
