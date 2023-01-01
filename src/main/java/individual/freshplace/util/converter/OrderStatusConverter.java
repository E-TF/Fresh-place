package individual.freshplace.util.converter;

import individual.freshplace.util.constant.code.order.OrderStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrderStatus attribute) {
        return attribute.getCodeValue();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(OrderStatus.class).stream()
                .filter(c -> c.getCodeValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException());
    }
}
