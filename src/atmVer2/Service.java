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
	 * 입금
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
		System.out.println("입금하실 금액을 입력해주세요");
		System.out.println("========================================");

		String amount;
		while (true) {
			amount = sc.nextLine();
			if (Check.checkPattern(amount)) {
				if (Integer.parseInt(amount) % 1000 != 0 || Integer.parseInt(amount) == 0) {
					System.out.println("1000원 단위로만 입금이 가능합니다. 입금하실 금액을 다시 입력해 주세요");
				} else {
					break;
				}
			} else {
				System.out.println("========================================");
				System.out.println("숫자만 입력 가능합니다. 입금하실 금액을 다시 입력해 주세요.");
				System.out.println("========================================");
			}
		}
		System.out.println("========================================");
		System.out.println("입금하실 금액이 " + amount + "원 맞으십니까? 맞으면 y, 아니면 n을 눌러주세요");
		System.out.println("========================================");
		while (true) {
			String yesOrNo = sc.nextLine();
			if (yesOrNo.equals("y")) {
				atm.setCash(atm.getCash() + Integer.parseInt(amount));
				atm.setBalance(atm.getBalance() + Integer.parseInt(amount));
				savedCustomer.setBalance(savedCustomer.getBalance() + Integer.parseInt(amount));
				System.out.println(amount + "원 입금되었습니다. 현재 잔액은 " + savedCustomer.getBalance() + "원 입니다.");

				Date date = new GregorianCalendar().getTime();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
				String transactionDate = dateFormat.format(date);
				String transactionTime = timeFormat.format(date);

				savedCustomer.getTransactionDataList().add(new TransactionData(transactionDate, "입금",
						Integer.parseInt(amount), savedCustomer.getBalance(), transactionTime));
				TransactionData.transactionDataMap.put(savedCustomer.getAccountNumber(),
						savedCustomer.getTransactionDataList());

				return savedCustomer;

			} else if (yesOrNo.equals("n")) {
				System.out.println("취소되었습니다. 처음으로 돌아갑니다.");
				return savedCustomer;
			} else {
				System.out.println("========================================");
				System.out.println("잘못 입력하셨습니다. y또는 n을 입력해 주세요");
				System.out.println("========================================");
			}
		}

	}

	/**
	 * 출금
	 */
	public Customer withdraw(Customer savedCustomer, Atm atm) { // 입금, 출금 등의 메소드들을 하나의 클레스에서 호출 - 호출할때 파라미터로 값받아서 돌리면 됨
		Scanner sc = new Scanner(System.in);
		if (savedCustomer == null) {
			savedCustomer = checkAccountNumber();
			if(savedCustomer == null) {
				return null;
			}
		}
		
		if (savedCustomer.getBalance() < 10000) {
			System.out.println("고객님의 잔고는 " + savedCustomer.getBalance() + "원으로 10000원 미만은 출금이 불가합니다.");
		} else {
			System.out.println("========================================");
			System.out.println("고객님의 잔고는 " + savedCustomer.getBalance() + "원 입니다 .\n출금하실 금액을 입력해주세요 (단위: 만원)");
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
									System.out.println("출금 신청 금액: " + inputtedAmount + "\n5만원권의 수량을 선택해주세요 (최대: "
											+ maxCount + ")");
									System.out.println("========================================");
									int inputtedCount = Integer.parseInt(sc.nextLine());
									try {
										if (inputtedCount <= maxCount && inputtedCount >= 0) {
											int result = (inputtedAmount - (50000 * inputtedCount)) / 10000;
											withdrawTransaction(atm, savedCustomer, inputtedAmount);
											System.out.println("========================================");
											System.out.println("5만원:" + inputtedCount + "장, " + "1만원:" + result + "장\n"
													+ inputtedAmount + "원 출금이 완료되었습니다");
											System.out.println("========================================");

											recordTransaction(savedCustomer, inputtedAmount);
											return savedCustomer;
										} else {
											System.out.println("========================================");
											System.out.println("선택 가능 수량이 아닙니다 다시 입력해주세요");
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
									System.out.println(inputtedAmount + "원 출금이 완료되었습니다");
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
	 * 송금
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
		System.out.println("송금 서비스를 시작합니다.");
		System.out.println("이체하실 금액을 입력해주세요.");
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
				System.out.println("잔액이 부족합니다.");
				System.out.println("현재 잔액은 " + savedCustomer.getBalance() + "원 입니다.");
				break;
			} else if (Integer.parseInt(money) <= 0) {
				System.out.println("금액은 0 이하가 될 수 없습니다.");
				break;
			} else {
				System.out.println("========================================");
				System.out.println(money + "원을 송금합니다.");
				System.out.println("받으실 분의 계좌번호를 입력해주세요.");
				System.out.println("========================================");
				while (true) {
					String customer2Num = sc.nextLine();
					if (Check.checkPattern(customer2Num)) {
						if (Check.checkAccountNumber(customer2Num)) {
							for (int i = 0; i < Bank.customerList.size(); i++) {
								if (Bank.customerList.get(i).getAccountNumber().equals(customer2Num)) {
									receiveCustomer = Bank.customerList.get(i);

									System.out.println("========================================");
									System.out.println(money + "원 이체되었습니다.");
									System.out.println("========================================");

									savedCustomer.setBalance(savedCustomer.getBalance() - Integer.parseInt(money));
									receiveCustomer
											.setBalance((receiveCustomer.getBalance() + Integer.parseInt(money)));

									System.out.println("========================================");
									System.out.println("송금 후 " + savedCustomer.getName() + "님의 잔고는 "
											+ savedCustomer.getBalance() + "원 입니다.");
									System.out.println("========================================");

									Date date = new GregorianCalendar().getTime();
									SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
									SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
									String transactionDate = dateFormat.format(date);
									String transactionTime = timeFormat.format(date);

									savedCustomer.getTransactionDataList()
											.add(new TransactionData(transactionDate, "송금", Integer.parseInt(money),
													savedCustomer.getBalance(), transactionTime));
									receiveCustomer.getTransactionDataList()
											.add(new TransactionData(transactionDate, "수금", Integer.parseInt(money), 
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
							System.out.println("없는 계좌번호 입니다. 계좌번호를 다시 입력해주세요.");
							System.out.println("========================================");
						}
					}else {
						System.out.println("========================================");
						System.out.println("계좌번호를 다시 입력해주세요.");
						System.out.println("========================================");
					}
				}
			}
		}
		return savedCustomer;
	}

	/**
	 * 잔고 조회
	 */
	public Customer balanceCheck(Customer savedCustomer) {
		if (savedCustomer == null) {
			savedCustomer = checkAccountNumber();
			if(savedCustomer == null) {
				return null;
			}
		}
		System.out.println("========================================");
		System.out.println("고객님의 잔고는 " + savedCustomer.getBalance() + "원 입니다.");
		System.out.println("========================================");
		return savedCustomer;
	}

	/**
	 * 거래내역 조회
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
			System.out.println("거래내역이 없습니다");
			return savedCustomer;
		}
		System.out.println("========================================");
		System.out.println("조회하실 기간을 선택해주세요(최대 3년까지 조회 가능합니다)");
		System.out.println("1. 지난 1개월 이내\t 2. 지난 6개월 이내 \t 3. 지난 1년 이내 \t 4. 전체 기간");
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
				System.out.println("잘못 입력하셨습니다. 다시 선택해 주세요.");
				System.out.println("========================================");
				break;
			}
		}
		return savedCustomer;
	}

	/**
	 * 신규 등록
	 */

	public void makeAccount(Bank bank) {
		Scanner sc = new Scanner(System.in);
		System.out.println("========================================");
		System.out.println("계좌번호는 이름과 비밀번호를 입력하시면 자동으로 생성됩니다.\n먼저 본인의 이름을 입력해주세요.");
		System.out.println("========================================");
		String name = inputOwnerName();

		System.out.println("========================================");
		System.out.println("사용하실 비밀번호를 입력해주세요. 비밀번호는 0을 제외한 숫자 4개 입니다.");
		System.out.println("========================================");
		String password = inputPassword();

		System.out.println("========================================");
		System.out.println("계좌를 생성하려면 초기 입금이 필요합니다. 천원단위로 입금해 주십시오.");
		System.out.println("========================================");
		int balance = Integer.parseInt(inputBalance());

		bank.addCustomer(name, password, balance);
	}

	/**
	 * 이름 입력받는 메소드
	 */
	public String inputOwnerName() {
		Scanner sc = new Scanner(System.in);
		String name;
		while (true) {
			name = sc.nextLine();
			if (name == null || name.length() == 0) {
				System.out.println("========================================");
				System.out.println("이름을 다시 입력해주세요.");
				System.out.println("========================================");
			} else if (name.matches(".*[0-9].*")) {
				System.out.println("========================================");
				System.out.println("이름은 숫자를 포함할 수 없습니다. 다시 입력해주세요.");
				System.out.println("========================================");
			} else {
				return name;
			}
		}
	}

	/**
	 * 비번 입력받는 메소드
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
				System.out.println("비밀번호는 숫자 4자리로 구성됩니다. 다시 입력해주세요.");
				System.out.println("========================================");
			}

		}
	}

	/**
	 * 초기 입금액 입력받는 메소드
	 */
	public String inputBalance() {
		Scanner sc = new Scanner(System.in);
		String balance;
		while (true) {
			balance = sc.nextLine();
			if (Check.checkPattern(balance)) {
				if (Integer.parseInt(balance) % 1000 != 0) {
					System.out.println("1000원 단위로만 입금이 가능합니다. 입금하실 금액을 다시 입력해 주세요");
				} else {
					return balance;
				}
			} else {
				System.out.println("========================================");
				System.out.println("숫자만 입력 가능합니다. 입금하실 금액을 다시 입력해 주세요.");
				System.out.println("========================================");
			}
		}
	}

	/**
	 * 대출
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
		System.out.println("대출을 진행합니다. 메뉴를 선택해주세요");
		System.out.println("1. 대출 신청     2. 대출 신청 결과 조회");
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
					System.out.println("유효하지 않은 메뉴입니다. 다시 선택해 주세요");
					System.out.println("========================================");
					break;
				}
			}else {
				System.out.println("========================================");
				System.out.println("유효하지 않은 메뉴입니다. 다시 선택해 주세요");
				System.out.println("========================================");
			}
		}
	}

	public void applicationLoan(Customer savedCustomer, Bank bank) {
		Scanner sc = new Scanner(System.in);
		if (savedCustomer.getBalance() < 10000000) {
			System.out.println("고객님은 대출 조건에 부합하시지 않습니다.");
			System.out.println("잔액이 1천만원 이상일 경우에 대출 신청이 가능합니다.");
		} else {
			System.out.println("========================================");
			System.out.println(savedCustomer.getName() + "고객님, 안녕하세요.");
			System.out.println("고객님의 잔액과 연봉을 조회하여 대출 신청을 진행합니다");
			System.out.println("고객님의 연봉을 입력해주세요");
			System.out.println("========================================");

			String income;
			boolean b = true;
			while (b) {
				income = sc.nextLine();
				if (Check.checkPattern(income) == false || Integer.parseInt(income) <= 0) {
					System.out.println("========================================");
					System.out.println("입력값이 유효하지 않습니다. 다시 입력해 주세요.");
					System.out.println("========================================");
				} else {
					System.out.println("========================================");
					System.out.println("입력하신 결과입니다.");
					System.out.println(Integer.parseInt(income));
					System.out.println("맞으시면 1번을 눌러주시고 다시 작성하시려면 2번을 눌러주세요.");
					System.out.println("========================================");
					String num = sc.nextLine();
					switch (num) {
					case "1":
						moneyAllowed(savedCustomer, bank, income);
						b = false;
						break;
					case "2":
						System.out.println("========================================");
						System.out.println("다시 작성합니다. 고객님의 연봉을 입력해주세요");
						System.out.println("========================================");
						break;
					default:
						System.out.println("========================================");
						System.out.println("유효하지 않은 메뉴입니다.");
						System.out.println("입력하신 결과 : " + Integer.parseInt(income));
						System.out.println("맞으시면 1번을 눌러주시고 다시 작성하시려면 2번을 눌러주세요.");
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
			System.out.println("고객님이 가능하신 대출 가능 최대 금액은 " + savedCustomer.getBalance() + "원 입니다.");
		} else {
			System.out.println("고객님이 가능하신 대출 가능 최대 금액은 250000000원 입니다.");
		}
		System.out.println("원하시는 대출 금액을 입력해주세요.");
		System.out.println("========================================");
		
		String money;
		boolean b = true;
		while(b) {
			money = sc.nextLine();
			if (Check.checkPattern(money) == false || Integer.parseInt(money) >= bank.getBalance()) {
				System.out.println("========================================");
				System.out.println("대출이 불가능한 금액입니다.");
				System.out.println("대출이 가능한 금액을 입력해 주세요.");
				System.out.println("========================================");
			} else if (Integer.parseInt(money) > savedCustomer.getBalance()) {
				System.out.println("========================================");
				System.out.println("대출이 불가능한 금액입니다.");
				System.out.println("대출이 가능한 금액을 입력해 주세요.");
				System.out.println("========================================");
			} else if (Integer.parseInt(money) < 10000000) {
				System.out.println("========================================");
				System.out.println("대출이 불가능한 금액입니다.");
				System.out.println("1천만원부터 대출이 가능합니다.");
				System.out.println("대출이 가능한 금액을 입력해 주세요.");
				System.out.println("========================================");
			}
			else {
				System.out.println("========================================");
				System.out.println(money + "원 대출 신청하셨습니다.");
				System.out.println("고객님의 대출 신청이 정상적으로 완료되었습니다.");
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
				writer2.write("승인");
				savedCustomer.setLoan(savedCustomer.getLoan() + Integer.parseInt(money));
				bank.setBalance(bank.getBalance() - Integer.parseInt(money));
			} else {
				writer2.write("미승인");
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
				System.out.println(savedCusotmer.getName() + "고객님의 대출 신청 결과입니다.");
				if (arr2.get(3).equals("승인")) {
					System.out.println("대출이 승인되었습니다.");
					System.out.println("현재까지 고객님의 대출금은 " + savedCusotmer.getLoan() + "입니다.");
				} else {
					System.out.println("대출이 승인되지 않았습니다.");
				}
				myFile2.delete();
			} else {
				System.out.println("현재까지의 대출금은 " + savedCusotmer.getLoan());
				System.out.println(savedCusotmer.getName() + "고객님의 대출 신청 내역이 존재하지 않습니다.");
			}
		} catch (FileNotFoundException e) {
			System.out.println("현재까지의 대출금은 " + savedCusotmer.getLoan() + "입니다.");
			System.out.println("대출 신청 내역이 존재하지 않습니다.");
		} catch (IOException e) {
			e.printStackTrace();
		}		
		System.out.println("이용해주셔서 감사합니다.");
		return;
	}
	
	/**
	 * 계좌번호 입력받아 유효한지 체크
	 */
	public Customer checkAccountNumber() {
		Scanner sc = new Scanner(System.in);
		System.out.println("========================================");
		System.out.println("계좌번호를 입력해주세요.");
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
					System.out.println("없는 계좌번호 입니다. 계좌번호를 다시 입력해주세요.");
					System.out.println("========================================");
				}
			} else {
				wrongInputMent();
			}
		}
	}

	/**
	 * 비밀번호 입력받아 유효한지 체크
	 */
	public Customer checkPassword(String inpttedAccountNumber) {
		Scanner sc = new Scanner(System.in);
		System.out.println("========================================");
		System.out.println("비밀번호를 입력해 주세요.");
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
	 * 선택한 기간에 맞는 사용내역들만 출력
	 */
	public void calculateDate(int interval, List<TransactionData> dataList) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new GregorianCalendar().getTime();

		System.out.println("========================================");
		System.out.printf("%12s \t %8s \t %18s \t %18s \t %12s", "거래일자", "사용내역", "금액", "잔액", "거래시간");
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
	
	// 출금 transaction
	public void withdrawTransaction(Atm atm, Customer savedCustomer, int inputtedAmount) {
		atm.setCash(atm.getCash() - inputtedAmount);
		atm.setBalance(atm.getBalance() - inputtedAmount);
		savedCustomer.setBalance(savedCustomer.getBalance() - inputtedAmount);
	}

	// 거래 기록
	public void recordTransaction(Customer savedCustomer, int inputtedAmount) {
		Date date = new GregorianCalendar().getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
		String transactionDate = dateFormat.format(date);
		String transactionTime = timeFormat.format(date);

		savedCustomer.getTransactionDataList().add(new TransactionData(transactionDate, "출금", inputtedAmount,
				savedCustomer.getBalance(), transactionTime));
		TransactionData.transactionDataMap.put(savedCustomer.getAccountNumber(),
				savedCustomer.getTransactionDataList());
	}

	public void showMainMenu() {
		System.out.println("========================================");
		System.out.println("이용하실 메뉴를 선택해 주세요.");
		System.out.println("1.입금       2.출금       3.송금     4.잔액조회   \n5.거래내역 조회     6.신규등록     7.대출     8.종료 ");
		System.out.println("========================================");
	}

	public void showContinueMent() {
		System.out.println("========================================");
		System.out.println("계속 거래 하시려면 1, 종료하시려면 2를 눌러주세요");
		System.out.println("1.계속거래   2.종료");
		System.out.println("========================================");
	}

	public void endMent() {
		System.out.println("========================================");
		System.out.println("거래가 종료되었습니다. \n감사합니다.");
		System.out.println("========================================");
	}

	public void wrongPasswordMent(String inpttedcustomerNumber, int i) {
		if (Check.checkTryCount(inpttedcustomerNumber, i)) {
			if (i < 5) {
				System.out.println("========================================");
				System.out.println("비밀번호가 틀립니다 다시 입력해주세요 (" + (i) + "/5)");
				System.out.println("========================================");
			} else {
				System.out.println("========================================");
				System.out.println("비밀번호 5회 오류로 거래가 정지되었습니다. \n가까운 영업점에 방문하여 주십시오.");
				System.out.println("========================================");
			}
		}
	}

	public void wrongInputMent() {
		System.out.println("========================================");
		System.out.println("입력값 오류입니다. 다시 입력해주세요.");
		System.out.println("========================================");
	}

	public void notExistMenuMent() {
		System.out.println("========================================");
		System.out.println("존재하지 않는 메뉴입니다 다시 선택해주세요");
		System.out.println("========================================");
	}

	public void rejectMent() {
		System.out.println("========================================");
		System.out.println("비밀번호 5회 오류로 정지된 계좌입니다. \n가까운 영업점에 방문하여 주십시오.");
		System.out.println("========================================");
	}
}
