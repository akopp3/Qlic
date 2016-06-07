package com.swap.mdb.qlic.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swap.mdb.qlic.R;
import com.swap.mdb.qlic.database.HistoryDatabaseHelper;
import com.swap.mdb.qlic.transfer.Receiver;

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

    private class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

        private Context context;
        private Cursor cursor;

        public HistoryAdapter(Context context, Cursor cursor) {
            this.context = context;
            this.cursor = cursor;
        }

        @Override
        public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row, parent, false);
            return new HistoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(HistoryViewHolder holder, int position) {
            cursor.moveToPosition(position);
            String data = cursor.getString(2);
            String date = cursor.getString(3);
            Receiver receiver = new Receiver(data);
            holder.nameTextView.setText(receiver.getName());
            holder.dateTextView.setText(date);
        }

        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        class HistoryViewHolder extends RecyclerView.ViewHolder {

            TextView nameTextView;
            TextView dateTextView;

            public HistoryViewHolder(View itemView) {
                super(itemView);
                nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
                dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ContactViewActivity.class);
                        cursor.moveToPosition(getAdapterPosition());
                        intent.putExtra(SendActivity.PEOPLE_KEY, cursor.getString(2));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }
}
