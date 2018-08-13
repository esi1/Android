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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CreateProductActivity extends MainActivity {
	private EditText name, description, cost, rrp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_product);
		new GetCategoriesFromServer().execute();
		Button button = (Button) findViewById(R.id.create);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				CreateProductOnServer createProductOnServer = new CreateProductOnServer(
						ip);
				boolean Empty=true;
				name = (EditText) findViewById(R.id.productnameview);
				description = (EditText) findViewById(R.id.descriptionview);
				cost = (EditText) findViewById(R.id.costview);
				rrp = (EditText) findViewById(R.id.rrpview);
				if (name.getText().toString().equals("")) {
					name.setError(" please write name");
					Empty = false;
				}
				if (description.getText().toString().equals("")) {
					description.setError(" please write description");
					Empty = false;
				}
				if (cost.getText().toString().equals("")) {
					cost.setError(" please write cost");
					Empty = false;
				}
				if (rrp.getText().toString().equals("")) {
					rrp.setError(" please write rrp");
					Empty = false;
				}
				if (name.getText().toString().equals("")
						&& description.getText().toString().equals("")
						&& cost.getText().toString().equals("")
						&& rrp.getText().toString().equals("")) {
					name.setError(" please write name");
					description.setError(" please write description");
					cost.setError(" please write cost");
					rrp.setError(" please write rrp");
					Empty = false;
				}
				Spinner category = (Spinner) findViewById(R.id.productspinner);
				String categoryId = Integer.toString((int) category
						.getSelectedItemId());
				createProductOnServer.setProductName(name.getText().toString());
				createProductOnServer.setProductDescription(description
						.getText().toString());
				createProductOnServer.setProductCost(cost.getText().toString());
				createProductOnServer.setProductRrp(rrp.getText().toString());
				createProductOnServer.setCategory(categoryId);
				if(Empty=true){
					createProductOnServer.execute();					
				}
			}
		});
	}

	class CreateProductOnServer extends AsyncTask<Void, Void, Boolean> {
		private String ip;
		private String name;
		private String description;
		private String cost;
		private String rrp;
		private String category;

		public CreateProductOnServer(String ip) {
			this.ip = ip;
		}

		public void setProductDescription(String description) {
			this.description = description;
		}

		public void setProductName(String name) {
			this.name = name;
		}

		public void setProductCost(String cost) {
			this.cost = cost;
		}

		public void setProductRrp(String rrp) {
			this.rrp = rrp;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				HttpPost post = new HttpPost("http://" + ip
						+ ":9000/product/showProductForm");

				DefaultHttpClient client = new DefaultHttpClient();
				if (cookies != null) {
					for (Cookie cookie : cookies) {
						client.getCookieStore().addCookie(cookie);
					}
				}

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("productname", name));
				nameValuePairs.add(new BasicNameValuePair("description",
						description));
				nameValuePairs.add(new BasicNameValuePair("cost", cost));
				nameValuePairs.add(new BasicNameValuePair("rrp", rrp));
				nameValuePairs.add(new BasicNameValuePair("category-id",
						category));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				client.execute(post, new BasicResponseHandler());

				return true;
			} catch (Exception e) {
				Log.e("Error creating product", e.getMessage());

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

	}// End of CreateProductOnServer

	class GetCategoriesFromServer extends AsyncTask<Void, Void, JSONArray> {
		@Override
		protected JSONArray doInBackground(Void... params) {
			try {
				String response = new DefaultHttpClient().execute(new HttpGet(
						"http://" + ip + ":9000/category/showCategory"),
						new BasicResponseHandler());
				return new JSONArray(response);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			Spinner spinner = (Spinner) findViewById(R.id.productspinner);
			spinner.setAdapter(new CategoryAdapter(result));
		}
	}// End of GetCategoriesFromServer

	class CategoryAdapter extends BaseAdapter implements SpinnerAdapter {
		private JSONArray categories;

		public CategoryAdapter(JSONArray categories) {
			this.categories = categories;
		}

		@Override
		public int getCount() {
			return categories.length();
		}

		@Override
		public Object getItem(int index) {
			try {
				return categories.getJSONObject(index);
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public long getItemId(int index) {
			try {
				return categories.getJSONObject(index).getInt("id");
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public View getView(int index, View convertView, ViewGroup parent) {
			TextView textView = new TextView(getApplicationContext());
			try {
				JSONObject category = categories.getJSONObject(index);
				textView.setText(category.getString("categoryname"));
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			textView.setTextSize(18);
			return textView;
		}
	}// End of CategoryAdapter

}
