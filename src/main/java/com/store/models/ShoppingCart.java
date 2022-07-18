package com.store.models;

import com.store.exception.NoRecordFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

import static com.store.utils.ApplicationConstants.NO_RECORD_ERROR;

@Getter
@Setter
@AllArgsConstructor
@Scope("session")
@Component
public class ShoppingCart {
    private List<DisplayProductDto> shoppingCart;

    public void add(DisplayProductDto productDto) {
        List<DisplayProductDto> newCart = this.getShoppingCart();
        newCart.add(productDto);
        this.setShoppingCart(newCart);
    }

    public void remove(int itemId) {
        this.getShoppingCart().remove(itemId - 1);
        IntStream.range(itemId - 1, this.getShoppingCart().size())
                 .forEach(idx -> this.getShoppingCart().get(idx).setProductIdInCartAfterRemoving(idx + 1));
    }

    public void updateQuantity(Long itemId, int newQuantity) {
        this.getShoppingCart().get(itemId.intValue() - 1).setNewQuantity(newQuantity);
    }

    public DisplayProductDto findProductFromCartById(int id) {
        return this.getShoppingCart()
                   .stream()
                   .filter(cartElement -> cartElement.getProductInCartId() == id)
                   .findFirst()
                   .orElseThrow(() -> new NoRecordFoundException(NO_RECORD_ERROR));
    }
}
