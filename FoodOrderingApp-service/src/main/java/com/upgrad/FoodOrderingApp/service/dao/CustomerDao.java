package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.businness.PasswordCryptographyProvider;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerDao {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    public CustomerEntity createUser(CustomerEntity customerEntity) {
        String[] encryptedText = passwordCryptographyProvider.encrypt(customerEntity.getPassword());
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);
        entityManager.persist(customerEntity);
        return customerEntity;
    }

    public CustomerEntity getCustomerByContact(String contactNumber) {
        try {
            return entityManager.createNamedQuery("customerByContactNumber", CustomerEntity.class).setParameter("contactNumber", contactNumber).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
