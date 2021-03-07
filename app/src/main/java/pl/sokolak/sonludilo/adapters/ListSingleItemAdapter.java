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


public class ListSingleItemAdapter extends ArrayAdapter<String> {

    public ListSingleItemAdapter(Context context, List<String> itemsList) {
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_single, parent, false);
        }

        TextView textView1 = convertView.findViewById(R.id.text_view_1);

        String text = getItem(position);
        if (text != null) {
            textView1.setText(text);
        }
        return convertView;
    }
}
