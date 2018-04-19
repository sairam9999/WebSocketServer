package com.example.svankayalapati.websocket;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

import static android.R.attr.port;


public class MainActivity extends Activity {
    WebSocketServer ws = null;
    int port = 9999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ws = new WebSocketServer(new InetSocketAddress( port )) {
            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                conn.send("Welcome to the server!"); //This method sends a message to the new client
                broadcast( "new connection: " + handshake.getResourceDescriptor() ); //This method sends a message to all clients connected
                Log.d("Web Socket",  conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!" );
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                broadcast( conn + " has left the room!" );
                Log.d("Web Socket",  conn + " has left the room!" );
            }

            @Override
            public void onMessage(WebSocket conn, String message) {
                broadcast( message );
                Log.d("Web Socket",  conn + ": " + message );
            }

            @Override
            public void onError(WebSocket conn, Exception ex) {
                ex.printStackTrace();
                if( conn != null ) {
                    // some errors like port binding failed may not be assignable to a specific websocket
                }
            }

            @Override
            public void onStart() {
                Log.d("Web Socket", "Server started!");
            }
        };

        WebSocketImpl.DEBUG = true;
        ws.start();
        Log.d("Web Socket",  "ChatServer started on port: " + ws.getPort() );

//
    }
}
