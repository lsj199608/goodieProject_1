package atmVer2;

import java.util.regex.Pattern;

public class Check {
	
	public static boolean checkAccountNumber(String inpttedAccountNumber) {
		for (Customer customer : Bank.customerList) {
			if(inpttedAccountNumber.equals(customer.getAccountNumber())){
				return true;
			}
		}
		return false;
	}
	
	public static Customer checkPasswordAndGetcustomerInfo(String inpttedAccountNumber, String inputtedPassword) {
		for (Customer customer : Bank.customerList) {
			if(inpttedAccountNumber.equals(customer.getAccountNumber()) && inputtedPassword.equals(customer.getPassword())) {
				Customer result = customer;
				
				return result;
			}	
		}
		return null;
	}

	public static int getTryCount(String inpttedAccountNumber) {
		for (Customer customer : Bank.customerList) {
			if(inpttedAccountNumber.equals(customer.getAccountNumber())) {
				Customer result = customer;
		
				return result.getTryCount();
			}
		}
		return 1;
	}
	
	public static boolean checkTryCount(String inpttedAccountNumber, int i) {
		for (Customer customer : Bank.customerList) {
			if(inpttedAccountNumber.equals(customer.getAccountNumber()) && customer.getTryCount() < 5) {
				Customer result = customer;
				
				result.setTryCount();
				
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkPattern(String inputtedValue) {
		if(Pattern.matches("^[0-9]*$", inputtedValue)) {
			
			return true;
		}
		return false;
	}
}