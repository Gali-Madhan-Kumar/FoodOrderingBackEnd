package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressService {

    @Autowired private StateDao stateDao;

    @Autowired private AddressDao addressDao;

    @Autowired private CustomerAddressDao customerAddressDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress(final AddressEntity addressEntity, final CustomerEntity customerEntity) throws SaveAddressException {

        if (addressEntity.getActive() == null || addressEntity.getLocality().equals("") ||
            addressEntity.getCity().equals("") || addressEntity.getFlatBuildingNumber().equals("") ||
            addressEntity.getPincode().equals("") || addressEntity.getState() == null) {
            throw new SaveAddressException("SAR-001", "No field can be empty");
        }
        if (!isValidPinCode(addressEntity.getPincode())) {
            throw new SaveAddressException("SAR-002", "Invalid pincode");
        }
        AddressEntity createdCustomerAddress = addressDao.createCustomerAddress(addressEntity);

        CustomerAddressEntity createdCustomerAddressEntity = new CustomerAddressEntity();
        createdCustomerAddressEntity.setCustomerId(customerEntity.getId());
        createdCustomerAddressEntity.setAddressId(createdCustomerAddress.getId());
        customerAddressDao.createCustomerAddress(createdCustomerAddressEntity);
        return createdCustomerAddress;
    }

    public StateEntity getStateByUUID(final String stateUuid) throws AddressNotFoundException {
        if (stateDao.getStateByUUID(stateUuid) == null) {
            throw new AddressNotFoundException("ANF-002", "No state by this id");
        }
        return stateDao.getStateByUUID(stateUuid);
    }

    private boolean isValidPinCode(final String pincode) {
        if (pincode.length() != 6) {
            return false;
        }
        for (int i = 0; i < pincode.length(); i++) {
            if (!Character.isDigit(pincode.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
