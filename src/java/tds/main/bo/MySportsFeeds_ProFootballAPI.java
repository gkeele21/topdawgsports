package tds.main.bo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import tds.fantasy.scripts.FootballStats_ProFootballAPI;
import tds.main.bo.football.stats.MySportsFeeds.Players_PlayerObj;
import tds.main.bo.football.stats.MySportsFeeds.Stats_PlayerObj;

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

    private final static String PLAYED_STATUS_COMPLETED = "COMPLETED";
    private final static String PLAYED_STATUS_UNPLAYED = "UNPLAYED";

    public static List<Players_PlayerObj> getPlayers() {

        String results = "";
        List<Players_PlayerObj> players = new ArrayList<>();
        try {
            results = getAPIResults("/pull/nfl/players.json");

            if (results != null && !"".equals(results)) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode playersObj = mapper.readTree(results);

                JsonNode playersJsonArray = playersObj.get("players");

                if (playersJsonArray.isArray()) {
                    players = mapper.readValue(playersJsonArray.toString(), new TypeReference<List<Players_PlayerObj>>(){});
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }

    private static void processPlayerStats(Stats_PlayerObj playerObj, Player player, int seasonweekid) {
        try {
            System.out.println("Inserting stats for " + player.getFirstName() + " " + player.getLastName());
            if (player.getPositionID() >= 1 && player.getPositionID() < 6) {
                // Offensive player
                FootballStats_ProFootballAPI.saveFootballStatsTable_Offense(playerObj, player, seasonweekid);
            } else if (player.getPositionID() >= 9 && player.getPositionID() < 12) {
                // Defensive player
                FootballStats_ProFootballAPI.saveFootballStatsTable_Defense(playerObj, player, seasonweekid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processAwayHomeNode(JsonNode teamNode, ObjectMapper mapper, int seasonweekid) throws Exception {
        JsonNode teamStatsArray = teamNode.get("teamStats");
        JsonNode playersArray = teamNode.get("players");

        if (playersArray.isArray()) {
            List<Stats_PlayerObj> players = mapper.readValue(playersArray.toString(), new TypeReference<List<Stats_PlayerObj>>(){});

            for (Stats_PlayerObj playerObj : players) {
                // grab Player obj for this player
                int extId = playerObj.getPlayer().getId();
                Player player = Player.createFromStatsID2(String.valueOf(extId));
                if (player == null || player.getPlayerID() < 1) {
                    continue;
                }

                processPlayerStats(playerObj, player, seasonweekid);
            }
            System.out.println("All player stats are done.");
        }
    }

    private static void getPlayerStatsFromGame(JsonNode statsNode, ObjectMapper mapper, int seasonweekid) {
        try {
            JsonNode homeNode = statsNode.get("home");
            JsonNode awayNode = statsNode.get("away");

            processAwayHomeNode(homeNode, mapper, seasonweekid);
            processAwayHomeNode(awayNode, mapper, seasonweekid);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getIndividualGameStats(Game game, int seasonweekid) {
        if (game.getExternalGameID() == null || "".equals(game.getExternalGameID())) {
            return "ERROR: That game has no external Id set.";
        }

        try {
            String results = getAPIResults("/pull/nfl/current/games/" + game.getExternalGameID() + "/boxscore.json");

            if (results != null && !"".equals(results)) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode gameObj = mapper.readTree(results);

                JsonNode gameNode = gameObj.get("game");
                JsonNode scoringNode = gameObj.get("scoring");
                JsonNode statsNode = gameObj.get("stats");
                JsonNode referencesNode = gameObj.get("references");

                // gameNode stuff
                String gameStatus = gameNode.get("playedStatus").asText();

                getPlayerStatsFromGame(statsNode, mapper, seasonweekid);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "";
    }

    private static String getAPIResults(String path) {
        String results = "";
        try {
            URL url = new URL(BASE_URL + path);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public static void main(String[] args) {
        try {
//            List<Players_PlayerObj> players = getPlayers();
//            Game game = new Game(19502);
//            game.getStats();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
