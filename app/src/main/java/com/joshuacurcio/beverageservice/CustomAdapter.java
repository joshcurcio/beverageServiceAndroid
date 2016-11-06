package com.joshuacurcio.beverageservice;

/**
 * Created by Josh on 11/3/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.joshuacurcio.beverageservice.Objects.OrderItem;

import java.util.ArrayList;

import static com.google.android.gms.internal.zzs.TAG;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class CustomAdapter extends BaseAdapter implements View.OnClickListener, Filterable {

    /***********
     * Declare Used Variables
     *********/
    private Activity activity;
    private ArrayList<OrderItem> data;
    private ArrayList<OrderItem> filterData;
    private ArrayList<OrderItem> filterData2;
    private ArrayList<OrderItem> dataOriginal;
    private static LayoutInflater inflater = null;
    public Resources res;
    OrderItem tempValues = null;
    int i = 0;

    /*************
     * CustomAdapter Constructor
     *****************/
    public CustomAdapter(Activity a, ArrayList<OrderItem> d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data = d;
        filterData = d;
        filterData2 = d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /********
     * What is the size of Passed Arraylist Size
     ************/
    public int getCount() {

        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                data = (ArrayList<OrderItem>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<OrderItem> FilteredArrList = new ArrayList<OrderItem>();

                if (dataOriginal == null) {
                    dataOriginal = new ArrayList<OrderItem>(data); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = dataOriginal.size();
                    results.values = dataOriginal;
                } else {

                    constraint = constraint.toString().toLowerCase();
                    Log.v(TAG, "category:" + constraint.toString().toLowerCase());


                    String constraints = constraint.toString();
                    if (constraint.toString().contains("category:")) {
                        for (int i = 0; i < dataOriginal.size(); i++) {
                            String data = dataOriginal.get(i).getName().toString().toLowerCase();


                            if (constraints.contains(data.toLowerCase())) {

                                OrderItem lm = new OrderItem();
                                lm.setName(dataOriginal.get(i).getName());
                                lm.setPrice(dataOriginal.get(i).getPrice());
                                lm.setQuantity(dataOriginal.get(i).getQuantity());

                                FilteredArrList.add(lm);
                            }
                        }
                        filterData2 = FilteredArrList;
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    } else {

                        for (int i = 0; i < filterData.size(); i++) {
                            String data = filterData.get(i).getName();
                            String data2 = Double.toString(filterData.get(i).getPrice());
                            if (data.toLowerCase().startsWith(constraint.toString())) {
                                OrderItem lm = new OrderItem();
                                lm.setName(filterData.get(i).getName());
                                lm.setPrice(filterData.get(i).getPrice());
                                lm.setQuantity(filterData.get(i).getQuantity());
                                FilteredArrList.add(lm);
                            }
                        }
                        // set the Filtered result to return
                        filterData2 = FilteredArrList;
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                }
                return results;
            }
        };
        return filter;
    }

    /*********
     * Create a holder Class to contain inflated xml file elements
     *********/
    public static class ViewHolder {

        public TextView name;
        public TextView price;
        public TextView qty;

    }

    /******
     * Depends upon data size called for each row , Create each ListView row
     *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;
        Button add;
        Button delete;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.tabitem, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.name = (TextView) vi.findViewById(R.id.txtName);
            holder.price = (TextView) vi.findViewById(R.id.txtPrice);
            holder.qty = (TextView) vi.findViewById(R.id.txtQty);
            add = (Button) vi.findViewById(R.id.butAddItem);
            delete = (Button) vi.findViewById(R.id.butDeleteItem);

            vi.findViewById(R.id.butAddItem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tempQty = Integer.parseInt(holder.qty.getText().toString())+ 1;
                    tempValues.setQuantity(tempQty);
                    Singleton.userCart.remove(tempValues);
                    Singleton.userCart.add(tempValues);

                    try {
                        holder.qty.setText(Integer.toString(tempValues.getQuantity()));
                    } catch (Exception e) {
                        Log.v(TAG, "you messed up");

                    }
                }
            });
            vi.findViewById(R.id.butDeleteItem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tempQty = Integer.parseInt(holder.qty.getText().toString()) - 1;
                    if(tempQty >= 0)
                    {
                        Singleton.userCart.remove(tempValues);
                        tempValues.setQuantity(tempQty);
                        if(tempQty == 0)
                        {
                           return;
                        }
                        else
                        {
                            try {
                                Singleton.userCart.add(tempValues);
                                holder.qty.setText(Integer.toString(tempValues.getQuantity()));
                            } catch (Exception e) {
                                Log.v(TAG, "you messed up");
                            }
                        }

                    }
                    else
                    {
                        tempQty = 0;
                        try {
                            holder.qty.setText(tempQty + "");
                        } catch (Exception e) {
                            Log.v(TAG, "you messed up");
                        }
                    }
                }
            });


            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            holder.name.setText("No Results found");
            holder.price.setText("0.00");
            holder.qty.setText("0");

        } else {
            /***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues = (OrderItem) data.get(position);

            /************  Set Model values in Holder elements ***********/
            try {
                holder.name.setText(tempValues.getName());
                holder.price.setText(Double.toString(tempValues.getPrice()));
                holder.qty.setText(Integer.toString(tempValues.getQuantity()));
            } catch (Exception e) {
                Log.v(TAG, "you messed up");

            }
        }
        return vi;
    }

    @Override
    public void onClick(View v) {

    }

    /*********
     * Called when Item click in ListView
     ************/
}
