//package jtkaiser.imags;
//
//import android.net.Uri;
//import android.util.Log;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
///**
// * Created by jtkai on 1/22/2018.
// */
//
//public class Searcher {
//
//    private static final String TAG = "Searcher";
//    private static final String CLIENT_ID = "0e496f3bf31344c0aaf87a89ea883e0d";
//
//    private String mToken;
//    private String mQuery;
//
//    public Searcher(String token, String query){
//        mToken = token;
//        mQuery = query;
//    }
//
//    public byte[] getUrlBytes(String urlSpec) throws IOException {
//        URL url = new URL(urlSpec);
//        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//        connection.setRequestProperty("Authorization", ("Authorization: Bearer " + mToken));
//        try {
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            InputStream in = connection.getInputStream();
//            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                throw new IOException(connection.getResponseMessage() +
//                        ": with " +
//                        urlSpec);
//            }
//            int bytesRead = 0;
//            byte[] buffer = new byte[1024];
//            while ((bytesRead = in.read(buffer)) > 0) {
//                out.write(buffer, 0, bytesRead);
//            }
//            out.close();
//            return out.toByteArray();
//        } finally {
//            connection.disconnect();
//        }
//    }
//
//    public String getUrlString(String urlSpec) throws IOException {
//        return new String(getUrlBytes(urlSpec));
//    }
//
//    public void fetchResults() {
//        try {
//            String jsonString = getUrlString(mQuery);
//            Log.i(TAG, "Received JSON: " + jsonString);
//        } catch (IOException ioe) {
//            Log.e(TAG, "Failed to fetch items", ioe);
//        }
//    }
//}
