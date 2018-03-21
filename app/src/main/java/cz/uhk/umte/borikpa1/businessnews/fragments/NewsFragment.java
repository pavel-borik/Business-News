package cz.uhk.umte.borikpa1.businessnews.fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import cz.uhk.umte.borikpa1.businessnews.R;
import cz.uhk.umte.borikpa1.businessnews.adapters.NewsItemsRecyclerViewAdapter;
import cz.uhk.umte.borikpa1.businessnews.model.NewsItem;
import cz.uhk.umte.borikpa1.businessnews.utils.NewsItemsXmlParser;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private NewsItemsRecyclerViewAdapter adapter;
    private static final String ARG_URL = "url";
    private String mUrl;
    private OnFragmentInteractionListener mListener;

    public NewsFragment() {

    }

    public static NewsFragment newInstance(String url) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(ARG_URL);
        }
        new RssFeedRetriever().execute(mUrl);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_news, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_newslist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        mRecyclerView.setAdapter(adapter);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class RssFeedRetriever extends AsyncTask<String, Void, List<NewsItem>> {
        int result;
        @Override
        protected List<NewsItem> doInBackground(String... urls) {
            result = 0;
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException | XmlPullParserException e) {
                Log.e("Error ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<NewsItem> newsItems) {

            if (result == 1) {
                adapter = new NewsItemsRecyclerViewAdapter(getActivity(), newsItems);
                mRecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }

        private List<NewsItem> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            NewsItemsXmlParser newsItemsXmlParser = new NewsItemsXmlParser();
            List<NewsItem> newsItems;

            try {
                stream = downloadUrl(urlString);
                newsItems = newsItemsXmlParser.parse(stream);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
            return newsItems;
        }

        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int statusCode = conn.getResponseCode();
            if (statusCode == 200) result = 1;
            return conn.getInputStream();
        }
    }
}
