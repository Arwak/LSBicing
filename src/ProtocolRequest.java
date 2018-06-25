
import io.restassured.path.json.JsonPath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ProtocolRequest {
    private boolean error;
    /**
     *  peticio GET
     * @param url url sobre el que es vol fer la peticio
     * @return la resposta de la petició al url
     */
    public String sendGet(String url){
        error = false;
        final String userAgent = "Mozilla/5.0";
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", userAgent);

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        in.close();
            System.out.println(response.toString());
        return response.toString();
    }catch (IOException e){
        System.out.println("Error, comprova la connexio a internet");
        error = true;
    }
    return "";
    }

    public boolean isError(){
        return error;
    }
    /**
     * Realitza una peticio GET i transforma la resposta en un JsonPath
     * @param url url sobre el que es vol fer la peticio
     * @return la resposta de la petició al url amb format JsonPath
     */
    public static JsonPath requestJsonPath(String url) {
        String aux1 = null;
        ProtocolRequest http = new ProtocolRequest();

        aux1 = http.sendGet(url);


        JsonPath jp = new JsonPath(aux1);
        return jp;
    }
}