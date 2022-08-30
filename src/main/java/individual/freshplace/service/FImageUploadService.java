package individual.freshplace.service;

import individual.freshplace.entity.Image;
import individual.freshplace.util.ImageUtil;
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

    private final S3Service s3Service;
    private final ItemService itemService;
    private final ImageService imageService;

    @Transactional
    public void saveItemImage(final Long id, final MultipartFile multipartFile) {

        if (!itemService.existsById(id)) {
            throw new NonExistentException(ErrorCode.BAD_VALUE, id.toString());
        }

        MultipartFile resizingImage = ImageUtil.imageResize(multipartFile, ImageFormat.STANDARD.getWeight(), ImageFormat.STANDARD.getHeight());

        String originObjectUrl = s3Service.upload(Folder.IMAGE.getDirectoryName()
                .concat("/").concat(Folder.GOODS.getDirectoryName().concat("/").concat(Folder.ORIGIN.getDirectoryName())), id, multipartFile);

        Image image = new Image(originObjectUrl);
        imageService.save(image);

        String resizingObjectUrl = s3Service.upload(Folder.IMAGE.getDirectoryName()
                .concat("/").concat(Folder.GOODS.getDirectoryName()), id, resizingImage);

        image = new Image(resizingObjectUrl);
        imageService.save(image);
    }
}
