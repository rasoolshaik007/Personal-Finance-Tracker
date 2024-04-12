package edu.nwmissouri.personalfinancetracker.helper;

public class Utils {
    public static boolean isValidEmail(String emailId) {
        String regexPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return emailId.matches(regexPattern);
    }
    public static boolean isValidPassword(String password) {
        String regexPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,16}$";
        return password.matches(regexPattern);
    }
}