package individual.freshplace.util.payment;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class KakaoPayProperties {

    public static final String ADMIN_KEY_PREFIX = "KakaoAK ";

    public static final String CID_KEY = "cid";
    public static final String ORDER_ID_KEY = "partner_order_id";
    public static final String USER_ID_KEY = "partner_user_id";

    public static final String ITEM_NAME_KEY = "item_name";
    public static final String QUANTITY_KEY = "quantity";
    public static final String TOTAL_PRICE_KEY = "total_amount";
    public static final String CANCEL_AMOUNT_KEY = "cancel_amount";
    public static final String TAX_FREE_AMOUNT_KEY = "tax_free_amount";
    public static final String CANCEL_TAX_FREE_AMOUNT_KEY = "cancel_tax_free_amount";
    public static final String APPROVAL_URL_KEY = "approval_url";
    public static final String CANCEL_URL_KEY = "cancel_url";
    public static final String FAIL_URL_KEY = "fail_url";

    public static final String TID_KEY = "tid";
    public static final String PG_TOKEN = "pg_token";


    private final String host;
    private final String readyPath;
    private final String approvePath;
    private final String orderPath;
    private final String cancelPath;
    private final String adminKey;
    private final String cid;
    private final String taxFreeAmount;
    private final String cancelTaxFreeAmount;
    private final String approvalUrl;
    private final String cancelUrl;
    private final String failUrl;

    public KakaoPayProperties(@Value("${kakaopay.host}") String host, @Value("${kakaopay.ready_path}") String readyPath, @Value("${kakaopay.approve_path}") String approvePath,
                              @Value("${kakaopay.order_path}") String orderPath, @Value("${kakaopay.cancel_path}") String cancelPath, @Value("${kakaopay.admin_key}") String adminKey,
                              @Value("${kakaopay.cid}") String cid, @Value("${kakaopay.tax_free_amount}") String taxFreeAmount, @Value("${kakaopay.cancel_tax_free_amount}") String cancelTaxFreeAmount,
                              @Value("${kakaopay.approval_url}") String approvalUrl, @Value("${kakaopay.cancel_url}") String cancelUrl, @Value("${kakaopay.fail_url}") String failUrl) {
        this.host = host;
        this.readyPath = readyPath;
        this.approvePath = approvePath;
        this.orderPath = orderPath;
        this.cancelPath = cancelPath;
        this.adminKey = adminKey;
        this.cid = cid;
        this.taxFreeAmount = taxFreeAmount;
        this.cancelTaxFreeAmount = cancelTaxFreeAmount;
        this.approvalUrl = approvalUrl;
        this.cancelUrl = cancelUrl;
        this.failUrl = failUrl;
    }
}
