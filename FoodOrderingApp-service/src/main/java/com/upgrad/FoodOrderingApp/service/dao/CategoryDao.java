package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryDao {

    @PersistenceContext private EntityManager entityManager;

    public List<CategoryEntity> getCategoriesByRestaurant(final String restaurantId) {
        try {
            return entityManager.createNamedQuery("getCategoriesByRestaurant", CategoryEntity.class).setParameter("restaurantId", restaurantId).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
