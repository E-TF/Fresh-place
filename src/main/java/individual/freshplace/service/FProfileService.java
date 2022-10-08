package individual.freshplace.service;

import individual.freshplace.dto.profile.ProfileUpdateRequest;
import individual.freshplace.dto.profile.ProfileResponse;
import individual.freshplace.entity.Member;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.constant.LockPrefix;
import individual.freshplace.util.exception.DuplicationException;
import individual.freshplace.util.lock.UserLevelLock;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FProfileService {

    private final UserLevelLock userLevelLock;
    private final MemberService memberService;
    private final ObjectProvider<FProfileService> fProfileServiceObjectProvider;

    @Transactional
    public ProfileResponse getProfile(final String memberId) {

        Member member = memberService.findByMemberId(memberId);

        return ProfileResponse.from(member);
    }

    public ProfileResponse updateProfile(final String memberId, final ProfileUpdateRequest profileUpdateRequest) {

        return userLevelLock.lockProcess(LockPrefix.UPDATE_MEMBER.getMethodName() + profileUpdateRequest.getMemberId(), () ->
                fProfileServiceObjectProvider.getObject().updateProfileInner(memberId, profileUpdateRequest));
    }

    @Transactional
    public ProfileResponse updateProfileInner(final String memberId, final ProfileUpdateRequest profileUpdateRequest) {

        if (memberService.existsByMemberId(profileUpdateRequest.getMemberId())) {
            throw new DuplicationException(ErrorCode.UN_AVAILABLE_ID, profileUpdateRequest.getMemberId());
        }

        Member member = memberService.findByMemberId(memberId);

        member.updateProfile(profileUpdateRequest);

        return ProfileResponse.from(member);
    }
}
