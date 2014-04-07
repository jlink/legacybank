package lbank;

/*
 * Copyright by Legacy Bank Inc
 * Canary Islands 
 */

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public 
class 
BankApplication extends JFrame 
{

	protected JTextField kundeField;
	protected JButton neuerKundeButton;
	protected JTextArea kundeDetailArea;
	protected JTable accountTable;

	/**
	 * Constructor for bank application
	 */
	public BankApplication() throws HeadlessException {
		super("Legacy Bank");
		createLayout(new LayoutCreator());
		List<Account> accountObjects = new ArrayList<Account>();
		Object[] accountIds = bank.getAllAccounts();
		for (int i = 0; i < accountIds.length; i++) {
			accountObjects. add(bank.getAccount((String) accountIds[i]));
		}
		StringBuffer detail = new StringBuffer();
		detail.append("Bank gerade gestartet...");
		((AccountEntriesTableModel) accountTable.getModel()).setAccountEntries(accountObjects);
		kundeDetailArea.setText(detail.toString());
	}

	// private method
	// creates swing layout
	private void createLayout(LayoutCreator creator) {
		creator.run(this);
	}

	public void neuesKontoErzeugen() {
		Account account = bank.newAccount(kundeField.getText());
		String[] accountNumbers = bank.getAllAccounts();
		List<Account> accounts = new ArrayList<Account>();
		for (String string : accountNumbers) {
			accounts.add(bank.getAccount(string));
		}
		StringBuffer detail = new StringBuffer();
		detail.append("Neues Konto erzeugt...");
		detail.append("\n");
		detail.append("Kontonummer: " + account.getID());
		detail.append("\n");
		detail.append("Kunde: " + account.getCustomer());
		((AccountEntriesTableModel) accountTable.getModel()).setAccountEntries(accounts);
		kundeDetailArea.setText(detail.toString());
	}
	
	//Bank object is a singleton
	private static Bank bank = Bank.getInstance();

	public static void main(String[] args) {
		BankApplication app = new BankApplication();
		app.setVisible(true);
	}
}
