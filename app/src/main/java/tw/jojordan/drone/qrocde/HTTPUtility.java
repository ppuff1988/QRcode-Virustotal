package tw.jojordan.drone.qrocde;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Jordan on 2016/1/4.
 */
public class HTTPUtility {

    private static HTTPUtility httpUtility ;
    private final String USER_AGENT = "Mozilla/5.0";

    public static HTTPUtility getInstance(){
        if( httpUtility == null){
            httpUtility = new HTTPUtility();
        } return httpUtility;
    }

    public String sendPost(String scan_url) throws Exception {


        String url = "https://www.virustotal.com/vtapi/v2/url/report";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "apikey=3f326884b72ba638ede12b38b259f38922e6643c7137bb2e23b091a9ce74ff23" + "&resource=" +scan_url.toString();

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

        return response.toString();
    }

}
