package individual.freshplace.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.FileUploadFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Utils {

    private final static String SLASH = "/";
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(String directoryName, Long objectName, MultipartFile multipartFile) {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        String key = createFilePath(directoryName, objectName, multipartFile.getOriginalFilename());

        try (InputStream inputStream = multipartFile.getInputStream()){
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));//새로운 개체에 대한 선택적 액세스 제어를 public read 함. ['public-read']
        } catch (IOException e) {
            throw new FileUploadFailedException(ErrorCode.FAILED_FILE_TO_STREAM);
        }

        return amazonS3Client.getUrl(bucket, key).toString();
    }

    public void delete(String filePath) {
        amazonS3Client.deleteObject(bucket, filePath);
    }

    private String createFilePath(String directoryName, Long objectName, String fileName) {
        return directoryName + SLASH + objectName.toString() + SLASH + UUID.randomUUID().toString() + fileName;
    }
}
