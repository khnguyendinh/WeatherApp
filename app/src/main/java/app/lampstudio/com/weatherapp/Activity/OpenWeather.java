package app.lampstudio.com.weatherapp.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import app.lampstudio.com.weatherapp.R;

public class OpenWeather extends AppCompatActivity {
    TextView result1;

    EditText cityName;

    // This method would be called when What's the weather button would be pressed
    public void findtheweather(View view) {

        //Assigning the value which user entered to String s
        String s = cityName.getText().toString();

        // Calling download task function
        download task = new download();

        // Executing download task and change this " uk&appid=9f681051b003f104ae5e2a0cbef19a02" with your own API KEY
        task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + s + "uk&appid=9f681051b003f104ae5e2a0cbef19a02");

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_weather);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        cityName = (EditText) findViewById(R.id.cityname);
    }
    // Creating download method with Async Task ( we r going to use this to get data from internet and parse it )
    private class download extends AsyncTask<String, Void, String> {

        // This method execute after doInBackground method and Parse the Json

        @Override
        protected void onPostExecute(String result) {
// Try and catch block to catch any errors
            try {

                // linking result1 with textView with id result
                result1 = (TextView) findViewById(R.id.result);

//                calling  JSONObject as jsonObject
                JSONObject jsonObject = new JSONObject(result);

//                using jsonObject to get String from Json which is tagged as weather
                String weather = jsonObject.getString("weather");
//        calling JSONArray as arr
                JSONArray arr    = new JSONArray(weather);
// using for loop to loop through arr array
                for (int i = 0; i <= arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    // using result1 to set Text of that Text view with id Result  to this
                    result1.setText(String.format("Weather : %s , Description : %s", jsonPart.getString("main"), jsonPart.getString("description")));
                }

//                e.printStackTrace will just print a error report like a normal program do when it crashes u can change this with anything u like
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // do doInBackground method we r using this method to download Json from site.
        @Override
        protected String doInBackground(String... urls) {
            String result = "";

//            calling url as url
            URL url;
//            calling HttpUrlConnection as urlConnection
            HttpURLConnection urlConnection;

//            Using try and catch block to find any errors
            try {
                // assigning url value of first object in array called urls which is declared in this start of this method
                url = new URL(urls[0]);

                // using urlConnection to open url which we assigning URL before
                urlConnection = (HttpURLConnection) url.openConnection();

                // Using InputStream to download the content
                InputStream inputStream = urlConnection.getInputStream();
                // Using InputStreamReader to read the inputstream or the data we r downloading
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

//                using it to check if we reached the end of String / Data
                int Data = inputStreamReader.read();
//              using While loop to assign that data to string called result because InputStreamReader reads only one character at a time
                while (Data != -1) {

                    char current = (char) Data;

                    result += current;

                    Data = inputStreamReader.read();
                }

                // returning value of result

                return result;

//                Try and catch block to catch any errors
            } catch (Exception e) {
//                e.printStackTrace will just print a error report like a normal program do when it crashes u can change this with anything u like
                e.printStackTrace();
            }

            return null;
        }


    }
}
