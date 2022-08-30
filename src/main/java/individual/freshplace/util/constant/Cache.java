package individual.freshplace.util.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Cache {

    GRADE("grade"),
    CATEGORY("category"),
    SUB_CATEGORY("subCategory"),
    THUMBNAIL("thumbNail");

    private String cacheName;
}
