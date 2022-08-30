package individual.freshplace.controller;

import individual.freshplace.dto.item.ItemByCategoryResponse;
import individual.freshplace.dto.item.ItemResponse;
import individual.freshplace.service.FCategoryService;
import individual.freshplace.service.FImageUploadService;
import individual.freshplace.service.FItemService;
import individual.freshplace.util.constant.Category;
import individual.freshplace.util.constant.CategoryType;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.EmptyFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final FItemService fItemService;
    private final FCategoryService fCategoryService;
    private final FImageUploadService fImageUploadService;

    @GetMapping("/category")
    @Cacheable(value = "category")
    public List<String> getCategory() {
        return Arrays.stream(Category.values()).map(Category::getCodeTypeName).collect(Collectors.toList());
    }

    @GetMapping("/category/{codeTypeName}")
    @Cacheable(value = "subCategory", key = "#codeTypeName")
    public List<String> getCategories(@PathVariable String codeTypeName) {
        return Category.findByCodeTypeName(codeTypeName).stream().map(CategoryType::getCodeName).collect(Collectors.toList());
    }

    @GetMapping("/items/category/{codeName}")
    public ResponseEntity<List<ItemByCategoryResponse>> getItems(@PathVariable String codeName) {
        CategoryType categoryType = CategoryType.findByCodeName(codeName);
        return ResponseEntity.ok().body(fCategoryService.getItems(categoryType));
    }

    @GetMapping("/items/{itemSeq}")
    public ResponseEntity<ItemResponse> getItem(@PathVariable Long itemSeq) {
        return ResponseEntity.ok().body(fItemService.getItemDetail(itemSeq));
    }

    @PostMapping("/items/image/upload")
    public ResponseEntity itemsUploadRequest(@RequestParam("objectName") Long objectName,
                                             @RequestParam("image") MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            throw new EmptyFileException(ErrorCode.FILE_ERROR, multipartFile.getName());
        }
        fImageUploadService.saveItemImage(objectName, multipartFile);
        return ResponseEntity.ok().build();
    }
}
