package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {

        private int port = 0;
        private ServerSocket serverSocket = null;

        public ServerThread(int port) {
            this.port = port;
            try {
                this.serverSocket = new ServerSocket(port);
            } catch (IOException ioException) {
                Log.e("tag", "An exception has occurred: " + ioException.getMessage());
            }
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
