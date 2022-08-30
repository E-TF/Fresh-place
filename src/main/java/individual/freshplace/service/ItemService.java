package individual.freshplace.service;

import individual.freshplace.entity.Item;
import individual.freshplace.repository.ItemRepository;
import individual.freshplace.util.constant.CategoryType;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.WrongValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public Item findById(final Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new WrongValueException(ErrorCode.BAD_VALUE, id.toString()));
    }

    @Transactional(readOnly = true)
    public List<Item> findByCategoryType(final CategoryType categoryType) {
        return itemRepository.findByCategoryCode(categoryType);
    }

    @Transactional(readOnly = true)
    public boolean existsById(final Long id) {
        return itemRepository.existsById(id);
    }
}
