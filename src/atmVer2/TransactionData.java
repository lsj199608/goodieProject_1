package atmVer2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionData {
	private String date;
	private String transactionType;
	private int amount;
	private int balance;
	private String time;
	public static Map<String, List> transactionDataMap = new HashMap<String, List>();	

	public String getDate() {
		return date;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public int getAmount() {
		return amount;
	}

	public int getBalance() {
		return balance;
	}

	public String getTime() {
		return time;
	}

	public TransactionData(String date, String transactionType, int amount, int balance, String time) {
		super();
		this.date = date;
		this.transactionType = transactionType;
		this.amount = amount;
		this.balance = balance;
		this.time = time;
	}
	
	
}
