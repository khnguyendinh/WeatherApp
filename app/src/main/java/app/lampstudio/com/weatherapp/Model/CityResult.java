package app.lampstudio.com.weatherapp.Model;

/**
 * Created by VS9 X64Bit on 05/05/2017.
 */

public class CityResult {
    private String woeid;
    private String cityName;
    private String country;

    public CityResult() {}

    public CityResult(String woeid, String cityName, String country) {
        this.woeid = woeid;
        this.cityName = cityName;
        this.country = country;
    }

    // get and set methods

    @Override
    public String toString() {
        return cityName + "," + country;
    }

    public String getWoeid() {
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
