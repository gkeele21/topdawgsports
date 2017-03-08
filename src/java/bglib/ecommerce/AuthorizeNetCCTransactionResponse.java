package bglib.ecommerce;

import bglib.util.AuDate;
import bglib.util.Application;
import bglib.util.AppSettings;
import bglib.util.Display;

import java.net.URL;
import java.net.URLConnection;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AuthorizeNetCCTransactionResponse implements CCTransactionResponse
{
    public static final String RESPONSE_DELIMITER = "\\|";
    String _ResponseStr;
    CCTransactionResult _Result;
    int _ResponseReasonCode;
    String _ResponseReasonText;
    String _ApprovalCode;
    String _AVSResultCode;
    String _TransactionID;
    String _MD5Hash;

    public AuthorizeNetCCTransactionResponse(String response) {
        _ResponseStr = response;
        parse(response);
    }

    private void parse(String response) {
        String[] responses = response.split(RESPONSE_DELIMITER);

        switch(Integer.parseInt(responses[0])) {
            case 1: _Result = CCTransactionResult.APPROVED; break;
            case 2: _Result = CCTransactionResult.DECLINED; break;
            case 3: _Result = CCTransactionResult.ERROR; break;
        }

        _ResponseReasonCode = Integer.parseInt(responses[2]);
        _ResponseReasonText = responses[3];
        _ApprovalCode = responses[4];
        _AVSResultCode = responses[5];
        _TransactionID = responses[6];
        _MD5Hash = responses[37];
    }

    public CCTransactionResult getTransactionResult() { return _Result; }
    public String getReasonText() { return _ResponseReasonText; }
    public String getAVSResultCode() { return _AVSResultCode; }
    public String getTransactionID() { return _TransactionID; }
    public String getMD5Hash() { return _MD5Hash; }
    public String getResponseString() { return _ResponseStr; }
}
