package individual.freshplace.util.constant;

public class Cache {

    public static final long DEFAULT_EXPIRE_SECONDS = -1;

    public static final String GRADE = "grade";

    public static final String CATEGORY = "category";
    public static final String MAIN_CATEGORY_KEY = "'mainCategory'";    //SPEL 표현식
    public static final long CATEGORY_EXPIRE = 7L;

    public static final String SUB_CATEGORY = "subCategory";
    public static final long SUB_CATEGORY_EXPIRE = 1L;

    public static final String ITEMS_BY_CATEGORY = "items";
    public static final long ITEMS_EXPIRE = 1L;

    public static final String ITEM = "item";
    public static final long ITEM_EXPIRE = 1L;

    public static final String ADDRESSES = "addresses";
    public static final long ADDRESS_EXPIRE = 1L;

    public static final String PROFILE = "profile";
    public static final long PROFILE_EXPIRE = 3L;

    public static final String REFRESH_TOKEN = "refreshToken";
    public static final long REFRESH_TOKEN_EXPIRE = 14L;
}
