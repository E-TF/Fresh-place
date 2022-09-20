package individual.freshplace.util.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SubCategory {

    ECO_FRU("C00101", "ecoFriendlyFruit", "친환경과일"),
    FRO_FRU("C00102", "driedFruit", "건과일"),
    DOM_FRU("C00103", "koreanFruit", "국산과일"),
    IMP_FRU("C00104", "importedFruit", "수입과일"),

    ECO_VEG("C00201", "ecoFriendlyFruitVegetable", "친환경채소"),
    ROO_VEG("C00202", "rootVegetable", "뿌리채소"),
    LEF_VEG("C00203", "leafVegetable", "잎줄기채소"),
    CAB_VEG("C00204", "CabbageVegetable", "양채채소"),
    SCA_VEG("C00205", "scalesVegetable", "비늘줄기채소"),
    FRU_VEG("C00206", "fruitVegetables", "열매채소"),
    MIX_VEG("C00207", "mixVegetable", "간편채소"),
    SPR_VEG("C00208", "sproutVegetable", "새싹채소"),
    MUS_VEG("C00209", "mushroomVegetable", "버섯채소"),

    BEE_MEA("C00301", "beefMeat", "소고기"),
    POR_MEA("C00302", "porkMeat", "돼지고기"),
    LAM_MEA("C00303", "lambMeat", "양고기"),
    CHI_MEA("C00304", "chickenMeat", "닭고기"),
    DUC_MEA("C00305", "duckMeat", "오리고기"),
    EGG_MEA("C00306", "eggMeat", "계란"),

    WAT_DRI("C00401", "waterDrink", "생수"),
    JUI_DRI("COO402", "juiceDrink", "쥬스"),
    SPA_DRI("C00403", "sparklingDrink", "탄산음료"),
    DAI_DRI("COO4O4", "dairyDrink", "유제품"),
    COF_DRI("COO405", "coffeeDrink", "커피"),
    TEA_DRI("C00406", "teaDrink", "차"),
    ALC_DRI("C00407", "alcoholDrink", "주류"),

    FIS_SEA("C00501", "fishSeafood", "생선류"),
    CEP_SEA("C00502", "cephalopodSeafood", "두족류"),
    CLA_SEA("C00503", "clamSeafood", "조개류"),
    CRU_SEA("C00504", "crustaceanSeafood", "갑각류"),
    WEE_SEA("C00505", "seaweedSeafood", "해조류"),

    SNA_SNA("C00601", "snack", "과자"),
    CHO_SNA("C00602", "chocolate", "초콜릿"),
    JEL_SNA("C00603", "jelly", "젤리"),
    CAN_SNA("C00604", "candy", "사탕"),
    ICE_SNA("C00605", "iceCream", "아이스크림"),

    BRE_BAK("C00701", "bread", "빵"),
    SPR_BAK("C00702", "spread", "스프레드"),
    PIE_BAK("C00703", "pie", "파이"),
    CHE_BAK("C00704", "cheese", "치즈"),

    DRE_SAU("C00801", "dressing", "드레싱"),
    SOY_SAU("C00802", "soySauce", "장류"),
    OIL_SAU("C00803", "oil", "기름"),
    SPI_SAU("C00804", "spice", "향신료"),
    POW_SAU("C00805", "powder", "가루"),

    SER_KIT("C00901", "serial", "선식"),
    NOO_KIT("C00902", "noodle", "면"),
    SOU_KIT("C00903", "soup", "스프"),
    SAL_KIT("C00904", "salad", "샐러드"),
    BOX_KIT("C00905", "lunchBox", "도시락"),
    SID_KIT("C00906", "sideDish", "반찬"),
    CAN_KIT("C00907", "cannedFood", "통조림"),
    MIL_KIT("C00908", "mealKit", "밀키트");

    private String codeValue;
    private String codeEngName;
    private String codeKorName;

    public static SubCategory findByCodeEngName(String codeEngName) {
        return Arrays.stream(SubCategory.values())
                .filter(subCategory -> subCategory.getCodeEngName().equals(codeEngName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static SubCategory findByCodeKorName(String codeValue) {
        return Arrays.stream(SubCategory.values())
                .filter(subCategory -> subCategory.getCodeKorName().equals(codeValue))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
