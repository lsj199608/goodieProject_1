package atmVer2;

public class Main {

	public static void main(String[] args) {

		Bank goodieBank = new Bank();
		Atm goodieBankAtm = new Atm(goodieBank);
		
		goodieBank.addCustomer("ù°", "1234", 10000,"000");
		goodieBank.addCustomer("��°", "1234", 54000,"001");
		goodieBank.addCustomer("��°", "1234", 10000000,"002");
		goodieBank.addCustomer("��°", "1234", 520000,"003");
		goodieBank.addCustomer("�ټ�°", "1234", 121000,"004");
		goodieBank.addCustomer("����°", "1234", 90230000,"005");
		goodieBank.addCustomer("�ϰ���", "1234", 299990000,"006");
		goodieBank.addCustomer("����°", "1234", 560000,"007");
		goodieBank.addCustomer("��ȩ°", "1234", 290000,"008");
		goodieBank.addCustomer("��°", "1234", 1040000,"009");
		
		goodieBankAtm.greeting(goodieBankAtm);
	}
	
}
