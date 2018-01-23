package jtkaiser.imags;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class SearchActivity extends AppCompatActivity implements Search.View{

    private static final String TAG = "SearchActivity";

    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    private static final String KEY_CURRENT_QUERY = "CURRENT_QUERY";

    private Search.ActionListener mActionListener;

    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    private ScrollListener mScrollListener = new ScrollListener(mLayoutManager);
    private SearchResultsAdapter mAdapter;
    private String mToken;


    private class ScrollListener extends ResultsScrollListener {

        public ScrollListener(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore() {
            mActionListener.loadMoreResults();
        }
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        String mToken = intent.getStringExtra(EXTRA_TOKEN);

        mActionListener = new ResultsPresenter(this, this);
        mActionListener.init(mToken);

        // Setup search field
        final SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mActionListener.search(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        // Setup search results list
        mAdapter = new SearchResultsAdapter(this, new SearchResultsAdapter.ItemSelectedListener() {
            @Override
            public void onItemSelected(View itemView, Track item) {
                mActionListener.selectTrack(item);
                Intent i =  new Intent(SearchActivity.this, SessionActivity.class);
                i.putExtra(SessionActivity.EXTRA_URI, item.uri);
                startActivity(i);
            }
        });

        RecyclerView resultsList = (RecyclerView) findViewById(R.id.search_results);
        resultsList.setHasFixedSize(true);
        resultsList.setLayoutManager(mLayoutManager);
        resultsList.setAdapter(mAdapter);
        resultsList.addOnScrollListener(mScrollListener);

        // If Activity was recreated wit active search restore it
        if (savedInstanceState != null) {
            String currentQuery = savedInstanceState.getString(KEY_CURRENT_QUERY);
            mActionListener.search(currentQuery);
        }
    }

    @Override
    public void reset() {
        mScrollListener.reset();
        mAdapter.clearData();
    }

    @Override
    public void addData(List<Track> items) {
        mAdapter.addData(items);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActionListener.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActionListener.resume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActionListener.getCurrentQuery() != null) {
            outState.putString(KEY_CURRENT_QUERY, mActionListener.getCurrentQuery());
        }
    }

    @Override
    protected void onDestroy() {
        mActionListener.destroy();
        super.onDestroy();
    }

//    private String mUri = "";
//    private EditText mUriBox;
//
//    private String mTrack = "";
//    private EditText mTrackBox;
//
//    private String mArtist = "";
//    private EditText mArtistBox;
//
//    private String mAlbum = "";
//    private EditText mAlbumBox;
//
//    private Button mButton;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//
//        mButton = (Button) findViewById(R.id.seachButton);
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startNextActivity();
//            }
//        });
//
//        mUriBox = (EditText) findViewById(R.id.uri_input);
//        mUriBox.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                mUri = charSequence.toString();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        mTrackBox = (EditText) findViewById(R.id.trackInput);
//        mTrackBox.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                mTrack = charSequence.toString();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//
//        mArtistBox = (EditText) findViewById(R.id.artistInput);
//        mArtistBox.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                mArtist = charSequence.toString();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//
//        mAlbumBox = (EditText) findViewById(R.id.albumInput);
//        mAlbumBox.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                mAlbum = charSequence.toString();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//    }
//
//    private String buildQuery(){
//        String query = "";
//        if((!(mTrack.equals(""))) && (mArtist.equals("")) && (mAlbum.equals(""))) {
//            query = Uri.parse("https://api.spotify.com/v1/search?")
//                    .buildUpon()
//                    .appendQueryParameter("q",mTrack)
//                    .appendQueryParameter("type", "track")
//                    .build().toString();
//        }
//        else if((mTrack.equals("")) && (!(mArtist.equals(""))) && (mAlbum.equals(""))) {
//            query = Uri.parse("https://api.spotify.com/v1/search?")
//                    .buildUpon()
//                    .appendQueryParameter("q", mArtist)
//                    .appendQueryParameter("type", "artist")
//                    .build().toString();
//        }
//        else if((mTrack.equals("")) && (mArtist.equals("")) && (!(mAlbum.equals("")))) {
//            query = Uri.parse("https://api.spotify.com/v1/search?")
//                    .buildUpon()
//                    .appendQueryParameter("q", mAlbum)
//                    .appendQueryParameter("type", "album")
//                    .build().toString();
//        }
//        Log.d(TAG, "query1 is " + query);
//        return query;
//    }
//
//    void startNextActivity(){
//        Intent i;
//        if(mUri.equals("")){
//            String query = buildQuery();
//            Log.d(TAG, "query2 is " + query);
//            i = SearchResultActivity.newIntent(SearchActivity.this, query);
//        }
//        else{
//            Log.d(TAG, "mUri is " + mUri);
//            i = SessionActivity.newIntent(SearchActivity.this, mUri);
//        }
//
//        startActivity(i);
//    }
}
