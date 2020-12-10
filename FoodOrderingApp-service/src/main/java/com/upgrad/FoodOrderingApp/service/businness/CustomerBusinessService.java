package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.common.ValidationUtilities;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class CustomerBusinessService {

    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = SignUpRestrictedException.class)
    public CustomerEntity signup(CustomerEntity customerEntity) throws SignUpRestrictedException {
        if (!ValidationUtilities.isValidCustomer(customerEntity)) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
        }
        if (customerDao.getCustomerByContact(customerEntity.getContactNumber()) != null) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
        }
        if (!ValidationUtilities.isValidEmail(customerEntity.getEmailAddress())) {
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
        }
        if (!ValidationUtilities.isValidContact(customerEntity.getContactNumber())) {
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
        }
        if (!ValidationUtilities.isValidPassword(customerEntity.getPassword())) {
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
        }

        return customerDao.createUser(customerEntity);

    }

    /*Method to login based on username and password else will throw authentication failure exception*/
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity authenticate(String username, String password) throws AuthenticationFailedException {

        CustomerEntity customerEntity = customerDao.getCustomerByContactNumber(username);

        if (customerEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }

        final String encryptedPassword = PasswordCryptographyProvider.encrypt(password, customerEntity.getSalt());

        if (encryptedPassword.equals(customerEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
            customerAuthEntity.setUuid(UUID.randomUUID().toString());
            customerAuthEntity.setCustomer(customerEntity);
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime expiresAt = now.plusHours(8);

            customerAuthEntity.setLoginAt(ZonedDateTime.now());
            customerAuthEntity.setExpiresAt(expiresAt);
            customerAuthEntity.setAccessToken(jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt));

            return customerDao.createCustomerAuth(customerAuthEntity);
        } else {
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity logout(String accessToken) throws AuthorizationFailedException {

        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerAuthByAccessToken(accessToken);

        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }

        if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }

        ZonedDateTime now = ZonedDateTime.now();
        if (customerAuthEntity.getExpiresAt().isBefore(now)) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

        customerAuthEntity.setLogoutAt(now);
        return customerDao.updateCustomerAuth(customerAuthEntity);
    }

    /*Get the Customer with details based on access-token else will throw not authorized exception*/
    public CustomerEntity getCustomer(String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerAuthByAccessToken(accessToken);

        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }

        if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }

        ZonedDateTime now = ZonedDateTime.now();
        if (customerAuthEntity.getExpiresAt().isBefore(now)) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

        return customerAuthEntity.getCustomer();
    }


    /* Update customer details*/
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(CustomerEntity customerEntity) {
        return customerDao.updateCustomerEntity(customerEntity);
    }

    /* Update the customer password */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomerPassword(String oldPassword, String newPassword, CustomerEntity customerEntity) throws UpdateCustomerException {

        // validation for new password strength
        if (!newPassword.matches("^(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[#@$%&*!^-]).{8,}$")) {
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }

        // validation for old password
        final String oldEncryptedPassword = PasswordCryptographyProvider.encrypt(oldPassword, customerEntity.getSalt());
        if (!oldEncryptedPassword.equals(customerEntity.getPassword())) {
            throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
        }

        // encrypt salt and new password
        String[] encryptedText = passwordCryptographyProvider.encrypt(newPassword);
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);

        return customerDao.updateCustomerEntity(customerEntity);
    }

}
