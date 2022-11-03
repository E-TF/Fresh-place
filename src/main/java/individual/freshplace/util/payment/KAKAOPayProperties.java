package individual.freshplace.util.payment;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class KAKAOPayProperties {

    public static final String CID_KEY = "cid";
    public static final String ORDER_ID_KEY = "partner_order_id";
    public static final String USER_ID_KEY = "partner_user_id";
    public static final String ITEM_NAME_KEY = "item_name";
    public static final String QUANTITY_KEY = "quantity";
    public static final String TOTAL_PRICE_KEY = "total_amount";
    public static final String TAX_FREE_AMOUNT_KEY = "tax_free_amount";
    public static final String APPROVAL_URL_KEY = "approval_url";
    public static final String CANCEL_URL_KEY = "cancel_url";
    public static final String FAIL_URL_KEY = "fail_url";
    public static final String ADMIN_KEY_PREFIX = "KakaoAK ";

    private final String host;
    private final String path;
    private final String adminKey;
    private final String cid;
    private final String taxFreeAmount;
    private final String approvalUrl;
    private final String cancelUrl;
    private final String failUrl;

    public KAKAOPayProperties(@Value("${kakaopay.host}") String host, @Value("${kakaopay.path}") String path,
                              @Value("${kakaopay.admin_key}") String adminKey, @Value("${kakaopay.cid}") String cid,
                              @Value("${kakaopay.tax_free_amount}") String taxFreeAmount, @Value("${kakaopay.approval_url}") String approvalUrl,
                              @Value("${kakaopay.cancel_url}") String cancelUrl, @Value("${kakaopay.fail_url}") String failUrl) {
        this.host = host;
        this.path = path;
        this.adminKey = adminKey;
        this.cid = cid;
        this.taxFreeAmount = taxFreeAmount;
        this.approvalUrl = approvalUrl;
        this.cancelUrl = cancelUrl;
        this.failUrl = failUrl;
    }
}
