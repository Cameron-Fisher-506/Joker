package za.co.joker.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class WSCallsUtils extends AsyncTask<String, Void, String>
{
    private WSCallsUtilsTaskCaller wsCallsUtilsTaskCaller;

    private int reqCode;

    private WSCallsUtils(WSCallsUtilsTaskCaller wsCallsUtilsTaskCaller, int reqCode)
    {
        this.wsCallsUtilsTaskCaller = wsCallsUtilsTaskCaller;
        this.reqCode = reqCode;
    }

    /**
     *
     * get is used to make GET Requests
     *
     * @param wsCallsUtilsTaskCaller Context which implements the callback method
     * @param url URL request is made to
     * @param reqCode Request code used to handled the request in the callback method
     */
    public static void get(WSCallsUtilsTaskCaller wsCallsUtilsTaskCaller, String url, int reqCode)
    {
        try
        {
            WSCallsUtils wsCallsUtils = new WSCallsUtils(wsCallsUtilsTaskCaller, reqCode);
            wsCallsUtils.execute(ConstantUtils.REQUEST_METHOD_GET, url);
        }catch(Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: WSCallsUtils - get"
                    + "\nURL: " + url
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }
    }

    /**
     * post is used to make POST Requests
     *
     * @param wsCallsUtilsTaskCaller Context which implements the callback method
     * @param url URL request is made to
     * @param body JSON to POST
     * @param reqCode Request code used to handled the request in the callback method
     */
    public static void post(WSCallsUtilsTaskCaller wsCallsUtilsTaskCaller, String url, String body, int reqCode)
    {
        try
        {
            WSCallsUtils wsCallsUtils = new WSCallsUtils(wsCallsUtilsTaskCaller, reqCode);
            wsCallsUtils.execute(ConstantUtils.REQUEST_METHOD_POST, url, body);
        }catch(Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: WSCallsUtils - post"
                    + "\nURL: " + url
                    + "\nBody: " + body
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... strings) {

        JSONObject toReturn = null;


        try
        {
            if(strings != null && strings.length > 0)
            {
                String requestMethod = strings[0];

                if(requestMethod.equals(ConstantUtils.REQUEST_METHOD_GET))
                {
                    if(strings.length > 1)
                    {
                        //GET
                        toReturn = new JSONObject();
                        String url = strings[1];
                        String response = genericGET(url);

                        toReturn.put("url", url);
                        toReturn.put("response", response);
                    }

                }else if(requestMethod.equals(ConstantUtils.REQUEST_METHOD_POST))
                {
                    if(strings.length > 2)
                    {
                        String url = strings[1];
                        //POST
                        String body = strings[2];

                        toReturn = new JSONObject();
                        String response = genericPOST(url, body);

                        toReturn.put("url", url);
                        toReturn.put("body", body);
                        toReturn.put("response", response);
                    }
                }
            }else
            {

            }

        }catch(Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: WSCallsUtils - doInBackground"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }finally
        {

        }

        if(toReturn != null)
        {
            return toReturn.toString();
        }

        return null;
    }


    @Override
    protected void onPostExecute(String s)
    {
        try
        {
            SQLiteUtils sqLiteUtils = new SQLiteUtils(this.wsCallsUtilsTaskCaller.getCallingContext());

            String url = null;
            String response = null;
            boolean isOffline = false;

            if(s != null && !s.equals(""))
            {
                JSONObject jsonObject = new JSONObject(s);
                url = jsonObject.getString("url");

                if(jsonObject.has("response"))
                {
                    //cache the response
                    isOffline = false;
                    response = jsonObject.getString("response");

                    sqLiteUtils.cacheResponse(url, response, DTUtils.getCurrentDateTime());

                }else
                {
                    isOffline = true;
                    url = jsonObject.getString("url");

                    //get from cache
                    response = sqLiteUtils.getResponse(url);
                }
            }else
            {
                //What happens if s is null
                isOffline = true;
            }

            this.wsCallsUtilsTaskCaller.taskCompleted(response, this.reqCode, isOffline);

        }catch(Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: WSCallsUtils - onPostExecute"
                    + "\nbody: " + s
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }

    }

    private String genericGET(String url)
    {
        String toReturn = null;

        HttpURLConnection connection = null;

        try
        {
            URL target = new URL(url);
            connection = (HttpURLConnection) target.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(ConstantUtils.TIMEOUT_CONNECTION);
            connection.setReadTimeout(ConstantUtils.TIMEOUT_READ);
            connection.setDoInput(true);

            connection.connect();

            if(connection != null)
            {
                if(connection.getResponseCode() >= 400)
                {
                    //Cache Data

                    Log.e(ConstantUtils.TAG, "\nError: " + connection.getResponseMessage()
                            + "\nCode: " + connection.getResponseCode()
                            + "\nURL: " + url
                            + "\nMethod: genericGET"
                            + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
                }else
                {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String output = "";
                    StringBuilder sb = new StringBuilder();

                    while((output = bufferedReader.readLine()) != null)
                    {
                        sb.append(output);
                    }
                    toReturn = sb.toString();
                }
            }

        }catch(Exception e)
        {
            //Cache Data

            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nURL: " + url
                    + "\nMethod: genericGET"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }finally
        {
            if(connection != null)
            {
                connection.disconnect();
                connection = null;
            }
        }

        return toReturn;
    }

    private String genericPOST(String url, String body)
    {
        String toReturn = null;

        HttpURLConnection connection = null;

        try
        {
            URL target = new URL(url);
            connection = (HttpURLConnection) target.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(ConstantUtils.TIMEOUT_CONNECTION);
            connection.setReadTimeout(ConstantUtils.TIMEOUT_READ);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            outputStreamWriter.write(body);
            outputStreamWriter.flush();
            outputStreamWriter.close();

            connection.connect();

            if(connection != null)
            {
                if(connection.getResponseCode() >= 400)
                {
                    //Cache Data

                    Log.e(ConstantUtils.TAG, "\nError: " + connection.getResponseMessage()
                            + "\nCode: " + connection.getResponseCode()
                            + "\nURL: " + url
                            + "\nMethod: genericPOST"
                            + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
                }else
                {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String output = "";

                    while((output = bufferedReader.readLine()) != null) {
                        sb.append(output);
                    }

                    toReturn = sb.toString();
                }
            }

        }catch(Exception e)
        {
            //Cache Data

            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nURL: " + url
                    + "\nMethod: genericPOST"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }finally
        {
            if(connection != null)
            {
                connection.disconnect();
                connection = null;
            }
        }

        return toReturn;
    }

}
