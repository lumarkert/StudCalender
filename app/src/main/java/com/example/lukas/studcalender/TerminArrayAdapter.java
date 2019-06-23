package com.example.lukas.studcalender;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class TerminArrayAdapter extends BaseAdapter implements ListAdapter{
    private ArrayList<Kalender> m_list;
    private Context m_context;

    public TerminArrayAdapter(ArrayList<Kalender> arrayKalender, Context context) {
        m_list = arrayKalender;
        m_context = context;
    }

    @Override
    public int getCount() {
        return m_list.size();
    }

    @Override
    public Object getItem(int position) {
        return m_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view==null) {
            LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_kalender, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = view.findViewById(R.id.list_item_string);
        listItemText.setText((CharSequence) m_list.get(position).owner);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity act = (MainActivity) m_context;
                Kalender kal = m_list.get(position);
                Intent intent = new Intent(m_context, DayActivity.class);
                intent.putExtra("kalendar", kal);
                intent.putExtra("date", act.getCurrentDate());
                act.startActivity(intent);
                notifyDataSetChanged();
            }
        });
        return view;
    }
}
