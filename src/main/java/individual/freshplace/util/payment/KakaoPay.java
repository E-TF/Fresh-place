package individual.freshplace.util.payment;

import individual.freshplace.dto.kakaopay.KakaoPayApprovalResponse;
import individual.freshplace.dto.kakaopay.KakaoPayOrderDetailsResponse;
import individual.freshplace.dto.kakaopay.KakaoPayReadyResponse;
import individual.freshplace.dto.order.OrderItem;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.UriException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class KakaoPay {

    private static final long ORDER_ID_MIN_VALUE = 1;
    private static final long ORDER_ID_MAX_VALUE = 10000000000L;

    private final RestTemplate restTemplate;
    private final KakaoPayProperties kakaoPayProperties;

    public KakaoPayReadyResponse getKakaoPayReadyResponse(final String memberId, final List<OrderItem> orderItems) {

        HttpHeaders httpHeaders = setHeaders();
        MultiValueMap<String, String> params = setParamsReady(memberId, createOrderTitle(orderItems.get(0).getItemName(), orderItems.size())
                , orderItems.stream().map(OrderItem::getItemCount).reduce(0L, Long::sum)
                , orderItems.stream().map(OrderItem::getTotalPrice).reduce(0L, Long::sum));
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, httpHeaders);
        String paymentReadyRequestUrl = kakaoPayProperties.getHost() + kakaoPayProperties.getReadyPath();

        try {
            return restTemplate.postForObject(new URI(paymentReadyRequestUrl), body, KakaoPayReadyResponse.class);
        } catch (URISyntaxException e) {
            throw new UriException(ErrorCode.NOT_FOUND, paymentReadyRequestUrl);
        }
    }

    public KakaoPayApprovalResponse getKakaoPayApprovalResponse(final String pgToken, final String memberId, final String tid, final String orderId) {

        HttpHeaders httpHeaders = setHeaders();
        MultiValueMap<String, String> params = setParamsApprove(pgToken, memberId, tid, orderId);
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, httpHeaders);
        String paymentApproveRequestUrl = kakaoPayProperties.getHost() + kakaoPayProperties.getApprovePath();

        try {
            return restTemplate.postForObject(new URI(paymentApproveRequestUrl), body, KakaoPayApprovalResponse.class);
        } catch (URISyntaxException e) {
            throw new UriException(ErrorCode.NOT_FOUND, paymentApproveRequestUrl);
        }
    }

    public KakaoPayOrderDetailsResponse getOrderDetailsResponse(final String tid) {

        HttpHeaders httpHeaders = setHeaders();
        MultiValueMap<String, String> params = setParamsOrder(tid);
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, httpHeaders);
        String paymentOrderRequestUrl = kakaoPayProperties.getHost() + kakaoPayProperties.getOrderPath();

        try {
            return restTemplate.postForObject(new URI(paymentOrderRequestUrl), body, KakaoPayOrderDetailsResponse.class);
        } catch (URISyntaxException e) {
            throw new UriException(ErrorCode.NOT_FOUND, paymentOrderRequestUrl);
        }
    }

    private HttpHeaders setHeaders() {

        //header 정보
        //POST /v1/payment/ready HTTP/1.1 && /v1/payment/approve HTTP/1.1
        //Host: kapi.kakao.com
        //Authorization: KakaoAK ${APP_ADMIN_KEY}
        //Content-type: application/x-www-form-urlencoded;charset=utf-8

        //header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, KakaoPayProperties.ADMIN_KEY_PREFIX + kakaoPayProperties.getAdminKey());
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");

        return httpHeaders;
    }

    private MultiValueMap<String, String> setParamsReady(String memberId, String itemNames, long totalCount, long totalPrice) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(KakaoPayProperties.CID_KEY, kakaoPayProperties.getCid());
        params.add(KakaoPayProperties.ORDER_ID_KEY, String.valueOf(createRandomOrderId()));
        params.add(KakaoPayProperties.USER_ID_KEY, memberId);
        params.add(KakaoPayProperties.ITEM_NAME_KEY, itemNames);
        params.add(KakaoPayProperties.QUANTITY_KEY, String.valueOf(totalCount));
        params.add(KakaoPayProperties.TOTAL_PRICE_KEY, String.valueOf(totalPrice));
        params.add(KakaoPayProperties.TAX_FREE_AMOUNT_KEY, kakaoPayProperties.getTaxFreeAmount());
        params.add(KakaoPayProperties.APPROVAL_URL_KEY, kakaoPayProperties.getApprovalUrl());
        params.add(KakaoPayProperties.CANCEL_URL_KEY, kakaoPayProperties.getCancelUrl());
        params.add(KakaoPayProperties.FAIL_URL_KEY, kakaoPayProperties.getFailUrl());

        return params;
    }

    private MultiValueMap<String, String> setParamsApprove(String pgToken, String memberId, String tid, String orderId) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(KakaoPayProperties.CID_KEY, kakaoPayProperties.getCid());
        params.add(KakaoPayProperties.TID_KEY, tid);
        params.add(KakaoPayProperties.ORDER_ID_KEY, orderId);
        params.add(KakaoPayProperties.USER_ID_KEY, memberId);
        params.add(KakaoPayProperties.PG_TOKEN, pgToken);

        return params;
    }

    private MultiValueMap<String, String> setParamsOrder(String tid) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(KakaoPayProperties.CID_KEY, kakaoPayProperties.getCid());
        params.add(KakaoPayProperties.TID_KEY, tid);

        return params;
    }

    private String createOrderTitle(String firstItemName, long orderItemSize) {
        return firstItemName + " 외" + (orderItemSize - 1) + "건";
    }

    private long createRandomOrderId() {
        return (long)(Math.random() * ORDER_ID_MAX_VALUE) + ORDER_ID_MIN_VALUE;
    }
}
