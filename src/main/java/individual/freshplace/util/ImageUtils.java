package individual.freshplace.util;

import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.FileUploadFailedException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    public static MultipartFile imageResize(MultipartFile multipartFile, int weight, int height) {

        try (InputStream inputStream = multipartFile.getInputStream()){
            BufferedImage fileImage = ImageIO.read(inputStream);
            Image scaledInstance = fileImage.getScaledInstance(weight, height, Image.SCALE_SMOOTH);

            int[] pixels = new int[weight * height];
            PixelGrabber pixelGrabber = new PixelGrabber(scaledInstance, 0, 0, weight, height, pixels, 0, weight);
            pixelGrabber.grabPixels();

            BufferedImage bufferedImage = new BufferedImage(weight, height, BufferedImage.TYPE_INT_BGR);
            bufferedImage.setRGB(0, 0, weight, height, pixels, 0, weight);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, findExtensionByFileName(multipartFile.getOriginalFilename()), byteArrayOutputStream);

            byte[] imageByte = byteArrayOutputStream.toByteArray();

            return new ConvertToMultipartFile(imageByte, multipartFile.getOriginalFilename(), multipartFile.getContentType(), imageByte.length);

        } catch (IOException e) {
            throw new FileUploadFailedException(ErrorCode.FAILED_FILE_TO_STREAM);
        } catch (InterruptedException e) {
            throw new FileUploadFailedException(ErrorCode.FAILED_PIXEL_GRABBER);
        }
    }

    private static String findExtensionByFileName(String fileName) {
        String[] split = fileName.split("\\.");
        return split[split.length-1];
    }
}
