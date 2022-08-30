package individual.freshplace.util.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Category {

    FRU_CAT("과일", Arrays.asList(CategoryType.ECO_FRU, CategoryType.FRO_FRU, CategoryType.DOM_FRU, CategoryType.IMP_FRU)),
    VEG_CAT("채소", Arrays.asList(CategoryType.ECO_VEG, CategoryType.ROO_VEG, CategoryType.LEF_VEG, CategoryType.CAB_VEG, CategoryType.SCA_VEG, CategoryType.FRU_VEG, CategoryType.MIX_VEG, CategoryType.SPR_VEG, CategoryType.MUS_VEG)),
    MEA_CAT( "육류", Arrays.asList(CategoryType.BEE_MEA, CategoryType.POR_MEA, CategoryType.LAM_MEA, CategoryType.CHI_MEA, CategoryType.DUC_MEA, CategoryType.EGG_MEA)),
    DRI_CAT("음료" , Arrays.asList(CategoryType.WAT_DRI, CategoryType.JUI_DRI, CategoryType.SPA_DRI, CategoryType.DAI_DRI, CategoryType.COF_DRI, CategoryType.TEA_DRI, CategoryType.ALC_DRI)),
    SEA_CAT("해산물", Arrays.asList(CategoryType.FIS_SEA, CategoryType.CEP_SEA, CategoryType.SHE_SEA, CategoryType.CLA_SEA, CategoryType.WEE_SEA)),
    SNA_CAT("간식", Arrays.asList(CategoryType.SNA_SNA, CategoryType.CHO_SNA, CategoryType.JEL_SNA, CategoryType.CAN_SNA, CategoryType.ICE_SNA)),
    BAK_CAT("베이커리", Arrays.asList(CategoryType.BRE_BAK, CategoryType.SPR_BAK, CategoryType.PIE_BAK, CategoryType.CHE_BAK)),
    SAU_CAT("양념", Arrays.asList(CategoryType.DRE_SAU, CategoryType.SOY_SAU, CategoryType.OIL_SAU, CategoryType.SPI_SAU, CategoryType.POW_SAU)),
    KIT_CAT("밀키트", Arrays.asList(CategoryType.SER_KIT, CategoryType.NOO_KIT, CategoryType.SOU_KIT, CategoryType.SAL_KIT, CategoryType.BOX_KIT, CategoryType.SID_KIT, CategoryType.CAN_KIT, CategoryType.MIL_KIT));

    private String codeTypeName;
    private List<CategoryType> categoryTypeList;

    public static List<CategoryType> findByCodeTypeName(String codeTypeName) {
        return Arrays.stream(Category.values())
                .filter(c -> c.getCodeTypeName().equals(codeTypeName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .getCategoryTypeList();
    }
}
