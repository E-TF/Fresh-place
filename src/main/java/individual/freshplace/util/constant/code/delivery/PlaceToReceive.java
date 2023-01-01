package individual.freshplace.util.constant.code.delivery;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PlaceToReceive {

    DIRECT("R001", "직접받기"),
    SECURITY_OFFICE("R002", "경비실"),
    COURIER_BOX("R003", "택배함");

    private String codeValue;
    private String codeName;

    public static PlaceToReceive findByCodeName(String codeName) {
        return Arrays.stream(PlaceToReceive.values())
                .filter(placeToReceive -> placeToReceive.getCodeName().equals(codeName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
