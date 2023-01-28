package individual.freshplace.util.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageFormat {

    STANDARD(550, 700, "jpg"),
    LETTERS(550, 700, "png");

    private int weight;
    private int height;
    private String extension;
}
