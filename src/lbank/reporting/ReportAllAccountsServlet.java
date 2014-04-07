package lbank.reporting;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import lbank.*;
import lbank.persistence.*;
import lbank.untouchable.SecurityAccessManager;

/*
 * Copyright by Legacy Bank Inc
 * Canary Islands 
 */

public class ReportAllAccountsServlet extends HttpServlet {

	protected 	Persistence 			persistence = null;
	private 	ArrayList<Account> 		accounts;
	private 	String[] 				ids = new String[0];

	private 	ArrayList<Account> 		filteredAccounts;

	// For checking if a user has access to accounts
	protected 	SecurityAccessManager 	sam = null;
	protected HttpServletRequest request;
	private double balanceAverage;
	private double balanceMean;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.request = request;
		String userId = request.getParameter("user");
		ids = (String[]) request.getAttribute("accountIds");
		int numberOfAccounts = ids.length;
		PrintWriter writer = response.getWriter();
		response.setContentType("text/xml");
		sam = (SecurityAccessManager) getServletContext().getAttribute("sam");
		persistence = (Persistence) getServletContext().getAttribute("database");

		// Write account report
		writer.println("<accounts-report>");

		// filter ids
		List ids2 = new ArrayList();
		for (int i = 0; i < ids.length; i++) {
			if (sam.canAccess(userId, ids[i]))
				ids2.add(ids[i]);
		}
		ids = ((List<String>) ids2).toArray(new String[0]);

/*
		if (ids2 instanceof ArrayList)
			filteredAccounts = (ArrayList<Account>) ids2;
*/
		writer.println("<accounts>");
		accounts = new ArrayList<Account>();
		for (int i = 0; i < ids.length; i++) {
			try {
				accounts.add(persistence.getAccount(ids[i]));
			} catch (PersistenceException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				// log.error(e);
				writer.println("<error>No account with id '" + ids[i] + "'</error>");
				// todo: duplication. move to finally block?
				writer.println("</accounts>");
				writer.println("</accounts-report>");

				// close writer to make sure everything is really written to
				// stream.
				// this is necessary, because it sometime did not work when I
				// left it out
				writer.close();
				return;
			}
		}
		
		if(false) { //Todo: Check with Frank if accounts should be output in a special order
			Collections.sort(accounts, new Comparator<Account>() {
				@Override
				public int compare(Account o1, Account o2) {
					// TODO Auto-generated method stub
					return 0;
				}
			});
		}

		// go through all accounts and add up their balance
		// print out <konto>-Entry for all accounts
		BigDecimal sum = new BigDecimal(0);
		for (Account account : accounts) {
			writer.println("<account id='" + account.getID() + "'>");
			writer.println("<owner>" + account.getCustomer() + "</owner>");
			// add account's balance to sum
			sum = sum.add(new BigDecimal(account.getBalance()));
			writer.println("<balance>" + account.getBalance() + "</balance>");
			writer.println("</account>");
		}

		// Report summary: Print sum and average of all balances of all accounts
		writer.println("<aggregate>");
		writer.println("<balance-sum>" + sum.toPlainString() + "</balance-sum>");
		writer.println("<balance-mean>" + sum.divide(new BigDecimal(numberOfAccounts)) + "</balance-sum>");
		writer.println("</aggregate>");

		writer.println("</accounts>");
		writer.println("</accounts-report>");
		// close writer to make sure everything is really written to stream.
		// this is necessary, because it sometime did not work when I left it
		// out
		writer.close();
	}
	
	/*
	 * Currently not used I think but left in because I am not really sure
	 */
	void filterAccounts(List in, List out) 
	{
		String user = request.getUserPrincipal().getName(); //DOES NOT WORK. WE HAVE DIFFERENT LOGIN MECHANISM!!!!!!!!!!!!
		//First opy in to out
		out = in;
		for (Object object : in) {
			Account a = (Account) object;
			if (!sam.canAccess(user, a.getID())) {
				out.remove(object);
			}
		}
	}
}
