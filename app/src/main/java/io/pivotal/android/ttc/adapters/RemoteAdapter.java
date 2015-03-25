package io.pivotal.android.ttc.adapters;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.pivotal.android.data.DataStore;
import io.pivotal.android.data.KeyValue;
import io.pivotal.android.data.KeyValueObject;
import io.pivotal.android.data.Response;
import io.pivotal.android.ttc.Const;

public abstract class RemoteAdapter<T> extends BaseAdapter {

    private static final String COLLECTION = "notifications";
    private static final String KEY = "my-notifications";

    private final Object mLock = new Object();
    private final KeyValueObject mRemote;
    private final Context mContext;
    private final DataStore.Listener<KeyValue> mListener;

    private List<T> mItems = new ArrayList<>();

    public RemoteAdapter(final Context context) {
        mContext = context;
        mRemote = KeyValueObject.create(context, COLLECTION, KEY);
        mListener = getListener();
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
        mRemote.get(mListener);
    }

    public void sync() {
        Log.v(Const.TAG, "sync");
        mRemote.put(new Gson().toJson(mItems), mListener);
    }

    private DataStore.Listener<KeyValue> getListener() {
        return new DataStore.Listener<KeyValue>() {
            @Override
            public void onResponse(Response<KeyValue> keyValueResponse) {

                if (keyValueResponse.isSuccess()) {
                    Log.d(Const.TAG, "Response : " + keyValueResponse.object);
                    Log.d(Const.TAG, "Response key : " + keyValueResponse.object.key);
                    Log.d(Const.TAG, "Response value : " + keyValueResponse.object.value);

                    final String json = keyValueResponse.object.value;
                    final List<T> items;

                    items = new Gson().fromJson(json, getListType());
                    Log.v(Const.TAG, "sync/refresh success");
                    setItems(items);
                } else if (keyValueResponse.isUnauthorized()) {
                    Toast.makeText(mContext, "Fetch Failure: Not Authorized", Toast.LENGTH_SHORT).show();
                } else if (keyValueResponse.isFailure()) {
                    Toast.makeText(mContext, "Fetch Failure: " + keyValueResponse.error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

            }
        };
    }
}
