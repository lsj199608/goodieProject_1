package atmVer2;

import java.util.Scanner;

public class Atm extends Bank {

	private int cash = 100000000;
	private Customer savedCustomer;
	private Customer receiveCustomer;
	Bank bank;
	
	Atm(){}
	
	Atm(Bank bank){
		this.bank = bank;
	}

	public int getCash() {
		return cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}

	public Customer getCustomer() {
		return savedCustomer;
	}

	public void setCustomer(Customer customer) {
		this.savedCustomer = customer;
	}

	Scanner sc = new Scanner(System.in);

	public void greeting(Atm atm) {
		System.out.println("========================================");
		System.out.println("환영합니다 GoodieBankATM 입니다.");
		service(atm);
	}

	public void service(Atm atm) {
		Service service = new Service();
		while (true) {
			service.showMainMenu();
			String selectedMenu = sc.nextLine();
			if (Check.checkPattern(selectedMenu)) {
				switch (selectedMenu) {
				case "1":
					savedCustomer = service.deposit(savedCustomer, atm);
					break;
				case "2":
					savedCustomer = service.withdraw(savedCustomer, atm);
					break;
				case "3":
					savedCustomer = service.transfer(savedCustomer, receiveCustomer);
					break;
				case "4":
					savedCustomer = service.balanceCheck(savedCustomer);
					break;
				case "5":
					savedCustomer = service.viewTransactionInquiry(savedCustomer);
					break;
				case "6":
					System.out.println("현재 거래 중인 계좌는 반환됩니다.");
					savedCustomer = null;
					service.makeAccount(bank);
					break;
				case "7":
					savedCustomer = service.loan(savedCustomer, bank);
					break;
				case "8":
					service.endMent();
					setCustomer(null);
					continue;
				case "800":
					System.out.println("========================================");
					System.out.println("시스템이 종료되었습니다.");
					System.out.println("========================================");
					System.exit(0);
				default:
					service.notExistMenuMent();
					continue;
				}
				boolean b = true;
				while (b) {
					service.showContinueMent();
					String selectedcontinueMenu = sc.nextLine();
					if (Check.checkPattern(selectedcontinueMenu)) {
						switch (selectedcontinueMenu) {
						case "1":
							b=false;
							break;
						case "2":
							service.endMent();
							setCustomer(null);
							b=false;
							break;
						default:
							service.notExistMenuMent();
						}
					}
					else {
						service.wrongInputMent();
					}
				}
			}
			else {
				service.wrongInputMent();
			}
		}
	}
}