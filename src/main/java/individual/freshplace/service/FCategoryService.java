package individual.freshplace.service;

import individual.freshplace.dto.item.ItemByCategoryResponse;
import individual.freshplace.util.constant.CategoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FCategoryService {

    private final ItemService itemService;

    @Transactional(readOnly = true)
    public List<ItemByCategoryResponse> getItems(final CategoryType categoryType) {
        return itemService.findByCategoryType(categoryType)
                .stream().map(ItemByCategoryResponse::from).collect(Collectors.toList());
    }
}
