package atmVer2;

public class Main {

	public static void main(String[] args) {

		Bank goodieBank = new Bank();
		Atm goodieBankAtm = new Atm(goodieBank);
		
		goodieBank.addCustomer("Ã¹Â°", "1234", 10000,"000");
		goodieBank.addCustomer("µÑÂ°", "1234", 54000,"001");
		goodieBank.addCustomer("¼ÂÂ°", "1234", 10000000,"002");
		goodieBank.addCustomer("³ÝÂ°", "1234", 520000,"003");
		goodieBank.addCustomer("´Ù¼¸Â°", "1234", 121000,"004");
		goodieBank.addCustomer("¿©¼¸Â°", "1234", 90230000,"005");
		goodieBank.addCustomer("ÀÏ°ö¤Š", "1234", 299990000,"006");
		goodieBank.addCustomer("¿©´üÂ°", "1234", 560000,"007");
		goodieBank.addCustomer("¾ÆÈ©Â°", "1234", 290000,"008");
		goodieBank.addCustomer("¿­Â°", "1234", 1040000,"009");
		
		goodieBankAtm.greeting(goodieBankAtm);
	}
	
}
