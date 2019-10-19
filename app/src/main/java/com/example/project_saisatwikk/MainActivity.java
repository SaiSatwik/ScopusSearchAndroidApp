package com.example.project_saisatwikk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    public static String articlesURL;

    static EditText firstName;
    static EditText lastName;

    Button bt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);

        bt = findViewById(R.id.submit);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Retrieving...");
                progressDialog.show();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                DownloadTask task = new DownloadTask();
                                try {
                                    String Url = "https://api.elsevier.com/content/search/author?query=authlast(" + lastName.getText().toString().trim() + ")%20and%20authfirst(" + firstName.getText().toString().trim() + ")&apiKey=0662f979f54e51fbb5189db5c6426822";
                                    Log.i("Hello", Url);
                                    task.execute(Url);

                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Author does not exit", Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            }
                        }, 15000);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.resume) {
            Intent it = new Intent(this, ResumeMain.class);
            startActivity(it);
        } else if (id == R.id.readLater) {

            Intent it = new Intent(this, ReadLaterPapers.class);
            startActivity(it);

        }

        return super.onOptionsItemSelected(item);
    }

    public void openURL() {
        Intent it = new Intent(this, Papers.class);
        it.putExtra("message", articlesURL);
        startActivity(it);
    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                //Log.i("Hello1",strings[0]);
                url = new URL(strings[0]);
                Log.i("Hello2", url.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char curr = (char) data;
                    result += curr;
                    data = reader.read();
                }
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                //Log.i("Hello3",result);
                JSONObject jsonObject = new JSONObject(result);
                String search = jsonObject.getString("search-results");
                jsonObject = new JSONObject(search);
                JSONArray arr = new JSONArray(jsonObject.getString("entry"));
                String entry = arr.getString(0);
                jsonObject = new JSONObject(entry);
                arr = new JSONArray(jsonObject.getString("link"));
                String link = arr.getString(3);
                jsonObject = new JSONObject(link);
                articlesURL = jsonObject.getString("@href");
                String s = articlesURL.substring(articlesURL.indexOf("authorId=") + 9);
                s = s.substring(0, s.indexOf("&origin"));
                articlesURL = "https://www.scopus.com/results/results.uri?sort=plf-f&src=s&sid=3b8fee4e5cf06ab9e6e32c3fc05d60d6&sot=aut&sdt=a&sl=18&s=AU-ID(" + s + ")&origin=AuthorProfile&zone=documentsTab&reselectAuthorsLinkName=" + lastName.getText().toString() + "%2c+" + firstName.getText().toString() + "&editSaveSearch=&txGid=88862b2bd55e5532fa6416af20de7a99";
                openURL();


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
