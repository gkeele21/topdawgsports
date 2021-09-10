package tds.fantasy.scripts;

public class FootballStats_ProFootballAPI {

    private static final String APIKEY = "d237939b-a86d-4081-8920-3d75d7";
    private static final String DOMAIN = "https://profootballapi.com";
//    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private static void getSchedule() {
        System.out.println("Start of authenticate...");

//        try {
//            String baseURL = DOMAIN + "/schedule";
//            String queryString = "";
//            String url = "https://www.brokersumo.com/api/v1/agent/update-kvcore-roster-only-by-email" + queryString;
//            System.out.println("URL : " + url);
//
//            String json = "";
//
//            final CloseableHttpClient httpclient = HttpClients.createDefault();
//
//            final HttpPost httppost = new HttpPost(url);
//
//                final File file = new File(args[0]);
//
//                final InputStreamEntity reqEntity = new InputStreamEntity(
//                        new FileInputStream(file), -1, ContentType.APPLICATION_OCTET_STREAM);
//                // It may be more appropriate to use FileEntity class in this particular
//                // instance but we are using a more generic InputStreamEntity to demonstrate
//                // the capability to stream out data from any arbitrary source
//                //
//                // FileEntity entity = new FileEntity(file, "binary/octet-stream");
//
//                httppost.setEntity(reqEntity);
//
//                System.out.println("Executing request " + httppost.getMethod() + " " + httppost.getUri());
//                try (final CloseableHttpResponse response = httpclient.execute(httppost)) {
//                    System.out.println("----------------------------------------");
//                    System.out.println(response.getCode() + " " + response.getReasonPhrase());
//                    System.out.println(EntityUtils.toString(response.getEntity()));
//                }
//            }
//            System.out.println("Done");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public static void main(String[] args) {
        System.out.println("Starting...");
        try {
            getSchedule();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
