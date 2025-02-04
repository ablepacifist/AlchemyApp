package alchemy.srsys.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import java.util.List;

import alchemy.srsys.R;

public class KnownIngredientsAdapter extends BaseAdapter {

    private Context context;
    private List<KnownIngredientItem> items;

    public KnownIngredientsAdapter(Context context, List<KnownIngredientItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public KnownIngredientItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position; // or a unique ID if available
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            // Inflate the layout for each list item
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.known_ingredient_item, parent, false);

            // Initialize the ViewHolder
            holder = new ViewHolder();
            holder.textViewIngredientName = convertView.findViewById(R.id.textViewIngredientName);
            holder.textViewEffects = convertView.findViewById(R.id.textViewEffects);

            // Set the holder as tag
            convertView.setTag(holder);
        } else {
            // Retrieve the holder from the tag
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current KnownIngredientItem
        KnownIngredientItem item = getItem(position);

        // Set the ingredient name and known effects
        holder.textViewIngredientName.setText(item.getIngredientName());
        holder.textViewEffects.setText("Effects: " + item.getEffectsString());

        return convertView;
    }

    // ViewHolder pattern for efficient list handling
    static class ViewHolder {
        TextView textViewIngredientName;
        TextView textViewEffects;
    }
}
