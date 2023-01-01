package individual.freshplace.service;

import individual.freshplace.dto.kakaopay.KakaoPayApprovalResponse;
import individual.freshplace.dto.kakaopay.KakaoPayOrderDetailsResponse;
import individual.freshplace.dto.kakaopay.KakaoPayReadyResponse;
import individual.freshplace.dto.order.*;
import individual.freshplace.dto.payment.Receipt;
import individual.freshplace.entity.*;
import individual.freshplace.repository.*;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.constant.LockPrefix;
import individual.freshplace.util.constant.RedisKeyPrefix;
import individual.freshplace.util.constant.code.delivery.PlaceToReceive;
import individual.freshplace.util.exception.NonExistentException;
import individual.freshplace.util.lock.UserLevelLock;
import individual.freshplace.util.payment.KakaoPay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserLevelLock userLevelLock;
    private final StockService stockService;
    private final CartReadService cartReadService;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ItemRepository itemRepository;
    private final KakaoPay kakaoPay;
    private final RedisTemplate<String, String> redisTemplate;
    private final DeliveryAddressRepository deliveryAddressRepository;
    private final PaymentRepository paymentRepository;

    public String addOrderAndGetPaymentRedirectUrl(final String memberId, final OrderRequest orderRequest, final Cookie[] cookies) {
        List<OrderItem> orderItems = cartReadService.getCartByMember(memberId, cookies).getCartItems().stream().map(cartItem -> new OrderItem(cartItem.getItemSeq(), cartItem.getItemName(), cartItem.getItemCounting(), cartItem.getDiscountPrice())).collect(Collectors.toList());
        String orderName = createOrderTitle(orderItems.get(0).getItemName(), orderItems.size());
        Long orderSeq = userLevelLock.lockProcess(LockPrefix.STOCK_CHECKING.getMethodName(), () -> changeItemsStockAndAddOrder(memberId, orderItems, orderRequest, orderName));
        orderSeqSaveToRedis(memberId, String.valueOf(orderSeq));
        KakaoPayReadyResponse kakaoPayReadyResponse = kakaoPay.getKakaoPayReadyResponse(memberId, orderItems);
        log.info(kakaoPayReadyResponse.getNextRedirectPcUrl());
        tidSaveToRedis(memberId, kakaoPayReadyResponse.getTid());
        return kakaoPayReadyResponse.getNextRedirectPcUrl();
    }

    @Transactional
    protected Long changeItemsStockAndAddOrder(String memberId, List<OrderItem> orderItems, OrderRequest orderRequest, String orderName) {
        stockService.stockCheckAndChange(orderItems);
        final Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, memberId));
        final DeliverAddress deliverAddress = deliveryAddressRepository.findById(orderRequest.getRecipientInformation()).orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, String.valueOf(orderRequest.getRecipientInformation())));
        final Order order = Order.builder().orderName(orderName).member(member).address(deliverAddress.getAddress()).receiverName(deliverAddress.getRecipient()).receiverPhoneNumber(deliverAddress.getContact()).placeToReceiveCode(PlaceToReceive.findByCodeName(orderRequest.getDeliveryRequirements())).build();
        orderRepository.save(order);
        addOrderDetails(order, orderItems);
        return order.getOrderSeq();
    }

    @Transactional
    public Receipt addPayment(final String pgToken, final String memberId) {
        String tid = redisTemplate.opsForValue().get(RedisKeyPrefix.PAYMENT + memberId);
        KakaoPayOrderDetailsResponse kakaoPayOrderDetailsResponse = kakaoPay.getOrderDetailsResponse(tid);
        KakaoPayApprovalResponse kakaoPayApprovalResponse = kakaoPay.getKakaoPayApprovalResponse(pgToken, kakaoPayOrderDetailsResponse.getPartnerUserId(), kakaoPayOrderDetailsResponse.getTid(), kakaoPayOrderDetailsResponse.getPartnerOrderId());
        Long orderSeq = Long.parseLong(redisTemplate.opsForValue().get(RedisKeyPrefix.ORDER + memberId));
        final Order order = orderRepository.findById(orderSeq).orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, String.valueOf(orderSeq)));
        final Payment payment = Payment.builder().paymentAmount(kakaoPayApprovalResponse.getAmount().getTotal()).paymentMethod(kakaoPayApprovalResponse.getPaymentMethodType()).order(order).paymentDate(kakaoPayApprovalResponse.getCreatedAt()).paymentTid(kakaoPayApprovalResponse.getTid()).build();
        paymentRepository.save(payment);
        return createReceiptFromPaymentInformation(order.getOrderName(), payment, kakaoPayApprovalResponse.getCardInfo().getIssuerCorp());
    }

    @Transactional
    public List<OrderResponse> getOrders(final String memberId) {
        final Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, memberId));
        return getOrdersInformationFromOrderListOfMember(member.getOrderList());
    }

    @Transactional
    public OrderDetailResponse getOrderDetailsAndPayment(final Long orderSeq) {
        final Order order = orderRepository.findById(orderSeq).orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, String.valueOf(orderSeq)));
        return getOrderDetailInformationFromOrder(order);
    }

    private String createOrderTitle(String firstItemName, long orderItemSize) {
        return firstItemName + " 외" + (orderItemSize - 1) + "건";
    }

    private void addOrderDetails(Order order, List<OrderItem> orderItems) {
        orderItems.stream().map(orderItem -> OrderDetail.builder()
                .order(order)
                .item(itemRepository.findById(orderItem.getItemSeq()).orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, String.valueOf(orderItem.getItemSeq()))))
                .count(orderItem.getItemCount())
                .price(orderItem.getTotalPrice()).build()).forEach(orderDetailRepository::save);
    }

    private void orderSeqSaveToRedis(String memberId, String orderSeq) {
        redisTemplate.opsForValue().set(RedisKeyPrefix.ORDER + memberId, orderSeq);
    }

    private void tidSaveToRedis(String memberId, String tid) {
        redisTemplate.opsForValue().set(RedisKeyPrefix.PAYMENT + memberId, tid);
    }

    private Receipt createReceiptFromPaymentInformation(String orderTitle, Payment payment, String cardName) {
        return new Receipt(orderTitle, payment.getOrder().getReceiverName() + " / " + payment.getOrder().getReceiverPhoneNumber(), payment.getOrder().getAddress().getZipCode() + " " + payment.getOrder().getAddress().getAddress(),
                payment.getOrder().getPlaceToReceiveCode().getCodeName(), payment.getPaymentAmount(), cardName);
    }

    private List<OrderResponse> getOrdersInformationFromOrderListOfMember(List<Order> orders) {
        final Map<Order, Payment> orderPaymentMap = convertListIntoMap(orders);
        return orders.stream().map(order -> new OrderResponse(order.getCreatedAt(), order.getDeliveryStatusCode().getCodeName(), order.getOrderName(),
                orderPaymentMap.get(order).getPaymentMethod(), orderPaymentMap.get(order).getPaymentAmount())).collect(Collectors.toList());
    }

    private OrderDetailResponse getOrderDetailInformationFromOrder(Order order) {
        List<OrderedItem> orderedItems = createOrderedItems(order.getOrderDetailList());
        final Payment payment = paymentRepository.findByOrder(order);
        long orderedItemsOriginPrice = getOrderedItemsOriginAmount(order.getOrderDetailList());
        long orderedItemsDiscountPrice = getOrderedItemsDiscountAmount(orderedItemsOriginPrice, payment.getPaymentAmount());
        return new OrderDetailResponse(orderedItems,
                orderedItemsOriginPrice, orderedItemsDiscountPrice, 0, payment.getPaymentAmount(), payment.getPaymentMethod(),
                order.getMember().getMemberName(), payment.getPaymentDate(),
                order.getReceiverName(), order.getReceiverPhoneNumber(), order.getAddress().getZipCode() + " " + order.getAddress().getAddress(), order.getPlaceToReceiveCode().getCodeName());
    }

    private List<OrderedItem> createOrderedItems(List<OrderDetail> orderDetails) {
        return orderDetails.stream().map(orderDetail -> new OrderedItem(orderDetail.getItem().getItemName(), orderDetail.getCount(), orderDetail.getPrice())).collect(Collectors.toList());
    }

    private long getOrderedItemsOriginAmount(List<OrderDetail> orderDetails) {
        return orderDetails.stream().map(orderDetail -> orderDetail.getItem().getPrice()).reduce(0L, Long::sum);
    }

    private long getOrderedItemsDiscountAmount(long originAmount, long paymentAmount) {
        return originAmount - paymentAmount;
    }

    private Map<Order, Payment> convertListIntoMap(List<Order> orders) {
        return orders.stream().collect(Collectors.toMap(order -> order, order -> paymentRepository.findByOrder(order)));
    }
}
