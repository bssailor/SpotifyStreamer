package com.example.android.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A fragment containing the list view of searched for artists.
 */
public class ArtistFragment extends Fragment {

    private ArtistAdapter artistAdapter;

    private ArrayList<nArtist> artistList;

    // Initialize data for adapter while waiting on data to return from AsyncTask
    private nArtist[] nArtists = new nArtist[]{};

    public ArtistFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("artists")) {
            artistList = new ArrayList<nArtist>(Arrays.asList(nArtists));
        } else {
            artistList = savedInstanceState.getParcelableArrayList("artists");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("artists", artistList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_artist, container, false);

        artistAdapter = new ArtistAdapter(getActivity(), artistList);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_artist);
        listView.setAdapter(artistAdapter);

        // Respond to selection of artist in listview.
        // Start top ten tracks activity based on artist selection.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                nArtist artist = artistAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, artist.artistId)
                        .putExtra("Artist", artist.artistName);
                startActivity(intent);
            }
        });

        // When user clicks "Done", search for artists matching EditText string
        final EditText editText = (EditText) rootView.findViewById(R.id.artist_search);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            // Task to retrieve top artists matching search string
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    // Check for internet connectivity before fetching artist data
                    ConnectivityManager cm =
                            (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();

                    if (isConnected) {
                        FetchArtistTask artistTask = new FetchArtistTask();
                        artistTask.execute(v.getText().toString());
                    } else {
                        Toast.makeText(getActivity(), "No internet connectivity", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        return rootView;
    }

    public class FetchArtistTask extends AsyncTask<String, Void, ArtistsPager> {

        private final String LOG_TAG = FetchArtistTask.class.getSimpleName();

        @Override
        protected ArtistsPager doInBackground(String... params) {

            // if there's no artist, there's nothing to look up. Verify size of params
            if (params.length == 0) {
                return null;
            }

            // Query spotify for artists matching search string
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(params[0]);
            return results;
        }

        @Override
        protected void onPostExecute(ArtistsPager artistsPager) {
            // If no artists returned from search, clear adapter and notify user
            // Otherwise, populate adapter
            if (artistsPager == null || artistsPager.artists.items.isEmpty()) {
                artistAdapter.clear();
                Toast.makeText(getActivity(), "Artist not found", Toast.LENGTH_SHORT).show();
            } else {
                artistAdapter.clear();
                for (Artist artist : artistsPager.artists.items) {
                    // Substitute placeholder image if no images returned with search
                    String image;
                    if (artist.images.isEmpty()) {
                        image = null;
                    } else {
                        image = artist.images.get(0).url;
                    }
                    artistAdapter.add(new nArtist(image, artist.name, artist.id));
                }
            }
        }
    }
}
