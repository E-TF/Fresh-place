package individual.freshplace.dto.category;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class CategoryResponse {

    private final String mainCategoryEngName;

    private final String mainCategoryKorName;

    private final List<SubCategoryResponse> subCategoryResponses;

    @JsonCreator
    public CategoryResponse(@JsonProperty("mainCategoryEngName") String mainCategoryEngName,
                            @JsonProperty("mainCategoryKorName") String mainCategoryKorName,
                            @JsonProperty("subCategoryResponses") List<SubCategoryResponse> subCategoryResponses) {

        this.mainCategoryEngName = mainCategoryEngName;
        this.mainCategoryKorName = mainCategoryKorName;
        this.subCategoryResponses = subCategoryResponses;
    }

    @Getter
    public static class SubCategoryResponse {

        private final String subCategoryEngName;

        private final String subCategoryKorName;

        @JsonCreator
        public SubCategoryResponse(@JsonProperty("subCategoryEngName") String subCategoryEngName,
                                   @JsonProperty("subCategoryKorName") String subCategoryKorName) {

            this.subCategoryEngName = subCategoryEngName;
            this.subCategoryKorName = subCategoryKorName;
        }
    }
}
