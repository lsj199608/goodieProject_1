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

	//���� ������ ���̽��� �����ϴ� ���� �ƴϹǷ� static�� �̿��Ͽ� �޸𸮸� �����ͺ��̽��� ���)
	public void addCustomer(String name, String password, int balace) {
		Customer customer = new Customer(name, password, balace);
		customerList.add(customer);
		System.out.println("������ ���°� �߱޵Ǿ����ϴ�.");
		System.out.println("������ ���¹�ȣ�� " + customer.getAccountNumber() + "�Դϴ�");
	}
	//���÷� ���� �����͵��� �ֱ� ���� �޼ҵ�
	public void addCustomer(String name, String password, int balace,String ex) {
		Customer customer = new Customer(name, password, balace,ex);
		customerList.add(customer);
		System.out.println("������ ���°� �߱޵Ǿ����ϴ�.");
		System.out.println("������ ���¹�ȣ�� " + customer.getAccountNumber() + "�Դϴ�");
	}
}


