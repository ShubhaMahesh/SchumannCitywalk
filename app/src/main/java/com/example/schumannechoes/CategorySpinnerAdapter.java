package com.example.schumannechoes;



import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategorySpinnerAdapter extends ArrayAdapter<String> {
    private String[] dropdownItems;

    public CategorySpinnerAdapter(Context context, String[] dropdownItems) {
        super(context, android.R.layout.simple_spinner_item, dropdownItems);
        this.dropdownItems = dropdownItems;
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // This is the view displayed when Spinner is NOT expanded
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText("Category: " + dropdownItems[position]);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // This is the view displayed in the dropdown
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(dropdownItems[position]);
        return view;
    }
}