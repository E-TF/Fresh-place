package individual.freshplace.service;

import individual.freshplace.entity.*;
import individual.freshplace.repository.OrderRepository;
import individual.freshplace.repository.PaymentRepository;
import individual.freshplace.util.constant.code.delivery.DeliveryStatus;
import individual.freshplace.util.constant.code.delivery.PlaceToReceive;
import individual.freshplace.util.constant.code.order.OrderStatus;
import individual.freshplace.util.constant.code.payment.PaymentStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("주문취소 시 주문, 배송, 결제상태가 모두 취소로 변경된다.")
    void checkingOrderStatusAndPaymentStatusAfterOrderCancel() {
        //given
        final Order order = order(deliverAddress(member()));
        final Payment payment = payment(order);
        given(orderRepository.findById(order.getOrderSeq())).willReturn(Optional.of(order));
        given(paymentRepository.findByOrder(any())).willReturn(payment);

        //when
        orderService.cancelOrderAndPaymentAndChangeStock(order.getOrderSeq());

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(order.getOrderStatusCode(), OrderStatus.CANCEL),
                () -> Assertions.assertEquals(order.getDeliveryStatusCode(), DeliveryStatus.CANCEL),
                () -> Assertions.assertEquals(payment.getPaymentStatusCode(), PaymentStatus.CANCEL));
    }

    private Member member() {
        return Member.builder().memberId("testId").memberBirth(LocalDate.of(2000, 1, 29)).memberName("testName")
                .email("test@test.com").password("1234").phoneNumber("000-0000-0000").build();
    }

    private DeliverAddress deliverAddress(Member member) {
        return DeliverAddress.builder().member(member).address(Address.builder().zipCode("12345").address("강남아파트 1020동 301호").build()).contact(member.getPhoneNumber()).recipient(member.getMemberName()).build();
    }

    private Order order(DeliverAddress deliverAddress) {
        return Order.builder().orderName("커피 외0건").member(deliverAddress.getMember()).address(deliverAddress.getAddress())
                .receiverName(deliverAddress.getMember().getMemberName()).receiverPhoneNumber(deliverAddress.getMember().getPhoneNumber()).placeToReceiveCode(PlaceToReceive.COURIER_BOX).build();
    }

    private Payment payment(Order order) {
        return Payment.builder().paymentAmount(3900L).paymentMethod("CARD").order(order)
                .paymentDate(LocalDateTime.now()).paymentTid("").build();
    }
}
