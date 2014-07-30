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
import io.pivotal.android.data.data.DataObject;

public abstract class RemoteAdapter<T> extends BaseAdapter {

    private final Object mLock = new Object();
    private final DataObject mRemote;
    private final Context mContext;

    private List<T> mItems = new ArrayList<T>();

    public RemoteAdapter(final Context context) {
        mContext = context;
        mRemote = new DataObject("notifications");
        mRemote.setObjectId("my-notifications");
    }

    @Override
    public int getCount() {
        synchronized (mLock) {
            return mItems.size();
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
        Log.v("ttc", "removeItem");
        if (object != null) {
            synchronized (mLock) {
                mItems.remove(object);
            }
            Log.v("ttc", "removeItem - notifyDataSetChanged");
            notifyDataSetChanged();
            sync();
        }
    }

    public void addItem(final T object) {
        Log.v("ttc", "addItem");
        if (object != null) {
            synchronized (mLock) {
                mItems.add(object);
            }
            Log.v("ttc", "addItem - notifyDataSetChanged");
            notifyDataSetChanged();
            sync();
        }
    }

    public void addItems(final List<T> items) {
        Log.v("ttc", "addItems");
        if (items != null) {
            synchronized (mLock) {
                mItems.addAll(items);
            }
            Log.v("ttc", "addItems - notifyDataSetChanged");
            notifyDataSetChanged();
            sync();
        }
    }

    private void setItems(final List<T> items) {
        Log.v("ttc", "setItems");
        if (items != null) {
            synchronized (mLock) {
                mItems = items;
            }
            Log.v("ttc", "setItems - notifyDataSetChanged");
            notifyDataSetChanged();
        }
    }

    protected abstract Type getListType();

    public void refresh() {
        Log.v("ttc", "refresh");
        mRemote.fetch(DataStore.getInstance().getClient(mContext), new TTCListener() {

            @Override
            public void success(final DataObject object) {
                final String json = (String) object.get("items");
                final List<T> items = new Gson().fromJson(json, getListType());
                Log.v("ttc", "refresh success");
                setItems(items);
            }

            @Override
            public void unauthorized(final DataObject object) {
                Toast.makeText(mContext, "Fetch Failure: Not Authorized", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(final DataObject object, final String reason) {
                Toast.makeText(mContext, "Fetch Failure: " + reason, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sync() {
        Log.v("ttc", "sync");
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
            }

            @Override
            public void failure(final DataObject object, final String reason) {
                Toast.makeText(mContext, "Save Failure: " + reason, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
