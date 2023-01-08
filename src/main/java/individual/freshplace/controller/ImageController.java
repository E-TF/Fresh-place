package individual.freshplace.controller;

import individual.freshplace.dto.image.ImageUploadResponse;
import individual.freshplace.service.ImageUploadService;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.EmptyFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageUploadService imageUploadService;

    @PostMapping("/admin/image/upload")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ImageUploadResponse itemsUploadRequest(@RequestParam("objectName") Long objectName,
                                                                  @RequestParam("image") MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            throw new EmptyFileException(ErrorCode.FILE_ERROR, multipartFile.getName());
        }

        return imageUploadService.saveItemImage(objectName, multipartFile);
    }
}
