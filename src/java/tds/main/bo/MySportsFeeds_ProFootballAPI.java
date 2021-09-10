package tds.main.bo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import tds.main.bo.football.stats.MySportsFeeds_PlayerList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class MySportsFeeds_ProFootballAPI {

    private final static String API_KEY = "d237939b-a86d-4081-8920-3d75d7";
    private final static String PASSWORD = "MYSPORTSFEEDS";
    private final static String BASE_URL = "https://api.mysportsfeeds.com/v2.1";

    public static List<MySportsFeeds_PlayerList> getPlayers() {

        String results = "";
        List<MySportsFeeds_PlayerList> players = new ArrayList<>();
        try {

//            String credential = Credentials.basic(API_KEY, PASSWORD);
//            String encoded = Base64.getEncoder().encodeToString(credential.getBytes());
//
//            OkHttpClient client = new OkHttpClient().newBuilder()
//                    .build();
//            Request request = new Request.Builder()
//                    .url(BASE_URL + "/pull/nfl/players.json")
//                    .method("GET", null)
//                    .addHeader("Authorization", "Basic " + encoded)
//                    .build();
//            Response response = client.newCall(request).execute();
//            ResponseBody body = response.body();
//            results = body.string();

//            String command = "curl --location --request GET 'https://api.mysportsfeeds.com/v2.1/pull/nfl/players.json' --header 'Authorization: Basic ZDIzNzkzOWItYTg2ZC00MDgxLTg5MjAtM2Q3NWQ3Ok1ZU1BPUlRTRkVFRFM='";
//            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
//            Process process = processBuilder.start();


//            Process process = Runtime.getRuntime().exec(command);
//            InputStream inputStream = process.getInputStream();
//            results = new BufferedReader(
//                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))
//                    .lines()
//                    .collect(Collectors.joining("\n"));

            URL url = new URL(BASE_URL + "/pull/nfl/players.json");
            URLConnection uc;
            uc = url.openConnection();

            uc.setRequestProperty("X-Requested-With", "Curl");

            String credential = API_KEY + ":" + PASSWORD;
            String basicAuth = "Basic " + Base64.getEncoder().encodeToString(credential.getBytes());
            uc.setRequestProperty("Authorization", basicAuth);

            BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));

            }
            results = builder.toString();
            if (results != null && !"".equals(results)) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode playersObj = mapper.readTree(results);

                JsonNode playersJsonArray = playersObj.get("players");

                if (playersJsonArray.isArray()) {
                    players = mapper.readValue(playersJsonArray.toString(), new TypeReference<List<MySportsFeeds_PlayerList>>(){});
                }
            }

//            process.destroy();
//            int exitCode = process.exitValue();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }

    public static void main(String[] args) {
        try {
            List<MySportsFeeds_PlayerList> players = getPlayers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
