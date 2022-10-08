package individual.freshplace.util.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Folder {

    IMAGE("image"),
    GOODS("goods"),
    REVIEW("review"),
    ORIGIN("origin");

    private String directoryName;
}
