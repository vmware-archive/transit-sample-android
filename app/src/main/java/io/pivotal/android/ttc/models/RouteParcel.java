package io.pivotal.android.ttc.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RouteParcel implements Parcelable {

    private Route mRoute;

    public RouteParcel(final Route route) {
        mRoute = route;
    }

    public RouteParcel(final Parcel in) {
        mRoute = new Route();
        mRoute.tag = in.readString();
        mRoute.title = in.readString();
    }

    public Route getRoute() {
        return mRoute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(mRoute.tag);
        dest.writeString(mRoute.title);
    }

    public static final Creator<RouteParcel> CREATOR = new Creator<RouteParcel>() {
        public RouteParcel createFromParcel(final Parcel in) {
            return new RouteParcel(in);
        }

        public RouteParcel[] newArray(final int size) {
            return new RouteParcel[size];
        }
    };
}
