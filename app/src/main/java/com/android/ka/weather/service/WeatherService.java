package com.android.ka.weather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.os.SystemClock;

import com.android.ka.weather.AppConfig;
import com.android.ka.weather.api.ServiceGenerator;
import com.android.ka.weather.api.service.ApiWeatherService;
import com.android.ka.weather.common.DateFormatter;
import com.android.ka.weather.common.Logger;
import com.android.ka.weather.common.StringUtils;
import com.android.ka.weather.common.TempUtils;
import com.android.ka.weather.common.WeatherUtils;
import com.android.ka.weather.model.DataResponse;
import com.android.ka.weather.model.Forecast;
import com.android.ka.weather.model.WeatherDisplay;
import com.android.ka.weather.prefs.WeatherPrefs;
import com.android.ka.weather.ui.MainActivity;
import com.android.ka.weather.ui.WidgetProvider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherService extends Service {


    List<WeatherDisplay> list;

    public static Intent newIntent(Context context) {
        return new Intent(context, WeatherService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        int time = WeatherPrefs.getUpdate();
        Logger.getLogger(WeatherService.class).info(time);
        if (time == 0) {
            context.stopService(WeatherService.newIntent(context));
            return;
        }
        Intent i = WeatherService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(
                context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), time * 1000 * 60, pi);
        } else {
            alarmManager.cancel(pi);
            context.stopService(WeatherService.newIntent(context));
            pi.cancel();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (!isNetworkAvailableAndConnected()) {
            Logger.getLogger(getClass()).info("No internet connection");
            stopSelf();
            return;
        }
        list = new ArrayList<>();
        Logger.getLogger(getClass()).info("Poll service");
        ApiWeatherService service = ServiceGenerator.create(
                ApiWeatherService.class);
        Call<DataResponse> call;

        call = service.getDataByCityId(AppConfig.WEATHER_API_KEY, WeatherPrefs.getId());
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

                WeatherPrefs.setMainWeather(weather);

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
                WeatherPrefs.setDay2(list.get(0));
                WeatherPrefs.setDay3(list.get(1));
                WeatherPrefs.setDay4(list.get(2));
                WeatherPrefs.setDay5(list.get(3));
                Intent intent = new Intent(WidgetProvider.UPDATE_WIDGET);
                intent.putExtra("main", weather);
                sendBroadcast(intent);
                stopSelf();
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Logger.getLogger(getClass()).error(t);
            }
        });
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        return isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
    }
}