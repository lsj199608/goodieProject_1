package atmVer2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Service {

	/**
	 * �Ա�
	 */
	public Customer deposit(Customer savedCustomer, Atm atm) {
		Scanner sc = new Scanner(System.in);
		if (savedCustomer == null) {
			savedCustomer = checkAccountNumber();
			if(savedCustomer == null) {
				return null;
			}
		}
		System.out.println("========================================");
		System.out.println("�Ա��Ͻ� �ݾ��� �Է����ּ���");
		System.out.println("========================================");

		String amount;
		while (true) {
			amount = sc.nextLine();
			if (Check.checkPattern(amount)) {
				if (Integer.parseInt(amount) % 1000 != 0 || Integer.parseInt(amount) == 0) {
					System.out.println("1000�� �����θ� �Ա��� �����մϴ�. �Ա��Ͻ� �ݾ��� �ٽ� �Է��� �ּ���");
				} else {
					break;
				}
			} else {
				System.out.println("========================================");
				System.out.println("���ڸ� �Է� �����մϴ�. �Ա��Ͻ� �ݾ��� �ٽ� �Է��� �ּ���.");
				System.out.println("========================================");
			}
		}
		System.out.println("========================================");
		System.out.println("�Ա��Ͻ� �ݾ��� " + amount + "�� �����ʴϱ�? ������ y, �ƴϸ� n�� �����ּ���");
		System.out.println("========================================");
		while (true) {
			String yesOrNo = sc.nextLine();
			if (yesOrNo.equals("y")) {
				atm.setCash(atm.getCash() + Integer.parseInt(amount));
				atm.setBalance(atm.getBalance() + Integer.parseInt(amount));
				savedCustomer.setBalance(savedCustomer.getBalance() + Integer.parseInt(amount));
				System.out.println(amount + "�� �ԱݵǾ����ϴ�. ���� �ܾ��� " + savedCustomer.getBalance() + "�� �Դϴ�.");

				Date date = new GregorianCalendar().getTime();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
				String transactionDate = dateFormat.format(date);
				String transactionTime = timeFormat.format(date);

				savedCustomer.getTransactionDataList().add(new TransactionData(transactionDate, "�Ա�",
						Integer.parseInt(amount), savedCustomer.getBalance(), transactionTime));
				TransactionData.transactionDataMap.put(savedCustomer.getAccountNumber(),
						savedCustomer.getTransactionDataList());

				return savedCustomer;

			} else if (yesOrNo.equals("n")) {
				System.out.println("��ҵǾ����ϴ�. ó������ ���ư��ϴ�.");
				return savedCustomer;
			} else {
				System.out.println("========================================");
				System.out.println("�߸� �Է��ϼ̽��ϴ�. y�Ǵ� n�� �Է��� �ּ���");
				System.out.println("========================================");
			}
		}

	}

	/**
	 * ���
	 */
	public Customer withdraw(Customer savedCustomer, Atm atm) { // �Ա�, ��� ���� �޼ҵ���� �ϳ��� Ŭ�������� ȣ�� - ȣ���Ҷ� �Ķ���ͷ� ���޾Ƽ� ������ ��
		Scanner sc = new Scanner(System.in);
		if (savedCustomer == null) {
			savedCustomer = checkAccountNumber();
			if(savedCustomer == null) {
				return null;
			}
		}
		
		if (savedCustomer.getBalance() < 10000) {
			System.out.println("������ �ܰ�� " + savedCustomer.getBalance() + "������ 10000�� �̸��� ����� �Ұ��մϴ�.");
		} else {
			System.out.println("========================================");
			System.out.println("������ �ܰ�� " + savedCustomer.getBalance() + "�� �Դϴ� .\n����Ͻ� �ݾ��� �Է����ּ��� (����: ����)");
			System.out.println("========================================");

			while (true) {
				try {
					int inputtedAmount = Integer.parseInt(sc.nextLine());
					if (inputtedAmount % 10000 == 0 && inputtedAmount <= 1000000
							&& savedCustomer.getBalance() >= inputtedAmount) {
						while (true) {
							try {
								if (inputtedAmount >= 50000) {
									int maxCount = inputtedAmount / 50000;
									System.out.println("========================================");
									System.out.println("��� ��û �ݾ�: " + inputtedAmount + "\n5�������� ������ �������ּ��� (�ִ�: "
											+ maxCount + ")");
									System.out.println("========================================");
									int inputtedCount = Integer.parseInt(sc.nextLine());
									try {
										if (inputtedCount <= maxCount && inputtedCount >= 0) {
											int result = (inputtedAmount - (50000 * inputtedCount)) / 10000;
											withdrawTransaction(atm, savedCustomer, inputtedAmount);
											System.out.println("========================================");
											System.out.println("5����:" + inputtedCount + "��, " + "1����:" + result + "��\n"
													+ inputtedAmount + "�� ����� �Ϸ�Ǿ����ϴ�");
											System.out.println("========================================");

											recordTransaction(savedCustomer, inputtedAmount);
											return savedCustomer;
										} else {
											System.out.println("========================================");
											System.out.println("���� ���� ������ �ƴմϴ� �ٽ� �Է����ּ���");
											System.out.println("========================================");
										}
									} catch (Exception e3) {
										wrongInputMent();
									}
								} else if (inputtedAmount < 0) {
									wrongInputMent();
									break;
								} else {
									withdrawTransaction(atm, savedCustomer, inputtedAmount);
									System.out.println("========================================");
									System.out.println(inputtedAmount + "�� ����� �Ϸ�Ǿ����ϴ�");
									System.out.println("========================================");

									recordTransaction(savedCustomer, inputtedAmount);
									return savedCustomer;
								}
							} catch (Exception e2) {
								wrongInputMent();
							}
						}
					} else {
						wrongInputMent();
					}
				} catch (Exception e) {
					wrongInputMent();
				}
			}
		}
		return savedCustomer;
	}

	/**
	 * �۱�
	 */
	public Customer transfer(Customer savedCustomer, Customer receiveCustomer) {
		String money = null;

		if (savedCustomer == null) {
			savedCustomer = checkAccountNumber();
			if(savedCustomer == null) {
				return null;
			}
		}
		Scanner sc = new Scanner(System.in);

		System.out.println("========================================");
		System.out.println("�۱� ���񽺸� �����մϴ�.");
		System.out.println("��ü�Ͻ� �ݾ��� �Է����ּ���.");
		System.out.println("========================================");

		while (true) {
			money = sc.nextLine();
			if (Check.checkPattern(money)) {
				break;
			} else {
				wrongInputMent();
			}
		}

		while (true) {
			if (savedCustomer.getBalance() < Integer.parseInt(money)) {
				System.out.println("�ܾ��� �����մϴ�.");
				System.out.println("���� �ܾ��� " + savedCustomer.getBalance() + "�� �Դϴ�.");
				break;
			} else if (Integer.parseInt(money) <= 0) {
				System.out.println("�ݾ��� 0 ���ϰ� �� �� �����ϴ�.");
				break;
			} else {
				System.out.println("========================================");
				System.out.println(money + "���� �۱��մϴ�.");
				System.out.println("������ ���� ���¹�ȣ�� �Է����ּ���.");
				System.out.println("========================================");
				while (true) {
					String customer2Num = sc.nextLine();
					if (Check.checkPattern(customer2Num)) {
						if (Check.checkAccountNumber(customer2Num)) {
							for (int i = 0; i < Bank.customerList.size(); i++) {
								if (Bank.customerList.get(i).getAccountNumber().equals(customer2Num)) {
									receiveCustomer = Bank.customerList.get(i);

									System.out.println("========================================");
									System.out.println(money + "�� ��ü�Ǿ����ϴ�.");
									System.out.println("========================================");

									savedCustomer.setBalance(savedCustomer.getBalance() - Integer.parseInt(money));
									receiveCustomer
											.setBalance((receiveCustomer.getBalance() + Integer.parseInt(money)));

									System.out.println("========================================");
									System.out.println("�۱� �� " + savedCustomer.getName() + "���� �ܰ�� "
											+ savedCustomer.getBalance() + "�� �Դϴ�.");
									System.out.println("========================================");

									Date date = new GregorianCalendar().getTime();
									SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
									SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
									String transactionDate = dateFormat.format(date);
									String transactionTime = timeFormat.format(date);

									savedCustomer.getTransactionDataList()
											.add(new TransactionData(transactionDate, "�۱�", Integer.parseInt(money),
													savedCustomer.getBalance(), transactionTime));
									receiveCustomer.getTransactionDataList()
											.add(new TransactionData(transactionDate, "����", Integer.parseInt(money), 
													receiveCustomer.getBalance(), transactionTime));
									TransactionData.transactionDataMap.put(savedCustomer.getAccountNumber(),
											savedCustomer.getTransactionDataList());
									TransactionData.transactionDataMap.put(receiveCustomer.getAccountNumber(),
											receiveCustomer.getTransactionDataList());

									return savedCustomer;
								}
							}
						} else {
							System.out.println("========================================");
							System.out.println("���� ���¹�ȣ �Դϴ�. ���¹�ȣ�� �ٽ� �Է����ּ���.");
							System.out.println("========================================");
						}
					}else {
						System.out.println("========================================");
						System.out.println("���¹�ȣ�� �ٽ� �Է����ּ���.");
						System.out.println("========================================");
					}
				}
			}
		}
		return savedCustomer;
	}

	/**
	 * �ܰ� ��ȸ
	 */
	public Customer balanceCheck(Customer savedCustomer) {
		if (savedCustomer == null) {
			savedCustomer = checkAccountNumber();
			if(savedCustomer == null) {
				return null;
			}
		}
		System.out.println("========================================");
		System.out.println("������ �ܰ�� " + savedCustomer.getBalance() + "�� �Դϴ�.");
		System.out.println("========================================");
		return savedCustomer;
	}

	/**
	 * �ŷ����� ��ȸ
	 */
	public Customer viewTransactionInquiry(Customer savedCustomer) {
		Scanner sc = new Scanner(System.in);
		if (savedCustomer == null) {
			savedCustomer = checkAccountNumber();
			if(savedCustomer == null) {
				return null;
			}
		}
		boolean bool = true;
		List<TransactionData> dataList = null;

		if (!(TransactionData.transactionDataMap.isEmpty())
				&& (TransactionData.transactionDataMap.get(savedCustomer.getAccountNumber()) != null)) {
			dataList = TransactionData.transactionDataMap.get(savedCustomer.getAccountNumber());
		} else {
			System.out.println("�ŷ������� �����ϴ�");
			return savedCustomer;
		}
		System.out.println("========================================");
		System.out.println("��ȸ�Ͻ� �Ⱓ�� �������ּ���(�ִ� 3����� ��ȸ �����մϴ�)");
		System.out.println("1. ���� 1���� �̳�\t 2. ���� 6���� �̳� \t 3. ���� 1�� �̳� \t 4. ��ü �Ⱓ");
		System.out.println("========================================");

		while (bool) {
			String cases = sc.nextLine();
			switch (cases) {
			case "1":
				calculateDate(31, dataList);
				bool = false;
				break;
			case "2":
				calculateDate(180, dataList);
				bool = false;
				break;
			case "3":
				calculateDate(365, dataList);
				bool = false;
				break;
			case "4":
				calculateDate(1100, dataList);
				bool = false;
				break;
			default:
				System.out.println("========================================");
				System.out.println("�߸� �Է��ϼ̽��ϴ�. �ٽ� ������ �ּ���.");
				System.out.println("========================================");
				break;
			}
		}
		return savedCustomer;
	}

	/**
	 * �ű� ���
	 */

	public void makeAccount(Bank bank) {
		Scanner sc = new Scanner(System.in);
		System.out.println("========================================");
		System.out.println("���¹�ȣ�� �̸��� ��й�ȣ�� �Է��Ͻø� �ڵ����� �����˴ϴ�.\n���� ������ �̸��� �Է����ּ���.");
		System.out.println("========================================");
		String name = inputOwnerName();

		System.out.println("========================================");
		System.out.println("����Ͻ� ��й�ȣ�� �Է����ּ���. ��й�ȣ�� 0�� ������ ���� 4�� �Դϴ�.");
		System.out.println("========================================");
		String password = inputPassword();

		System.out.println("========================================");
		System.out.println("���¸� �����Ϸ��� �ʱ� �Ա��� �ʿ��մϴ�. õ�������� �Ա��� �ֽʽÿ�.");
		System.out.println("========================================");
		int balance = Integer.parseInt(inputBalance());

		bank.addCustomer(name, password, balance);
	}

	/**
	 * �̸� �Է¹޴� �޼ҵ�
	 */
	public String inputOwnerName() {
		Scanner sc = new Scanner(System.in);
		String name;
		while (true) {
			name = sc.nextLine();
			if (name == null || name.length() == 0) {
				System.out.println("========================================");
				System.out.println("�̸��� �ٽ� �Է����ּ���.");
				System.out.println("========================================");
			} else if (name.matches(".*[0-9].*")) {
				System.out.println("========================================");
				System.out.println("�̸��� ���ڸ� ������ �� �����ϴ�. �ٽ� �Է����ּ���.");
				System.out.println("========================================");
			} else {
				return name;
			}
		}
	}

	/**
	 * ��� �Է¹޴� �޼ҵ�
	 */
	public String inputPassword() {
		Scanner sc = new Scanner(System.in);
		String password;
		while (true) {
			password = sc.nextLine();
			if (Check.checkPattern(password) && password.length() == 4) {
				return password;
			} else {
				System.out.println("========================================");
				System.out.println("��й�ȣ�� ���� 4�ڸ��� �����˴ϴ�. �ٽ� �Է����ּ���.");
				System.out.println("========================================");
			}

		}
	}

	/**
	 * �ʱ� �Աݾ� �Է¹޴� �޼ҵ�
	 */
	public String inputBalance() {
		Scanner sc = new Scanner(System.in);
		String balance;
		while (true) {
			balance = sc.nextLine();
			if (Check.checkPattern(balance)) {
				if (Integer.parseInt(balance) % 1000 != 0) {
					System.out.println("1000�� �����θ� �Ա��� �����մϴ�. �Ա��Ͻ� �ݾ��� �ٽ� �Է��� �ּ���");
				} else {
					return balance;
				}
			} else {
				System.out.println("========================================");
				System.out.println("���ڸ� �Է� �����մϴ�. �Ա��Ͻ� �ݾ��� �ٽ� �Է��� �ּ���.");
				System.out.println("========================================");
			}
		}
	}

	/**
	 * ����
	 */
	public Customer loan(Customer savedCustomer, Bank bank) {
		if (savedCustomer == null) {
			savedCustomer = checkAccountNumber();
			if(savedCustomer == null) {
				return null;
			}
		}
		try {
			runLoan(savedCustomer, bank);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return savedCustomer;
	}
	
	public Customer runLoan(Customer savedCustomer, Bank bank){
		Scanner sc = new Scanner(System.in);
		System.out.println("========================================");
		System.out.println("������ �����մϴ�. �޴��� �������ּ���");
		System.out.println("1. ���� ��û     2. ���� ��û ��� ��ȸ");
		System.out.println("========================================");
		while (true) {
			String num = sc.nextLine();
			if (Check.checkPattern(num)) {
				switch (num) {
				case "1":
					applicationLoan(savedCustomer, bank);
					return savedCustomer;
				case "2":
					checkLoanResult(savedCustomer);
					return savedCustomer;
				default:
					System.out.println("========================================");
					System.out.println("��ȿ���� ���� �޴��Դϴ�. �ٽ� ������ �ּ���");
					System.out.println("========================================");
					break;
				}
			}else {
				System.out.println("========================================");
				System.out.println("��ȿ���� ���� �޴��Դϴ�. �ٽ� ������ �ּ���");
				System.out.println("========================================");
			}
		}
	}

	public void applicationLoan(Customer savedCustomer, Bank bank) {
		Scanner sc = new Scanner(System.in);
		if (savedCustomer.getBalance() < 10000000) {
			System.out.println("������ ���� ���ǿ� �����Ͻ��� �ʽ��ϴ�.");
			System.out.println("�ܾ��� 1õ���� �̻��� ��쿡 ���� ��û�� �����մϴ�.");
		} else {
			System.out.println("========================================");
			System.out.println(savedCustomer.getName() + "����, �ȳ��ϼ���.");
			System.out.println("������ �ܾװ� ������ ��ȸ�Ͽ� ���� ��û�� �����մϴ�");
			System.out.println("������ ������ �Է����ּ���");
			System.out.println("========================================");

			String income;
			boolean b = true;
			while (b) {
				income = sc.nextLine();
				if (Check.checkPattern(income) == false || Integer.parseInt(income) <= 0) {
					System.out.println("========================================");
					System.out.println("�Է°��� ��ȿ���� �ʽ��ϴ�. �ٽ� �Է��� �ּ���.");
					System.out.println("========================================");
				} else {
					System.out.println("========================================");
					System.out.println("�Է��Ͻ� ����Դϴ�.");
					System.out.println(Integer.parseInt(income));
					System.out.println("�����ø� 1���� �����ֽð� �ٽ� �ۼ��Ͻ÷��� 2���� �����ּ���.");
					System.out.println("========================================");
					String num = sc.nextLine();
					switch (num) {
					case "1":
						moneyAllowed(savedCustomer, bank, income);
						b = false;
						break;
					case "2":
						System.out.println("========================================");
						System.out.println("�ٽ� �ۼ��մϴ�. ������ ������ �Է����ּ���");
						System.out.println("========================================");
						break;
					default:
						System.out.println("========================================");
						System.out.println("��ȿ���� ���� �޴��Դϴ�.");
						System.out.println("�Է��Ͻ� ��� : " + Integer.parseInt(income));
						System.out.println("�����ø� 1���� �����ֽð� �ٽ� �ۼ��Ͻ÷��� 2���� �����ּ���.");
						System.out.println("========================================");
						break;
					}
				}
			}
		}	
	}
	
	public void moneyAllowed(Customer savedCustomer, Bank bank, String income) {
		Scanner sc = new Scanner(System.in);
		
		if (savedCustomer.getBalance() <= 250000000) {
			System.out.println("������ �����Ͻ� ���� ���� �ִ� �ݾ��� " + savedCustomer.getBalance() + "�� �Դϴ�.");
		} else {
			System.out.println("������ �����Ͻ� ���� ���� �ִ� �ݾ��� 250000000�� �Դϴ�.");
		}
		System.out.println("���Ͻô� ���� �ݾ��� �Է����ּ���.");
		System.out.println("========================================");
		
		String money;
		boolean b = true;
		while(b) {
			money = sc.nextLine();
			if (Check.checkPattern(money) == false || Integer.parseInt(money) >= bank.getBalance()) {
				System.out.println("========================================");
				System.out.println("������ �Ұ����� �ݾ��Դϴ�.");
				System.out.println("������ ������ �ݾ��� �Է��� �ּ���.");
				System.out.println("========================================");
			} else if (Integer.parseInt(money) > savedCustomer.getBalance()) {
				System.out.println("========================================");
				System.out.println("������ �Ұ����� �ݾ��Դϴ�.");
				System.out.println("������ ������ �ݾ��� �Է��� �ּ���.");
				System.out.println("========================================");
			} else if (Integer.parseInt(money) < 10000000) {
				System.out.println("========================================");
				System.out.println("������ �Ұ����� �ݾ��Դϴ�.");
				System.out.println("1õ�������� ������ �����մϴ�.");
				System.out.println("������ ������ �ݾ��� �Է��� �ּ���.");
				System.out.println("========================================");
			}
			else {
				System.out.println("========================================");
				System.out.println(money + "�� ���� ��û�ϼ̽��ϴ�.");
				System.out.println("������ ���� ��û�� ���������� �Ϸ�Ǿ����ϴ�.");
				System.out.println("========================================");
				b=false;
				readFile(savedCustomer, bank, income, money);
			}
		} 
	}
	
	public void readFile(Customer savedCustomer, Bank bank, String income, String money) {
		ArrayList<String> arr1 = new ArrayList<>();
		try {
			FileWriter writer = new FileWriter(savedCustomer.getAccountNumber() + ".txt");
			writer.write(savedCustomer.getAccountNumber() + "\n");
			writer.write(Integer.parseInt(income) + "\n");
			writer.write(savedCustomer.getBalance() + "\n");
			writer.close();

			File myFile = new File(savedCustomer.getAccountNumber() + ".txt");
			BufferedReader reader = new BufferedReader(new FileReader(myFile));
			String aLine = "";
			while ((aLine = reader.readLine()) != null) {
				arr1.add(aLine);
			}
			reader.close();

			FileWriter writer2 = new FileWriter(savedCustomer.getAccountNumber() + "_check.txt");
			writer2.write(savedCustomer.getName() + "\n");
			writer2.write(savedCustomer.getAccountNumber() + "\n");
			writer2.write(Integer.parseInt(money) + "\n");
			if (moneyQulification(arr1) >= 110) {
				writer2.write("����");
				savedCustomer.setLoan(savedCustomer.getLoan() + Integer.parseInt(money));
				bank.setBalance(bank.getBalance() - Integer.parseInt(money));
			} else {
				writer2.write("�̽���");
			}
			writer2.close();

		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	public int moneyQulification(ArrayList<String> arr1) {
		int incomeScore, moneyScore;
		if (Integer.parseInt(arr1.get(1)) >= 70000000) {
			incomeScore = 100;
		} else if (Integer.parseInt(arr1.get(1)) >= 50000000 && Integer.parseInt(arr1.get(1)) < 70000000) {
			incomeScore = 80;
		} else if (Integer.parseInt(arr1.get(1)) >= 40000000 && Integer.parseInt(arr1.get(1)) < 50000000) {
			incomeScore = 60;
		} else if (Integer.parseInt(arr1.get(1)) >= 30000000 && Integer.parseInt(arr1.get(1)) < 40000000) {
			incomeScore = 40;
		} else if (Integer.parseInt(arr1.get(1)) >= 20000000 && Integer.parseInt(arr1.get(1)) < 30000000) {
			incomeScore = 20;
		} else {
			incomeScore = 0;
		}

		if (Integer.parseInt(arr1.get(2)) >= 500000000) {
			moneyScore = 100;
		} else if (Integer.parseInt(arr1.get(2)) >= 300000000 && Integer.parseInt(arr1.get(2)) < 500000000) {
			moneyScore = 80;
		} else if (Integer.parseInt(arr1.get(2)) >= 200000000 && Integer.parseInt(arr1.get(2)) < 300000000) {
			moneyScore = 60;
		} else if (Integer.parseInt(arr1.get(2)) >= 100000000 && Integer.parseInt(arr1.get(2)) < 200000000) {
			moneyScore = 40;
		} else if (Integer.parseInt(arr1.get(2)) >= 50000000 && Integer.parseInt(arr1.get(2)) < 100000000) {
			moneyScore = 20;
		} else {
			moneyScore = 0;
		}
		return incomeScore + moneyScore;
	}
	
	public void checkLoanResult(Customer savedCusotmer) {
		List<String> arr2 = new ArrayList<>();
		try {
			File myFile2 = new File(savedCusotmer.getAccountNumber() + "_check.txt");
			BufferedReader reader2 = new BufferedReader(new FileReader(myFile2));
			String aLine2 = "";
			while ((aLine2 = reader2.readLine()) != null) {
				arr2.add(aLine2);
			}
			reader2.close();

			if (myFile2.exists()) {
				System.out.println("========================================");
				System.out.println(savedCusotmer.getName() + "������ ���� ��û ����Դϴ�.");
				if (arr2.get(3).equals("����")) {
					System.out.println("������ ���εǾ����ϴ�.");
					System.out.println("������� ������ ������� " + savedCusotmer.getLoan() + "�Դϴ�.");
				} else {
					System.out.println("������ ���ε��� �ʾҽ��ϴ�.");
				}
				myFile2.delete();
			} else {
				System.out.println("��������� ������� " + savedCusotmer.getLoan());
				System.out.println(savedCusotmer.getName() + "������ ���� ��û ������ �������� �ʽ��ϴ�.");
			}
		} catch (FileNotFoundException e) {
			System.out.println("��������� ������� " + savedCusotmer.getLoan() + "�Դϴ�.");
			System.out.println("���� ��û ������ �������� �ʽ��ϴ�.");
		} catch (IOException e) {
			e.printStackTrace();
		}		
		System.out.println("�̿����ּż� �����մϴ�.");
		return;
	}
	
	/**
	 * ���¹�ȣ �Է¹޾� ��ȿ���� üũ
	 */
	public Customer checkAccountNumber() {
		Scanner sc = new Scanner(System.in);
		System.out.println("========================================");
		System.out.println("���¹�ȣ�� �Է����ּ���.");
		System.out.println("========================================");
		while (true) {
			String inpttedAccountNumber = sc.nextLine();
			if (Check.checkPattern(inpttedAccountNumber)) {
				if (Check.checkAccountNumber(inpttedAccountNumber)) {
					Customer savedCustomer = checkPassword(inpttedAccountNumber);
					if(savedCustomer == null) {
						return null;
					}
					return savedCustomer;
				} else {
					System.out.println("========================================");
					System.out.println("���� ���¹�ȣ �Դϴ�. ���¹�ȣ�� �ٽ� �Է����ּ���.");
					System.out.println("========================================");
				}
			} else {
				wrongInputMent();
			}
		}
	}

	/**
	 * ��й�ȣ �Է¹޾� ��ȿ���� üũ
	 */
	public Customer checkPassword(String inpttedAccountNumber) {
		Scanner sc = new Scanner(System.in);
		System.out.println("========================================");
		System.out.println("��й�ȣ�� �Է��� �ּ���.");
		System.out.println("========================================");

		for (int i = 1; i < 6; i++) {
			String inputtedPassword = sc.nextLine();
			i = Check.getTryCount(inpttedAccountNumber) + 1;
			if (Check.checkPattern(inputtedPassword)) {

				if (i == 6) {
					rejectMent();
					continue;
				}
				Customer getCustomer = Check.checkPasswordAndGetcustomerInfo(inpttedAccountNumber, inputtedPassword);

				if (getCustomer != null) {
					getCustomer.setTryCount(0);
					return getCustomer;
				} else {
					wrongPasswordMent(inpttedAccountNumber, i);
				}
			} else {
				if (i == 6) {
					rejectMent();
				} else {
					wrongPasswordMent(inpttedAccountNumber, i);
				}
			}
		}
		return null;
	}

	/**
	 * ������ �Ⱓ�� �´� ��볻���鸸 ���
	 */
	public void calculateDate(int interval, List<TransactionData> dataList) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new GregorianCalendar().getTime();

		System.out.println("========================================");
		System.out.printf("%12s \t %8s \t %18s \t %18s \t %12s", "�ŷ�����", "��볻��", "�ݾ�", "�ܾ�", "�ŷ��ð�");
		System.out.println();

		for (TransactionData transactionData : dataList) {
			try {
				Date tradeDate = format.parse(transactionData.getDate());
				long calculateDate = (today.getTime() - tradeDate.getTime()) / (24 * 60 * 60 * 1000);
				if (calculateDate <= interval) {
					
					System.out.printf("%12s \t %8s \t %18d \t%13d \t %12s", transactionData.getDate(),
							transactionData.getTransactionType(), transactionData.getAmount(),
							transactionData.getBalance(), transactionData.getTime());
					System.out.println();
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		System.out.println("========================================");
	}
	
	// ��� transaction
	public void withdrawTransaction(Atm atm, Customer savedCustomer, int inputtedAmount) {
		atm.setCash(atm.getCash() - inputtedAmount);
		atm.setBalance(atm.getBalance() - inputtedAmount);
		savedCustomer.setBalance(savedCustomer.getBalance() - inputtedAmount);
	}

	// �ŷ� ���
	public void recordTransaction(Customer savedCustomer, int inputtedAmount) {
		Date date = new GregorianCalendar().getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
		String transactionDate = dateFormat.format(date);
		String transactionTime = timeFormat.format(date);

		savedCustomer.getTransactionDataList().add(new TransactionData(transactionDate, "���", inputtedAmount,
				savedCustomer.getBalance(), transactionTime));
		TransactionData.transactionDataMap.put(savedCustomer.getAccountNumber(),
				savedCustomer.getTransactionDataList());
	}

	public void showMainMenu() {
		System.out.println("========================================");
		System.out.println("�̿��Ͻ� �޴��� ������ �ּ���.");
		System.out.println("1.�Ա�       2.���       3.�۱�     4.�ܾ���ȸ   \n5.�ŷ����� ��ȸ     6.�űԵ��     7.����     8.���� ");
		System.out.println("========================================");
	}

	public void showContinueMent() {
		System.out.println("========================================");
		System.out.println("��� �ŷ� �Ͻ÷��� 1, �����Ͻ÷��� 2�� �����ּ���");
		System.out.println("1.��Ӱŷ�   2.����");
		System.out.println("========================================");
	}

	public void endMent() {
		System.out.println("========================================");
		System.out.println("�ŷ��� ����Ǿ����ϴ�. \n�����մϴ�.");
		System.out.println("========================================");
	}

	public void wrongPasswordMent(String inpttedcustomerNumber, int i) {
		if (Check.checkTryCount(inpttedcustomerNumber, i)) {
			if (i < 5) {
				System.out.println("========================================");
				System.out.println("��й�ȣ�� Ʋ���ϴ� �ٽ� �Է����ּ��� (" + (i) + "/5)");
				System.out.println("========================================");
			} else {
				System.out.println("========================================");
				System.out.println("��й�ȣ 5ȸ ������ �ŷ��� �����Ǿ����ϴ�. \n����� �������� �湮�Ͽ� �ֽʽÿ�.");
				System.out.println("========================================");
			}
		}
	}

	public void wrongInputMent() {
		System.out.println("========================================");
		System.out.println("�Է°� �����Դϴ�. �ٽ� �Է����ּ���.");
		System.out.println("========================================");
	}

	public void notExistMenuMent() {
		System.out.println("========================================");
		System.out.println("�������� �ʴ� �޴��Դϴ� �ٽ� �������ּ���");
		System.out.println("========================================");
	}

	public void rejectMent() {
		System.out.println("========================================");
		System.out.println("��й�ȣ 5ȸ ������ ������ �����Դϴ�. \n����� �������� �湮�Ͽ� �ֽʽÿ�.");
		System.out.println("========================================");
	}
}
