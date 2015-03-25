package io.pivotal.android.ttc.models;

import java.util.ArrayList;

public class Stop {

    public static final class List extends ArrayList<Stop> {
        private static final long serialVersionUID = 1L;
    }

    public static final class Response {
        public List stops;
    }

    public String tag;

    public String title;

    public String stopId;
}
