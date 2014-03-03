package jp.tohoh.DTCollection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity {

	public static int REQ_LOAD = 1;
	public static int REQ_SAVE = 12;
	
	private MainView mv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mv = new MainView(this);
		setContentView(mv);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent intent;
		switch(item.getItemId()){
		case R.id.action_settings:
			intent = new Intent(this,PrefActivity.class);
			startActivity(intent);
			break;
		case R.id.item1:
			intent = new Intent(this,PrefActivity.class);
			startActivity(intent);
			break;
		case R.id.item2:
			intent = new Intent(this,PrefActivity.class);
			startActivity(intent);
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

	
	
	//	設定画面を開く
	public void onSetting(MenuItem item){
		Toast.makeText(this, "テスト", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(this,PrefActivity.class);
		startActivity(intent);
	}
	
	//	セーブ画面を開く
	public void onSave(MenuItem item){
		Toast.makeText(this, "テスト", Toast.LENGTH_LONG).show();
	}
	
	//	ロード画面を開く
	public void onLoad(MenuItem item){
		Toast.makeText(this, "テスト", Toast.LENGTH_LONG).show();
		
	}
	


}
