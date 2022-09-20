package individual.freshplace.controller;

import individual.freshplace.service.FImageUploadService;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.constant.Folder;
import individual.freshplace.util.exception.EmptyFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ImageController {

    private final FImageUploadService fImageUploadService;

    @PostMapping("/image/upload")
    public ResponseEntity itemsUploadRequest(@RequestParam("objectName") Long objectName,
                                             @RequestParam("image") MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            throw new EmptyFileException(ErrorCode.FILE_ERROR, multipartFile.getName());
        }

        if(Folder.GOODS.getDirectoryName().equals(objectName)) {
            fImageUploadService.saveItemImage(objectName, multipartFile);
        }

        return ResponseEntity.ok().build();
    }
}
