package bglib.ecommerce;

import bglib.util.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.net.URL;
import java.net.URLConnection;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AuthorizeNetCCTransaction implements CCTransaction
{
    private String _CCNumber;
    private AuDate _ExpirationDate;
    private double _Amount;

    private static String _APILoginID = Application._GLOBAL_SETTINGS.getProperty(AppSettings.CC_API_LOGINID);
    private static String _TransactionKey = Application._GLOBAL_SETTINGS.getProperty(AppSettings.CC_TRANSACTION_KEY);
    // want to make sure that they have to explicitly and correctly set TestMode to false
    private static boolean _TestMode = !Application._GLOBAL_SETTINGS.getProperty(AppSettings.CC_TEST_MODE, "true").toLowerCase().equals("false");

    public AuthorizeNetCCTransaction(String number, AuDate expirationDate, double amount) {
        if (expirationDate==null || expirationDate.isNull()) {
            throw new IllegalArgumentException("Bad expiration date: " + expirationDate);
        }
        _CCNumber=number;
        _ExpirationDate = new AuDate(expirationDate);
        _Amount=amount;
    }

    public CCTransactionResponse sendTransaction(CCTransactionType ct) {
        AuthorizeNetCCTransactionResponse ret=null;
        try{
          // standard variables for basic Java AIM test
          // use your own values where appropriate

          StringBuffer sb = new StringBuffer();

          // mandatory name/value pairs for all AIM CC transactions
          // as well as some "good to have" values
          sb.append("x_login=" + _APILoginID + "&");
          sb.append("x_tran_key=" + _TransactionKey + "&");
          sb.append("x_version=3.1&");
          if (_TestMode) {
            sb.append("x_test_request=TRUE&");             // for testing
          }
          sb.append("x_method=CC&");
          sb.append("x_type=" + ct.getApiString() + "&");
          sb.append("x_amount=" + Display.twoDecimals(_Amount) + "&");
          sb.append("x_delim_data=TRUE&");
          sb.append("x_delim_char=|&");
          sb.append("x_relay_response=FALSE&");

          // CC information
          sb.append("x_card_num=" + _CCNumber + "&");
          sb.append("x_exp_date=" + _ExpirationDate.toString("MMyy") + "&");

          // not required...but my test account is set up to require it
          sb.append("x_description=Java Transaction&");

          // open secure connection
          URL url = new URL(
//              "https://test.authorize.net/gateway/transact.dll");
         //  Uncomment the line ABOVE for test accounts or BELOW for live merchant accounts
            "https://secure.authorize.net/gateway/transact.dll");

          /* NOTE: If you want to use SSL-specific features,change to:
              HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
          */

         URLConnection connection = url.openConnection();
         connection.setDoOutput(true);
         connection.setUseCaches(false);

         // not necessarily required but fixes a bug with some servers
         connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

         System.out.println("sending: " + sb);

         // POST the data in the string buffer
         DataOutputStream out = new DataOutputStream( connection.getOutputStream() );
         out.write(sb.toString().getBytes());
         out.flush();
         out.close();

         // process and read the gateway response
         BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         String line;
         line = in.readLine();
         in.close();	                     // no more data
         System.err.println(line);
         ret = new AuthorizeNetCCTransactionResponse(line);

        }catch(Exception e){
          e.printStackTrace();
        }

        return ret;
    }
}
