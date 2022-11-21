package individual.freshplace.util.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Membership {

    GENERAL("G001", "일반회원", 7),
    ELITE("G002", "우수회원", 10),
    VIP("G003", "VIP회원", 15),
    VVIP("G004", "VVIP회원", 19);

    private String codeValue;
    private String codeName;
    private long discountRate;

    public static Membership findByCodeName(String codeName) {
        return Arrays.stream(Membership.values())
                .filter(membership -> membership.getCodeName().equals(codeName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
