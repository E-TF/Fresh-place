package individual.freshplace.util.constant;

public enum LockPrefix {

    SIGNUP("signup"),
    UPDATE_MEMBER("updateMember");

    private String methodName;

    LockPrefix(String methodName) {
        this.methodName = methodName;
    }

    public String getLockPrefix() {
        return this.methodName;
    }
}
