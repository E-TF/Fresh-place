package individual.freshplace.util;

import individual.freshplace.util.constant.SubCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

@Converter(autoApply = true)
public class CategoryTypeConverter implements AttributeConverter<SubCategory, String> {

    @Override
    public String convertToDatabaseColumn(SubCategory attribute) {
        return attribute.getCodeValue();
    }

    @Override
    public SubCategory convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(SubCategory.class).stream()
                .filter(c -> c.getCodeValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException());
    }
}
