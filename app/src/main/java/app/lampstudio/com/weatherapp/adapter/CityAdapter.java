package app.lampstudio.com.weatherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.lampstudio.com.weatherapp.Model.CityResult;
import app.lampstudio.com.weatherapp.R;
import app.lampstudio.com.weatherapp.parser.YahooClient;

/**
 * Created by VS9 X64Bit on 05/05/2017.
 */

public class CityAdapter extends ArrayAdapter<CityResult> implements Filterable {
    private Context ctx;
    private List<CityResult> cityList = new ArrayList<CityResult>();
    public CityAdapter(Context ctx, List<CityResult> cityList) {
        super(ctx, R.layout.cityresult_layout, cityList);
        this.cityList = cityList;
        this.ctx = ctx;
    }

    @Override
    public CityResult getItem(int position) {
        if (cityList != null)
            return cityList.get(position);
        return null;
    }

    @Override
    public int getCount() {
        if (cityList != null)
            return cityList.size();

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result = convertView;

        if (result == null) {
            LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            result = inf.inflate(R.layout.cityresult_layout, parent, false);

        }

        TextView tv = (TextView) result.findViewById(R.id.txtCityName);
        tv.setText(cityList.get(position).getCityName() + "," + cityList.get(position).getCountry());

        return result;
    }

    @Override
    public long getItemId(int position) {
        if (cityList != null)
            return cityList.get(position).hashCode();

        return 0;
    }
    @Override
    public Filter getFilter() {
        Filter cityFilter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() < 2)
                return results;

                List<CityResult> cityResultList = YahooClient.getCityList(constraint.toString());
                results.values = cityResultList;
                results.count = cityResultList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                cityList = (List) results.values;
                notifyDataSetChanged();
            }
        };

        return cityFilter;
    }
}
