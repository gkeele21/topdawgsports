package bglib.ecommerce;

public enum CCTransactionType {
    AN_AUTHORIZE("AUTH_ONLY"), AN_CAPTURE("CAPTURE_ONLY"), AN_AUTHORIZE_AND_CAPTURE("AUTH_CAPTURE");

    String _ApiString;

    CCTransactionType(String apiString) {
        _ApiString = apiString;
    }

    public String getApiString() { return _ApiString; }
}
