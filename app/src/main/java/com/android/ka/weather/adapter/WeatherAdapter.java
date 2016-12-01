package com.android.ka.weather.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ka.weather.R;
import com.android.ka.weather.common.WeatherUtils;
import com.android.ka.weather.model.WeatherDisplay;

import java.util.List;

/**
 * Created by ka on 27/11/2016.
 */

public class WeatherAdapter extends BaseAdapter {
    private List<WeatherDisplay> list;
    private Context context;

    public WeatherAdapter(Context context, List<WeatherDisplay> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeatherDisplay weatherDisplay = list.get(position);
        ViewHolder holder;
        ViewHolderForecast holderForecast;
        if (getItemViewType(position) == 0) {
            convertView = View.inflate(context, R.layout.item_main, null);
            holder = new ViewHolder(convertView);
            holder.tvHumidity.setText(weatherDisplay.getHumidity());
            holder.tvCity.setText(weatherDisplay.getCity());
            holder.tvDate.setText(weatherDisplay.getTimeForecast());
            holder.tvWind.setText(weatherDisplay.getWind());
            holder.tvStatus.setText(weatherDisplay.getStatus());
            holder.tvTempAva.setText(weatherDisplay.getTempAva());
            holder.tvTemp.setText(weatherDisplay.getTemp());
            holder.imageWeather.setImageResource(WeatherUtils.getArtResourceForWeatherCondition
                    (weatherDisplay.getWeatherId()));
        } else {
            convertView = View.inflate(context, R.layout.item_forecast, null);
            holderForecast = new ViewHolderForecast(convertView);
            holderForecast.tvTempAva.setText(weatherDisplay.getTempAva());
            holderForecast.tvDate.setText(weatherDisplay.getTimeForecast());
            holderForecast.imageWeather.setImageResource(WeatherUtils.getArtResourceForWeatherCondition
                    (weatherDisplay.getWeatherId()));

        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvTemp;
        TextView tvCity;
        TextView tvDate;
        TextView tvStatus;
        TextView tvWind;
        TextView tvTempAva;
        TextView tvHumidity;
        ImageView imageWeather;

        ViewHolder(View item) {
            tvCity = (TextView) item.findViewById(R.id.tvCity);
            tvTemp = (TextView) item.findViewById(R.id.tvTemp);
            tvTempAva = (TextView) item.findViewById(R.id.tvTempAva);
            tvStatus = (TextView) item.findViewById(R.id.tvStatus);
            tvWind = (TextView) item.findViewById(R.id.tvWind);
            tvDate = (TextView) item.findViewById(R.id.tvDate);
            tvHumidity = (TextView) item.findViewById(R.id.tvHumidity);
            imageWeather = (ImageView) item.findViewById(R.id.imgWeather);
        }
    }

    private class ViewHolderForecast {
        TextView tvTempAva;
        TextView tvDate;
        ImageView imageWeather;

        ViewHolderForecast(View item) {
            tvDate = (TextView) item.findViewById(R.id.tvDate);
            tvTempAva = (TextView) item.findViewById(R.id.tvTempAva);
            imageWeather = (ImageView) item.findViewById(R.id.imgStatus);
        }
    }
}
