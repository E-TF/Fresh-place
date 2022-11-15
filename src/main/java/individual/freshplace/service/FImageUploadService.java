package individual.freshplace.service;

import individual.freshplace.dto.image.ImageUploadResponse;
import individual.freshplace.entity.Image;
import individual.freshplace.util.ImageUtils;
import individual.freshplace.util.S3Uploader;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.constant.Folder;
import individual.freshplace.util.constant.ImageFormat;
import individual.freshplace.util.exception.NonExistentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FImageUploadService {

    private final static String SLASH = "/";
    private final S3Uploader s3Uploader;
    private final ItemService itemService;
    private final ImageService imageService;

    @Transactional
    public ImageUploadResponse saveItemImage(final Long itemId, final MultipartFile multipartFile) {

        if (!itemService.existsById(itemId)) {
            throw new NonExistentException(ErrorCode.BAD_VALUE, itemId.toString());
        }

        resizingUpload(itemId, multipartFile);
        return ImageUploadResponse.from(originalUpload(itemId, multipartFile));
    }

    private void resizingUpload(final Long itemId, final MultipartFile multipartFile) {
        CompletableFuture.runAsync(() -> {
            MultipartFile resizingImage = ImageUtils.imageResize(multipartFile, ImageFormat.STANDARD.getWeight(), ImageFormat.STANDARD.getHeight());
            String resizingItemImageUrl = s3Uploader.upload(getDirectoryName(), itemId, Folder.RESIZE.getDirectoryName(), resizingImage);
            createItemEntityAndInsert(resizingItemImageUrl);
        });
    }

    private String originalUpload(final Long itemId, final MultipartFile multipartFile) {
        String originalItemImageUrl = s3Uploader.upload(getDirectoryName(), itemId, Folder.ORIGIN.getDirectoryName(), multipartFile);
        createItemEntityAndInsert(originalItemImageUrl);
        return originalItemImageUrl;
    }

    private String getDirectoryName() {
        return Folder.IMAGE.getDirectoryName() + SLASH + Folder.GOODS.getDirectoryName();
    }

    private void createItemEntityAndInsert(final String url) {
        Image image = Image.builder().imagePath(url).build();
        imageService.save(image);
    }
}
