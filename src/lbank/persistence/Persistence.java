package lbank.persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import lbank.Account;

public class Persistence {

	private static final String BALANCE_COLUMN = "balance";
	private static final String CUSTOMER_COLUMN = "customer";
	private static final String ID_COLUMN = "id";
	private static final String ACCOUNT_TABLE = "ACCOUNT";
	private static final int DEFAULT_BALANCE = 0;

	private Connection connection;
	private static Persistence instance = null;
	private Properties properties;

	public static Persistence getInstance() {
		if (instance == null) {
			instance = createInstance();
		}
		return instance;
	}

	private static Persistence createInstance() {
		return new Persistence();
	}

	public interface StatementRunner {
		Object run(Statement statement) throws PersistenceException,
				SQLException;
	}

	public Persistence() {
		readProperties();
		initConnection();
		createAccountTable();
	}

	private void readProperties() {
		properties = new Properties();
		try {
			properties.load(getClass().getResourceAsStream("/lbank/lbank.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void initConnection() {
		String driver = properties.getProperty("db.driver");
		String url = properties.getProperty("db.url");
		String user = properties.getProperty("db.user");
		String password = properties.getProperty("db.password");
		try {
			Class.forName(driver);
			this.connection = DriverManager.getConnection(url, user, password);
			connection.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getHighestId() throws PersistenceException {
		StatementRunner runner = new StatementRunner() {
			public Object run(Statement statement) throws PersistenceException,
					SQLException {
				ResultSet result = statement.executeQuery("select " + ID_COLUMN
						+ " from " + ACCOUNT_TABLE);
				String highest = "0000000000";
				while (result.next()) {
					String id = result.getString(1);
					if (id.compareTo(highest) >0 ) {
						highest = id;
					}
				}
				return highest;
			}
		};
		return (String) execute(runner);
	}

	public Account createAccount(final String customer, final String id)
			throws PersistenceException {
		StatementRunner runner = new StatementRunner() {
			public Object run(Statement statement) throws PersistenceException,
					SQLException {
				insertAccount(customer, id, statement);
				return new Account(customer, id);
			}
		};
		return (Account) execute(runner);
	}

	public Account getAccount(final String id) throws PersistenceException {
		StatementRunner runner = new StatementRunner() {
			public Object run(Statement statement) throws PersistenceException,
					SQLException {
				ResultSet result = retrieveAccountResultSet(statement, id);
				if (!result.next()) {
					return null;
				}
				return createAccountFromResultSet(result);
			}
		};
		return (Account) execute(runner);
	}

	public int countAccounts() throws PersistenceException {
		StatementRunner runner = new StatementRunner() {
			public Object run(Statement statement) throws PersistenceException,
					SQLException {
				ResultSet result = retrieveAllAccountsResultSet(statement);
				int count = 0;
				while (result.next()) {
					count++;
				}
				return new Integer(count);
			}
		};
		return ((Integer) execute(runner)).intValue();
	}

	public void writeAccount(final Account account) throws PersistenceException {
		StatementRunner runner = new StatementRunner() {
			public Object run(Statement statement) throws PersistenceException,
					SQLException {
				statement.executeUpdate(writeAccountSQL(account.getID(),
						account.getBalance()));
				return null;
			}
		};
		execute(runner);
	}

	private String writeAccountSQL(String id, int balance) {
		return "update " + ACCOUNT_TABLE + " set " + BALANCE_COLUMN + " = '"
				+ balance + "' where id = '" + id + "'";
	}

	private Object execute(StatementRunner runner) throws PersistenceException {
		try {
			Statement statement = connection.createStatement();
			Object result = runner.run(statement);
			statement.close();
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void insertAccount(String customer, String id, Statement statement)
			throws SQLException, PersistenceException {
		if (getAccount(id) != null) {
			throw new PersistenceException("There's already a user with ID '"
					+ id + "' in the database");
		}
		statement.executeUpdate(insertAccountSQL(customer, id));
	}

	private String insertAccountSQL(String customer, String id) {
		return "insert into " + ACCOUNT_TABLE + " values ('" + id + "', '"
				+ customer + "', '" + DEFAULT_BALANCE + "')";
	}

	private void createAccountTable() {
		StatementRunner runner = new StatementRunner() {
			public Object run(Statement statement) throws PersistenceException,
					SQLException {
				try {
					statement.executeUpdate(createAccountTableSQL());
				} catch (SQLException e) {
					// happens if table already exists
				}
				return null;
			}
		};
		try {
			execute(runner);
		} catch (PersistenceException cannotHappen) {
		}
	}

	private String createAccountTableSQL() {
		return "create table " + ACCOUNT_TABLE + " (" + ID_COLUMN
				+ " varchar(16), " + CUSTOMER_COLUMN + " varchar(256), "
				+ BALANCE_COLUMN + " integer )";
	}

	private Account createAccountFromResultSet(ResultSet result)
			throws SQLException {
		Account account = new Account(result.getString(CUSTOMER_COLUMN), result
				.getString(ID_COLUMN));
		account.setBalance(result.getInt(BALANCE_COLUMN));
		return account;
	}

	private ResultSet retrieveAccountResultSet(Statement statement, String id)
			throws SQLException {
		return statement.executeQuery(selectFromAccountSQL() + " where id = '"
				+ id + "'");
	}

	private ResultSet retrieveAllAccountsResultSet(Statement statement)
			throws SQLException {
		return statement.executeQuery(selectFromAccountSQL());
	}

	private String selectFromAccountSQL() {
		return "select * from " + ACCOUNT_TABLE;
	}

	public String[] getAllAccountIds() throws PersistenceException {
		StatementRunner runner = new StatementRunner() {
			public Object run(Statement statement) throws PersistenceException,
					SQLException {
				ResultSet result = statement.executeQuery("select " + ID_COLUMN
						+ " from " + ACCOUNT_TABLE);
				ArrayList<String> ids = new ArrayList<String>();
				while (result.next()) {
					String id = result.getString(1);
					ids.add(id);
				}
				return ids.toArray(new String[ids.size()]);
			}
		};
		return (String[]) execute(runner);
	}
}