import okhttp3.*;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class TestAPICall {

    public static final String API_URL = "https://api.openai.com/v1";

    private static final String API_TOKEN = System.getenv("API_TOKEN");

    public static String getApiToken() {
        return API_TOKEN;
    }

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType,
                "{\"model\":\"gpt-3.5-turbo-instruct\"," +
                "\"prompt\":\"Say this is a test\"," +
                "\"temperature\":0.7}");

        Request request = new Request.Builder()
                .url(API_URL + "/completions")
                .addHeader("Authorization", "Bearer " + API_TOKEN)
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build();

        System.out.println(API_TOKEN);
        System.out.println(request);

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response);
            JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt("status_code") == 200) {
                String responseString = response.body().string();
                System.out.println(responseString);
            }

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }
}