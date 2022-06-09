package woowacourse.shoppingcart.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.global.exception.InvalidCartItemException;
import woowacourse.global.exception.InvalidProductException;
import woowacourse.shoppingcart.application.dto.CartResponse;
import woowacourse.shoppingcart.application.dto.ProductResponse;
import woowacourse.shoppingcart.dao.CartItemDao;
import woowacourse.shoppingcart.domain.Cart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CartService {

    public static final int INIT_QUANTITY = 1;
    private final CartItemDao cartItemDao;
    private final CustomerService customerService;
    private final ProductService productService;

    public CartService(CartItemDao cartItemDao, CustomerService customerService, ProductService productService) {
        this.cartItemDao = cartItemDao;
        this.customerService = customerService;
        this.productService = productService;
    }

    @Transactional
    public void save(final Long customerId, final Long productId) {
        validateSave(customerId, productId);
        cartItemDao.save(customerId, productId, INIT_QUANTITY);
    }

    private void validateSave(Long customerId, Long productId) {
        customerService.findById(customerId);
        productService.findById(productId);
        validateCartInProductId(productId);
    }

    private void validateCartInProductId(Long productId) {
        if (cartItemDao.existProductId(productId)) {
            throw new InvalidCartItemException("[ERROR] 장바구니에 이미 등록된 상품입니다.");
        }
    }

    public CartResponse findById(Long id) {
        Cart cart = cartItemDao.findById(id)
                .orElseThrow(() -> new InvalidProductException("[ERROR] ID가 존재하지 않습니다."));
        ProductResponse product = productService.findById(cart.getProductId());
        return new CartResponse(cart.getId(), product.getName(), product.getPrice(), cart.getQuantity(), product.getImageUrl());
    }

    public List<CartResponse> findAll(Long customerId) {
        List<Cart> carts = cartItemDao.findAllByCustomerId(customerId);
        Map<Long, Integer> productIdByQuantity = carts.stream()
                .collect(Collectors.toMap(Cart::getProductId, Cart::getQuantity));
        List<Long> productIds = carts.stream()
                .map(it -> it.getProductId())
                .collect(Collectors.toList());
        return productService.findByIds(productIds).stream()
                .map(it -> new CartResponse(it.getId(), it.getName(), it.getPrice(), productIdByQuantity.get(it.getId()), it.getImageUrl()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateQuantity(Long customerId, long productId, int quantity) {
        cartItemDao.updateQuantity(customerId, productId, quantity);
    }

    @Transactional
    public void delete(Long customerId, Long productId) {
        cartItemDao.deleteByCustomerIdAndProductId(customerId, productId);
    }
}
