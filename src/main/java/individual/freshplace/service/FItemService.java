package individual.freshplace.service;

import individual.freshplace.dto.item.ItemResponse;
import individual.freshplace.dto.item.ItemUpdateRequest;
import individual.freshplace.entity.Image;
import individual.freshplace.entity.Item;
import individual.freshplace.util.constant.Folder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FItemService {

    private final static String SLASH = "/";

    private final ItemService itemService;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    public ItemResponse getItemDetail(final Long itemSeq) {

        Item item = itemService.findById(itemSeq);
        List<Image> images = imageService.findByImagePath(getUrlPrefix(itemSeq));

        return ItemResponse.of(item, images);
    }

    @Transactional
    public ItemResponse updateItemDetail(final ItemUpdateRequest itemUpdateRequest) {

        Item item = itemService.findById(itemUpdateRequest.getItemSeq());
        item.updateItem(itemUpdateRequest);
        List<Image> images = imageService.findByImagePath(getUrlPrefix(itemUpdateRequest.getItemSeq()));

        return ItemResponse.of(item, images);
    }

    private String getUrlPrefix(final Long itemSeq) {
        return Folder.IMAGE.getDirectoryName() + SLASH + Folder.GOODS.getDirectoryName() + SLASH + itemSeq.toString();
    }
}
