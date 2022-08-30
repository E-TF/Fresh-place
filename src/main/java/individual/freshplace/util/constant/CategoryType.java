package individual.freshplace.util.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CategoryType {

    ECO_FRU("C00101", "친환경과일"),
    FRO_FRU("C00102", "건과일"),
    DOM_FRU("C00103", "국산과일"),
    IMP_FRU("C00104", "수입과일"),

    ECO_VEG("C00201", "친환경채소"),
    ROO_VEG("C00202", "뿌리채소"),
    LEF_VEG("C00203", "잎줄기채소"),
    CAB_VEG("C00204", "양채채소"),
    SCA_VEG("C00205", "비늘줄기채소"),
    FRU_VEG("C00206", "열매채소"),
    MIX_VEG("C00207", "간편채소"),
    SPR_VEG("C00208", "새싹채소"),
    MUS_VEG("C00209", "버섯채소"),

    BEE_MEA("C00301", "소고기"),
    POR_MEA("C00302", "돼지고기"),
    LAM_MEA("C00303", "양고기"),
    CHI_MEA("C00304", "닭고기"),
    DUC_MEA("C00305", "오리고기"),
    EGG_MEA("C00306", "계란"),

    WAT_DRI("C00401", "생수"),
    JUI_DRI("COO402", "쥬스"),
    SPA_DRI("C00403", "탄산음료"),
    DAI_DRI("COO4O4", "유제품"),
    COF_DRI("COO405", "커피"),
    TEA_DRI("C00406", "차"),
    ALC_DRI("C00407", "주류"),

    FIS_SEA("C00501", "생선류"),
    CEP_SEA("C00502", "두족류"),
    SHE_SEA("C00503", "갑각류"),
    CLA_SEA("C00504", "조개류"),
    WEE_SEA("C00505", "해조류"),

    SNA_SNA("C00601", "과자"),
    CHO_SNA("C00602", "초콜릿"),
    JEL_SNA("C00603", "젤리"),
    CAN_SNA("C00604", "사탕"),
    ICE_SNA("C00605", "아이스크림"),

    BRE_BAK("C00701", "빵"),
    SPR_BAK("C00702", "스프레드"),
    PIE_BAK("C00703", "파이"),
    CHE_BAK("C00704", "치즈"),

    DRE_SAU("C00801", "드레싱"),
    SOY_SAU("C00802", "장류"),
    OIL_SAU("C00803", "기름"),
    SPI_SAU("C00804", "향신료"),
    POW_SAU("C00805", "가루"),

    SER_KIT("C00901", "선식"),
    NOO_KIT("C00902", "면"),
    SOU_KIT("C00903", "스프"),
    SAL_KIT("C00904", "샐러드"),
    BOX_KIT("C00905", "도시락"),
    SID_KIT("C00906", "반찬"),
    CAN_KIT("C00907", "통조림"),
    MIL_KIT("C00908", "밀키트");

    private String codeValue;
    private String codeName;

    public static CategoryType findByCodeName(String codeName) {
        return Arrays.stream(CategoryType.values())
                .filter(categoryType -> categoryType.getCodeName().equals(codeName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
