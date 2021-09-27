package atmVer2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Customer {

	private String name;
	private String accountNumber;
	private String password;
	private int balance;
	private int loan;
	private int tryCount;
	private static int i;
	private List<TransactionData> transactionDataList = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int amount) {
		this.balance = amount;
	}

	public int getLoan() {
		return loan;
	}

	public void setLoan(int loan) {
		this.loan = loan;
	}

	public int getTryCount() {
		return tryCount;
	}

	public void setTryCount() {
		this.tryCount += 1;
	}

	public void setTryCount(int reset) {
		this.tryCount = reset;
	}

	public List<TransactionData> getTransactionDataList() {
		return transactionDataList;
	}

	public void setTransactionDataList(List<TransactionData> transactionDataList) {
		this.transactionDataList = transactionDataList;
	}

	public Customer() {
	}

	public Customer(String name, String password) {
		this.name = name;
		this.password = password;
		setAccountNumber(makeAccountNumber());
	}

	public Customer(String name, String password, int balance) {
		this.name = name;
		this.password = password;
		this.balance = balance;
		String result = makeAccountNumber();
		setAccountNumber(result);
	}
	//데이터를 넣기 위한 생성자.
	public Customer(String name, String password, int balance, String ex) {
		this.name = name;
		this.password = password;
		this.balance = balance;
		setAccountNumber(ex);
		pushData(getAccountNumber());
	}

	public String makeAccountNumber() {

		Calendar cal = Calendar.getInstance();

		String year = String.valueOf(cal.get(Calendar.YEAR)).substring(2, 4);
		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
//		String min = String.valueOf(cal.get(Calendar.MINUTE));
//		String sec = String.valueOf(cal.get(Calendar.SECOND));

		String middleNumber = year + month + day + hour + i;
		String LastNumber = Integer.toString(((int) (Math.random() * 10000) + 1));

		String result = middleNumber + LastNumber;
		i++;
		if (i == 100) {
			i = 0;
		}
		return result;
	}
	
	//예시데이터
	public void pushData(String result) {
		if (result.equals("000")) {
			
		}else if(result.equals("001")) {
			TransactionData td1 = new TransactionData("2015-08-10", "입금", 30000, 31000, "00:00:00");
			TransactionData td2 = new TransactionData("2017-08-11", "송금", 10000, 21000, "00:00:00");
			TransactionData td3 = new TransactionData("2019-08-12", "수금", 50000, 71000, "00:00:00");
			TransactionData td4 = new TransactionData("2020-08-13", "출금", 20000, 54000, "00:00:00");
			
			transactionDataList.add(td1);
			transactionDataList.add(td2);
			transactionDataList.add(td3);
			transactionDataList.add(td4);
			
			TransactionData.transactionDataMap.put(result, transactionDataList);
		}else if(result.equals("002")) {
			TransactionData td1 = new TransactionData("2021-08-10", "입금", 30000, 10000000, "00:00:00");
			
			transactionDataList.add(td1);
			
			TransactionData.transactionDataMap.put(result, transactionDataList);
		}else if(result.equals("003")) {
			TransactionData td1 = new TransactionData("2020-08-10", "입금", 30000, 530000, "00:00:00");
			TransactionData td2 = new TransactionData("2021-08-25", "출금", 10000, 520000, "00:00:00");
			
			transactionDataList.add(td1);
			transactionDataList.add(td2);
			
			TransactionData.transactionDataMap.put(result, transactionDataList);
		}else if(result.equals("004")) {
			TransactionData td1 = new TransactionData("2018-08-31", "입금", 30000, 101000, "00:00:00");
			TransactionData td2 = new TransactionData("2019-08-31", "출금", 10000, 91000, "00:00:00");
			TransactionData td3 = new TransactionData("2020-08-31", "입금", 50000, 141000, "00:00:00");
			TransactionData td4 = new TransactionData("2021-08-31", "출금", 20000, 121000, "00:00:00");
			
			transactionDataList.add(td1);
			transactionDataList.add(td2);
			transactionDataList.add(td3);
			transactionDataList.add(td4);
			
			TransactionData.transactionDataMap.put(result, transactionDataList);
		}else if(result.equals("005")) {
			TransactionData td1 = new TransactionData("2015-08-10", "입금", 100000, 100000, "00:00:00");
			TransactionData td2 = new TransactionData("2019-08-11", "출금", 10000, 90000, "00:00:00");
			TransactionData td3 = new TransactionData("2020-08-12", "입금", 50000, 140000, "00:00:00");
			TransactionData td4 = new TransactionData("2020-08-30", "출금", 20000, 120000, "00:00:00");
			TransactionData td5 = new TransactionData("2020-01-14", "입금", 40000, 160000, "00:00:00");
			TransactionData td6 = new TransactionData("2021-03-15", "출금", 70000, 90000, "00:00:00");
			TransactionData td7 = new TransactionData("2021-04-16", "입금", 220000, 310000, "00:00:00");
			TransactionData td8 = new TransactionData("2021-07-30", "출금", 80000, 230000, "00:00:00");
			TransactionData td9 = new TransactionData("2021-08-25", "입금", 90000000, 90230000, "00:00:00");
			transactionDataList.add(td1);
			transactionDataList.add(td2);
			transactionDataList.add(td3);
			transactionDataList.add(td4);
			transactionDataList.add(td5);
			transactionDataList.add(td6);
			transactionDataList.add(td7);
			transactionDataList.add(td8);
			transactionDataList.add(td9);
			TransactionData.transactionDataMap.put(result, transactionDataList);
		}else if(result.equals("006")) {
			TransactionData td1 = new TransactionData("2021-07-31", "출금", 30000, 300000000, "00:00:00");
			TransactionData td2 = new TransactionData("2021-08-29", "출금", 10000, 299990000, "00:00:00");
			
			transactionDataList.add(td1);
			transactionDataList.add(td2);
			
			TransactionData.transactionDataMap.put(result, transactionDataList);
		}else if(result.equals("007")) {
			TransactionData td1 = new TransactionData("2021-08-10", "송금", 200000, 500000, "00:00:00");
			TransactionData td2 = new TransactionData("2021-08-11", "송금", 10000, 490000, "00:00:00");
			TransactionData td3 = new TransactionData("2021-08-12", "수금", 50000, 540000, "00:00:00");
			TransactionData td4 = new TransactionData("2021-08-13", "수금", 20000, 560000, "00:00:00");
			
			transactionDataList.add(td1);
			transactionDataList.add(td2);
			transactionDataList.add(td3);
			transactionDataList.add(td4);
			
			TransactionData.transactionDataMap.put(result, transactionDataList);
		}else if(result.equals("008")) {
			TransactionData td1 = new TransactionData("2021-08-10", "입금", 30000, 250000, "00:00:00");
			TransactionData td2 = new TransactionData("2021-08-11", "출금", 10000, 240000, "00:00:00");
			TransactionData td3 = new TransactionData("2021-08-12", "송금", 50000, 190000, "00:00:00");
			TransactionData td4 = new TransactionData("2021-08-13", "출금", 20000, 170000, "00:00:00");
			TransactionData td5 = new TransactionData("2021-08-14", "송금", 40000, 130000, "00:00:00");
			TransactionData td6 = new TransactionData("2021-08-15", "출금", 70000, 60000, "00:00:00");
			TransactionData td7 = new TransactionData("2021-08-16", "입금", 220000, 280000, "00:00:00");
			TransactionData td8 = new TransactionData("2021-08-17", "출금", 80000, 200000, "00:00:00");
			TransactionData td9 = new TransactionData("2021-08-25", "입금", 90000, 290000, "00:00:00");
			transactionDataList.add(td1);
			transactionDataList.add(td2);
			transactionDataList.add(td3);
			transactionDataList.add(td4);
			transactionDataList.add(td5);
			transactionDataList.add(td6);
			transactionDataList.add(td7);
			transactionDataList.add(td8);
			transactionDataList.add(td9);
			TransactionData.transactionDataMap.put(result, transactionDataList);
		}else if(result.equals("009")) {
			TransactionData td1 = new TransactionData("2020-12-10", "입금", 10000, 1000000, "00:00:00");
			TransactionData td2 = new TransactionData("2021-01-11", "출금", 20000, 980000, "00:00:00");
			TransactionData td3 = new TransactionData("2021-02-12", "입금", 30000, 1010000, "00:00:00");
			TransactionData td4 = new TransactionData("2021-03-13", "출금", 40000, 970000, "00:00:00");
			TransactionData td5 = new TransactionData("2021-04-14", "입금", 50000, 1020000, "00:00:00");
			TransactionData td6 = new TransactionData("2021-05-15", "출금", 60000, 960000, "00:00:00");
			TransactionData td7 = new TransactionData("2021-06-16", "입금", 70000, 1030000, "00:00:00");
			TransactionData td8 = new TransactionData("2021-07-30", "출금", 80000, 950000, "00:00:00");
			TransactionData td9 = new TransactionData("2021-08-25", "입금", 90000, 1040000, "00:00:00");
			transactionDataList.add(td1);
			transactionDataList.add(td2);
			transactionDataList.add(td3);
			transactionDataList.add(td4);
			transactionDataList.add(td5);
			transactionDataList.add(td6);
			transactionDataList.add(td7);
			transactionDataList.add(td8);
			transactionDataList.add(td9);
			TransactionData.transactionDataMap.put(result, transactionDataList);
		}
	}
	
}
