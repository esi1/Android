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
import android.widget.TextView;

public class ShowShoppingBasketActivity extends MainActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_shoppingbasket);
		new LoadShoppingBaskettList(ip).execute();
	}

	class LoadShoppingBaskettList extends AsyncTask<Void, Void, JSONArray> {
		private String ip;

		public LoadShoppingBaskettList(String ip) {
			this.ip = ip;
		}

		@Override
		protected JSONArray doInBackground(Void... params) {
			try {
				String response = new DefaultHttpClient().execute(new HttpGet(
						"http://" + ip + ":9000/order/showOrder"),
						new BasicResponseHandler());
				return new JSONArray(response);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			ListView listView = (ListView) findViewById(R.id.showshoppingbasket_view);
			listView.setAdapter(new ShoppingBasketAdapter(result));
		}
	}

	class ShoppingBasketAdapter extends BaseAdapter {
		private JSONArray shoppingbaskets;

		public ShoppingBasketAdapter(JSONArray shoppingbasket) {
			this.shoppingbaskets = shoppingbasket;
		}

		@Override
		public int getCount() {
			return shoppingbaskets.length();
		}

		@Override
		public Object getItem(int index) {
			try {
				return shoppingbaskets.getJSONObject(index);
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
					.inflate(R.layout.show_shoppingbasket_item, parent, false);

			try {
				JSONObject shoppingbasket = shoppingbaskets
						.getJSONObject(index);
				TextView title = (TextView) productView
						.findViewById(R.id.title);
				TextView subtitle = (TextView) productView
						.findViewById(R.id.subtitle);
				TextView subsubtitle = (TextView) productView
						.findViewById(R.id.subsubtitle);

				String quantity = shoppingbasket.getString("quantity");
				subtitle.setText("quantity: " + quantity);
				JSONArray prodArray = shoppingbasket.getJSONArray("products");
				StringBuilder builder = new StringBuilder();
				StringBuilder builder1 = new StringBuilder();
				String shop = "";
				String shop1 = "";
				for (int i = 0; prodArray.length() > i; i++) {

					JSONObject jsonObject = prodArray.getJSONObject(i);

					shop = jsonObject.getString("productname");
					shop1 = jsonObject.getString("cost");
					builder.append(shop);
					builder1.append(shop1);
				}
				title.setText("Product name: " + builder.toString());
				subsubtitle.setText("Cost (Pcs): " + builder1.toString());

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return productView;
		}
	}// ShoppingBasketAdapter

}
