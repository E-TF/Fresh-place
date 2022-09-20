package individual.freshplace.service;

import individual.freshplace.entity.Item;
import individual.freshplace.repository.ItemRepository;
import individual.freshplace.util.constant.SubCategory;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.NonExistentException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public Item findById(final Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, id.toString()));
    }

    @Transactional(readOnly = true)
    public Page<Item> findByCategoryType(final SubCategory subCategory, final Pageable pageable) {
        return itemRepository.findByCategoryCode(subCategory, pageable);
    }

    @Transactional(readOnly = true)
    public boolean existsById(final Long id) {
        return itemRepository.existsById(id);
    }
}
