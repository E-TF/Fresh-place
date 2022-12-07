package individual.freshplace.controller;

import individual.freshplace.dto.item.ItemByCategoryResponse;
import individual.freshplace.dto.item.ItemResponse;
import individual.freshplace.dto.item.ItemUpdateRequest;
import individual.freshplace.service.FCategoryService;
import individual.freshplace.service.FItemService;
import individual.freshplace.util.constant.Cache;
import individual.freshplace.util.constant.code.category.Category;
import individual.freshplace.util.constant.code.category.SubCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final FItemService fItemService;
    private final FCategoryService fCategoryService;

    @GetMapping("/public/category")
    @Cacheable(cacheNames = Cache.CATEGORY, key = Cache.MAIN_CATEGORY_KEY)
    public List<String> getCategory() {
        return Arrays.stream(Category.values()).map(Category::getCodeKorName).collect(Collectors.toList());
    }

    @GetMapping("/public/category/{codeEngName}")
    @Cacheable(cacheNames = Cache.SUB_CATEGORY, key = "#codeEngName")
    public List<String> getCategories(@PathVariable String codeEngName) {
        return Category.findByCodeEngName(codeEngName).stream().map(SubCategory::getCodeKorName).collect(Collectors.toList());
    }

    @GetMapping("/public/items/category")
    @Cacheable(cacheNames = Cache.ITEMS_BY_CATEGORY, key = "#codeEngName + (#pageable.getPageNumber() + 1)")
    public List<ItemByCategoryResponse> getItems(@RequestParam String codeEngName, @PageableDefault(size = 1) Pageable pageable) {
        SubCategory subCategory = SubCategory.findByCodeEngName(codeEngName);
        return fCategoryService.getItems(subCategory, pageable);
    }

    @GetMapping("/public/item/{itemSeq}")
    @Cacheable(cacheNames = Cache.ITEM, key = "#itemSeq")
    public ItemResponse getItem(@PathVariable Long itemSeq) {
        return fItemService.getItemDetail(itemSeq);
    }

    @PutMapping("/admin/item")
    @CacheEvict(cacheNames = Cache.ITEMS_BY_CATEGORY, allEntries = true)
    @CachePut(cacheNames = Cache.ITEM, key = "#itemUpdateRequest.getItemSeq()")
    public ItemResponse updateItem(@RequestBody ItemUpdateRequest itemUpdateRequest) {
        return fItemService.updateItemDetail(itemUpdateRequest);
    }

}
