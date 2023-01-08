package individual.freshplace.service;

import individual.freshplace.dto.image.ImageUploadResponse;
import individual.freshplace.entity.Image;
import individual.freshplace.repository.ImageRepository;
import individual.freshplace.repository.ItemRepository;
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
public class ImageUploadService {

    private final static String SLASH = "/";
    private final S3Uploader s3Uploader;
    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public ImageUploadResponse saveItemImage(final Long itemId, final MultipartFile multipartFile) {

        if (!itemRepository.existsById(itemId)) {
            throw new NonExistentException(ErrorCode.BAD_VALUE, itemId.toString());
        }

        resizingUpload(itemId, multipartFile);
        return new ImageUploadResponse(originalUpload(itemId, multipartFile));
    }

    private void resizingUpload(final Long itemId, final MultipartFile multipartFile) {
        CompletableFuture
                .supplyAsync(() -> multipartFile.getContentType().equals(Folder.IMAGE.getDirectoryName() + SLASH + ImageFormat.LETTERS.getExtension()) ? ImageUtils.pngToJpeg(multipartFile) : multipartFile)
                .thenApply((file) -> ImageUtils.imageResize(file, ImageFormat.STANDARD.getWeight(), ImageFormat.STANDARD.getHeight()))
                .thenApply((resizingImage) -> s3Uploader.upload(getDirectoryName(), itemId, Folder.RESIZE.getDirectoryName(), resizingImage))
                .thenAccept((resizingItemImageUrl) -> createItemEntityAndInsert(resizingItemImageUrl));
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
        final Image image = Image.builder().imagePath(url).build();
        imageRepository.save(image);
    }
}
