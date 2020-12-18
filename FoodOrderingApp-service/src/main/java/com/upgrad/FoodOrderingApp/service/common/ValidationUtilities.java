package com.upgrad.FoodOrderingApp.service.common;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtilities {

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean isValidContact(String contactNumber) {
        String contactRegex = "^[0-9]{10}$";

        Pattern pat = Pattern.compile(contactRegex);
        if (contactNumber == null)
            return false;
        return pat.matcher(contactNumber).matches();
    }


    public static boolean isValidPassword(String password) {

        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the password is empty
        // return false
        if (password == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);

        // Return if the password
        // matched the ReGex
        return m.matches();
    }

    public static boolean isValidCustomer(CustomerEntity customerEntity) {
        if (customerEntity.getFirstName() == null || customerEntity.getContactNumber() == null || customerEntity.getEmailAddress() == null || customerEntity.getPassword() == null) {
            return false;
        } else if (customerEntity.getFirstName().equals("") || customerEntity.getContactNumber().equals("") || customerEntity.getEmailAddress().equals("") || customerEntity.getPassword().equals("")) {
            return false;
        }
        return true;
    }
}
