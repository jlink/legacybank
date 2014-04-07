package lbank;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AccountTest {
    private Account account;

    @Before
    public void initializeAccount() throws Exception {
        account = new Account("Customer", "0001");
    }

    @Test
    public void createAccount() {
        assertEquals("Customer", account.getCustomer());
        assertEquals("0001", account.getID());
        assertEquals(0, account.getBalance());
    }

    @Test
    public void deposit() {
        account.deposit(100);
        assertEquals(100, account.getBalance());
        account.deposit(50);
        assertEquals(150, account.getBalance());
    }

    @Test
    public void negativeDeposit() {
        try {
            account.deposit(-10);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {}
        assertEquals(0, account.getBalance());
    }

    @Test
    public void withdraw() throws Exception {
        account.deposit(100);
        account.withdraw(50);
        assertEquals(50, account.getBalance());
        try {
            account.withdraw(51);
            fail("WithdrawNotCoveredException expected");
        } catch (WithdrawNotCoveredException expected) {}
        assertEquals(50, account.getBalance());
    }

    @Test
    public void negativeWithdraw() throws Exception {
        try {
            account.withdraw(-20);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {}
        assertEquals(0, account.getBalance());
    }
    
    @Test
    public void equality() {
		assertTrue(account.equals(account));
		assertTrue(account.equals(new Account("Customer", "0001")));
		assertFalse(account.equals(new Account("Customer2", "0001")));
		assertFalse(account.equals(new Account("Customer", "0002")));
		Account accountDifferentBalance = new Account("Customer", "0001");
		account.deposit(10);
		assertFalse(account.equals(accountDifferentBalance));
	}
}