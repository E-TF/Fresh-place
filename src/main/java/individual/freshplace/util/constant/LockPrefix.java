package individual.freshplace.util.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LockPrefix {

    SIGNUP("signup"),
    UPDATE_MEMBER("updateMember");

    private String methodName;
}
