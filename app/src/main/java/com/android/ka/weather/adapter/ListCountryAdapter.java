package com.android.ka.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.ka.weather.R;
import com.android.ka.weather.model.Country;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ka on 29/11/2016.
 */

public class ListCountryAdapter extends BaseAdapter {
    private Context context;
    private List<Country> countryList;
    private List<Country> result;

    public ListCountryAdapter(Context context, List<Country> countryList) {
        this.context = context;
        this.countryList = countryList;
        result = new ArrayList<>();
        result.addAll(countryList);
    }

    @Override
    public int getCount() {
        if (countryList != null) {
            return countryList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return countryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        Country country = countryList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_country, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvName.setText(country.getName() + ", " + country.getCountry());
        return convertView;
    }

    public void filter(String query) {
        query = query.toLowerCase(Locale.getDefault());

        countryList.clear();
        if (query.length() == 0) {
            countryList.addAll(result);
        } else {
            for (Country c : result) {
                if (query.length() != 0 && c.toString().toLowerCase(Locale.getDefault()).contains(query)) {
                    countryList.add(c);
                }
            }
        }
        notifyDataSetChanged();
    }

    private class Holder {
        TextView tvName;

        Holder(View view) {
            tvName = (TextView) view.findViewById(R.id.tvCountry);
        }
    }
}
