package pl.sokolak.sonludilo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import pl.sokolak.sonludilo.R;


public class ListTripleItemAdapter extends ArrayAdapter<List<String>> {

    public ListTripleItemAdapter(Context context, List<List<String>> itemsList) {
        super(context, 0, itemsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_triple, parent, false);
        }

        TextView textView1 = convertView.findViewById(R.id.text_view_1);
        TextView textView2 = convertView.findViewById(R.id.text_view_2);
        TextView textView3 = convertView.findViewById(R.id.text_view_3);

        List<String> textList = getItem(position);
        if (textList != null) {
            if(textList.size() > 0)
                textView1.setText(textList.get(0));
            if(textList.size() > 1)
                textView2.setText(textList.get(1));
            if(textList.size() > 2)
                textView3.setText(textList.get(2));
        }
        return convertView;
    }
}
