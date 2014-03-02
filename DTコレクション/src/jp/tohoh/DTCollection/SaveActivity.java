package jp.tohoh.DTCollection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

/*************************************
 * セーブ画面のアクティビティー
 *
 *************************************/
public class SaveActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new MainView(this));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Intent intent = new Intent(this,PrefActivity.class);
		startActivity(intent);
		
		//	getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/* セーブボタンが押されたとき */
	public void onSaveButton(View v){
		//	とりあえず0番が押されたことにしておく
		setResult(0);
	    finish();
	
	}
	
	

}
