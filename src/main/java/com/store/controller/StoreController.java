package com.store.controller;

import com.store.models.ProductDto;
import com.store.service.impl.ShoppingCartService;
import com.store.service.impl.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.store.utils.ApplicationConstants.SUCCESS_ADD_TO_CART;
import static com.store.utils.ApplicationConstants.SUCCESS_DELETE_FROM_CART;
import static com.store.utils.ApplicationConstants.SUCCESS_ORDER;
import static com.store.utils.ApplicationConstants.SUCCESS_QUANTITY_UPDATE;

@RestController
@Scope("session")
public class StoreController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("products")
    public ResponseEntity<Object> getAllProducts() {
        return new ResponseEntity<>(storeService.getAllProducts(), HttpStatus.OK);
    }

    @PostMapping("products")
    public Object addProductToShoppingCart(@RequestBody ProductDto productDto) {
        List<String> errors = shoppingCartService.addToCart(productDto);
        if (errors.isEmpty()) {
            return new ResponseEntity<>(SUCCESS_ADD_TO_CART, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("cart")
    public Object getShoppingCart() {
        return new ResponseEntity<>(shoppingCartService.getCart(), HttpStatus.OK);
    }

    @DeleteMapping("cart/{id}")
    public Object deleteFromShoppingCart(@PathVariable int id) {
        String response = shoppingCartService.removeFromCart(id);
        if (response.contains(SUCCESS_DELETE_FROM_CART)) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }

    @PatchMapping("cart")
    public Object updateProductQuantityInCart(@RequestBody ProductDto productDto) {
        String response = shoppingCartService.updateProductQuantity(productDto);
        if (response.contains(SUCCESS_QUANTITY_UPDATE)) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("order")
    public Object makeOrder() {
        List<String> errors = shoppingCartService.makeOrder();
        if (errors.isEmpty()) {
            return new ResponseEntity<>(SUCCESS_ORDER, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }

}
