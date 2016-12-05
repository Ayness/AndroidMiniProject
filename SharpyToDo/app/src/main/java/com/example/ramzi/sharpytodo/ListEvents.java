package com.example.ramzi.sharpytodo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListEvents extends AppCompatActivity {
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Log.e("elele","test");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                final View popupLayout = inflater.inflate(R.layout.popup, null);
                AlertDialog dialog = new AlertDialog.Builder(ListEvents.this)
                        .setTitle("Add New Task")
                        .setMessage("What do you want to do next")
                        .setView(popupLayout)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(ListEvents.this,"marker",Toast.LENGTH_SHORT).show();
                                String name =((EditText) popupLayout.findViewById(R.id.name)).getText().toString();
                                Float lat =Float.parseFloat(((EditText) popupLayout.findViewById(R.id.lat)).getText().toString());
                                Float lng =Float.parseFloat(((EditText) popupLayout.findViewById(R.id.lng)).getText().toString());
                                DatePicker date =((DatePicker) popupLayout.findViewById(R.id.date));
//                                TimePicker time =((TimePicker) popupLayout.findViewById(R.id.time));

//                                Date d = new Date(date.getYear(),date.getMonth(),date.getDayOfMonth(),time.getCurrentHour(),time.getCurrentMinute());
                                Date d = new Date(date.getYear(),date.getMonth(),date.getDayOfMonth());
                                String dt = new SimpleDateFormat("dd-MM-yyyy").format(d);
                                SharedPreferences prefs = ListEvents.this.getSharedPreferences(
                                        "com.example.ramzi.sharpytodo", Context.MODE_PRIVATE);
                                String login = prefs.getString("login","");
                                Event e = new Event(name, lat, lng, login, dt);
                                new InsertEvent().execute(e);
                                myAdapter.add(e);
                                myAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                Snackbar.make(view, "Event Added Successfully", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ListView lv = (ListView)findViewById(R.id.list_event);
        myAdapter = new MyAdapter(this);
        lv.setAdapter(myAdapter);
        SharedPreferences prefs = ListEvents.this.getSharedPreferences(
                "com.example.ramzi.sharpytodo", Context.MODE_PRIVATE);
        String login = prefs.getString("login","dummy");
        Log.e("elele","le "+login.length());
        new myAsync().execute(login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class myAsync extends AsyncTask<String, Void, ArrayList<Event>>{
        public static final String URL_GETALLEVENTS= "getAllEvents.php?login=";
        public static final String BASE_URL= "http://10.42.0.1/todo/";
        private ArrayList<Event> eventArrayList = new ArrayList<Event>();
        @Override
        protected ArrayList<Event> doInBackground(String... asb) {
            try{
                URL url = new URL(BASE_URL + URL_GETALLEVENTS + asb[0]);
                HttpURLConnection cnx = (HttpURLConnection) url.openConnection();
                cnx.setReadTimeout(10000 /* milliseconds */);
                cnx.setConnectTimeout(15000 /* milliseconds */);
                cnx.setRequestMethod("GET");
                cnx.setDoInput(true);
                cnx.connect();
                InputStream stream = cnx.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
                String s = "";
                StringBuffer output = new StringBuffer("");
                while ((s = buffer.readLine()) != null) {
                    output.append(s);
                }
                stream.close();
                if(!output.toString().equals("0")){
                    JSONArray jsonArray = new JSONArray(output.toString());
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");
                        float lat = Float.parseFloat(jsonObject.getString("lat"));
                        float lng = Float.parseFloat(jsonObject.getString("lng"));
                        String time = jsonObject.getString("time");
                        Event e = new Event(id, name, lat, lng, time);
                        eventArrayList.add(e);
                    }
                    return  eventArrayList;
                }
                return null;

            }
            catch (Exception e) {
                Log.e("hihi",Log.getStackTraceString(e));
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Event> events) {
            if(events!=null)
                 myAdapter.addAll(events);
        }
    }
    class InsertEvent extends AsyncTask<Event, Void, Void> {
        public static final String URL_DELETE= "insert.php?name=";
        public static final String BASE_URL= "http://10.42.0.1/todo/";
        private ArrayList<Event> eventArrayList = new ArrayList<Event>();
        @Override
        protected Void doInBackground(Event... asb) {
            try{
                SharedPreferences prefs = ListEvents.this.getSharedPreferences(
                        "com.example.ramzi.sharpytodo", Context.MODE_PRIVATE);
                String login = prefs.getString("login","");
                String timedate = "&date="+asb[0].getTime();
                URL url = new URL(BASE_URL + URL_DELETE + asb[0].getName() + "&lat="+ asb[0].getLat() + "&lng="+ asb[0].getLng()+"&login="+login + timedate);
                Log.e("url", url.toString());
                HttpURLConnection cnx = (HttpURLConnection) url.openConnection();
                cnx.setReadTimeout(10000 /* milliseconds */);
                cnx.setConnectTimeout(15000 /* milliseconds */);
                cnx.setRequestMethod("GET");
                cnx.setDoInput(true);
                cnx.connect();
                InputStream stream = cnx.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
                String s = "";
                StringBuffer output = new StringBuffer("");
                while ((s = buffer.readLine()) != null) {
                    output.append(s);
                }
                stream.close();
                Log.e("dfgbn", output.toString());
                return null;
            }
            catch (Exception e) {
                Log.e("hihi",Log.getStackTraceString(e));
                return null;
            }
        }
    }
}
