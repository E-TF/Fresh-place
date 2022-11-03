package individual.freshplace.util.payment;

import individual.freshplace.dto.kakaopay.KakaoPayReadResponse;
import individual.freshplace.dto.order.OrderItem;
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

@Component
@RequiredArgsConstructor
public class KAKAOPay {

    private final KAKAOPayProperties kakaoPayProperties;
    private final RestTemplate restTemplate;

    public String addKAKAOPayReadyResponse(String memberId, OrderItem orderItem) throws URISyntaxException {

        HttpHeaders httpHeaders = addHeaders();
        MultiValueMap<String, String> params = addParams(memberId, orderItem);
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, httpHeaders);

        KakaoPayReadResponse kakaoPayReadResponse = restTemplate.postForObject(new URI(kakaoPayProperties.getHost() + kakaoPayProperties.getPath()), body, KakaoPayReadResponse.class);

        return kakaoPayReadResponse.getNext_redirect_pc_url();
    }

    private HttpHeaders addHeaders() {

        //header 정보
        //POST /v1/payment/ready HTTP/1.1
        //Host: kapi.kakao.com
        //Authorization: KakaoAK ${APP_ADMIN_KEY}
        //Content-type: application/x-www-form-urlencoded;charset=utf-8

        //header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, KAKAOPayProperties.ADMIN_KEY_PREFIX + kakaoPayProperties.getAdminKey());
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");

        return httpHeaders;
    }

    private MultiValueMap<String, String> addParams(String memberId, OrderItem orderItem) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(KAKAOPayProperties.CID_KEY, kakaoPayProperties.getCid());
        params.add(KAKAOPayProperties.ORDER_ID_KEY, String.valueOf(makeRandomOrderId()));
        params.add(KAKAOPayProperties.USER_ID_KEY, memberId);
        params.add(KAKAOPayProperties.ITEM_NAME_KEY, orderItem.getItemName());
        params.add(KAKAOPayProperties.QUANTITY_KEY, String.valueOf(orderItem.getItemCount()));
        params.add(KAKAOPayProperties.TOTAL_PRICE_KEY, String.valueOf(orderItem.getTotalPrice()));
        params.add(KAKAOPayProperties.TAX_FREE_AMOUNT_KEY, kakaoPayProperties.getTaxFreeAmount());
        params.add(KAKAOPayProperties.APPROVAL_URL_KEY, kakaoPayProperties.getApprovalUrl());
        params.add(KAKAOPayProperties.CANCEL_URL_KEY, kakaoPayProperties.getCancelUrl());
        params.add(KAKAOPayProperties.FAIL_URL_KEY, kakaoPayProperties.getFailUrl());

        return params;
    }

    private long makeRandomOrderId() {
        return (long) Math.random() * 10000000000L + 1;
    }
}
