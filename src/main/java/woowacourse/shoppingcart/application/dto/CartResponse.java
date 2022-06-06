package woowacourse.shoppingcart.application.dto;

public class CartResponse {

    Long id;
    String name;
    int price;
    int quantity;
    String imageUrl;

    public CartResponse() {
    }

    public CartResponse(Long id, String name, int price, int quantity, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
