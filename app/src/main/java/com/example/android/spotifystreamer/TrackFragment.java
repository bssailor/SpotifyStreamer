package com.example.android.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;


/**
 * A fragment containing the list view of top ten tracked for selected for artist.
 */
public class TrackFragment extends Fragment {

    private TrackAdapter trackAdapter;

    private ArrayList<TrackStorage> trackList;

    // Initialize data for adapter while waiting on data to return from AsyncTask
    private TrackStorage[] TrackStorages = new TrackStorage[]{};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("tracks")) {

            // Task to retrieve top ten tracks for selected artist
            String id = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);

            // Check for internet connectivity before fetching track data
            ConnectivityManager cm =
                    (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            if (isConnected) {
                FetchTrackTask trackTask = new FetchTrackTask();
                trackTask.execute(id);
            } else {
                Toast.makeText(getActivity(), "No internet connectivity", Toast.LENGTH_SHORT).show();
            }

            trackList = new ArrayList<TrackStorage>(Arrays.asList(TrackStorages));
        } else {
            trackList = savedInstanceState.getParcelableArrayList("tracks");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("tracks", trackList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tracks, container, false);

        trackAdapter = new TrackAdapter(getActivity(), trackList);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listView_tracks);
        listView.setAdapter(trackAdapter);

        return rootView;
    }

    public class FetchTrackTask extends AsyncTask<String, Void, Tracks> {

        private final String LOG_TAG = FetchTrackTask.class.getSimpleName();

        @Override
        protected Tracks doInBackground(String... params) {

            // if there's no id, there's nothing to look up. Verify size of params
            if (params.length == 0) {
                return null;
            }

            // Query spotify for top ten tracks for selected artist
            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();
            Map<String, Object> options = new HashMap<>();
            options.put("country", "US");
            try {
                return spotifyService.getArtistTopTrack(params[0], options);
            } catch (RetrofitError retrofitError) {
                Log.e("Retrofit Error", retrofitError.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Tracks tracks) {
            // If no tracks returned from search, clear adapter and notify user
            // Otherwise, populate adapter
            if (tracks == null || tracks.tracks.isEmpty()) {
                trackAdapter.clear();
                Toast.makeText(getActivity().getApplicationContext(), "Track not found", Toast.LENGTH_SHORT).show();
            } else {
                trackAdapter.clear();
                for (Track track : tracks.tracks) {
                    // Substitute placeholder image if no images returned with search
                    String image;
                    if (track.album.images.isEmpty()) {
                        image = null;
                    } else {
                        image = track.album.images.get(0).url;
                    }
                    trackAdapter.add(new TrackStorage(image, track.name, track.album.name));
                }
            }
        }
    }
}
