package individual.freshplace.controller;

import individual.freshplace.dto.image.ImageResizingResponse;
import individual.freshplace.service.FImageUploadService;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.EmptyFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ImageController {

    private final FImageUploadService fImageUploadService;

    @PostMapping("/image/upload")
    public ResponseEntity<ImageResizingResponse> itemsUploadRequest(@RequestParam("objectName") Long objectName,
                                                                    @RequestParam("image") MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            throw new EmptyFileException(ErrorCode.FILE_ERROR, multipartFile.getName());
        }

        return ResponseEntity.ok().body(fImageUploadService.saveItemImage(objectName, multipartFile));
    }
}
