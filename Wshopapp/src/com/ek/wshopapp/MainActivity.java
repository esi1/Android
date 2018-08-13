package com.ek.wshopapp;

import java.util.List;

import org.apache.http.cookie.Cookie;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	protected final String ip = "192.168.1.23";
	static protected List<Cookie> cookies;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);

		menu.findItem(R.id.createproductmenu).setVisible(false);
		menu.findItem(R.id.createcategorymenu).setVisible(false);
		menu.findItem(R.id.logoutmenu).setVisible(false);
		menu.findItem(R.id.shoppingbasketmenu).setVisible(false);
		menu.findItem(R.id.showshoppingbasketmenu).setVisible(false);
		
		boolean loggedIn = false;
		if (cookies != null) {

			for (Cookie cookie : cookies) {
				if ("PLAY_SESSION".equals(cookie.getName())) {
					loggedIn = true;
				}
			}
			if (loggedIn) {
				MenuItem item = menu.findItem(R.id.loginmenu);
				item.setVisible(false);
				menu.findItem(R.id.createcategorymenu).setVisible(true);
				menu.findItem(R.id.createproductmenu).setVisible(true);
				menu.findItem(R.id.logoutmenu).setVisible(true);
				menu.findItem(R.id.shoppingbasketmenu).setVisible(true);
				menu.findItem(R.id.showshoppingbasketmenu).setVisible(true);
			}
		}

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.loginmenu) {
			startActivity(new Intent(this, LoginActivity.class));
			return true;
		}
		if (item.getItemId() == R.id.logoutmenu) {
			startActivity(new Intent(this, LogoutActivity.class));
			return true;
		}
		if (item.getItemId() == R.id.showproductsmenu) {
			startActivity(new Intent(this, ShowProductsActivity.class));
			return true;
		}
		if (item.getItemId() == R.id.showcategorymenu) {
			startActivity(new Intent(this, ShowCategoryActivity.class));
			return true;
		}

		if (item.getItemId() == R.id.createproductmenu) {
			startActivity(new Intent(this, CreateProductActivity.class));
			return true;
		}
		if (item.getItemId() == R.id.createcategorymenu) {
			startActivity(new Intent(this, CreateCategoryActivity.class));
			return true;
		}
		if (item.getItemId() == R.id.shoppingbasketmenu) {
			startActivity(new Intent(this, CreateShoppingCartActivity.class));
			return true;
		}
		if (item.getItemId() == R.id.showshoppingbasketmenu) {
			startActivity(new Intent(this, ShowShoppingBasketActivity.class));
			return true;
		}

		return false;
	}
}
