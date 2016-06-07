package com.swap.mdb.qlic;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by aneeshjindal on 4/20/16.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

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
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        for (int i = 0; i < SendActivity.types.length; i++) {
//            String typeVar = SendActivity.types[i];
//            Social social = receiver.getSocial(typeVar);
//            if (social != null) {
//                LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.history_row_icon_view, null, false);
//                ImageView imageView = (ImageView) linearLayout.findViewById(R.id.imageView);
//                imageView.setImageResource(ContactViewActivity.SOCIAL_ICON_IDS[i]);
//                holder.iconLinearLayout.addView(linearLayout);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView dateTextView;
//        LinearLayout iconLinearLayout;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
//            iconLinearLayout = (LinearLayout) itemView.findViewById(R.id.iconLinearLayout);

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
