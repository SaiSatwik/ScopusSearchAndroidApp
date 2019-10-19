package com.example.project_saisatwikk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ReadLaterPapers extends AppCompatActivity {

    ReadLater sQLiteHelper;
    ArrayList<String> links;
    ArrayList<String> titles;
    ArrayAdapter arrayAdapter;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_later_papers);
        sQLiteHelper = new ReadLater(ReadLaterPapers.this);

        ArrayList<PaperModel> items = sQLiteHelper.getAllRecords();
        titles = new ArrayList<>();
        links = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            titles.add(items.get(i).getTitle());
            links.add(items.get(i).getLink());
            Log.i("Hello" + i, titles.get(i));
        }
        lv = findViewById(R.id.read_later);
        arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, titles);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in = new Intent(getApplicationContext(), Web.class);
                in.putExtra("link", links.get(i));
                startActivity(in);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                AlertDialog.Builder myalert = new AlertDialog.Builder(ReadLaterPapers.this);
                myalert.setTitle("Delete Paper from Read Later");
                myalert.setMessage("Click OK or Cancel");
                final int pos1 = pos;
                myalert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PaperModel paper = new PaperModel();
                        paper.setTitle(titles.get(pos1));
                        paper.setLink(links.get(pos1));
                        sQLiteHelper.deleteRecord(paper);
                        arrayAdapter.remove(arrayAdapter.getItem(pos1));
                        arrayAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Paper Deleted", Toast.LENGTH_SHORT).show();

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
