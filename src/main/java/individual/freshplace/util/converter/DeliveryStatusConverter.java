package individual.freshplace.util.converter;

import individual.freshplace.util.constant.code.delivery.DeliveryStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

@Converter(autoApply = true)
public class DeliveryStatusConverter implements AttributeConverter<DeliveryStatus, String> {

    @Override
    public String convertToDatabaseColumn(DeliveryStatus attribute) {
        return attribute.getCodeValue();
    }

    @Override
    public DeliveryStatus convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(DeliveryStatus.class).stream()
                .filter(c -> c.getCodeValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException());

    }
}
