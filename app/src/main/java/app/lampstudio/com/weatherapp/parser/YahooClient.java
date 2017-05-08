package app.lampstudio.com.weatherapp.parser;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import app.lampstudio.com.weatherapp.Config;
import app.lampstudio.com.weatherapp.Model.CityResult;

/**
 * Created by VS9 X64Bit on 05/05/2017.
 */

public class YahooClient {
    public static String YAHOO_GEO_URL = "http://where.yahooapis.com/v1";
    public static String YAHOO_WEATHER_URL = "http://weather.yahooapis.com/forecastrss";

    private static String APPID = "APP_ID_KEY";

    public static List<CityResult> getCityList(String cityName) {
        List<CityResult> result = new ArrayList<CityResult>();
        HttpURLConnection yahooHttpConn = null;
        String query =makeQueryCityURL(cityName);

        XmlPullParser parser = null;
        try {
            yahooHttpConn= (HttpURLConnection) (new URL(query)).openConnection();
            yahooHttpConn.connect();
            parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new InputStreamReader(yahooHttpConn.getInputStream()));
            Log.d("Swa", "XML Parser ok");
            int event = parser.getEventType();

            CityResult cty = null;
            String tagName = null;
            String currentTag = null;

            // We start parsing the XML
            while (event != XmlPullParser.END_DOCUMENT) {
                tagName = parser.getName();

                if (event == XmlPullParser.START_TAG) {
                    if (tagName.equals("place")) {
                        // place Tag Found so we create a new CityResult
                        cty = new CityResult();
                        //  Log.d("Swa", "New City found");
                    }
                    currentTag = tagName;
                    // Log.d("Swa", "Tag ["+tagName+"]");
                }
                else if (event == XmlPullParser.TEXT) {
                    // We found some text. let's see the tagName to know the tag related to the text
                    if ("woeid".equals(currentTag))
                        cty.setWoeid(parser.getText());
                    else if ("name".equals(currentTag))
                        cty.setCityName(parser.getText());
                    else if ("country".equals(currentTag))
                        cty.setCountry(parser.getText());

                    // We don't want to analyze other tag at the moment
                }
                else if (event == XmlPullParser.END_TAG) {
                    if ("place".equals(tagName))
                        result.add(cty);
                }

                event = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
            // Log.e("Error in getCityList", t.getMessage());
        }
        return result;

    }
    private static String makeQueryCityURL(String cityName) {
        // We remove spaces in cityName
        cityName = cityName.replaceAll(" ", "%20");
        return YAHOO_GEO_URL + "/places.q(" + cityName + "%2A);count=" + Config.MAX_CITY_RESULT + "?appid=" + APPID;
    }
}
