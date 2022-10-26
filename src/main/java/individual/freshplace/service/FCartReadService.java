package individual.freshplace.service;

import individual.freshplace.dto.cart.CartResponse;
import individual.freshplace.dto.profile.ProfileResponse;
import individual.freshplace.entity.Item;
import individual.freshplace.util.constant.Membership;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FCartReadService {

    private final ItemService itemService;
    private final FProfileService fProfileService;

    @Transactional
    public List<CartResponse> executeReadCookiesNonMember(final Cookie[] cookies) {

        List<Item> items = Arrays.stream(cookies).map(cookie -> itemService.findById(Long.parseLong(cookie.getName()))).collect(Collectors.toList());
        List<Cookie> cookieList = Arrays.stream(cookies).collect(Collectors.toList());

        return items.stream()
                .map(item -> CartResponse.of(item.getItemName()
                        , cookieList.stream().filter(cookie -> cookie.getName().equals(String.valueOf(item.getItemSeq()))).mapToLong(cookie -> Long.parseLong(cookie.getValue())).findFirst().getAsLong()
                        , item.getPrice() * cookieList.stream().filter(cookie -> cookie.getName().equals(String.valueOf(item.getItemSeq()))).mapToLong(cookie -> Long.parseLong(cookie.getValue())).findFirst().getAsLong()
                        ,item.getPrice() * cookieList.stream().filter(cookie -> cookie.getName().equals(String.valueOf(item.getItemSeq()))).mapToLong(cookie -> Long.parseLong(cookie.getValue())).findFirst().getAsLong()
                )).collect(Collectors.toList());
    }

    @Transactional
    public List<CartResponse> executeReadCookiesMember(final String memberId, final Cookie[] cookies) {

        List<Item> items = Arrays.stream(cookies).map(cookie -> itemService.findById(Long.parseLong(cookie.getName()))).collect(Collectors.toList());
        List<Cookie> cookieList = Arrays.stream(cookies).collect(Collectors.toList());
        ProfileResponse profile = fProfileService.getProfile(memberId);

        return items.stream()
                .map(item -> CartResponse.of(item.getItemName()
                        , cookieList.stream().filter(cookie -> cookie.getName().equals(String.valueOf(item.getItemSeq()))).mapToLong(cookie -> Long.parseLong(cookie.getValue())).findFirst().getAsLong()
                        , item.getPrice() * cookieList.stream().filter(cookie -> cookie.getName().equals(String.valueOf(item.getItemSeq()))).mapToLong(cookie -> Long.parseLong(cookie.getValue())).findFirst().getAsLong()
                        , Math.round(item.getPrice()
                                * cookieList.stream().filter(cookie -> cookie.getName().equals(String.valueOf(item.getItemSeq()))).mapToLong(cookie -> Long.parseLong(cookie.getValue())).findFirst().getAsLong()
                        * (100 - Membership.findByCodeName(profile.getGradeName()).getDiscountRate()) / 100.0)
                )).collect(Collectors.toList());
    }
}
