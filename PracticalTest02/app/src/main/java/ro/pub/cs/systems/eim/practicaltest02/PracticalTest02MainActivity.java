package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    EditText srvPort   = null;
    EditText clPort    = null;
    EditText clMoneda  = null;
    EditText clAddress = null;
    TextView clPost    = null;

    ServerThread st = null;
    ClientThread ct = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        srvPort = findViewById(R.id.srvPort);
        clPort = findViewById(R.id.clPort);
        clMoneda = findViewById(R.id.clMoneda);
        clAddress = findViewById(R.id.clAddress);
        clPost = findViewById(R.id.clPost);
    }

    @Override
    protected void onDestroy() {
        if (st != null) {
            st.stopThread();
        }
        super.onDestroy();
    }

    public void serverBtn(View View) {
        if (st != null) return;
        Toast.makeText(getApplicationContext(), "Server created", Toast.LENGTH_SHORT).show();
        String serverPort = srvPort.getText().toString();
        st = new ServerThread(Integer.parseInt(serverPort));
        st.start();
    }

    public void clBtn(View View) {
        String clientAddress = clAddress.getText().toString();
        String clientPort = clPort.getText().toString();
        if (clientAddress == null || clientAddress.isEmpty()
                || clientPort == null || clientPort.isEmpty()) {
            Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (st == null || !st.isAlive()) {
            Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
            return;
        }
        String moneda = clMoneda.getText().toString();
        if (moneda == null || moneda.isEmpty()) {
            Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client should be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        clPost.setText("");

        Toast.makeText(getApplicationContext(), "Client created", Toast.LENGTH_SHORT).show();
        ct = new ClientThread(clientAddress, Integer.parseInt(clientPort), moneda, clPost);
        ct.start();
    }
}
