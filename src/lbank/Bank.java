package lbank;

/*
 * Copyright by Legacy Bank Inc
 * Canary Islands 
 */
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import lbank.persistence.Persistence;
import lbank.persistence.PersistenceException;

/*
 * Copyright by Legacy Bank Inc
 * Canary Islands 
 */
public 
class 
Bank 
{
	// The famous singleton pattern from the GOF book
	// I really llike it because it can be used everywhere
	private static Bank instance = null;
	public static Bank getInstance() {
		if (instance == null) 
		{
			instance = new Bank();
		}
		return instance;
	}
	
    /*
     * Copyright by Legacy Bank Inc
     * Canary Islands 
     */
	/**
	 * This constructor initializes the bank object
	 */
    private Bank() 
	    {
	    	String highestId = null;
			try 
			{
				//ask database
				highestId = Persistence.getInstance().getHighestId();
			} catch (PersistenceException e) {
				e.printStackTrace();}
				id = Integer.parseInt(highestId) + 1;
		}
    
    /**
     * Cache for all accounts to make it faster
     */
    private HashMap<String, Account> cache = new HashMap<String, Account>(100000); //100000 should be enough 
    private Account lastAccount;
    
    /*
     * Copyright by Legacy Bank Inc
     * Canary Islands 
     */
    /**
     * Create a new account
     * @customer the customer
     * Create account and give new account id to accountr
     */
    public Account newAccount(
    		String customer//,
    		//String firstName,
    		//String lastName,
    		//String street,
    		//String city
    		) {
    	//todo: In the future a customer should have first name, last name and an address
        int currentId = id;
		try 
        {
//account IDS SHOULD ALWAYS HAVE 10 digits
            cache.remove("000000000" + currentId); //account 
			String nextId = "000000000" + currentId;
			/*
			 * Call persistence so that a new persistent account 
			 * for a customer and wih
			 * a next id is created
			 */
			Persistence database = Persistence.getInstance();
			
			if(false) { //Todo: currrently disabled
				checkIfValidNewAccount(customer, /* firstName, lastName, street, city,*/nextId);
			}

			Account a = database.createAccount(customer, /* firstName, lastName, street, city,*/nextId);
			/*
			if (!(a instanceof Account)) {
				log.error(new RuntimeException("error from database"));
			}
			*/
			if (!(a instanceof Account) && (a != null)) {
				throw new RuntimeException("error from database");
			}
			return a;
        } 
        catch (PersistenceException e) 
        {
        	e.printStackTrace();
        	//log.logWarning(e.toString())
            throw new RuntimeException(e);
        } finally 
        	{
        	id = currentId + 1; //increment id by one
            }
    }

    private void checkIfValidNewAccount(String customer, String nextId) {
    	//todo: check with Frank what to do here
	}

	private int id;

    /*
     * Copyright by Legacy Bank Inc
     * Canary Islands 
     */
    /**
     * Create a new account
     * @customer the customer
     * Create account and give new account id to account
     */
    public void saveAccount(Account account) {
        try {
        	Persistence.getInstance().writeAccount(account);
        } catch (PersistenceException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Copyright by Legacy Bank Inc
     * Canary Islands 
     */
    /**
     * Create a new account
     * @customer the customer
     * Create account and give new account id to account
     */
    public Account getAccount(String id) {
        try {
            Account account = Persistence.getInstance().getAccount(id);
            cache.put(account.getID(), account);
			return account;
        } catch (PersistenceException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Compact the cache so that it only holds max. 10 accounts
     */
    private void cleanUpCache() {
    	cache.clear();
    }
    
    /*
     * Copyright by Legacy Bank Inc
     * Canary Islands 
     */
    /**
     * Create a new account
     * @customer the customer
     * Create account and give new account id to account
     */
   public String[] getAllAccounts() {
    	try {
			return Persistence.getInstance().getAllAccountIds();
		} catch (PersistenceException e) {
			throw new RuntimeException(e);
		}
    }
}