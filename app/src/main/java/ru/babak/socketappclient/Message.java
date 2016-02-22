package ru.babak.socketappclient;

import com.google.gson.*;

/**
 * Created by docto on 19.02.2016.
 * это объект Сообщение,
 * в нём сейчас левые поля для нашего левого серверного примера с девайсами,
 * надо привести к нормальным полям
 *
 * id
 * timestamp
 * author_id
 * status
 * text
 *
 */
public class Message {
    public String action = "add";
    public String type = "Appliance";
    public String description = "";

    public String name;

    public String getName() {
        return name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean setName( String name ) {
        this.name = name;
        return true;
    }

    public String toJson( String name ) {
        this.setName( name );

        Gson mGson = new Gson();
        return mGson.toJson( this );

    }

}
