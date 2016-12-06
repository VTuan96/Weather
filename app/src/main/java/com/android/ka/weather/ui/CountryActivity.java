package com.android.ka.weather.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.ka.weather.R;
import com.android.ka.weather.common.JsonUtil;
import com.android.ka.weather.common.WeatherUtils;
import com.android.ka.weather.model.Country;
import com.android.ka.weather.prefs.ShowWallpaperPrefs;
import com.android.ka.weather.prefs.WeatherPrefs;
import com.android.ka.weather.ui.adapter.ListCountryAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CountryActivity extends AppCompatActivity {
    RelativeLayout rela;
    private ListView lvCountry;
    private List<Country> countryList;
    private ListCountryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_country);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        lvCountry = (ListView) findViewById(R.id.lvCountry);
        rela = (RelativeLayout) findViewById(R.id.rela);

        countryList = loadJSONFromAsset();
        adapter = new ListCountryAdapter(this, countryList);
        lvCountry.setAdapter(adapter);

        lvCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Country c = countryList.get(position);
                Intent intent = getIntent();
                intent.putExtra("id", c.get_id());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        if (ShowWallpaperPrefs.getShowWallpaper()) {
            rela.setBackgroundResource(WeatherUtils.getBackgroundResource(
                    WeatherPrefs.getMainWeather().getWeatherId()));
        } else {
            rela.setBackgroundResource(R.drawable.clear);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.filter(query.trim());
                lvCountry.invalidate();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public List<Country> loadJSONFromAsset() {
        String json = null;
        List<Country> countryList = new ArrayList<>();
        try {
            InputStream inputStream = getAssets().open("city.list.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (OutOfMemoryError | IOException e) {
            e.printStackTrace();
        }

        JSONArray root = JsonUtil.createJSONArray(json);
        assert root != null;
        int size = root.length();
        for (int i = 0; i < size - 1; i++) {
            Country country = new Country();
            JSONObject object = JsonUtil.getJSONObject(root, i);
            country.set_id(JsonUtil.getString(object, "_id", ""));
            String name = JsonUtil.getString(object, "country", "");
            country.setCountry(name);
            country.setName(JsonUtil.getString(object, "name", ""));
            countryList.add(country);
        }
        return countryList;
    }
}
