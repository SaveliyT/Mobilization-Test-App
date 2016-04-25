package com.saveliy.third;



import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Musicians> musiciansList;

    MusiciansAdapter adapter;
    DBHelper dbHelper;
    public static String LOG_TAG = "my_log";
    ListView listview;
    static SQLiteDatabase db;
    static Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);


        musiciansList = new ArrayList<Musicians>(); //creating ArrayList

        listview = (ListView )findViewById(R.id.list);

        dbHelper = new DBHelper(this); //creating SQLite database builder
        db = dbHelper.getWritableDatabase();

        btn = (Button)findViewById(R.id.btn); //button btn to refresh if there's no internet connection
        btn.setVisibility(View.INVISIBLE);



        adapter = new MusiciansAdapter(getApplicationContext(), R.layout.item, musiciansList); //adapter
        listview.setAdapter(adapter);

        Cursor cursor = db.query("myTable", null, null, null, null, null, null); //creating cursor

        if (cursor.moveToFirst()){ //if database exist and filled

            Log.d(LOG_TAG, "show data from db");
            Toast toast = Toast.makeText(getApplicationContext(), "Show data from db", Toast.LENGTH_SHORT);
            toast.show();




            Log.d(LOG_TAG, "get inf from database");

            int idIndex = cursor.getColumnIndex("id");                      //taking indexes for columns
            int nameIndex = cursor.getColumnIndex("name");
            int albumsIndex = cursor.getColumnIndex("albums");
            int tracksIndex = cursor.getColumnIndex("tracks");
            int descriptionIndex = cursor.getColumnIndex("description");
            int smallCoverIndex = cursor.getColumnIndex("smallCover");
            int bigCoverIndex = cursor.getColumnIndex("bigCover");
            int genreIndex = cursor.getColumnIndex("genre");
            int websiteIndex = cursor.getColumnIndex("website");
            cursor.moveToFirst();
            do {
                Musicians musiciant = new Musicians(); // setting information from database
                musiciant.setName(cursor.getString(nameIndex));
                Log.d(LOG_TAG, cursor.getString(nameIndex) + " sat");
                musiciant.setSongs(cursor.getInt(tracksIndex));
                musiciant.setAlbums(cursor.getInt(albumsIndex));
                musiciant.setDescription(cursor.getString(descriptionIndex));
                musiciant.setSmallImage(cursor.getString(smallCoverIndex));
                musiciant.setBigInage(cursor.getString(bigCoverIndex));
                musiciant.setGenre(cursor.getString(genreIndex));
                musiciant.setWebsite(cursor.getString(websiteIndex));

                musiciansList.add(musiciant); //adding to ArrayList
                adapter.notifyDataSetChanged();

            } while (cursor.moveToNext());




        } else if (isConnectedToInternet()){ //if database isn't filled and internet connection exist
            Log.d(LOG_TAG, "connecting to json");
            Toast toast = Toast.makeText(getApplicationContext(), "Downloading data and writing database",
                    Toast.LENGTH_SHORT);
            toast.show();
            new MusiciansAsynTask().execute(); // download data from internet

        } else {
            Log.d (LOG_TAG, "No internet and no db"); //if database isn't filled and there's no internet connection
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Check internet connection to load data", Toast.LENGTH_SHORT);
            toast.show();

            btn.setVisibility(View.VISIBLE); //refresh button is visible

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()){
                        Toast toast = Toast.makeText(getApplicationContext(), "Downloading data and writing database",
                                Toast.LENGTH_SHORT);
                        toast.show();

                        new MusiciansAsynTask().execute();
                        btn.setVisibility(View.INVISIBLE);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "still no internet", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });

        }

        cursor.close();





        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), SecondActivity.class);

                intent.putExtra("pstn", position); //when musician is touched, giving position to second actvity
                startActivity(intent);
            }
        });





    }


    public boolean isConnectedToInternet(){ // checking if there is internet connection
        Context context = getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    class MusiciansAsynTask extends AsyncTask<Void, Integer, String>{

        HttpURLConnection urlConnection = null; //creating url connection
        BufferedReader reader = null;
        String resultJson = "";
        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL("http://download.cdn.yandex.net/mobilization-2016/artists.json");

                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestProperty("connection", "keep-alive"); //
                urlConnection.setReadTimeout(4000);     //
                urlConnection.setConnectTimeout(10000); // it's not neccessary
                urlConnection.setRequestMethod("GET");


                int response = urlConnection.getResponseCode();
                Log.d(LOG_TAG, "response = " + response); //checking response code



                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                resultJson = buffer.toString(); //resultJson contains json string
                inputStream.close();
            }catch (Exception e){e.printStackTrace();}

            // start here

            return resultJson;
        }




         @Override
         protected void onPostExecute(String strJson) {
             super.onPostExecute(strJson);
             //public String strGenres;
             Log.d(LOG_TAG, "json = " + strJson);

             JSONArray musicians = null;
             try {
                 musicians = new JSONArray(resultJson);

                 for (int i = 0; i < musicians.length(); i++) {
                     JSONObject object = musicians.getJSONObject(i);

                     ContentValues cv = new ContentValues();    //contains values for database

                     JSONArray genre = object.getJSONArray("genres");


                     String strname = object.getString("name");
                     String strgenres = object.getString("genres");

                     Log.d(LOG_TAG, "NAME = " + strname);
                     Log.d(LOG_TAG, "GENRE = " + strgenres);

                     Musicians musiciant = new Musicians();

                     String buff = "";
                     for (int k = 0; k < genre.length(); k++) { //parsing genres
                         musiciant.setGenre(genre.getString(k));
                         cv.put("genre", buff + genre.getString(k));
                         buff = buff + genre.getString(k) + ", ";
                     }
                     musiciant.setName(object.getString("name"));
                     musiciant.setSongs(object.getInt("tracks"));
                     musiciant.setAlbums(object.getInt("albums"));
                     musiciant.setDescription(object.getString("description"));

                     if( object.optString("link") != "") { //checking if website exist in JSON object
                         musiciant.setWebsite(object.optString("link"));
                         cv.put("website", object.optString("link"));
                         Log.d (LOG_TAG, "website = " + object.optString("link"));
                     }
                     JSONObject pictures = object.getJSONObject("cover");   //
                     musiciant.setSmallImage(pictures.getString("small"));  //
                     musiciant.setBigInage(pictures.getString("big"));      // parsing links of images


//

                     cv.put("name", object.getString("name"));              //
                     cv.put("tracks", object.getInt("tracks"));             //
                     cv.put("albums", object.getInt("albums"));             //
                     cv.put("description", object.getString("description"));//
                     cv.put("smallCover", pictures.getString("small"));     //
                     cv.put("bigCover", pictures.getString("big"));         // write values into contentValues


                     long rowID = MainActivity.db.insert("myTable", null, cv); //insert content values into database
                     Log.d(LOG_TAG, "row inserted, id = " + rowID);

                     Log.d(LOG_TAG, "progress = " + (i * 100) / musicians.length()); //checking progress



                     musiciansList.add(musiciant);
                     adapter.notifyDataSetChanged();
                 }



             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }
    }





    class DBHelper extends SQLiteOpenHelper {                               //SQLite database builder
        public DBHelper(Context context){
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            Log.d(LOG_TAG, "creating database");


            db.execSQL("create table myTable (" +
                    "id integer primary key autoincrement," +
                    "name text," +
                    "tracks int," +
                    "albums int," +
                    "description text," +
                    "smallCover text," +
                    "bigCover text," +
                    "website text," +
                    "genre text" +  ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        }

    }


}
