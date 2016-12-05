package com.example.ramzi.sharpytodo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ramzi on 01/12/16.
 */
public class MyAdapter extends ArrayAdapter<Event> {
    Context context;
    public MyAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Event e = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
        }
        TextView name = (TextView)convertView.findViewById(R.id.name);
        name.setText(e.getName());
        ImageView marker =(ImageView)convertView.findViewById(R.id.marker);
        TextView time = (TextView)convertView.findViewById(R.id.time);
        time.setText(e.getTime());

        marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"marker",Toast.LENGTH_SHORT).show();
                double latitude = getItem(position).getLat();
                double longitude = getItem(position).getLng();
                String label = "ABC Label";
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
        ImageView del =(ImageView)convertView.findViewById(R.id.supp);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"delete",Toast.LENGTH_SHORT).show();
                new DeleteEvent().execute(getItem(position).getId());
                remove(getItem(position));
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
    class DeleteEvent extends AsyncTask<Integer, Void, Void> {
        public static final String URL_DELETE= "delete.php?id=";
        public static final String BASE_URL= "http://10.42.0.1/todo/";
        private ArrayList<Event> eventArrayList = new ArrayList<Event>();
        @Override
        protected Void doInBackground(Integer... asb) {
            try{
                URL url = new URL(BASE_URL + URL_DELETE + asb[0]);
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
