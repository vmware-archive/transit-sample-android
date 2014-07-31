package io.pivotal.android.ttc;

import android.app.Activity;
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

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class RoutesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Route.List>, AdapterView.OnItemClickListener {

    private static interface RequestCode {
        public static final int REQUEST_STOP = 0;
    }

    private RoutesAdapter mAdapter;
    private AbsListView mAdapterView;

    private Route mRoute;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_routes, container, false);
        mAdapterView = (AbsListView) view.findViewById(android.R.id.list);
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new RoutesAdapter(getActivity());

        mAdapterView.setOnItemClickListener(this);
        mAdapterView.setAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Route.List> onCreateLoader(final int id, final Bundle args) {
        Crouton.makeText(getActivity(), "Loading...", Style.INFO).show();
        return new RoutesLoader(getActivity());
    }

    @Override
    public void onLoadFinished(final Loader<Route.List> loader, final Route.List data) {
        Crouton.cancelAllCroutons();
        mAdapter.clear();
        mAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(final Loader<Route.List> loader) {
        Crouton.cancelAllCroutons();
        mAdapter.clear();
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        mRoute = (Route) parent.getItemAtPosition(position);
        StopsActivity.newInstanceForResult(this, RequestCode.REQUEST_STOP, new RouteParcel(mRoute));
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestCode.REQUEST_STOP && resultCode == Activity.RESULT_OK) {
            final Stop stop = StopsActivity.getStop(data);
            RoutesActivity.killInstanceWithResult(this, mRoute, stop);
        }
    }

}
