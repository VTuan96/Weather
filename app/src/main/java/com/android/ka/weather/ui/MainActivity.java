package com.android.ka.weather.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.ka.weather.AppConfig;
import com.android.ka.weather.R;
import com.android.ka.weather.api.ServiceGenerator;
import com.android.ka.weather.api.service.WeatherService;
import com.android.ka.weather.common.DateFormatter;
import com.android.ka.weather.common.Logger;
import com.android.ka.weather.common.StringUtils;
import com.android.ka.weather.common.TempUtils;
import com.android.ka.weather.common.WeatherUtils;
import com.android.ka.weather.model.DataResponse;
import com.android.ka.weather.model.Forecast;
import com.android.ka.weather.model.WeatherDisplay;
import com.android.ka.weather.prefs.ShowWallpaperPrefs;
import com.android.ka.weather.prefs.UseAppPrefs;
import com.android.ka.weather.prefs.UsingCurrentLocationPrefs;
import com.android.ka.weather.prefs.WeatherPrefs;
import com.android.ka.weather.service.LocationService;
import com.android.ka.weather.ui.adapter.WeatherAdapter;
import com.android.ka.weather.ui.fragment.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 111;
    public static final String LOCATION = "com.android.ka.weather.ui.LOCATION";

    private RelativeLayout rela;
    private WeatherAdapter adapter;
    private List<WeatherDisplay> list;
    private ProgressDialog dialog;
    private SwipeRefreshLayout pull;
    private String _id;
    private IntentFilter filter = new IntentFilter("com.android.ka.weather.ui.LOCATION");
    private String lon;
    private String lat;
    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(LOCATION)) {
                lon = intent.getStringExtra("lon");
                lat = intent.getStringExtra("lat");
                getData();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        ImageView imgLocation = (ImageView) findViewById(R.id.ivLocation);
        ImageView imgCalendar = (ImageView) findViewById(R.id.ivCalendar);
        ImageView imgSettings = (ImageView) findViewById(R.id.ivSettings);
        ListView lv = (ListView) findViewById(R.id.lv);
        rela = (RelativeLayout) findViewById(R.id.relativeLayout);
        pull = (SwipeRefreshLayout) findViewById(R.id.pullToUpdate);

        list = new ArrayList<>();
        adapter = new WeatherAdapter(this, list);
        lv.setAdapter(adapter);
        dialog = new ProgressDialog(this, R.style.Theme_MyDialog);
        if (UseAppPrefs.checkFirstTime() || (getIntent() != null && getIntent().getAction().
                equals(SettingsFragment.CURRENT))) {
            dialog.setMessage("Loading weather");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setProgress(0);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setIndeterminate(false);
            dialog.show();
            Intent intent = new Intent(this, LocationService.class);
            startService(intent);
        } else {
            list.add(WeatherPrefs.getMainWeather());
            list.add(WeatherPrefs.getDay2());
            list.add(WeatherPrefs.getDay3());
            list.add(WeatherPrefs.getDay4());
            list.add(WeatherPrefs.getDay5());
            _id = WeatherPrefs.getId();
            if (ShowWallpaperPrefs.getShowWallpaper()) {
                rela.setBackgroundResource(WeatherUtils.getBackgroundResource(
                        WeatherPrefs.getMainWeather().getWeatherId()));
            }
            adapter.notifyDataSetChanged();
        }

        pull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pull.setRefreshing(false);
                        getData();
                        adapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });
        imgLocation.setOnClickListener(this);
        imgCalendar.setOnClickListener(this);
        imgSettings.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(locationReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        WeatherPrefs.setMainWeather(list.get(0));
        WeatherPrefs.setDay2(list.get(1));
        WeatherPrefs.setDay3(list.get(2));
        WeatherPrefs.setDay4(list.get(3));
        WeatherPrefs.setDay5(list.get(4));
        WeatherPrefs.setId(_id);
        unregisterReceiver(locationReceiver);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivLocation) {
            Intent intent = new Intent(MainActivity.this, CountryActivity.class);
            intent.putExtra("weatherId", list.get(0).getWeatherId());
            startActivityForResult(intent, REQUEST_CODE);
        } else if (v.getId() == R.id.ivCalendar) {
            startActivity(new Intent(MainActivity.this, Lunar.class));
        } else if (v.getId() == R.id.ivSettings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("weatherId", list.get(0).getWeatherId());
            startActivity(intent);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            _id = data.getStringExtra("id");
            getData();
        }
    }

    private void getData() {
        list.clear();
        WeatherService service = ServiceGenerator.create(WeatherService.class);
        Call<DataResponse> call;

        if (UseAppPrefs.checkFirstTime() || UsingCurrentLocationPrefs.getUsingCurrentLocation()) {
            call = service.getDataByLocation(AppConfig.WEATHER_API_KEY, lat, lon);
            UseAppPrefs.isFirstTime(false);
            UsingCurrentLocationPrefs.setUsingCurrentLocation(false);
        } else {
            call = service.getDataByCityId(AppConfig.WEATHER_API_KEY, _id);
        }
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if (!response.isSuccessful()) {
                    Logger.getLogger(MainActivity.class).error(response.code());
                    return;
                }
                DataResponse dataResponse = response.body();
                WeatherDisplay weather = new WeatherDisplay();
                List<Forecast> forecasts = dataResponse.list;
                long currentDate = forecasts.get(0).timeForecast;
                _id = dataResponse.city._id;
                weather.setTimeForecast(DateFormatter.formatString(forecasts.get(0).realTime));
                weather.setHumidity(forecasts.get(0).main.humidity + "%");
                weather.setCity(dataResponse.city.name + ", " + dataResponse.city.country);
                weather.setWeatherId(forecasts.get(0).weather.get(0).weatherId);
                weather.setTemp(TempUtils.convertKtoC(forecasts.get(0).main.temp));
                weather.setWind(forecasts.get(0).wind.speed + " m/s " +
                        WeatherUtils.getFormattedWind(forecasts.get(0).wind.deg));
                weather.setTempAva(TempUtils.convert(forecasts.get(0).main.tempMin) +
                        TempUtils.convertKtoC(forecasts.get(0).main.tempMax));
                weather.setStatus(StringUtils.capitalizeString(forecasts.get(0).weather.get(0).description));

                list.add(weather);
                for (int i = 1; i < forecasts.size(); i++) {
                    if ((currentDate + 86400) == forecasts.get(i).timeForecast) {
                        Forecast forecast = forecasts.get(i);
                        WeatherDisplay day1 = new WeatherDisplay();
                        day1.setTempAva(TempUtils.convert(forecast.main.tempMin) +
                                TempUtils.convertKtoC(forecast.main.tempMax));
                        day1.setWeatherId(forecast.weather.get(0).weatherId);
                        day1.setTimeForecast(DateFormatter.formatString(forecast.realTime));
                        list.add(day1);
                        currentDate += 86400;
                    }
                }
                if (ShowWallpaperPrefs.getShowWallpaper()) {
                    rela.setBackgroundResource(WeatherUtils.getBackgroundResource(weather.getWeatherId()));
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Logger.getLogger(MainActivity.class).error(t);
            }
        });
    }
}