package individual.freshplace.dto.kakaopay;

import lombok.Getter;
import lombok.ToString;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

@Getter
@ToString
public class KakaoPayReadResponse {

    private String tid;
    private String next_redirect_pc_url;
    private LocalDateTime created_at;

    @ConstructorProperties({"tid", "next_redirect_pc_url", "created_at"})
    public KakaoPayReadResponse(String tid, String next_redirect_pc_url, LocalDateTime created_at) {
        this.tid = tid;
        this.next_redirect_pc_url = next_redirect_pc_url;
        this.created_at = created_at;
    }
}
