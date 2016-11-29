package com.android.ka.weather.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.ka.weather.AppConfig;
import com.android.ka.weather.R;
import com.android.ka.weather.adapter.WeatherAdapter;
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
import com.android.ka.weather.prefs.UseAppPrefs;
import com.android.ka.weather.prefs.WeatherPrefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 111;
    private ListView lv;
    private WeatherAdapter adapter;
    private List<WeatherDisplay> list;
    private ProgressDialog dialog;
    private SwipeRefreshLayout pull;
    private Button btnSearchLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSearchLocation = (Button) findViewById(R.id.btnLocation);
        lv = (ListView) findViewById(R.id.lv);
        pull = (SwipeRefreshLayout) findViewById(R.id.pullToUpdate);

        list = new ArrayList<>();
        adapter = new WeatherAdapter(this, list);
        lv.setAdapter(adapter);
        dialog = new ProgressDialog(this);
        if (UseAppPrefs.checkFirstTime()) {
            dialog.setMessage("Loading weather");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Weather");
            dialog.setProgress(0);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setIndeterminate(false);
            dialog.show();
            getData();
        } else {
            list.add(WeatherPrefs.getMainWeather());
            list.add(WeatherPrefs.getDay2());
            list.add(WeatherPrefs.getDay3());
            list.add(WeatherPrefs.getDay4());
            list.add(WeatherPrefs.getDay5());
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
        btnSearchLocation.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        WeatherPrefs.setMainWeather(list.get(0));
//        WeatherPrefs.setDay2(list.get(1));
//        WeatherPrefs.setDay3(list.get(2));
//        WeatherPrefs.setDay4(list.get(3));
//        WeatherPrefs.setDay5(list.get(4));
//        UseAppPrefs.isFirstTime(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLocation) {
            Intent intent = new Intent(MainActivity.this, CountryActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getData() {
        list.clear();
        WeatherService service = ServiceGenerator.create(WeatherService.class);
        Call<DataResponse> call = service.getDataByCityId(AppConfig.WEATHER_API_KEY, "1581130");
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

                weather.setTimeForecast(DateFormatter.convertLongToStringFull(forecasts.get(0)
                        .timeForecast));
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
                        day1.setTimeForecast(DateFormatter.convertLongToDayName(forecast.timeForecast));
                        list.add(day1);
                        currentDate += 86400;
                    }
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
