package individual.freshplace.service;

import individual.freshplace.dto.item.ItemByCategoryResponse;
import individual.freshplace.util.constant.SubCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FCategoryService {

    private final ItemService itemService;

    @Transactional(readOnly = true)
    public List<ItemByCategoryResponse> getItems(final SubCategory subCategory, final Pageable pageable) {
        return itemService.findByCategoryType(subCategory, pageable)
                .stream().map(ItemByCategoryResponse::from).collect(Collectors.toList());
    }
}
