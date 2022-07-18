package com.store.service;

import com.store.entity.ProductEntity;

import java.util.List;

public interface IStoreService {
    List<ProductEntity> getAllProducts();
}
