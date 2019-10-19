package com.example.project_saisatwikk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Papers extends AppCompatActivity {

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> tlinks = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    ReadLater sQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papers);
        Intent it = getIntent();
        String url = it.getStringExtra("message");
        sQLiteHelper = new ReadLater(Papers.this);
        DownloadTask1 task = new DownloadTask1();
        try {
            task.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class DownloadTask1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Document doc = null;
            try {
                doc = Jsoup.connect(strings[0]).get();
                Elements links = doc.select("td[data-type]");
                int count = 1;
                for (Element link : links) {
                    //System.out.println("\nlink : "+link.attr("data-type"));
                    if (link.attr("data-type").equals("docTitle")) {
                        String articleTitle = link.text();
                        String articleLink = link.select("a[href]").attr("href");
                        titles.add(articleTitle);
                        tlinks.add(articleLink);
                        count++;
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ListView lv = findViewById(R.id.lv);
            arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, titles);
            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent in = new Intent(getApplicationContext(), Web.class);
                    in.putExtra("link", tlinks.get(i));
                    startActivity(in);
                }
            });

            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    AlertDialog.Builder myalert = new AlertDialog.Builder(Papers.this);
                    myalert.setTitle("Add paper to read later");
                    myalert.setMessage("Click OK or Cancel");
                    final int pos1 = pos;
                    myalert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            PaperModel paper = new PaperModel();
                            paper.setTitle(titles.get(pos1));
                            paper.setLink(tlinks.get(pos1));
                            Log.i("Hello Sai",tlinks.get(pos1));
                            sQLiteHelper.insertRecord(paper);
                            Toast.makeText(getApplicationContext(), "Paper Added to Read Later", Toast.LENGTH_SHORT).show();
                        }
                    });
                    myalert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog al = myalert.create();
                    al.show();

                    return true;
                }
            });
        }
    }

}
