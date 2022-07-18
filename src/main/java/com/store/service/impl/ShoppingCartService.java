package com.store.service.impl;

import com.store.entity.ProductEntity;
import com.store.exception.NoRecordFoundException;
import com.store.models.DisplayProductDto;
import com.store.models.ProductDto;
import com.store.models.ShoppingCart;
import com.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.store.utils.ApplicationConstants.EMPTY_CART_ERROR;
import static com.store.utils.ApplicationConstants.INVALID_PARAMETER;
import static com.store.utils.ApplicationConstants.INVALID_UPDATE;
import static com.store.utils.ApplicationConstants.NO_RECORD_ERROR;
import static com.store.utils.ApplicationConstants.SUCCESS_DELETE_FROM_CART;
import static com.store.utils.ApplicationConstants.SUCCESS_QUANTITY_UPDATE;
import static com.store.utils.ApplicationConstants.WRONG_QUANTITY;

@Service
@Scope("session")
public class ShoppingCartService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ShoppingCart shoppingCart;

    public List<String> addToCart(ProductDto productDto) {
        List<String> errors = new ArrayList<>();

        if (productDto.getId() <= 0 || productDto.getQuantity() <= 0) {
            errors.add(INVALID_PARAMETER);
            return errors;
        }
        try {
            ProductEntity productEntity = productRepository.findProductEntityById(productDto.getId())
                                                           .orElseThrow(() -> new NoRecordFoundException(NO_RECORD_ERROR));

            int quantityInCart = 0;
            int productInCartId = 1;
            DisplayProductDto productInCart = null;

            if (!shoppingCart.getShoppingCart().isEmpty()) {
                productInCart = shoppingCart.getShoppingCart()
                                            .stream()
                                            .filter(cartElement -> cartElement.getEntityId().equals(productDto.getId()))
                                            .findFirst()
                                            .orElse(null);

                if (productInCart != null) {
                    quantityInCart = productInCart.getQuantity();
                    productInCartId = shoppingCart.getShoppingCart().size();
                } else {
                    productInCartId = shoppingCart.getShoppingCart().size() + 1;
                }
            }
            int currentProductAvailability = productEntity.getAvailable() - (productDto.getQuantity() + quantityInCart);
            if (currentProductAvailability < 0) {
                errors.add(WRONG_QUANTITY);
                return errors;
            }

            if (productInCart != null) {
                productInCart.increaseQuantity(productDto.getQuantity(), productEntity.getPrice());
            } else {
                productInCart = new DisplayProductDto(productEntity.getId(),
                                                      productInCartId,
                                                      productEntity.getTitle(),
                                                      productDto.getQuantity(),
                                                      setPriceToQuantity(productEntity.getPrice(),
                                                                         productDto.getQuantity()));
                shoppingCart.add(productInCart);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()); //TODO logger
        }
        return errors;
    }

    @Transactional
    public List<String> makeOrder() {
        List<String> errors = new ArrayList<>();
        List<Integer> updatedRows = new ArrayList<>();
        if (shoppingCart.getShoppingCart().isEmpty()) {
            errors.add(EMPTY_CART_ERROR);
            return errors;
        }
        try {
            shoppingCart.getShoppingCart().stream()
                        .collect(Collectors.toMap(DisplayProductDto::getEntityId,
                                                  DisplayProductDto::getQuantity))
                        .forEach((id, quantity) -> updatedRows.add(productRepository.updateProductAvailability(id,
                                                                                                               quantity)));

            boolean isUpdateFailed = updatedRows.stream().anyMatch(updatedRow -> updatedRow <= 0);
            if (isUpdateFailed) {
                errors.add(INVALID_UPDATE);
                throw new SQLDataException();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()); //TODO logger
        }
        shoppingCart = new ShoppingCart(Collections.emptyList());
        return errors;
    }

    public ShoppingCart getCart() {
        return shoppingCart;
    }

    public String removeFromCart(int itemId) {
        if (itemId <= 0) {
            return INVALID_PARAMETER;
        }
        shoppingCart.findProductFromCartById(itemId);
        shoppingCart.remove(itemId);
        return SUCCESS_DELETE_FROM_CART;
    }

    public String updateProductQuantity(ProductDto productDto) {
        if (productDto.getId() <= 0 || productDto.getQuantity() <= 0) {
            return INVALID_PARAMETER;
        }
        shoppingCart.findProductFromCartById(productDto.getId().intValue());
        shoppingCart.updateQuantity(productDto.getId(), productDto.getQuantity());
        return SUCCESS_QUANTITY_UPDATE;
    }

    private BigDecimal setPriceToQuantity(BigDecimal productPrice, int currentProductQuantity) {
        return productPrice.multiply(BigDecimal.valueOf(currentProductQuantity));
    }
}
