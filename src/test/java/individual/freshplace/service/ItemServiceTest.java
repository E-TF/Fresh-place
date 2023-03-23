package individual.freshplace.service;

import individual.freshplace.util.constant.Folder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Test
    @DisplayName("이미지 경로가 포맷함수 값과 일치하는지 확인")
    void checkingImageUrlPathEqualsFormatValue() {

        final Long itemSeq = 1L;
        final String expectedUrlPrefix = Folder.IMAGE.getDirectoryName() + "/" + Folder.GOODS.getDirectoryName()
                + "/" + itemSeq.toString() + "/" + Folder.RESIZE.getDirectoryName();
        final String actualUrlPrefix =
                String.format("%s/%s/%s/%s",
                Folder.IMAGE.getDirectoryName(), Folder.GOODS.getDirectoryName(),itemSeq.toString(), Folder.RESIZE.getDirectoryName());

        Assertions.assertEquals(expectedUrlPrefix, actualUrlPrefix);
    }
}
