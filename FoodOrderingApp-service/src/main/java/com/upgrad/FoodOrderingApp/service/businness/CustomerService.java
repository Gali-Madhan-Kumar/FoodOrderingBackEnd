package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CustomerService {

    @Autowired private CustomerDao customerDao;

    @Autowired private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity saveCustomer(CustomerEntity customerEntity) throws SignUpRestrictedException {
        if (isContactNumberInUse(customerEntity.getContactNumber())) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
        }
        if (!isEmailValid(customerEntity.getEmailAddress())) {
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
        }
        if (!isValidContactNumber(customerEntity.getContactNumber())) {
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
        }
        if (!isValidPassword(customerEntity.getPassword())) {
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
        }
        customerEntity.setUuid(UUID.randomUUID().toString());
        String[] encryptedText = passwordCryptographyProvider.encrypt(customerEntity.getPassword());
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);
        return customerDao.createCustomer(customerEntity);
    }

    private boolean isContactNumberInUse(final String contactNumber) {
        return customerDao.getCustomerByContactNumber(contactNumber) != null;
    }

    private boolean isEmailValid(final String emailAddress) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(emailAddress);
    }

    private boolean isValidContactNumber(final String contactNumber) {
        if (contactNumber.length() != 10) {
            return false;
        }
        for (int i = 0; i < contactNumber.length(); i++) {
            if (Character.isLetter(contactNumber.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidPassword(final String password) {
        return password.matches("^(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[#@$%&*!^-]).{8,}$");
    }
}
