package com.ek.wshopapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class CreateShoppingCartActivity extends MainActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_shoppingbasket);
		new GetProductsFromServer().execute();
		Button button = (Button) findViewById(R.id.create);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				CreateShoppingCartOnServer createShoppingCartOnServer = new CreateShoppingCartOnServer(
						ip);
				boolean Empty = true;
				Spinner product = (Spinner) findViewById(R.id.prospinner);
				EditText quantity = (EditText) findViewById(R.id.quantityview);
				if (quantity.getText().toString().equals("")) {
					quantity.setError(" please write quantity");
					Empty = false;
				}
				String productId = Integer.toString((int) product
						.getSelectedItemId());
				
				createShoppingCartOnServer.setQuantity(quantity.getText().toString());
				createShoppingCartOnServer.setProduct(productId);//?????
				if(Empty=true){					
					createShoppingCartOnServer.execute();
				}
			}
		});
	}

	class CreateShoppingCartOnServer extends AsyncTask<Void, Void, Boolean> {
		private String ip;
		private String product;
		private String quantity;
		
		public CreateShoppingCartOnServer(String ip) {
			this.ip = ip;
		}

		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}

		public void setProduct(String product) {
			this.product = product;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				HttpPost post = new HttpPost("http://" + ip
						+ ":9000/order/showOrderForm");

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				//
				nameValuePairs.add(new BasicNameValuePair("quantity", quantity));
				nameValuePairs.add(new BasicNameValuePair("product-id",
						product));//???
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				new DefaultHttpClient().execute(post,
						new BasicResponseHandler());
				
				DefaultHttpClient client= new DefaultHttpClient();
				if(cookies != null){
					for(Cookie cookie: cookies){
						client.getCookieStore().addCookie(cookie);
					}
				}
				client.execute(post,new BasicResponseHandler());
				
				return true;
			} catch (Exception e) {
				Log.e("Error creating shoppingcart", e.getMessage());

				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean success) {
			if (success == true) {
				Toast.makeText(getApplicationContext(), "Success!",
						Toast.LENGTH_LONG).show();
				
			} else {
				Toast.makeText(getApplicationContext(), "Big problem!",
						Toast.LENGTH_LONG).show();
			}
		}
	}// End of CreateShoppingCartOnServer

	class GetProductsFromServer extends AsyncTask<Void, Void, JSONArray> {
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
			Spinner spinner = (Spinner) findViewById(R.id.prospinner);
			spinner.setAdapter(new ProductAdapter(result));
		}
	}// End of GetCategoriesFromServer

	class ProductAdapter extends BaseAdapter implements SpinnerAdapter {
		private JSONArray products;

		public ProductAdapter(JSONArray products) {
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
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public long getItemId(int index) {
			try {
				return products.getJSONObject(index).getInt("id");
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public View getView(int index, View convertView, ViewGroup parent) {
			TextView textView = new TextView(getApplicationContext());
			try {
				JSONObject category = products.getJSONObject(index);
				textView.setText(category.getString("productname"));
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			textView.setTextSize(18);
			return textView;
		}
	}// End of CategoryAdapter

}
