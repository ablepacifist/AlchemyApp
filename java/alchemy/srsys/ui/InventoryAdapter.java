package alchemy.srsys.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import alchemy.srsys.R;
import alchemy.srsys.object.IIngredient;

import java.util.List;

public class InventoryAdapter extends BaseAdapter {

    private Context context;
    private List<IIngredient> inventory;

    public InventoryAdapter(Context context, List<IIngredient> inventory) {
        this.context = context;
        this.inventory = inventory;
    }

    @Override
    public int getCount() {
        return inventory.size();
    }

    @Override
    public IIngredient getItem(int position) {
        return inventory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position; // Use position as ID if there's no unique identifier
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            // Inflate the layout for each list item
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.inventory_item, parent, false);

            // Initialize the ViewHolder
            holder = new ViewHolder();
            holder.textViewIngredientName = convertView.findViewById(R.id.textViewIngredientName);

            // Set the holder as tag
            convertView.setTag(holder);
        } else {
            // Retrieve the holder from the tag
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current ingredient
        IIngredient ingredient = getItem(position);

        // Set the ingredient name
        holder.textViewIngredientName.setText(ingredient.getName());

        return convertView;
    }

    // ViewHolder pattern for efficient list handling
    static class ViewHolder {
        TextView textViewIngredientName;
    }
}
