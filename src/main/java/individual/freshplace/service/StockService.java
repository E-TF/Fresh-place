package individual.freshplace.service;

import individual.freshplace.dto.order.OrderItem;
import individual.freshplace.repository.ItemRepository;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.NonExistentException;
import individual.freshplace.util.exception.OutOfInventoryException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final ItemRepository itemRepository;

    @Transactional
    public void checkStockAndChange(final List<OrderItem> orderItems) {
        Map<Long, Long> orderItemsMap = convertListIntoMap(orderItems);
        orderItems.stream().map(orderItem -> itemRepository.findById(orderItem.getItemSeq())
                        .orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, String.valueOf(orderItem.getItemSeq()))))
                .forEach(item -> {
                    if (item.getInventory() < orderItemsMap.get(item.getItemSeq())) {
                        throw new OutOfInventoryException(ErrorCode.NO_STOCK, item.getItemName());
                    }
                    item.decreaseInventoryAndIncreaseSalesQuantity(orderItemsMap.get(item.getItemSeq()));
                });
    }

    @Transactional
    public void revertStock(final List<OrderItem> orderItems) {
        Map<Long, Long> orderItemsMap = convertListIntoMap(orderItems);
        orderItems.stream().map(orderItem -> itemRepository.findById(orderItem.getItemSeq())
                .orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, String.valueOf(orderItem.getItemSeq()))))
                        .forEach(item -> item.increaseInventoryForOrderCancel(orderItemsMap.get(item.getItemSeq())));
    }

    private Map<Long, Long> convertListIntoMap(List<OrderItem> orderItems) {
        return orderItems.stream().collect(Collectors.toMap(orderItem -> orderItem.getItemSeq(), orderItem -> orderItem.getItemCount()));
    }
}
