package za.co.joker;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import za.co.joker.objs.Joke;

public class JokeUnitTest
{
    @Test
    public void jokePopulate()
    {
        try
        {
            String strJSONObject = "{\n" +
                    "  \"categories\": [\n" +
                    "    \"animal\"\n" +
                    "  ],\n" +
                    "  \"created_at\": \"2020-01-05 13:42:19.104863\",\n" +
                    "  \"icon_url\": \"https://assets.chucknorris.host/img/avatar/chuck-norris.png\",\n" +
                    "  \"id\": \"zjuwql5ns-mklqumqezlhg\",\n" +
                    "  \"updated_at\": \"2020-01-05 13:42:19.104863\",\n" +
                    "  \"url\": \"https://api.chucknorris.io/jokes/zjuwql5ns-mklqumqezlhg\",\n" +
                    "  \"value\": \"Chuck Norris can skeletize a cow in two minutes.\"\n" +
                    "}";

            JSONObject jsonObject = new JSONObject(strJSONObject);

            Joke joke = new Joke();
            joke.populate(jsonObject); // Test the populate method

            JSONObject jsonObject1 = joke.toJSONObject(); //Test the toJSONObject method

            System.out.println(jsonObject1.toString());
        }catch(JSONException e)
        {
            e.printStackTrace();
        }

    }
}
