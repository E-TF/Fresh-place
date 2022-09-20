package individual.freshplace.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ItemUpdateRequest {

    private long itemSeq;

    private String itemName;

    private String description;

    private long weight;

    private long price;

    private String origin;

    private LocalDate expirationDate;

    private String categoryEngName;
}
