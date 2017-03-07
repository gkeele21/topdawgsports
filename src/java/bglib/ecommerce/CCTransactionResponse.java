package bglib.ecommerce;

public interface CCTransactionResponse
{
    public CCTransactionResult getTransactionResult();
    public String getReasonText();
    public String getAVSResultCode();
    public String getTransactionID();
    public String getMD5Hash();
    public String getResponseString();
}
