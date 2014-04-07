package lbank;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class LayoutCreator {

	void run(final BankApplication bankApplication) {
		bankApplication.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		BorderLayout borderLayout = new BorderLayout(10, 10);
		bankApplication.getContentPane().setLayout(borderLayout);
		bankApplication.getContentPane().add(new JLabel("Kunde:"), BorderLayout.WEST);
		bankApplication.kundeField = new JTextField();
		bankApplication.kundeField.setText("<Neuer Kundenname>");
		bankApplication.getContentPane().add(bankApplication.kundeField, BorderLayout.CENTER);
		bankApplication.neuerKundeButton = new JButton("Neues Konto");
		bankApplication.kundeDetailArea = new JTextArea();
		bankApplication.kundeDetailArea.setText("Kein neues Konto");
		bankApplication.getContentPane().add(bankApplication.neuerKundeButton, BorderLayout.EAST);
		// getContentPane().add(kundeDetailArea, BorderLayout.SOUTH);
		bankApplication.getContentPane().add(createAccountEntriesTable(bankApplication), BorderLayout.SOUTH);
		bankApplication.neuerKundeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bankApplication.neuesKontoErzeugen();
			}
		});
		bankApplication.pack();
	}

	private JScrollPane createAccountEntriesTable(BankApplication bankApplication) {
		bankApplication.accountTable = new JTable(15, 3);
		bankApplication.accountTable.setName("accountTable");
		bankApplication.accountTable.setModel(new AccountEntriesTableModel());
		JScrollPane scrollPane = new JScrollPane(bankApplication.accountTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(250, 300));
		return scrollPane;
	}


}
