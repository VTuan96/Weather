package com.android.ka.weather.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import com.android.ka.weather.R;
import com.android.ka.weather.adapter.ListCountryAdapter;
import com.android.ka.weather.common.JsonUtil;
import com.android.ka.weather.model.Country;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CountryActivity extends AppCompatActivity {
    private ListView lvCountry;
    private List<Country> countryList;
    private ListCountryAdapter adapter;
    private String s = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_country);
        lvCountry = (ListView) findViewById(R.id.lvCountry);
        countryList = loadJSONFromAsset();
        adapter = new ListCountryAdapter(this, countryList);
        lvCountry.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
        int size = root.length();
        for (int i = 0; i < size - 1; i++) {
            Country country = new Country();
            JSONObject object = JsonUtil.getJSONObject(root, i);
            Log.e("fuck", i + "");
            country.set_id(JsonUtil.getString(object, "_id", ""));
            String name = JsonUtil.getString(object, "country", "");
            if (name.equals("VN")) {
                name = "Viet Nam";
            }
            country.setCountry(name);
            country.setName(JsonUtil.getString(object, "name", ""));
            countryList.add(country);
        }
        return countryList;
    }
}
