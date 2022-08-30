package individual.freshplace.util;

import individual.freshplace.util.constant.CategoryType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

@Converter(autoApply = true)
public class CategoryTypeConverter implements AttributeConverter<CategoryType, String> {

    @Override
    public String convertToDatabaseColumn(CategoryType attribute) {
        return attribute.getCodeValue();
    }

    @Override
    public CategoryType convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(CategoryType.class).stream()
                .filter(c -> c.getCodeValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException());
    }
}
