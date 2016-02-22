package ru.babak.socketappclient;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.neovisionaries.ws.client.OpeningHandshakeException;
import com.neovisionaries.ws.client.StatusLine;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * Created by docto on 19.02.2016.
 * Это поток, в котором выполняется сетевая активность
 */
public class socketThread extends AsyncTask<String, Void, Bitmap> {
    private WebSocket ws;
    private static final String SERVER = "ws://91.201.42.22:8080/WebApplication3/actions";
    private static final int TIMEOUT = 5000;

    private static WebSocket connect()
    {
        return null;
    }


    /**
     * Wrap the standard input with BufferedReader.
     */
    private static BufferedReader getInput() throws IOException
    {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    public boolean sendMessage ( String text ) {
        Message newMessage = new Message();
        String jsonText = newMessage.toJson( text );
        ws.sendText( jsonText );

        return true;
    }

    /**
     * Выполнение в бекграунде,
     * params не задействованы
     */
    @Override
    protected Bitmap doInBackground(String... params) {

        try {
            this.ws = new WebSocketFactory()
                    .setConnectionTimeout(TIMEOUT)
                    .createSocket(SERVER)
                    .addListener(new WebSocketAdapter() {

                        // Получение сообщения с сервера -- сейчас просто выводится в консоль Android Studio
                        public void onTextMessage(WebSocket websocket, String message) {
                            System.out.println(message);

                            MainActivity mMainActivity = MainActivity.getInstance();
                            mMainActivity.recieveMethod( message );

                        }
                    })
                    .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
                    .connect();


        } catch (OpeningHandshakeException e) {
            //это полезная обработка ошибок handshake , но у нас его нет

            // Status line.
            StatusLine sl = e.getStatusLine();
            System.out.println("=== Status Line ===");
            System.out.format("HTTP Version  = %s\n", sl.getHttpVersion());
            System.out.format("Status Code   = %d\n", sl.getStatusCode());
            System.out.format("Reason Phrase = %s\n", sl.getReasonPhrase());

            // HTTP headers.
            Map<String, List<String>> headers = e.getHeaders();
            System.out.println("=== HTTP Headers ===");
            for (Map.Entry<String, List<String>> entry : headers.entrySet())
            {
                // Header name.
                String name = entry.getKey();

                // Values of the header.
                List<String> values = entry.getValue();

                if (values == null || values.size() == 0)
                {
                    // Print the name only.
                    System.out.println(name);
                    continue;
                }

                for (String value : values)
                {
                    // Print the name and the value.
                    System.out.format("%s: %s\n", name, value);

                }
            }
        } catch (WebSocketException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }

        return null;
    }




}
