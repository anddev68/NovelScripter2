package jp.tohoh.DTCollection;

import java.io.FileNotFoundException;

import scripter.Variable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/*************************************
 * セーブ画面のアクティビティー
 *
 * @see activity_save
 *************************************/
public class LoadActivity extends Activity implements OnItemClickListener{
	
	ListView list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save);
		
		list = (ListView) findViewById(R.id.ListView1);
		list.setOnItemClickListener(this);
		list.setAdapter(getAdapter());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	/* データを取得します */
	public ListAdapter getAdapter(){
		String members[] = {"01","02","03"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, members);
		return adapter;
	}


	
	
	/*************************************************************************
	 * アイテムが押されたときの処理です
	 * 上から順番に0.txtというように名前をつけてあります
	 * ファイル名に保存されているデータを取り出します
	 ************************************************************************/
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
		try {
			Variable.setData(this.openFileInput(pos+".txt"));
		} catch (FileNotFoundException e) {
			new AlertDialog.Builder(this)
			.setTitle("データの読み込みに失敗しました")
			.setPositiveButton("OK", new OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					finish();
				}
				
			}).show();
			
		}
		finish();
	}
	
}
