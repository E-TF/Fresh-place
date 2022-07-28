package individual.freshplace.util.constant;

public enum GradeCode {

    GENERAL("G001");

    private String codeValue;

    GradeCode(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getGradeCode() {
        return this.codeValue;
    }
}
