package individual.freshplace.service;

import individual.freshplace.dto.cart.CartItem;
import individual.freshplace.dto.cart.CartResponse;
import individual.freshplace.entity.Item;
import individual.freshplace.entity.Member;
import individual.freshplace.repository.ItemRepository;
import individual.freshplace.repository.MemberRepository;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.NonExistentException;
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
public class CartReadService {

    private final static int PERCENTAGE_INTEGER = 100;
    private final static float PERCENTAGE_SHORT = 100.0f;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CartResponse getCartByNonMember(final Cookie[] cookies) {

        List<Item> items = Arrays.stream(cookies).map(cookie -> itemRepository.findById(Long.parseLong(cookie.getName()))
                .orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, cookie.getName()))).collect(Collectors.toList());
        Map<Long, Long> cookiesMap = convertArrayIntoMap(cookies);

        List<CartItem> cartItems = items.stream()
                .map(item -> new CartItem(item.getItemSeq(), item.getItemName(), cookiesMap.get(item.getItemSeq()),
                        getPriceCartItem(item.getPrice(), cookiesMap.get(item.getItemSeq())),
                        getPriceCartItem(item.getPrice(), cookiesMap.get(item.getItemSeq()))
                )).collect(Collectors.toList());

        return new CartResponse(cartItems);
    }

    @Transactional
    public CartResponse getCartByMember(final String memberId, final Cookie[] cookies) {

        List<Item> items = Arrays.stream(cookies).map(cookie -> itemRepository.findById(Long.parseLong(cookie.getName()))
                .orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, cookie.getName()))).collect(Collectors.toList());
        Map<Long, Long> cookiesMap = convertArrayIntoMap(cookies);
        final Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, memberId));

        List<CartItem> cartItems = items.stream()
                .map(item -> new CartItem(item.getItemSeq(), item.getItemName(), cookiesMap.get(item.getItemSeq()),
                        getPriceCartItem(item.getPrice(), cookiesMap.get(item.getItemSeq())),
                        getDiscountPriceCartItem(item.getPrice(), cookiesMap.get(item.getItemSeq()), member.getGradeCode().getDiscountRate())
                )).collect(Collectors.toList());

        return new CartResponse(cartItems);
    }

    private Map<Long, Long> convertArrayIntoMap(Cookie[] cookies) {
        return Arrays.stream(cookies).collect(Collectors.toMap(cookie -> Long.parseLong(cookie.getName()), cookie -> Long.parseLong(cookie.getValue())));
    }

    private long getPriceCartItem(long originalPrice, long itemCounting) {
        return originalPrice * itemCounting;
    }

    private long getDiscountPriceCartItem(long originalPrice, long itemCounting, long discountRate) {
        return Math.round(originalPrice * itemCounting * (PERCENTAGE_INTEGER - discountRate) / PERCENTAGE_SHORT);
    }
}
