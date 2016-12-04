package com.android.ka.weather.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.ka.weather.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Lunar extends AppCompatActivity {

    private static final double LOCAL_TIMEZONE = 7.0;
    private static final String DATABASE_NAME = "Folk.sqlite";
    private static final String DATABaSE_TABLE = "FolkTable";
    private final String DIRECTORY_PATH = "/data/data/com.android.ka.weather/databases/";
    private final String FULL_PATH = DIRECTORY_PATH + DATABASE_NAME;
    Calendar c = Calendar.getInstance();
    int DAY_OF_WEEK = c.get(Calendar.DAY_OF_WEEK);
    int DAY = c.get(Calendar.DAY_OF_MONTH);
    int MONTH = c.get(Calendar.MONTH) + 1;
    int YEAR = c.get(Calendar.YEAR);
    int lunar[] = Solar2Lunar(DAY, MONTH, YEAR);
    int DAYL = lunar[0];
    int MONTHL = lunar[1];
    int YEARL = lunar[2];
    private TextView tvMonthYear;
    private TextView tvDayOfMonth;
    private TextView tvDay;
    private TextView tvQuote;
    private TextView tvMonthLunar;
    private TextView tvDayLunar;
    private TextView tvYearLunar;
    private TextView tvNameOfMonth;
    private TextView tvNameOfDay;
    private TextView tvNameOfYear;
    private SQLiteDatabase database = null;
    private ArrayList<String> mListFolk = new ArrayList<>();
    private int quoteBefore = 0;
    private Random random;

    public static int INT(double d) {
        return (int) Math.floor(d);
    }

    public static int MOD(int x, int y) {
        int z = x - (int) (y * Math.floor(((double) x / y)));
        if (z == 0) {
            z = y;
        }
        return z;
    }

    public static double UniversalToJD(int D, int M, int Y) {
        double JD;
        if (Y > 1582 || (Y == 1582 && M > 10) || (Y == 1582 && M == 10 && D > 14)) {
            JD = 367 * Y - INT(7 * (Y + INT((M + 9) / 12)) / 4) - INT(3 * (INT((Y + (M - 9) / 7) / 100) + 1) / 4) + INT(275 * M / 9) + D + 1721028.5;
        } else {
            JD = 367 * Y - INT(7 * (Y + 5001 + INT((M - 9) / 7)) / 4) + INT(275 * M / 9) + D + 1729776.5;
        }
        return JD;
    }

    public static int[] UniversalFromJD(double JD) {
        int Z, A, alpha, B, C, D, E, dd, mm, yyyy;
        double F;
        Z = INT(JD + 0.5);
        F = (JD + 0.5) - Z;
        if (Z < 2299161) {
            A = Z;
        } else {
            alpha = INT((Z - 1867216.25) / 36524.25);
            A = Z + 1 + alpha - INT(alpha / 4);
        }
        B = A + 1524;
        C = INT((B - 122.1) / 365.25);
        D = INT(365.25 * C);
        E = INT((B - D) / 30.6001);
        dd = INT(B - D - INT(30.6001 * E) + F);
        if (E < 14) {
            mm = E - 1;
        } else {
            mm = E - 13;
        }
        if (mm < 3) {
            yyyy = C - 4715;
        } else {
            yyyy = C - 4716;
        }
        return new int[]{dd, mm, yyyy};
    }

    public static int[] LocalFromJD(double JD) {
        return UniversalFromJD(JD + LOCAL_TIMEZONE / 24.0);
    }

    public static double LocalToJD(int D, int M, int Y) {
        return UniversalToJD(D, M, Y) - LOCAL_TIMEZONE / 24.0;
    }

    public static double NewMoon(int k) {
        double T = k / 1236.85; // Time in Julian centuries from 1900 January 0.5
        double T2 = T * T;
        double T3 = T2 * T;
        double dr = Math.PI / 180;
        double Jd1 = 2415020.75933 + 29.53058868 * k + 0.0001178 * T2 - 0.000000155 * T3;
        Jd1 = Jd1 + 0.00033 * Math.sin((166.56 + 132.87 * T - 0.009173 * T2) * dr); // Mean new moon
        double M = 359.2242 + 29.10535608 * k - 0.0000333 * T2 - 0.00000347 * T3; // Sun's mean anomaly
        double Mpr = 306.0253 + 385.81691806 * k + 0.0107306 * T2 + 0.00001236 * T3; // Moon's mean anomaly
        double F = 21.2964 + 390.67050646 * k - 0.0016528 * T2 - 0.00000239 * T3; // Moon's argument of latitude
        double C1 = (0.1734 - 0.000393 * T) * Math.sin(M * dr) + 0.0021 * Math.sin(2 * dr * M);
        C1 = C1 - 0.4068 * Math.sin(Mpr * dr) + 0.0161 * Math.sin(dr * 2 * Mpr);
        C1 = C1 - 0.0004 * Math.sin(dr * 3 * Mpr);
        C1 = C1 + 0.0104 * Math.sin(dr * 2 * F) - 0.0051 * Math.sin(dr * (M + Mpr));
        C1 = C1 - 0.0074 * Math.sin(dr * (M - Mpr)) + 0.0004 * Math.sin(dr * (2 * F + M));
        C1 = C1 - 0.0004 * Math.sin(dr * (2 * F - M)) - 0.0006 * Math.sin(dr * (2 * F + Mpr));
        C1 = C1 + 0.0010 * Math.sin(dr * (2 * F - Mpr)) + 0.0005 * Math.sin(dr * (2 * Mpr + M));
        double deltat;
        if (T < -11) {
            deltat = 0.001 + 0.000839 * T + 0.0002261 * T2 - 0.00000845 * T3 - 0.000000081 * T * T3;
        } else {
            deltat = -0.000278 + 0.000265 * T + 0.000262 * T2;
        }
        ;
        return Jd1 + C1 - deltat;
    }

    public static double SunLongitude(double jdn) {
        double T = (jdn - 2451545.0) / 36525; // Time in Julian centuries from 2000-01-01 12:00:00 GMT
        double T2 = T * T;
        double dr = Math.PI / 180; // degree to radian
        double M = 357.52910 + 35999.05030 * T - 0.0001559 * T2 - 0.00000048 * T * T2; // mean anomaly, degree
        double L0 = 280.46645 + 36000.76983 * T + 0.0003032 * T2; // mean longitude, degree
        double DL = (1.914600 - 0.004817 * T - 0.000014 * T2) * Math.sin(dr * M);
        DL = DL + (0.019993 - 0.000101 * T) * Math.sin(dr * 2 * M) + 0.000290 * Math.sin(dr * 3 * M);
        double L = L0 + DL; // true longitude, degree
        L = L * dr;
        L = L - Math.PI * 2 * (INT(L / (Math.PI * 2))); // Normalize to (0, 2*PI)
        return L;
    }

    public static int[] LunarMonth11(int Y) {
        double off = LocalToJD(31, 12, Y) - 2415021.076998695;
        int k = INT(off / 29.530588853);
        double jd = NewMoon(k);
        int[] ret = LocalFromJD(jd);
        double sunLong = SunLongitude(LocalToJD(ret[0], ret[1], ret[2])); // sun longitude at local midnight
        if (sunLong > 3 * Math.PI / 2) {
            jd = NewMoon(k - 1);
        }
        return LocalFromJD(jd);
    }

    public static int[][] LunarYear(int Y) {
        int[][] ret = null;
        int[] month11A = LunarMonth11(Y - 1);
        double jdMonth11A = LocalToJD(month11A[0], month11A[1], month11A[2]);
        int k = (int) Math.floor(0.5 + (jdMonth11A - 2415021.076998695) / 29.530588853);
        int[] month11B = LunarMonth11(Y);
        double off = LocalToJD(month11B[0], month11B[1], month11B[2]) - jdMonth11A;
        boolean leap = off > 365.0;
        if (!leap) {
            ret = new int[13][5];
        } else {
            ret = new int[14][5];
        }
        ret[0] = new int[]{month11A[0], month11A[1], month11A[2], 0, 0};
        ret[ret.length - 1] = new int[]{month11B[0], month11B[1], month11B[2], 0, 0};
        for (int i = 1; i < ret.length - 1; i++) {
            double nm = NewMoon(k + i);
            int[] a = LocalFromJD(nm);
            ret[i] = new int[]{a[0], a[1], a[2], 0, 0};
        }
        for (int i = 0; i < ret.length; i++) {
            ret[i][3] = MOD(i + 11, 12);
        }
        if (leap) {
            initLeapYear(ret);
        }
        return ret;
    }

    static void initLeapYear(int[][] ret) {
        double[] sunLongitudes = new double[ret.length];
        for (int i = 0; i < ret.length; i++) {
            int[] a = ret[i];
            double jdAtMonthBegin = LocalToJD(a[0], a[1], a[2]);
            sunLongitudes[i] = SunLongitude(jdAtMonthBegin);
        }
        boolean found = false;
        for (int i = 0; i < ret.length; i++) {
            if (found) {
                ret[i][3] = MOD(i + 10, 12);
                continue;
            }
            double sl1 = sunLongitudes[i];
            double sl2 = sunLongitudes[i + 1];
            boolean hasMajorTerm = Math.floor(sl1 / Math.PI * 6) != Math.floor(sl2 / Math.PI * 6);
            if (!hasMajorTerm) {
                found = true;
                ret[i][4] = 1;
                ret[i][3] = MOD(i + 10, 12);
            }
        }
    }

    public static int[] Solar2Lunar(int D, int M, int Y) {
        int yy = Y;
        int[][] ly = LunarYear(Y); // Please cache the result of this computation for later use!!!
        int[] month11 = ly[ly.length - 1];
        double jdToday = LocalToJD(D, M, Y);
        double jdMonth11 = LocalToJD(month11[0], month11[1], month11[2]);
        if (jdToday >= jdMonth11) {
            ly = LunarYear(Y + 1);
            yy = Y + 1;
        }
        int i = ly.length - 1;
        while (jdToday < LocalToJD(ly[i][0], ly[i][1], ly[i][2])) {
            i--;
        }
        int dd = (int) (jdToday - LocalToJD(ly[i][0], ly[i][1], ly[i][2])) + 1;
        int mm = ly[i][3];
        if (mm >= 11) {
            yy--;
        }
        return new int[]{dd, mm, yy, ly[i][4]};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunar);

        mapping();

        File fileFolk = new File(FULL_PATH);
        if (!fileFolk.exists()) {
            Log.d("Full path", FULL_PATH);
            File fileDir = new File(DIRECTORY_PATH);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
                Log.d("Directory path", DIRECTORY_PATH);
                try {
                    copyDataToDatabase(getAssets().open(DATABASE_NAME), new FileOutputStream(fileFolk));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            loadDataFromDatabase();
        }

        random = new Random();
        quoteBefore = random.nextInt(mListFolk.size());
    }

    private void loadDataFromDatabase() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.query(DATABaSE_TABLE, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                mListFolk.add(cursor.getString(1));
            }
            Log.d("Load data ", mListFolk.size() + "");
        }
        database.close();
    }

    private void copyDataToDatabase(InputStream open, OutputStream fileOutputStream) throws IOException {
        int readline = 0;
        byte[] buff = new byte[1024];
        while ((readline = open.read(buff)) > 0) {
            fileOutputStream.write(buff, 0, readline);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        open.close();
    }

    private void mapping() {
        tvMonthYear = (TextView) findViewById(R.id.tvMonthYear);
        tvDayOfMonth = (TextView) findViewById(R.id.tvDayOfMonth);
        tvDay = (TextView) findViewById(R.id.tvDay);
        tvQuote = (TextView) findViewById(R.id.tvQuote);
        tvMonthLunar = (TextView) findViewById(R.id.tvMonthLunar);
        tvDayLunar = (TextView) findViewById(R.id.tvDayLunar);
        tvYearLunar = (TextView) findViewById(R.id.tvYearLunar);
        tvNameOfMonth = (TextView) findViewById(R.id.tvNameOfMonth);
        tvNameOfDay = (TextView) findViewById(R.id.tvNameOfDay);
        tvNameOfYear = (TextView) findViewById(R.id.tvNameOfYear);
    }

    private void showCanChiDay() {
        int a, y, m, jd;
        a = (14 - MONTH) / 12;
        y = YEAR + 4800 - a;
        m = MONTH + 12 * a - 3;
        jd = DAY + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045;
        if (jd < 2299161) {
            jd = DAY + (153 * m + 2) / 5 + 365 * y + y / 4 - 32083;
        }
        int CAN_DAY = (jd + 9) % 10;
        int CHI_DAY = (jd + 1) % 12;
        String DAY_CAN = "", DAY_CHI = "";

        switch (CAN_DAY) {
            case 0:
                DAY_CAN = "Giáp";
                break;
            case 1:
                DAY_CAN = "Ất";
                break;
            case 2:
                DAY_CAN = "Bính";
                break;
            case 3:
                DAY_CAN = "Đinh";
                break;
            case 4:
                DAY_CAN = "Mậu";
                break;
            case 5:
                DAY_CAN = "Kỷ";
                break;
            case 6:
                DAY_CAN = "Canh";
                break;
            case 7:
                DAY_CAN = "Tân";
                break;
            case 8:
                DAY_CAN = "Nhâm";
                break;
            case 9:
                DAY_CAN = "Quý";
                break;
        }
        switch (CHI_DAY) {
            case 0:
                DAY_CHI = "Tý";
                break;
            case 1:
                DAY_CHI = "Sửu";
                break;
            case 2:
                DAY_CHI = "Giáp";
                break;
            case 3:
                DAY_CHI = "Mão";
                break;
            case 4:
                DAY_CHI = "Thìn";
                break;
            case 5:
                DAY_CHI = "Tị";
                break;
            case 6:
                DAY_CHI = "Ngọ";
                break;
            case 7:
                DAY_CHI = "Mùi";
                break;
            case 8:
                DAY_CHI = "Thân";
                break;
            case 9:
                DAY_CHI = "Dậu";
                break;
            case 10:
                DAY_CHI = "Tuất";
                break;
            case 11:
                DAY_CHI = "Hợi";
                break;
        }

        Log.d("Năm can chi ", DAY_CAN + " " + DAY_CHI);
        tvNameOfDay.setText("Ngày " + DAY_CAN + " " + DAY_CHI);

    }

    private void showCanChiMonth() {
        int CAN_MONTH_LUNAR = (MONTHL + YEARL * 12 + 3) % 10;
        String MONTH_LUNAR_CAN = "", MONTH_LUNAR_CHI = "";
        switch (CAN_MONTH_LUNAR) {
            case 0:
                MONTH_LUNAR_CAN = "Giáp";
                break;
            case 1:
                MONTH_LUNAR_CAN = "Ất";
                break;
            case 2:
                MONTH_LUNAR_CAN = "Bính";
                break;
            case 3:
                MONTH_LUNAR_CAN = "Đinh";
                break;
            case 4:
                MONTH_LUNAR_CAN = "Mậu";
                break;
            case 5:
                MONTH_LUNAR_CAN = "Kỷ";
                break;
            case 6:
                MONTH_LUNAR_CAN = "Canh";
                break;
            case 7:
                MONTH_LUNAR_CAN = "Tân";
                break;
            case 8:
                MONTH_LUNAR_CAN = "Nhâm";
                break;
            case 9:
                MONTH_LUNAR_CAN = "Quý";
                break;
        }

        switch (MONTHL) {
            case 1:
                MONTH_LUNAR_CHI = "Dần";
                break;
            case 2:
                MONTH_LUNAR_CHI = "Mão";
                break;
            case 3:
                MONTH_LUNAR_CHI = "Thin";
                break;
            case 4:
                MONTH_LUNAR_CHI = "Tị";
                break;
            case 5:
                MONTH_LUNAR_CHI = "Ngọ";
                break;
            case 6:
                MONTH_LUNAR_CHI = "Mùi";
                break;
            case 7:
                MONTH_LUNAR_CHI = "Thân";
                break;
            case 8:
                MONTH_LUNAR_CHI = "Dậu";
                break;
            case 9:
                MONTH_LUNAR_CHI = "Tuất";
                break;
            case 10:
                MONTH_LUNAR_CHI = "H";
                break;
            case 11:
                MONTH_LUNAR_CHI = "Tý";
                break;
            case 12:
                MONTH_LUNAR_CHI = "Sửu";
                break;
        }
        Log.d("Can chi tháng âm lịch ", MONTH_LUNAR_CAN + " " + MONTH_LUNAR_CHI);
        tvNameOfMonth.setText("Tháng " + MONTH_LUNAR_CAN + " " + MONTH_LUNAR_CHI);
    }

    private void showCanChiYear() {
        int CAN_YEAR = (YEAR + 6) % 10;
        int CHI_YEAR = (YEAR + 8) % 12;

        String YEAR_CAN = "", YEAR_CHI = "";

        switch (CAN_YEAR) {
            case 0:
                YEAR_CAN = "Giáp";
                break;
            case 1:
                YEAR_CAN = "Ất";
                break;
            case 2:
                YEAR_CAN = "Bính";
                break;
            case 3:
                YEAR_CAN = "Đinh";
                break;
            case 4:
                YEAR_CAN = "Mậu";
                break;
            case 5:
                YEAR_CAN = "Kỷ";
                break;
            case 6:
                YEAR_CAN = "Canh";
                break;
            case 7:
                YEAR_CAN = "Tân";
                break;
            case 8:
                YEAR_CAN = "Nhâm";
                break;
            case 9:
                YEAR_CAN = "Quý";
                break;
        }
        switch (CHI_YEAR) {
            case 0:
                YEAR_CHI = "Tý";
                break;
            case 1:
                YEAR_CHI = "Sửu";
                break;
            case 2:
                YEAR_CHI = "Giáp";
                break;
            case 3:
                YEAR_CHI = "Mão";
                break;
            case 4:
                YEAR_CHI = "Thìn";
                break;
            case 5:
                YEAR_CHI = "Tị";
                break;
            case 6:
                YEAR_CHI = "Ngọ";
                break;
            case 7:
                YEAR_CHI = "Mùi";
                break;
            case 8:
                YEAR_CHI = "Thân";
                break;
            case 9:
                YEAR_CHI = "Dậu";
                break;
            case 10:
                YEAR_CHI = "Tuất";
                break;
            case 11:
                YEAR_CHI = "Hợi";
                break;
        }

        Log.d("Năm can chi ", YEAR_CAN + " " + YEAR_CHI);
        tvNameOfYear.setText("Năm " + YEAR_CAN + " " + YEAR_CHI);
    }

    private void showDayOfWeekLunar() {
        switch (DAY_OF_WEEK) {
            case 1:
                tvDay.setText("Chủ nhật");
                break;
            case 2:
                tvDay.setText("Thứ hai");
                break;
            case 3:
                tvDay.setText("Thứ ba");
                break;
            case 4:
                tvDay.setText("Thứ tư");
                break;
            case 5:
                tvDay.setText("Thứ năm");
                break;
            case 6:
                tvDay.setText("Thứ sáu");
                break;
            case 7:
                tvDay.setText("Thứ bảy");
                break;
        }
    }

    private void showQuote() {
        String quote = mListFolk.get(quoteBefore);
        tvQuote.setText(quote);
        quoteBefore++;
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvDayOfMonth.setText(DAY + "");
        tvMonthLunar.setText("Tháng " + MONTHL);
        tvYearLunar.setText(YEARL + "");
        tvMonthYear.setText("Tháng " + MONTH + " Năm " + YEAR);
        tvDayLunar.setText(DAYL + "");
        showCanChiMonth();
        showCanChiDay();
        showCanChiYear();
        showDayOfWeekLunar();
        showQuote();

    }


}
