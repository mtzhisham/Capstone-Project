package com.moataz.eventboard.ParserUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moataz on 1/6/2017.
 */
public class EventResponse {

   public List<Event> events;

    public  EventResponse(){
        events = new ArrayList<Event>();
    }


}
