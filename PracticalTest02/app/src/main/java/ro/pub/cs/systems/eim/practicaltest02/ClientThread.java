package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private String address;
    private int port;
    private String moneda;
    private TextView post;

    private Socket socket;

    public ClientThread(String address, int port, String moneda, TextView post) {
        this.address = address;
        this.port = port;
        this.moneda = moneda;
        this.post = post;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e("tag", "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            if (bufferedReader == null || printWriter == null) {
                Log.e("tag", "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(moneda);
            printWriter.flush();
            String result;
            while ((result = bufferedReader.readLine()) != null) {
                final String final_result = result;
                post.post(new Runnable() {
                    @Override
                    public void run() {
                        post.setText("Rata pentru " + moneda + " este " + final_result);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e("tag", "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e("tag", "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                }
            }
        }
    }
}
