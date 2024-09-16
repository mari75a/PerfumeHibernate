
package model;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class Validation {
    
    public static boolean isEmailValid(String email){
        return email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }
    
    public static boolean isPasswordValid(String password){
    
    return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$");
    }
    public static boolean isMobileInvalid(String mobile){
        return !mobile.matches("07[01245678]{1}[0-9]{7}");
    }
    
    public static String getCode(){
        try {
            javax.crypto.KeyGenerator keygen = javax.crypto.KeyGenerator.getInstance("DES");
            SecureRandom sr = new SecureRandom();
            keygen.init(sr);
            return keygen.generateKey().getEncoded().toString().split("@")[1].substring(0, 6);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
    
    public static boolean isPriceInvalid(String price){
        return !price.matches("^\\d+(\\.\\d{2})?$");
    }
    
    public static boolean isNotInteger(String quantity){
        return !quantity.matches("^\\d+$");
    }
}
