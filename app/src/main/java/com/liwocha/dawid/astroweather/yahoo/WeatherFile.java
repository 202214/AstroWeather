package com.liwocha.dawid.astroweather.yahoo;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class WeatherFile {

    private static final String FILE_NAME = "LAST_WEATHER_INFO.txt";

    public static void writeToFile(String data, Context context) throws Exception{
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE));
        outputStreamWriter.write(data);
        outputStreamWriter.close();
    }

    public static String readFromFile(Context context) throws Exception {
        String ret = "";
        InputStream inputStream = context.openFileInput(FILE_NAME);
        if ( inputStream != null ) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ( (receiveString = bufferedReader.readLine()) != null ) {
                stringBuilder.append(receiveString);
            }
            inputStream.close();
            ret = stringBuilder.toString();
        }
        return ret;
    }

}
