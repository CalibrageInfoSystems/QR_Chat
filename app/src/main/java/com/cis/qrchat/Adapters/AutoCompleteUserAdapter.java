package com.cis.qrchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.cis.qrchat.Model.Regionitems;

import java.util.ArrayList;
import java.util.List;


public class AutoCompleteUserAdapter extends ArrayAdapter<Regionitems> {

    Context context;
    int resource, textViewResourceId;
    List<Regionitems> items, tempItems, suggestions; //Searchmodel is a custom model class

    public AutoCompleteUserAdapter(Context context, List<Regionitems> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        tempItems = new ArrayList<Regionitems>(items); // this makes the difference.
        suggestions = new ArrayList<Regionitems>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false); //Set your Custom Layout
        }
        final Regionitems searchdata = items.get(position);
        if (searchdata != null) {
//set text Whatever you want from your custom model
            TextView lblName = (TextView) view.findViewById(android.R.id.text1);
            if (lblName != null)
                lblName.setText(searchdata.getName());



        }


        return view;

    }
    public int getId(int position) {
        return items.get(position).getId();
    }
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Regionitems) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Regionitems people : tempItems) {
                    if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Regionitems> filterList = (ArrayList<Regionitems>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Regionitems people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}