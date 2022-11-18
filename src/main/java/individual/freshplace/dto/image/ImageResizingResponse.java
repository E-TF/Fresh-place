package individual.freshplace.dto.image;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ImageResizingResponse {

    private final String originUrl;
    private final String resizingUrl;

    public static ImageResizingResponse of(final String originUrl, final String resizingUrl) {
        return new ImageResizingResponse(originUrl, resizingUrl);
    }
}
