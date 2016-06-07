package com.swap.mdb.qlic;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.swap.mdb.qlic.database.HistoryDatabaseHelper;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));

        HistoryDatabaseHelper helper = new HistoryDatabaseHelper(getApplicationContext());
        database = helper.getWritableDatabase();

        Cursor cursor = database.query(HistoryDatabaseHelper.TABLE_NAME, HistoryDatabaseHelper.ALL_COLUMNS, null, null, null, null, HistoryDatabaseHelper.DATE);
        HistoryAdapter adapter = new HistoryAdapter(getApplicationContext(), cursor);
        recyclerView.setAdapter(adapter);
    }
}
