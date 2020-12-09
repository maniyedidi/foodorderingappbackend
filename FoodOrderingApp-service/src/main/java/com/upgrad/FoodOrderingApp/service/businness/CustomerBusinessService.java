package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.common.ValidationUtilities;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerBusinessService {

    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = SignUpRestrictedException.class)
    public CustomerEntity signup(CustomerEntity customerEntity) throws SignUpRestrictedException {
        try {
            if(!customerEntity.isValidCustomer()){
                throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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

}
