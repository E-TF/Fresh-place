package individual.freshplace.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class ConvertToMultipartFile implements MultipartFile {

    private byte[] fileBytes;
    private String originalFileName;
    private String contentType;
    private boolean isEmpty;
    private long size;

    public ConvertToMultipartFile(byte[] fileBytes, String originalFileName, String contentType, long size) {
        this.fileBytes = fileBytes;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.size = size;
        this.isEmpty = false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getOriginalFilename() {
        return originalFileName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public byte[] getBytes() {
        return fileBytes;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(fileBytes);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(fileBytes);
    }
}
