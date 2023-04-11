package individual.freshplace.dto.auth.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenReissueResponse {

    private final String accessToken;
}
