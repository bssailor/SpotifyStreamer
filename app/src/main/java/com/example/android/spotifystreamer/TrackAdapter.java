package com.example.android.spotifystreamer;

import android.app.Activity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TrackAdapter extends ArrayAdapter<TrackStorage> {
    private static final String LOG_TAG = TrackAdapter.class.getSimpleName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context The current context. Used to inflate the layout file.
     * @param tracks  A List of Track objects to display in a list
     */
    public TrackAdapter(Activity context, List<TrackStorage> tracks) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, tracks);
    }

    static class TrackViewHolder {
        ImageView albumImage;
        TextView trackName;
        TextView albumName;
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the Artist object from the ArrayAdapter at the appropriate position
        TrackStorage track = getItem(position);

        TrackViewHolder holder;

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_track, parent, false);
            holder = new TrackViewHolder();
            holder.albumImage = (ImageView) convertView.findViewById(R.id.list_item_album_icon);
            holder.trackName = (TextView) convertView.findViewById(R.id.list_item_track);
            holder.albumName = (TextView) convertView.findViewById(R.id.list_item_album);
            convertView.setTag(holder);
        }
        else {
            holder = (TrackViewHolder) convertView.getTag();
        }

        // Check for valid image URL
        if (track.albumImage != null && Patterns.WEB_URL.matcher(track.albumImage).matches()) {
            Picasso.with(getContext()).load(track.albumImage).into(holder.albumImage);
        } else {
            Picasso.with(getContext()).load(R.drawable.placeholder).into(holder.albumImage);
        }

        holder.trackName.setText(track.trackName);

        holder.albumName.setText(track.albumName);

        return convertView;
    }
}
