package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired private RestaurantService restaurantService;

    @Autowired private CategoryService categoryService;

    @CrossOrigin
    @RequestMapping(path = "/restaurant", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants() {

        final List<RestaurantEntity> restaurantEntityList = restaurantService.restaurantsByRating();

        final RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        for (RestaurantEntity restaurantEntity : restaurantEntityList) {

            final StateEntity stateEntity = new StateEntity();
            stateEntity.setUuid(restaurantEntity.getAddress().getState().getUuid());
            stateEntity.setStateName(restaurantEntity.getAddress().getState().getStateName());

            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                    .id(UUID.fromString(stateEntity.getUuid()))
                    .stateName(stateEntity.getStateName());

            final RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                    .id(UUID.fromString(restaurantEntity.getAddress().getUuid()))
                    .city(restaurantEntity.getAddress().getCity())
                    .flatBuildingName(restaurantEntity.getAddress().getFlatBuildingNumber())
                    .locality(restaurantEntity.getAddress().getLocality())
                    .pincode(restaurantEntity.getAddress().getPincode())
                    .state(restaurantDetailsResponseAddressState);

            final List<CategoryEntity> categoryEntityList = categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid());

            StringBuilder stringBuilder = new StringBuilder();
            
            for (CategoryEntity categoryEntity : categoryEntityList) {
                stringBuilder.append(categoryEntity.getCategoryName()).append(", ");
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            }

            final RestaurantList restaurantList = new RestaurantList()
                    .id(UUID.fromString(restaurantEntity.getUuid()))
                    .restaurantName(restaurantEntity.getRestaurantName())
                    .numberCustomersRated(restaurantEntity.getNumberOfCustomersRated())
                    .averagePrice(restaurantEntity.getAveragePriceForTwo())
                    .customerRating(restaurantEntity.getCustomerRating())
                    .photoURL(restaurantEntity.getPhotoUrl())
                    .address(restaurantDetailsResponseAddress)
                    .categories(stringBuilder.toString());
            restaurantListResponse.addRestaurantsItem(restaurantList);
        }
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }
}
