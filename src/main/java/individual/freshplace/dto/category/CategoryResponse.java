package individual.freshplace.dto.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class CategoryResponse {

    private final String engName;
    private final String korName;
    private final List<SubCategoryResponse> subCategoryResponses;

    @Getter
    @NoArgsConstructor(force = true)
    @RequiredArgsConstructor
    public static class SubCategoryResponse {
        private final String engName;
        private final String korName;
    }
}
