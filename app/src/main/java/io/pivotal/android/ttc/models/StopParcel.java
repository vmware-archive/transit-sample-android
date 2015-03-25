package io.pivotal.android.ttc.models;

import android.os.Parcel;
import android.os.Parcelable;

public class StopParcel implements Parcelable {

    private Stop mStop;

    public StopParcel(final Stop stop) {
        mStop = stop;
    }

    public StopParcel(final Parcel in) {
        mStop = new Stop();
        mStop.tag = in.readString();
        mStop.title = in.readString();
        mStop.stopId = in.readString();
    }

    public Stop getStop() {
        return mStop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(mStop.tag);
        dest.writeString(mStop.title);
        dest.writeString(mStop.stopId);
    }

    public static final Creator<StopParcel> CREATOR = new Creator<StopParcel>() {
        public StopParcel createFromParcel(final Parcel in) {
            return new StopParcel(in);
        }

        public StopParcel[] newArray(final int size) {
            return new StopParcel[size];
        }
    };
}
