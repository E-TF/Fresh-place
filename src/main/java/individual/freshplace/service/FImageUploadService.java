package individual.freshplace.service;

import individual.freshplace.dto.image.ImageResizingResponse;
import individual.freshplace.entity.Image;
import individual.freshplace.util.ImageUtils;
import individual.freshplace.util.S3Utils;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.constant.Folder;
import individual.freshplace.util.constant.ImageFormat;
import individual.freshplace.util.exception.NonExistentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FImageUploadService {

    private final static String SLASH = "/";

    private final S3Utils s3Utils;
    private final ItemService itemService;
    private final ImageService imageService;

    @Transactional
    public ImageResizingResponse saveItemImage(final Long itemId, final MultipartFile multipartFile) {

        if (!itemService.existsById(itemId)) {
            throw new NonExistentException(ErrorCode.BAD_VALUE, itemId.toString());
        }

        MultipartFile resizingImage = ImageUtils.imageResize(multipartFile, ImageFormat.STANDARD.getWeight(), ImageFormat.STANDARD.getHeight());

        String originObjectUrl = s3Utils.upload(Folder.IMAGE.getDirectoryName()
                + SLASH + Folder.GOODS.getDirectoryName() + SLASH + Folder.ORIGIN.getDirectoryName(), itemId, multipartFile);

        Image image = new Image(originObjectUrl);
        imageService.save(image);

        String resizingObjectUrl = s3Utils.upload(Folder.IMAGE.getDirectoryName()
                + SLASH + Folder.GOODS.getDirectoryName(), itemId, resizingImage);

        image = new Image(resizingObjectUrl);
        imageService.save(image);

        return ImageResizingResponse.of(originObjectUrl, resizingObjectUrl);
    }
}
