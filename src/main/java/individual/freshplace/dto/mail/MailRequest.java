package individual.freshplace.dto.mail;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MailRequest {

    private String address;
    private String title;
    private String content;

    @Builder
    public MailRequest(String address, String title, String content) {
        this.address = address;
        this.title = title;
        this.content = content;
    }
}
