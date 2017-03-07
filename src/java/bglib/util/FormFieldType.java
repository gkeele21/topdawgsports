package bglib.util;

public enum FormFieldType {
    STRING, DATE, INT, NULLABLE_INT, DOUBLE, NULLABLE_DOUBLE;

    public boolean isNumber() {
        switch (this) {
            case DOUBLE:
            case INT:
            case NULLABLE_DOUBLE:
            case NULLABLE_INT:
                return true;
            default:
                return false;
        }
    }
}
