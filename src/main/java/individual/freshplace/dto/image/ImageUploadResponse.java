package individual.freshplace.dto.image;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ImageUploadResponse {

    private final String originUrl;

    public static ImageUploadResponse from(final String originUrl) {
        return new ImageUploadResponse(originUrl);
    }
}
