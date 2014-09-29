package io.pivotal.android.ttc.notification;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationParcel implements Parcelable {

    private Notification mNotification;

    public NotificationParcel(final Notification notification) {
        mNotification = notification;
    }

    public NotificationParcel(final Parcel in) {
        mNotification = new Notification();
        mNotification.route = in.readString();
        mNotification.stop = in.readString();
        mNotification.time = in.readString();
        mNotification.tag = in.readString();
        mNotification.enabled = in.readInt() == 1;
    }

    public Notification getNotification() {
        return mNotification;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(mNotification.route);
        dest.writeString(mNotification.stop);
        dest.writeString(mNotification.time);
        dest.writeString(mNotification.tag);
        dest.writeInt(mNotification.enabled ? 1 : 0);
    }

    public static final Parcelable.Creator<NotificationParcel> CREATOR = new Parcelable.Creator<NotificationParcel>() {
        public NotificationParcel createFromParcel(final Parcel in) {
            return new NotificationParcel(in);
        }

        public NotificationParcel[] newArray(final int size) {
            return new NotificationParcel[size];
        }
    };
}
