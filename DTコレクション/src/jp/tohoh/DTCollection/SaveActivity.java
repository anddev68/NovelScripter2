package jp.tohoh.DTCollection;

import java.io.FileNotFoundException;

import scripter.Variable;
import android.app.Activity;
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
public class SaveActivity extends Activity implements OnItemClickListener{
	
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
			Variable.writeData(this.openFileOutput(pos+".txt",MODE_PRIVATE));
			finish();
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	

}
