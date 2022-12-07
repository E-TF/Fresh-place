package individual.freshplace.util.constant.code.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Category {

    FRU_CAT("C001", "fruit", "과일", Arrays.asList(SubCategory.ECO_FRU, SubCategory.FRO_FRU, SubCategory.DOM_FRU, SubCategory.IMP_FRU)),
    VEG_CAT("C002", "vegetable", "채소", Arrays.asList(SubCategory.ECO_VEG, SubCategory.ROO_VEG, SubCategory.LEF_VEG, SubCategory.CAB_VEG, SubCategory.SCA_VEG, SubCategory.FRU_VEG, SubCategory.MIX_VEG, SubCategory.SPR_VEG, SubCategory.MUS_VEG)),
    MEA_CAT("C003", "meat", "육류", Arrays.asList(SubCategory.BEE_MEA, SubCategory.POR_MEA, SubCategory.LAM_MEA, SubCategory.CHI_MEA, SubCategory.DUC_MEA, SubCategory.EGG_MEA)),
    DRI_CAT("C004", "drink", "음료", Arrays.asList(SubCategory.WAT_DRI, SubCategory.JUI_DRI, SubCategory.SPA_DRI, SubCategory.DAI_DRI, SubCategory.COF_DRI, SubCategory.TEA_DRI, SubCategory.ALC_DRI)),
    SEA_CAT("C005", "seafood", "해산물", Arrays.asList(SubCategory.FIS_SEA, SubCategory.CEP_SEA, SubCategory.CLA_SEA, SubCategory.CRU_SEA, SubCategory.WEE_SEA)),
    SNA_CAT("C006", "snack", "간식", Arrays.asList(SubCategory.SNA_SNA, SubCategory.CHO_SNA, SubCategory.JEL_SNA, SubCategory.CAN_SNA, SubCategory.ICE_SNA)),
    BAK_CAT("C007", "bakery", "베이커리", Arrays.asList(SubCategory.BRE_BAK, SubCategory.SPR_BAK, SubCategory.PIE_BAK, SubCategory.CHE_BAK)),
    SAU_CAT("C008", "seasoning", "양념", Arrays.asList(SubCategory.DRE_SAU, SubCategory.SOY_SAU, SubCategory.OIL_SAU, SubCategory.SPI_SAU, SubCategory.POW_SAU)),
    KIT_CAT("C009", "mealKit", "밀키트", Arrays.asList(SubCategory.SER_KIT, SubCategory.NOO_KIT, SubCategory.SOU_KIT, SubCategory.SAL_KIT, SubCategory.BOX_KIT, SubCategory.SID_KIT, SubCategory.CAN_KIT, SubCategory.MIL_KIT));

    private String codeValue;
    private String codeEngName;
    private String codeKorName;
    private List<SubCategory> subCategoryList;

    public static List<SubCategory> findByCodeEngName(String codeEngName) {
        return Arrays.stream(Category.values())
                .filter(c -> c.getCodeEngName().equals(codeEngName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .getSubCategoryList();
    }
}
