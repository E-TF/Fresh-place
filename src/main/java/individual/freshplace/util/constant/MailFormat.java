package individual.freshplace.util.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MailFormat {

    IMAGE_UPLOAD_FAILED("이미지처리 오류입니다.");

    private String title;
}
