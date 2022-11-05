package individual.freshplace.service;

import individual.freshplace.dto.cart.CartOnItems;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FCartReadService {

    private final static int PERCENTAGE_INTEGER = 100;
    private final static float PERCENTAGE_SHORT = 100.0f;
    private final ItemService itemService;
    private final FProfileService fProfileService;

    @Transactional
    public CartResponse getCartByNonMember(final Cookie[] cookies) {

        List<Item> items = Arrays.stream(cookies).map(cookie -> itemService.findById(Long.parseLong(cookie.getName()))).collect(Collectors.toList());
        Map<Long, Long> cookiesMap = convertArrayIntoMap(cookies);

        List<CartOnItems> cartOnItems = items.stream()
                .map(item -> CartOnItems.of(item.getItemName(), cookiesMap.get(item.getItemSeq()), item.getPrice() * cookiesMap.get(item.getItemSeq()),
                        item.getPrice() * cookiesMap.get(item.getItemSeq()))).collect(Collectors.toList());

        return CartResponse.from(cartOnItems);
    }

    @Transactional
    public CartResponse getCartByMember(final String memberId, final Cookie[] cookies) {

        List<Item> items = Arrays.stream(cookies).map(cookie -> itemService.findById(Long.parseLong(cookie.getName()))).collect(Collectors.toList());
        Map<Long, Long> cookiesMap = convertArrayIntoMap(cookies);
        ProfileResponse profile = fProfileService.getProfile(memberId);

        List<CartOnItems> cartOnItems = items.stream()
                .map(item -> CartOnItems.of(item.getItemName(), cookiesMap.get(item.getItemSeq()), item.getPrice() * cookiesMap.get(item.getItemSeq()),
                        Math.round(item.getPrice() * cookiesMap.get(item.getItemSeq()) * (PERCENTAGE_INTEGER - Membership.findByCodeName(profile.getMembership()).getDiscountRate()) / PERCENTAGE_SHORT)
                )).collect(Collectors.toList());

        return CartResponse.from(cartOnItems);
    }

    private Map<Long, Long> convertArrayIntoMap(Cookie[] cookies) {
        return Arrays.stream(cookies).collect(Collectors.toMap(cookie -> Long.parseLong(cookie.getName()), cookie -> Long.parseLong(cookie.getValue())));
    }
}
