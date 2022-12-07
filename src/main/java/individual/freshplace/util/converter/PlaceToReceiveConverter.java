package individual.freshplace.util.converter;

import individual.freshplace.util.constant.code.delivery.PlaceToReceive;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

@Converter(autoApply = true)
public class PlaceToReceiveConverter implements AttributeConverter<PlaceToReceive, String> {

    @Override
    public String convertToDatabaseColumn(PlaceToReceive attribute) {
        return attribute.getCodeValue();
    }

    @Override
    public PlaceToReceive convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(PlaceToReceive.class).stream()
                .filter(c -> c.getCodeValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException());
    }
}
