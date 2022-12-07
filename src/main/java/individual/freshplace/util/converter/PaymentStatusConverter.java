package individual.freshplace.util.converter;

import individual.freshplace.util.constant.code.payment.PaymentStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

@Converter(autoApply = true)
public class PaymentStatusConverter implements AttributeConverter<PaymentStatus, String> {

    @Override
    public String convertToDatabaseColumn(PaymentStatus attribute) {
        return attribute.getCodeValue();
    }

    @Override
    public PaymentStatus convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(PaymentStatus.class).stream()
                .filter(c -> c.getCodeValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException());
    }
}
