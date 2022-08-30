package individual.freshplace.service;

import individual.freshplace.dto.item.ItemResponse;
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

    private final ItemService itemService;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    public ItemResponse getItemDetail(final Long itemSeq) {
        Item item = itemService.findById(itemSeq);
        List<Image> images = imageService.findByImagePath(Folder.IMAGE.getDirectoryName().concat("/")
                .concat(Folder.GOODS.getDirectoryName()).concat("/").concat(itemSeq.toString()));
        return ItemResponse.from(item, images);
    }
}
