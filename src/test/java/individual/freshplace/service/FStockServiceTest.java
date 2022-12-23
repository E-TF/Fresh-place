package individual.freshplace.service;

import individual.freshplace.dto.order.OrderItem;
import individual.freshplace.entity.Item;
import individual.freshplace.util.constant.code.category.SubCategory;
import individual.freshplace.util.exception.OutOfInventoryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FStockServiceTest {

    @InjectMocks
    private FStockService fStockService;

    @Mock
    private ItemService itemService;

    @Test
    @DisplayName("주문아이템을 받으면 아이템 재고가 감소하고 판매량이 증가한다.")
    void DecreaseInventoryAndIncreaseSalesQuantityAfterGetOrderItems() {

        //given
        Item item = item();
        final long purchaseAmount = 3L;
        List<OrderItem> orderItems = List.of(orderItem(item.getItemSeq(), item.getItemName(), purchaseAmount, purchaseAmount * item.getPrice()));
        given(itemService.getById(anyLong())).willReturn(item);

        //when
        fStockService.stockCheckAndChange(orderItems);
        Item changedItem = itemService.getById(item.getItemSeq());

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(changedItem.getInventory(), item.getInventory() - purchaseAmount),
                () -> Assertions.assertEquals(changedItem.getSalesQuantity(), item.getSalesQuantity() + purchaseAmount));
    }

    @Test
    @DisplayName("구매량보다 재고가 적을 경우 예외가 발생한다.")
    void ThrowExceptionWhenInventoryIsLessThenPurchaseAmount() {

        //given
        Item item = item();
        final long purchaseAmount = item.getInventory() + 1;
        List<OrderItem> orderItems = List.of(orderItem(item.getItemSeq(), item.getItemName(), purchaseAmount, purchaseAmount * item.getPrice()));
        given(itemService.getById(anyLong())).willReturn(item);

        //when && then
        Assertions.assertThrowsExactly(OutOfInventoryException.class, () -> fStockService.stockCheckAndChange(orderItems));
    }

    private Item item() {
        return Item.builder().itemSeq(1L).itemName("커피").description("향긋한 원두커피").price(3900).inventory(2000)
                .origin("원두:미국산").subCategory(SubCategory.COF_DRI).build();
    }

    private OrderItem orderItem(long itemSeq, String itemName, long itemCount, long totalPrice) {
        return new OrderItem(itemSeq, itemName, itemCount, totalPrice);
    }
}
