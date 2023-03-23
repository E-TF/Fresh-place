package individual.freshplace.service;

import individual.freshplace.dto.category.CategoryResponse;
import individual.freshplace.dto.item.ItemByCategoryResponse;
import individual.freshplace.repository.ImageRepository;
import individual.freshplace.repository.ItemRepository;
import individual.freshplace.util.constant.code.category.Category;
import individual.freshplace.util.constant.code.category.SubCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public List<ItemByCategoryResponse> getItems(final SubCategory subCategory, final Pageable pageable) {
        return itemRepository.findByCategoryCode(subCategory, pageable).stream()
                .map(item -> new ItemByCategoryResponse(item, imageRepository.findByImagePathContaining(ItemService
                        .getUrlPrefix(item.getItemSeq())).get(0).getImagePath())).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories() {
        return Arrays.stream(Category.values()).map(category -> new CategoryResponse(category.getCodeEngName()
                , category.getCodeKorName(), category.getSubCategoryList().stream()
                .map(subCategory -> new CategoryResponse.SubCategoryResponse(subCategory.getCodeEngName(), subCategory.getCodeKorName()))
                .collect(Collectors.toList()))).collect(Collectors.toList());
    }
}
