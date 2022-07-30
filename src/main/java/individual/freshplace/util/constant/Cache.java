package individual.freshplace.util.constant;

public enum Cache {

    GRADE("grade");

    private String cacheName;

    Cache(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getCache() {
        return this.cacheName;
    }
}
