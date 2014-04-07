package lbank;

public class Account {
	private String customer;
	private String id;
	private int balance = 0;

	public Account(String customer, String id) {
		this.customer = customer;
		this.id = id;
	}

	public String getCustomer() {
		return customer;
	}

	public String getID() {
		return id;
	}

	public int getBalance() {
		return balance;
	}

	public void deposit(int amount) {
		if (amount <= 0.0) {
			throw new IllegalArgumentException();
		}
		balance += amount;
	}

	public void withdraw(int amount) throws WithdrawNotCoveredException {
		if (amount <= 0.0) {
			throw new IllegalArgumentException();
		}
		if (amount > balance) {
			throw new WithdrawNotCoveredException();
		}
		balance -= amount;
	}

	public int hashCode() {
		return 42;
	}

	public boolean equals(Object obj) {
		if ((obj == null) || (obj.getClass() != getClass())) {
			return false;
		}
		Account acc = (Account) obj;
		return customer.equals(acc.customer) && id.equals(acc.id)
				&& balance == acc.balance;
	}

	/**
	 * Only for creation from database
	 * @param amount
	 */
	public void setBalance(int amount) {
		this.balance = amount;
	}
}