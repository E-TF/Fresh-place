package individual.freshplace.util;

import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.constant.ImageFormat;
import individual.freshplace.util.exception.FileConvertFiledException;
import individual.freshplace.util.exception.FileUploadFailedException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

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
            throw new FileUploadFailedException(ErrorCode.FAILED_FILE_TO_STREAM, multipartFile.getOriginalFilename());
        } catch (InterruptedException e) {
            throw new FileUploadFailedException(ErrorCode.FAILED_PIXEL_GRABBER, multipartFile.getOriginalFilename());
        }
    }

    public static MultipartFile pngToJpeg(MultipartFile multipartFile) {

        try (InputStream inputStream = multipartFile.getInputStream()){
            File next = new File(findTitleByFileName(multipartFile.getOriginalFilename()) + "." + ImageFormat.STANDARD.getExtension());
            BufferedImage initBufferedImage = ImageIO.read(inputStream);
            BufferedImage convertBufferedImage  = new BufferedImage(initBufferedImage.getWidth(), initBufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            convertBufferedImage.createGraphics().drawImage(initBufferedImage, 0, 0, Color.white, null);
            ImageIO.write(convertBufferedImage, ImageFormat.STANDARD.getExtension(), next);
            FileItem fileItem = new DiskFileItem(next.getName(), Files.probeContentType(next.toPath()), false, next.getName(), (int) next.length(), next.getParentFile());
            InputStream fileInputStream = new FileInputStream(next);
            OutputStream outputStream = fileItem.getOutputStream();
            IOUtils.copy(fileInputStream, outputStream);
            return new CommonsMultipartFile(fileItem);

        } catch (IOException e) {
            throw new FileConvertFiledException(ErrorCode.FAILED_FILE_TO_STREAM, multipartFile.getOriginalFilename());
        }


    }

    private static String findExtensionByFileName(String fileName) {
        String[] split = fileName.split("\\.");
        return split[split.length-1];
    }

    private static String findTitleByFileName(String fileName) {
        String[] split = fileName.split("\\.");
        return Arrays.stream(split).limit(split.length-1).reduce(String::concat).get();
    }
}
