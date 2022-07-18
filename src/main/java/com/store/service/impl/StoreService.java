package com.store.service.impl;

import com.store.entity.ProductEntity;
import com.store.exception.NoRecordFoundException;
import com.store.repository.ProductRepository;
import com.store.service.IStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.store.utils.ApplicationConstants.NO_RECORD_ERROR;

@Service
public class StoreService implements IStoreService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductEntity> getAllProducts() {
        List<ProductEntity> productList = productRepository.findAll();
        if (productList.isEmpty()) {
            throw new NoRecordFoundException(NO_RECORD_ERROR);
        }
        return productList;
    }
}
