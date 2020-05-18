package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerThread extends Thread {

        private int port = 0;
        private ServerSocket serverSocket = null;
        private int hour = 0, min = 0;
        private String eurrate, usdrate;

        public ServerThread(int port) {
            this.port = port;
            try {
                this.serverSocket = new ServerSocket(port);
            } catch (IOException ioException) {
                Log.e("tag", "An exception has occurred: " + ioException.getMessage());
            }
        }

        public void setUSD(String rate) {
            this.usdrate = rate;
        }

        public String getUSD() {
            return usdrate;
        }

        public void setEUR(String rate) {
            this.eurrate = rate;
        }

        public String getEUR() {
            return eurrate;
        }

        public void setTime(int hour, int min) {
            this.hour = hour;
            this.min = min;
        }

        public int getTime() {
            return hour * 100 + min;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getPort() {
            return port;
        }

        public void setServerSocket(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        public ServerSocket getServerSocket() {
            return serverSocket;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Socket socket = serverSocket.accept();
                    CommunicationThread communicationThread = new CommunicationThread(this, socket);
                    communicationThread.start();
                }
            } catch (IOException ioException) {
                Log.e("tag", "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
            }
        }

        public void stopThread() {
            interrupt();
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException ioException) {
                    Log.e("tag", "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
                }
            }
        }
}
