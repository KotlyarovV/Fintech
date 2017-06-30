package newtwork_working;

import android.os.AsyncTask;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;

/**
 * Created by vitaly on 25.06.17.
 */

public class HttpConnectionTask extends AsyncTask<String, Void, String> {

    TypeRequest request;
    private static String IP = "http://188.120.244.56";

    private static String authorizeURL = IP + "/qiwi/authorize";
    private static String getTokenURL = IP + "/qiwi/access_token";
    private static String sendNameURL = IP + "/user/setName";
    private static String getNameURL = IP + "/user/getName";
    private static  String findUserURL = IP + "/user/findUser";
    private static String getUsersURL = IP + "/chat/getChats";
    private static String payURL = IP + "/qiwi/sendmoney";
    /**
    must have a phone number in the end
     */
    private static String URL_PARAMETRS_AUTHORIZE = "username=7";

    /**
     * после данной строки идет <результат предыдущего запроса>&vcode=<код из смс>
    */
    private static String URL_PARAMETRS_GET_TOKEN = "client_id=qw-fintech&client_secret=Xghj!" +
            "bkjv64&client-software=qw-fintech-0.0.1&grant_type=urn:qiwi:oauth:" +
            "grant-type:vcode&code=";

    /**
     * после строки - номер телефона
     */
    private static String URL_PARAMETRS_GET_NAME = "phone=";

    /**
     *
     * "phone=<phonenumber>&name=<Last+First name>";
     */
    private static String URL_PARAMETRS_SEND_NAME = "phone=";
    private static String URL_PARAMETRS_FIND_USER = "phone=test&access_token=test&query=";
    private static String URL_PARAMETRS_GET_USERS = "phone=";



    public HttpConnectionTask(TypeRequest request) {
        this.request = request;
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            String url = "";
            String urlParameters = "";

            switch (this.request) {
                case GetSms:
                    url = authorizeURL;
                    urlParameters = URL_PARAMETRS_AUTHORIZE + params[0];
                    break;

                case GetToken:
                    url = getTokenURL;
                    urlParameters = URL_PARAMETRS_GET_TOKEN + params[0] + "&vcode=" + params[1];
                    break;

                case GetName:
                    url = getNameURL;
                    urlParameters = URL_PARAMETRS_GET_NAME + params[0] + "&access_token=" + params[1];
                    break;

                case SendName:
                    url = sendNameURL;
                    urlParameters = URL_PARAMETRS_SEND_NAME +params[0] + "&name=" + params[1] +
                            "&access_token=" + params[2];
                    break;

                case FindUser:
                    url = findUserURL;
                    urlParameters = URL_PARAMETRS_FIND_USER + params[0];
                    break;

                case GetUsers:
                    url = getUsersURL;
                    urlParameters = URL_PARAMETRS_GET_USERS + params[0] + "&access_token=" + params[1];
                    break;

                case Pay:
                    url = payURL;
                    urlParameters = "phone=" + params[0] + "&access_token=" + params[1] +
                            "&receiver=" + params[2] + "&amount=" + params[3];
            }

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Accept", "*/*");
            con.setRequestProperty("Accept-Encoding", "gzip, deflate, compress");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("User-Agent", "HTTPie/0.3.0");


            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            if (responseCode == 401) {
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String jsonString = response.toString();
            return jsonString;
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    @Override
    public void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
