package individual.freshplace.service;

import individual.freshplace.dto.cart.CartItem;
import individual.freshplace.dto.kakaopay.KakaoPayApprovalResponse;
import individual.freshplace.dto.kakaopay.KakaoPayOrderDetailsResponse;
import individual.freshplace.dto.kakaopay.KakaoPayReadyResponse;
import individual.freshplace.dto.order.OrderItem;
import individual.freshplace.dto.order.OrderRequest;
import individual.freshplace.dto.payment.Receipt;
import individual.freshplace.entity.*;
import individual.freshplace.util.constant.LockPrefix;
import individual.freshplace.util.constant.RedisKeyPrefix;
import individual.freshplace.util.constant.code.delivery.PlaceToReceive;
import individual.freshplace.util.lock.UserLevelLock;
import individual.freshplace.util.payment.KakaoPay;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FOrderService {

    private final UserLevelLock userLevelLock;
    private final FStockService fStockService;
    private final FCartReadService fCartReadService;
    private final MemberService memberService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final ItemService itemService;
    private final KakaoPay kakaoPay;
    private final RedisTemplate<String, String> redisTemplate;
    private final DeliveryAddressService deliveryAddressService;
    private final PaymentService paymentService;

    public String addOrderAndGetPaymentRedirectUrl(final String memberId, final OrderRequest orderRequest, final Cookie[] cookies) {

        List<CartItem> cartItems = fCartReadService.getCartByMember(memberId, cookies).getCartItems();
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> new OrderItem(cartItem.getItemSeq(), cartItem.getItemName(), cartItem.getItemCounting(), cartItem.getPrice())).collect(Collectors.toList());
        Long orderSeq = userLevelLock.lockProcess(LockPrefix.STOCK_CHECKING.getMethodName(), () -> changeItemsStockAndAddOrder(memberId, orderItems, orderRequest));
        orderSeqSaveToRedis(memberId, String.valueOf(orderSeq));
        KakaoPayReadyResponse kakaoPayReadyResponse = kakaoPay.getKakaoPayReadyResponse(memberId, orderItems);
        tidSaveToRedis(memberId, kakaoPayReadyResponse.getTid());
        return kakaoPayReadyResponse.getNextRedirectPcUrl();
    }

    @Transactional
    protected Long changeItemsStockAndAddOrder(String memberId, List<OrderItem> orderItems, OrderRequest orderRequest) {

        fStockService.stockCheckAndChange(orderItems);
        final Member member = memberService.getByMemberId(memberId);
        final DeliverAddress deliverAddress = deliveryAddressService.getById(orderRequest.getRecipientInformation());
        final Order order = Order.builder().member(member).address(deliverAddress.getAddress()).receiverName(deliverAddress.getRecipient()).receiverPhoneNumber(deliverAddress.getContact()).placeToReceiveCode(PlaceToReceive.findByCodeName(orderRequest.getDeliveryRequirements())).build();
        orderService.save(order);
        addOrderDetails(order, orderItems);
        return order.getOrderSeq();
    }

    @Transactional
    public Receipt addPayment(final String pgToken, final String memberId) {

        String tid = redisTemplate.opsForValue().get(RedisKeyPrefix.PAYMENT + memberId);
        KakaoPayOrderDetailsResponse kakaoPayOrderDetailsResponse = kakaoPay.getOrderDetailsResponse(tid);
        KakaoPayApprovalResponse kakaoPayApprovalResponse = kakaoPay.getKakaoPayApprovalResponse(pgToken, kakaoPayOrderDetailsResponse.getPartnerUserId(), kakaoPayOrderDetailsResponse.getTid(), kakaoPayOrderDetailsResponse.getPartnerOrderId());
        Long orderSeq = Long.parseLong(redisTemplate.opsForValue().get(RedisKeyPrefix.ORDER + memberId));
        final Order order = orderService.getById(orderSeq);
        final Payment payment = Payment.builder().paymentMethod(kakaoPayApprovalResponse.getPaymentMethodType()).order(order).paymentDate(kakaoPayApprovalResponse.getCreatedAt()).paymentTid(kakaoPayApprovalResponse.getTid()).build();
        paymentService.save(payment);
        return new Receipt(kakaoPayApprovalResponse.getItemName(),payment.getOrder().getReceiverName() + " / " + payment.getOrder().getReceiverPhoneNumber(), payment.getOrder().getAddress().getZipCode() + " " + payment.getOrder().getAddress().getAddress(),
                payment.getOrder().getPlaceToReceiveCode().getCodeName(), kakaoPayApprovalResponse.getAmount().getTotal(), kakaoPayApprovalResponse.getCardInfo().getIssuerCorp());
    }

    private void addOrderDetails(Order order, List<OrderItem> orderItems) {
        orderItems.stream().map(orderItem -> OrderDetail.builder()
                .order(order)
                .item(itemService.getById(orderItem.getItemSeq()))
                .count(orderItem.getItemCount())
                .price(orderItem.getTotalPrice()).build()).forEach(orderDetailService::save);
    }

    private void orderSeqSaveToRedis(String memberId, String orderSeq) {
        redisTemplate.opsForValue().set(RedisKeyPrefix.ORDER + memberId, orderSeq);
    }

    private void tidSaveToRedis(String memberId, String tid) {
        redisTemplate.opsForValue().set(RedisKeyPrefix.PAYMENT + memberId, tid);
    }
}
