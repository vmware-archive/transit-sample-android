package io.pivotal.android.ttc.util;

import io.pivotal.android.ttc.models.RouteTitleModel;

/**
 * Created by dx181-xl on 15-03-30.
 */
public class RouteUtil {

    public static RouteTitleModel parseRouteTitle(String routeTitle) {
        int delimiterIndex = routeTitle.indexOf("-");
        String routeNumber = "";
        String routeName = routeTitle;
        if (delimiterIndex != -1) {
            routeNumber = routeTitle.substring(0, delimiterIndex);
            routeName = routeTitle.substring(delimiterIndex + 1);
        }
        return new RouteTitleModel(routeNumber, routeName);
    }
}
