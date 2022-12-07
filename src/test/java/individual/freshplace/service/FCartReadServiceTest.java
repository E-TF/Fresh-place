package individual.freshplace.service;

import individual.freshplace.dto.cart.CartItem;
import individual.freshplace.dto.profile.ProfileResponse;
import individual.freshplace.entity.Item;
import individual.freshplace.entity.Member;
import individual.freshplace.util.CookieUtils;
import individual.freshplace.util.constant.code.category.SubCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.Cookie;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FCartReadServiceTest {

    @InjectMocks
    private FCartReadService fCartReadService;

    @Mock
    private ItemService itemService;

    @Mock
    private FProfileService fProfileService;

    @Test
    @DisplayName("비회원의 장바구니 상품은 원가와 할인가가 동일하다.")
    void sameProductsCostAndDiscountPriceIntoCartOfNonMember() {
        //given
        Item item = item();
        given(itemService.findById(anyLong())).willReturn(item);
        Cookie[] cookies = cookies();

        //when
        CartItem cartItem = fCartReadService.getCartByNonMember(cookies).getCartItems().get(0);

        //then
        assertEquals(cartItem.getPrice(), cartItem.getDiscountPrice());
    }

    @Test
    @DisplayName("회원의 장바구니 상품은 원가와 할인가가 달라야한다.")
    void differentProductsCostAndDiscountPriceIntoCartOfMember() {
        //given
        Item item = item();
        ProfileResponse profileResponse = profileResponse();
        given(itemService.findById(anyLong())).willReturn(item);
        given(fProfileService.getProfile(anyString())).willReturn(profileResponse);

        Cookie[] cookies = cookies();

        //when
        CartItem cartItem = fCartReadService.getCartByMember(profileResponse.getMemberId(), cookies).getCartItems().get(0);

        //then
        assertNotEquals(cartItem.getPrice(), cartItem.getDiscountPrice());
    }

    private Member member() {
        return Member.builder().memberId("testId").memberBirth(LocalDate.of(2000, 1, 29)).memberName("testName")
                .email("test@test.com").password("1234").phoneNumber("000-0000-0000").build();
    }

    private Item item() {
        return Item.builder().itemSeq(1L).itemName("커피").description("향긋한 원두커피").price(3900).inventory(2000)
                .origin("원두:미국산").subCategory(SubCategory.COF_DRI).build();
    }

    private Cookie[] cookies() {
        return new Cookie[]{CookieUtils.createCookie("1", "1")};
    }

    private ProfileResponse profileResponse() {
        return ProfileResponse.from(member());
    }
}
