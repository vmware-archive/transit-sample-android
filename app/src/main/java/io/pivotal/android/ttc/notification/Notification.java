package io.pivotal.android.ttc.notification;

import java.util.ArrayList;

public class Notification {

    public static final class List extends ArrayList<Notification> {
        private static final long serialVersionUID = 1L;
    }

    public String route;

    public String stop;

    public String time;

    public String tag;

    public boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;

        if (route != null ? !route.equals(that.route) : that.route != null) return false;
        if (stop != null ? !stop.equals(that.stop) : that.stop != null) return false;
        if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = route != null ? route.hashCode() : 0;
        result = 31 * result + (stop != null ? stop.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        return result;
    }
}
