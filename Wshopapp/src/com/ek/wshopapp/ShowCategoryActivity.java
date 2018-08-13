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

public class ShowCategoryActivity extends MainActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_categories);

		new LoadCategoryList(ip).execute();
	}

	class LoadCategoryList extends AsyncTask<Void, Void, JSONArray> {
		private String ip;

		public LoadCategoryList(String ip) {
			this.ip = ip;
		}

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
			ListView listView = (ListView) findViewById(R.id.showcategories_views);
			listView.setAdapter(new CategoryListAdapter(result));
		}
	}// End of LoadCategoryList

	class CategoryListAdapter extends BaseAdapter {
		private JSONArray categories;

		public CategoryListAdapter(JSONArray categories) {
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
			RelativeLayout categoryView = (RelativeLayout) getLayoutInflater()
					.inflate(R.layout.show_categories_item, parent, false);

			try {
				JSONObject category = categories.getJSONObject(index);

				TextView title = (TextView) categoryView
						.findViewById(R.id.title);
				TextView subtitle = (TextView) categoryView
						.findViewById(R.id.subtitle);
				title.setText("Category name: "
						+ category.getString("categoryname"));
				subtitle.setText("category id: "
						+ category.getString("id"));
				

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return categoryView;
		}
	} //CategoryListAdapter

}
