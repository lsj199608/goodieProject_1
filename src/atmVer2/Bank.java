package atmVer2;

import java.util.ArrayList;
import java.util.List;

public class Bank {

	private int balance = 2000000000;
	
	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	static List<Customer> customerList = new ArrayList<Customer>();

	//실제 데이터 베이스를 연동하는 것이 아니므로 static을 이용하여 메모리를 데이터베이스로 사용)
	public void addCustomer(String name, String password, int balace) {
		Customer customer = new Customer(name, password, balace);
		customerList.add(customer);
		System.out.println("고객님의 계좌가 발급되었습니다.");
		System.out.println("고객님의 계좌번호는 " + customer.getAccountNumber() + "입니다");
	}
	//예시로 넣을 데이터들을 넣기 위한 메소드
	public void addCustomer(String name, String password, int balace,String ex) {
		Customer customer = new Customer(name, password, balace,ex);
		customerList.add(customer);
		System.out.println("고객님의 계좌가 발급되었습니다.");
		System.out.println("고객님의 계좌번호는 " + customer.getAccountNumber() + "입니다");
	}
}


