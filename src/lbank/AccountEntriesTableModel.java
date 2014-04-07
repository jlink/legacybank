package lbank;

import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class AccountEntriesTableModel extends AbstractTableModel {

	private List<Account> accountEntries = Collections.emptyList();
	
	public void setAccountEntries(List<Account> accountEntries) {
		this.accountEntries = accountEntries;
		fireTableDataChanged();
	}

	public int getColumnCount() {
		return 3;
	}

	public int getRowCount() {
		return accountEntries.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Account entry = accountEntries.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return entry.getID();
		case 1:
			return entry.getCustomer();
		case 2:
			return "" + entry.getBalance();
		default:
			return "unknown";
		}
	}

	public String getColumnName(int column) {
		switch (column) {
		case 0: {
			return "Kontonummer";
		}
		case 1: {
			return "Inhaber";
		}
		case 2: {
			return "Saldo";
		}
		default: {
			return "unknown";
		}
		}
	}
}
