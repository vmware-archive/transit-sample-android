package io.pivotal.android.ttc.models;

/**
 * Created by dx181-xl on 15-03-30.
 */
public class RouteTitleModel {

    private String mRouteNumber;
    private String mRouteName;

    public RouteTitleModel(String routeNumber, String routeName) {
        this.mRouteNumber = routeNumber;
        this.mRouteName = routeName;
    }

    public String getRouteNumber() {
        return mRouteNumber;
    }

    public void setRouteNumber(String routeNumber) {
        mRouteNumber = routeNumber;
    }

    public String getRouteName() {
        return mRouteName;
    }

    public void setRouteName(String routeName) {
        mRouteName = routeName;
    }
}
