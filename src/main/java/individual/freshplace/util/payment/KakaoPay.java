package individual.freshplace.util.payment;

import individual.freshplace.dto.kakaopay.KakaoPayApprovalResponse;
import individual.freshplace.dto.kakaopay.KakaoPayReadyResponse;
import individual.freshplace.dto.kakaopay.PayView;
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

    public PayView getKAKAOPayReadyResponse(final String memberId, final List<OrderItem> orderItems) {

        HttpHeaders httpHeaders = setHeaders();

        MultiValueMap<String, String> params = setParamsReady(memberId, orderItems.get(0).getItemName() + " 외" + (orderItems.size() - 1) + "건"
                , orderItems.stream().map(OrderItem::getItemCount).reduce(0L, (a, b) -> a + b)
                , orderItems.stream().map(OrderItem::getTotalPrice).reduce(0L, (a, b) -> a + b));
        setParamsCommon(memberId, params);

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, httpHeaders);

        KakaoPayReadyResponse kakaoPayReadyResponse;
        String paymentReadyRequestUrl = kakaoPayProperties.getHost() + kakaoPayProperties.getReadyPath();

        try {
            kakaoPayReadyResponse = restTemplate.postForObject(new URI(paymentReadyRequestUrl), body, KakaoPayReadyResponse.class);
        } catch (URISyntaxException e) {
            throw new UriException(ErrorCode.NON_FOUND, paymentReadyRequestUrl);
        }

        return PayView.of(kakaoPayReadyResponse.getTid(), params.getFirst(KakaoPayProperties.ORDER_ID_KEY),
                memberId, kakaoPayReadyResponse.getRedirectUrl());
    }

    public KakaoPayApprovalResponse getKAKAOPaymentInformation(final String pgToken, final PayView payView) {

        HttpHeaders httpHeaders = setHeaders();

        MultiValueMap<String, String> params = setParamsApprove(pgToken, payView);
        setParamsCommon(payView.getUserId(), params);

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, httpHeaders);

        KakaoPayApprovalResponse kakaoPayApprovalResponse;
        String paymentApproveRequestUrl = kakaoPayProperties.getHost() + kakaoPayProperties.getApprovePath();

        try {
            kakaoPayApprovalResponse = restTemplate.postForObject(new URI(paymentApproveRequestUrl), body, KakaoPayApprovalResponse.class);
        } catch (URISyntaxException e) {
            throw new UriException(ErrorCode.NON_FOUND, paymentApproveRequestUrl);
        }

        return kakaoPayApprovalResponse;
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

    private void setParamsCommon(String memberId, MultiValueMap<String, String> params) {

        params.add(KakaoPayProperties.CID_KEY, kakaoPayProperties.getCid());
        params.add(KakaoPayProperties.ORDER_ID_KEY, String.valueOf(makeRandomOrderId()));
        params.add(KakaoPayProperties.USER_ID_KEY, memberId);
    }

    private MultiValueMap<String, String> setParamsReady(String memberId, String itemNames, long totalCount, long totalPrice) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(KakaoPayProperties.CID_KEY, kakaoPayProperties.getCid());
        params.add(KakaoPayProperties.ORDER_ID_KEY, String.valueOf(makeRandomOrderId()));
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

    private MultiValueMap<String, String> setParamsApprove(String pg_token, PayView payView) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(KakaoPayProperties.CID_KEY, kakaoPayProperties.getCid());
        params.add(KakaoPayProperties.TID_KEY, payView.getTid());
        params.add(KakaoPayProperties.ORDER_ID_KEY, payView.getOrderId());
        params.add(KakaoPayProperties.USER_ID_KEY, payView.getUserId());
        params.add(kakaoPayProperties.PG_TOKEN, pg_token);

        return params;
    }

    private long makeRandomOrderId() {
        return (long)(Math.random() * ORDER_ID_MAX_VALUE) + ORDER_ID_MIN_VALUE;
    }
}
