package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null)
            return;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            String moneda = bufferedReader.readLine();
            Log.d("tag","moneda esta " + moneda);

            HttpClient httpClient = new DefaultHttpClient();
            String pageSourceCode = "";
            String apiaddr = "https://api.coindesk.com/v1/bpi/currentprice/" + moneda + ".json";
            HttpGet get = new HttpGet(apiaddr);
            HttpResponse httpGetResponse = httpClient.execute(get);
            HttpEntity httpGetEntity = httpGetResponse.getEntity();
            if (httpGetEntity != null) {
                pageSourceCode = EntityUtils.toString(httpGetEntity);
            }

            JSONObject content = new JSONObject(pageSourceCode);
            JSONObject obj = content.getJSONObject("bpi");
            JSONObject aux = obj.getJSONObject(moneda);
            String result = aux.getString("rate");

            Log.d("tag", "[CommThread] rate = " + result);

            printWriter.println(result);
            printWriter.flush();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
