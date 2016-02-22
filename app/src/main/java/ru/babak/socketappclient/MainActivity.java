package ru.babak.socketappclient;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


import com.google.gson.Gson;
import com.neovisionaries.ws.client.*;

import java.util.ArrayList;

/**
 * В проекте есть левые библиотеки, но нужны только 'com.neovisionaries.ws.client.*' и 'gson'
 */
public class MainActivity extends AppCompatActivity {

    /**
     * это лист, масси в и адаптер,
     * которые у меня не работают.
     */
    ListView messageListView;
    ArrayList<String> messagesList;
    ArrayAdapter<String> adapter;

    /**
     * это единственный экземпляр сетевого класса,
     * во всех методах работаем с ним.
     */
    //TODO: добавить в каждый метод проверку, работает ли этот класс, и если нет, пересоздавать
    private socketThread mSocjetThread;

    /**
     * "Singleton Design Pattern"
     * это + setInstance(this) ниже создаёт инстанс
     * для обращения к этой активности из других классов
     * в нашем случае, из сетевого класса
     */
    public static MainActivity instance;
    public static MainActivity getInstance() {
        return instance;
    }
    public static void setInstance(MainActivity instance) {
        MainActivity.instance = instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("DOC: ", "socketThread onCreate");
        WebSocketFactory factory = new WebSocketFactory();

        setInstance(this);

    }

    @Override
    protected void onResume() {
        super.onResume();


        mSocjetThread = new socketThread();
        mSocjetThread.execute("params");

        Log.i("DOC: ", "socketThread onResume");
        setInstance(this);
    }


    /**
     * отправка  текстовых сообщений, берёт текст из поля и вызывает метод сетевго класса для отправки
     */
    public void sendMethod(View view) {

        EditText mEditText = (EditText)findViewById( R.id.messageEditText );
        String message = mEditText.getText().toString().trim();
        if ( mSocjetThread.sendMessage( message ) ){
            mEditText.setText(null);

        }

    }

    /**
     * приём текстовых сообщений,
     * сюда пориходят строки jSon,
     * хотелось бы их разложить обратно в объекты типа Message
     * добавить в коллекцию, типизированную Messagе,
     * и из этой коллекции показывать и обновлять в listView
     */
    public void recieveMethod( String message ) {
        System.out.println("recieveMethod message 1 " + message );

        Gson mGson = new Gson();
        Message mMessage = mGson.fromJson( message, Message.class );

        if ( mMessage.getAction().equals("add") && !mMessage.getName().isEmpty() ) {
            System.out.println("recieveMethod message 2 ");
            String thisName = mMessage.getName();
            messagesList.add( thisName );

            messagesList.add("test 1");
            messagesList.add("test 2");

            System.out.println("---------------------- " + thisName );
            for (String item : messagesList) {
                System.out.println(item);
            }


            messageListView = (ListView)findViewById( R.id.messagesList );
            adapter = new ArrayAdapter<String>( this,
                    R.layout.message_item,
                    R.id.message_item_id,
                    messagesList );
            messageListView.setAdapter(adapter);

            adapter.notifyDataSetChanged();

        }

    }

}

