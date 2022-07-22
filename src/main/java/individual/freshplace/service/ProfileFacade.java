package individual.freshplace.service;

import individual.freshplace.dto.profile.ProfileUpdateRequest;
import individual.freshplace.dto.profile.ProfileResponse;
import individual.freshplace.entity.Member;
import individual.freshplace.util.ErrorCode;
import individual.freshplace.util.constant.Prefix;
import individual.freshplace.util.exception.DuplicationException;
import individual.freshplace.util.lock.UserLevelLock;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileFacade {

    private final UserLevelLock userLevelLock;
    private final MemberService memberService;
    private final ObjectProvider<ProfileFacade> memberServiceObjectProvider;

    @Transactional
    public ProfileResponse getProfile(final String memberId) {

        Member member = memberService.findByMemberId(memberId);

        return ProfileResponse.from(member);
    }

    public ProfileResponse updateProfile(final String memberId, final ProfileUpdateRequest profileUpdateRequest) {

        return userLevelLock.lockProcess(Prefix.LOCK_PREFIX_UPDATE_MEMBER + profileUpdateRequest.getMemberId(), () ->
                ProfileResponse.from(memberServiceObjectProvider.getObject().updateProfileInner(memberId, profileUpdateRequest)));
    }

    @Transactional
    public Member updateProfileInner(final String memberId, final ProfileUpdateRequest profileUpdateRequest) {

        Member member = memberService.findByMemberId(memberId);

        if (memberService.existsByMemberId(profileUpdateRequest.getMemberId())) {
            throw new DuplicationException(ErrorCode.UN_AVAILABLE_ID, profileUpdateRequest.getMemberId());
        }

        member.updateProfile(profileUpdateRequest);

        return member;
    }
}
