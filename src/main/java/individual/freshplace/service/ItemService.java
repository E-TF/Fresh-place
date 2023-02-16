package individual.freshplace.service;

import individual.freshplace.dto.item.ItemResponse;
import individual.freshplace.dto.item.ItemUpdateRequest;
import individual.freshplace.entity.Image;
import individual.freshplace.entity.Item;
import individual.freshplace.repository.ImageRepository;
import individual.freshplace.repository.ItemRepository;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.constant.Folder;
import individual.freshplace.util.exception.NonExistentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final static String SLASH = "/";

    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public ItemResponse getItemDetail(final Long itemSeq) {
        final Item item = itemRepository.findById(itemSeq).orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, String.valueOf(itemSeq)));
        final List<Image> images = imageRepository.findByImagePathContaining(getUrlPrefix(itemSeq));
        return ItemResponse.of(item, images);
    }

    @Transactional
    public ItemResponse updateItemDetail(final ItemUpdateRequest itemUpdateRequest) {
        Item item = itemRepository.findById(itemUpdateRequest.getItemSeq()).orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, String.valueOf(itemUpdateRequest.getItemSeq())));
        item.updateItem(itemUpdateRequest);
        final List<Image> images = imageRepository.findByImagePathContaining(getUrlPrefix(itemUpdateRequest.getItemSeq()));
        return ItemResponse.of(item, images);
    }

    protected static String getUrlPrefix(final Long itemSeq) {
        return Folder.IMAGE.getDirectoryName() + SLASH + Folder.GOODS.getDirectoryName() + SLASH + itemSeq.toString();
    }
}
