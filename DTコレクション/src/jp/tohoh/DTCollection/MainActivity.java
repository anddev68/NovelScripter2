package jp.tohoh.DTCollection;

import scripter.RecordData;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

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
		// Inflate the menu; this adds items to the action bar if it is present.
		Intent intent = new Intent(this,PrefActivity.class);
		startActivity(intent);
		
		//	getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == REQ_LOAD){
			//	resultCodeはファイル番号
		}else if(requestCode == REQ_SAVE){
			RecordData r = mv.getData();
		
		}
		
		
	}

}
