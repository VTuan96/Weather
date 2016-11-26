package com.android.ka.weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.ka.weather.api.ServiceGenerator;
import com.android.ka.weather.api.service.WeatherService;
import com.android.ka.weather.common.Logger;
import com.android.ka.weather.common.TempUtils;
import com.android.ka.weather.model.DataResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvHello = (TextView) findViewById(R.id.tvHello);
        WeatherService service = ServiceGenerator.create(WeatherService.class);
        Call<DataResponse> call = service.getDataResponse(AppConfig.WEATHER_API_KEY, "1562820");
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if (!response.isSuccessful()) {
                    Logger.getLogger(MainActivity.class).error(response.code());
                    return;
                }
                DataResponse dataResponse = response.body();
                String name = dataResponse.city.name;
                String status = dataResponse.list.get(0).weather.get(0).statusWeather;
                String desc = dataResponse.list.get(0).weather.get(0).description;
                String temp = TempUtils.convertKtoC(dataResponse.list.get(0).main.temp);
                tvHello.setText(name + " " + status + " " + desc + " " + temp);

            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

                Logger.getLogger(MainActivity.class).error(t);

            }
        });
    }
}
