package com.ek.wshopapp;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ShowProductsActivity extends MainActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_products);

		new LoadProductList(ip).execute();
	}

	class LoadProductList extends AsyncTask<Void, Void, JSONArray> {
		private String ip;

		public LoadProductList(String ip) {
			this.ip = ip;
		}

		@Override
		protected JSONArray doInBackground(Void... params) {
			try {
				String response = new DefaultHttpClient().execute(new HttpGet(
						"http://" + ip + ":9000/product/showProduct"),
						new BasicResponseHandler());
				return new JSONArray(response);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			ListView listView = (ListView) findViewById(R.id.showproduct_view);
			listView.setAdapter(new ProductListAdapter(result));
		}
	}

	class ProductListAdapter extends BaseAdapter {
		private JSONArray products;

		public ProductListAdapter(JSONArray products) {
			this.products = products;
		}

		@Override
		public int getCount() {
			return products.length();
		}

		@Override
		public Object getItem(int index) {
			try {
				return products.getJSONObject(index);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public long getItemId(int index) {
			return index;
		}

		@Override
		public View getView(int index, View convertView, ViewGroup parent) {
			RelativeLayout productView = (RelativeLayout) getLayoutInflater()
					.inflate(R.layout.show_products_item, parent, false);

			try {
				JSONObject product = products.getJSONObject(index);
				TextView title = (TextView) productView
						.findViewById(R.id.title);
				TextView subtitle = (TextView) productView
						.findViewById(R.id.subtitle);

				title.setText("Product name: "+product.getString("productname") +"\nDescription: "+ product.getString("description")+"\nCost: "+product.getString("cost"));
				subtitle.setText("Category: " + product.getJSONObject("category").getString("categoryname"));
				

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return productView;
		}
	}//ProductListAdapter
}
