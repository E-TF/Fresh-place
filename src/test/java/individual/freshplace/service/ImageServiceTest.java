package individual.freshplace.service;

import individual.freshplace.util.ImageUtils;
import individual.freshplace.util.constant.ImageFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Test
    @DisplayName("파일사이즈가 주어지는 크기만큼 줄어드는지 확인")
    void multipartFileResizeChecking() throws IOException {
        //given
        MockMultipartFile multipartFile = multipartFile();

        //when
        MultipartFile resizeFile = ImageUtils.imageResize(multipartFile, ImageFormat.STANDARD.getWeight(), ImageFormat.STANDARD.getHeight());
        BufferedImage resizeFileBufferedImage = ImageIO.read(resizeFile.getInputStream());
        int resizeFileWidthSize = resizeFileBufferedImage.getWidth();
        int resizeFileHeightSize = resizeFileBufferedImage.getHeight();

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(ImageFormat.STANDARD.getWeight(), resizeFileWidthSize),
                () -> Assertions.assertEquals(ImageFormat.STANDARD.getHeight(), resizeFileHeightSize));
    }

    private MockMultipartFile multipartFile() throws IOException {
        return new MockMultipartFile("image",
                "coffee.jpg",
                "image/jpeg",
                new FileInputStream("C:\\upload\\coffee.jpg"));
    }
}
