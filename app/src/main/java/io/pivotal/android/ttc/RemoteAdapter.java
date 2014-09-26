package io.pivotal.android.ttc;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.pivotal.android.data.DataStore;
import io.pivotal.android.data.DataObject;

public abstract class RemoteAdapter<T> extends BaseAdapter {

    public interface UnauthorizedListener {
        public void onUnauthorized();
    }

    private final Object mLock = new Object();
    private final DataObject mRemote;
    private final Context mContext;
    private final UnauthorizedListener mUnauthorizedListener;

    private List<T> mItems = new ArrayList<T>();

    public RemoteAdapter(final Context context, UnauthorizedListener unauthorizedListener) {
        mContext = context;
        mUnauthorizedListener = unauthorizedListener;
        mRemote = new DataObject("notifications");
        mRemote.setObjectId("my-notifications");
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        synchronized (mLock) {
            return mItems.size();
        }
    }

    public List<T> getItems() {
        synchronized (mLock) {
            return mItems;
        }
    }

    @Override
    public T getItem(final int position) {
        synchronized (mLock) {
            return mItems.get(position);
        }
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    public void removeItem(final T object) {
        Log.v(Const.TAG, "removeItem");
        if (object != null) {
            synchronized (mLock) {
                mItems.remove(object);
            }
            Log.v(Const.TAG, "removeItem - notifyDataSetChanged");
            notifyDataSetChanged();
            sync();
        }
    }

    public void addItem(final T object) {
        Log.v(Const.TAG, "addItem");
        if (object != null) {
            synchronized (mLock) {
                mItems.add(object);
            }
            Log.v(Const.TAG, "addItem - notifyDataSetChanged");
            notifyDataSetChanged();
            sync();
        }
    }

    public void addItems(final List<T> items) {
        Log.v(Const.TAG, "addItems");
        if (items != null) {
            synchronized (mLock) {
                mItems.addAll(items);
            }
            Log.v(Const.TAG, "addItems - notifyDataSetChanged");
            notifyDataSetChanged();
            sync();
        }
    }

    private void setItems(final List<T> items) {
        Log.v(Const.TAG, "setItems");
        if (items != null) {
            synchronized (mLock) {
                mItems = items;
            }
            Log.v("ttc", "setItems - notifyDataSetChanged");
            notifyDataSetChanged();
            onItemsChanged();
        }
    }

    public abstract void onItemsChanged();

    protected abstract Type getListType();

    public void refresh() {
        Log.v(Const.TAG, "refresh");
        mRemote.fetch(DataStore.getInstance().getClient(mContext), new TTCListener() {

            @Override
            public void success(final DataObject object) {
                final String json = (String) object.get("items");
                final List<T> items = new Gson().fromJson(json, getListType());
                Log.v(Const.TAG, "refresh success");
                setItems(items);
            }

            @Override
            public void unauthorized(final DataObject object) {
                Toast.makeText(mContext, "Fetch Failure: Not Authorized", Toast.LENGTH_SHORT).show();
                if (mUnauthorizedListener != null) {
                    mUnauthorizedListener.onUnauthorized();
                }
            }

            @Override
            public void failure(final DataObject object, final String reason) {
                // The reporting of 404 errors (i.e.: no items) are handled by NotificationsFragment
                if (!reason.contains("404")) {
                    Toast.makeText(mContext, "Fetch Failure: " + reason, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void sync() {
        Log.v(Const.TAG, "sync");
        mRemote.put("items", new Gson().toJson(mItems));
        mRemote.save(DataStore.getInstance().getClient(mContext), new TTCListener() {

            @Override
            public void success(final DataObject object) {
                final String json = (String) object.get("items");
                final List<T> items = new Gson().fromJson(json, getListType());
                Log.v("ttc", "sync success");
                setItems(items);
            }

            @Override
            public void unauthorized(final DataObject object) {
                Toast.makeText(mContext, "Save Failure: Not Authorized", Toast.LENGTH_SHORT).show();
                if (mUnauthorizedListener != null) {
                    mUnauthorizedListener.onUnauthorized();
                }
            }

            @Override
            public void failure(final DataObject object, final String reason) {
                Toast.makeText(mContext, "Save Failure: " + reason, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
