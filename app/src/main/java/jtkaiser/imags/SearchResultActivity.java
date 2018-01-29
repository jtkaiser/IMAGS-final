//package jtkaiser.imags;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.spotify.sdk.android.authentication.AuthenticationClient;
//import com.spotify.sdk.android.authentication.AuthenticationResponse;
//
//import java.io.IOException;
//
//public class SearchResultActivity extends AppCompatActivity {
//
//    private static final int REQUEST_CODE = 1337;
//    private static final String TAG = "SearchResultActivity";
//    private static final String EXTRA_QUERY = "imags.query";
//
//    private RecyclerView mResultsRecyclerView;
//    private String mToken;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_result);
//
//        mResultsRecyclerView = (RecyclerView) findViewById(R.id.results_recycler_view);
//        mResultsRecyclerView.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this));
//
//
//
//        new FetchResultsTask().execute();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//
//        // Check if result comes from the correct activity
//        if (requestCode == REQUEST_CODE) {
//            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
//            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
//            mToken = response.getAccessToken();}
//        }
//    }
//
//    public static Intent newIntent(Context packageContext, String query){
//        Intent i = new Intent(packageContext, SearchResultActivity.class);
//        i.putExtra(EXTRA_QUERY, query);
//        return i;
//    }
//
//    private void startSession(){
//        Intent i = SessionActivity.newIntent(SearchResultActivity.this, "");
//        startActivity(i);
//    }
//
//    private class FetchResultsTask extends AsyncTask<Void,Void,Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//            new Searcher(mToken, (getIntent().getStringExtra(EXTRA_QUERY))).fetchResults();
//            return null;
//        }
//    }
//
//}
