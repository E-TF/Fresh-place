package individual.freshplace.controller;

import individual.freshplace.dto.category.CategoryResponse;
import individual.freshplace.dto.item.ItemByCategoryResponse;
import individual.freshplace.dto.item.ItemResponse;
import individual.freshplace.dto.item.ItemUpdateRequest;
import individual.freshplace.service.CategoryService;
import individual.freshplace.service.ItemService;
import individual.freshplace.util.constant.Cache;
import individual.freshplace.util.constant.code.category.SubCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;

    @GetMapping("/public/category")
    @Cacheable(cacheNames = Cache.CATEGORY, key = Cache.MAIN_CATEGORY_KEY)
    public List<CategoryResponse> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/public/items/category")
    @Cacheable(cacheNames = Cache.ITEMS_BY_CATEGORY, key = "#codeEngName + (#pageable.getPageNumber() + 1)")
    public List<ItemByCategoryResponse> getItems(@RequestParam String codeEngName, @PageableDefault(size = 30) Pageable pageable) {
        SubCategory subCategory = SubCategory.findByCodeEngName(codeEngName);
        return categoryService.getItems(subCategory, pageable);
    }

    @GetMapping("/public/item/{itemSeq}")
    @Cacheable(cacheNames = Cache.ITEM, key = "#itemSeq")
    public ItemResponse getItem(@PathVariable Long itemSeq) {
        return itemService.getItemDetail(itemSeq);
    }

    @PutMapping("/admin/item")
    @CacheEvict(cacheNames = Cache.ITEMS_BY_CATEGORY, allEntries = true)
    @CachePut(cacheNames = Cache.ITEM, key = "#itemUpdateRequest.getItemSeq()")
    public ItemResponse updateItem(@RequestBody ItemUpdateRequest itemUpdateRequest) {
        return itemService.updateItemDetail(itemUpdateRequest);
    }

}
