package individual.freshplace.service;

import individual.freshplace.dto.profile.ProfileUpdateRequest;
import individual.freshplace.dto.profile.ProfileResponse;
import individual.freshplace.entity.Member;
import individual.freshplace.repository.MemberRepository;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.constant.LockPrefix;
import individual.freshplace.util.exception.DuplicationException;
import individual.freshplace.util.exception.NonExistentException;
import individual.freshplace.util.lock.UserLevelLock;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserLevelLock userLevelLock;
    private final MemberRepository memberRepository;
    private final ObjectProvider<ProfileService> fProfileServiceObjectProvider;

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(final String memberId) {
        final Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, memberId));
        return ProfileResponse.from(member);
    }

    public ProfileResponse updateProfile(final String memberId, final ProfileUpdateRequest profileUpdateRequest) {
        return userLevelLock.lockProcess(LockPrefix.UPDATE_MEMBER.getMethodName() + profileUpdateRequest.getMemberId(), () ->
                fProfileServiceObjectProvider.getObject().updateProfileInner(memberId, profileUpdateRequest));
    }

    @Transactional
    public ProfileResponse updateProfileInner(final String memberId, final ProfileUpdateRequest profileUpdateRequest) {
        if (memberRepository.existsByMemberId(profileUpdateRequest.getMemberId())) {
            throw new DuplicationException(ErrorCode.UN_AVAILABLE_ID, profileUpdateRequest.getMemberId());
        }

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, memberId));
        member.updateProfile(profileUpdateRequest);
        return ProfileResponse.from(member);
    }
}
