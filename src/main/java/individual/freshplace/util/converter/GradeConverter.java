package individual.freshplace.util.converter;

import individual.freshplace.util.constant.Membership;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

@Converter(autoApply = true)
public class GradeConverter implements AttributeConverter<Membership, String> {

    @Override
    public String convertToDatabaseColumn(Membership attribute) {
        return attribute.getCodeValue();
    }

    @Override
    public Membership convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(Membership.class).stream()
                .filter(c -> c.getCodeValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException());
    }
}
