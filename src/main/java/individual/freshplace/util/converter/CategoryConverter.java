package individual.freshplace.util.converter;

import individual.freshplace.util.constant.code.category.SubCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

@Converter(autoApply = true)
public class CategoryConverter implements AttributeConverter<SubCategory, String> {

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
