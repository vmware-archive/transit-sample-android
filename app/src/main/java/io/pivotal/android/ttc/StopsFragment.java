package io.pivotal.android.ttc;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

public class StopsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Stop.List>, AdapterView.OnItemClickListener {

    private StopsAdapter mAdapter;
    private AbsListView mAdapterView;

    private Route mRoute;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_stops, container, false);
        mAdapterView = (AbsListView) view.findViewById(android.R.id.list);
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new StopsAdapter(getActivity());

        mAdapterView.setOnItemClickListener(this);
        mAdapterView.setAdapter(mAdapter);

        if (mRoute != null) {
            getLoaderManager().initLoader(0, null, this);
        }
    }

    public void setRoute(final Route route) {
        if (route != null) {
            mRoute = route;
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public Loader<Stop.List> onCreateLoader(final int id, final Bundle args) {
        return new StopsLoader(getActivity(), mRoute.tag);
    }

    @Override
    public void onLoadFinished(final Loader<Stop.List> loader, final Stop.List data) {
        mAdapter.clear();
        mAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(final Loader<Stop.List> loader) {
        mAdapter.clear();
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        final Stop stop = (Stop) parent.getItemAtPosition(position);
        StopsActivity.killInstanceWithResult(this, stop, new Intent());
    }
}
