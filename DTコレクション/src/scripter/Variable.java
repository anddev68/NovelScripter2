package scripter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


/***************************************************
 * NScripter互換のグローバル変数・内部変数を扱うクラスです
 * 各変数に名前でアクセスできるように設定してあります。 
 * 
 * 変数にはグローバル領域・ローカルがあります。
 * $0が文字変数、%0が数値変数です。
 * それぞれ200以降はグローバルとなります。
 * 
 * このクラスは1個以上プログラムに存在することができないため、
 * 静的参照が行えます。
 * 
 * 行番号もこのクラスに静的変数として残しました。
 * セーブ・ロードにはこのクラスに静的メソッドでアクセスしてください。
 * 
 * 
 * 【グローバル変数】
 * ・セーブしようがロードしようが変わらない
 * ・アプリ終了時にストリームか何かに吐き出します。
 * ・アプリ開始時にストリームか何かから読み込みます。
 * ・おまけルート・エピソード解放などに使用。
 * 
 * 【ローカル変数】
 * ・セーブ・ロードで変わってくる変数
 * ・アプリ開始時にリセット
 * ・ロードする度に変更
 * ・各ヒロインの好感度等に使用。
 * 
 * 【各関数の概要】
 * ・Variable()：このクラスのコンストラクタです
 * ・getValue()：指定した変数名の値をString型で取得します。
 * ・setValue()：指定した変数名の値をセットします。
 * ・addValue()：指定した変数名に足します。文字でも数値でも使えます。
 * ・subValue()：指定した変数名を引きます。
 * 
 * 
 ****************************************************/
public class Variable {
	//	グローバルの最大値
	//	メモリの状況で変更してください。
	public static final int GLOBAL_MAX = 400;
	
	//	プログラムで試用する変数です
	public static int[] mValInt = new int[GLOBAL_MAX];
	public static String[] mValStr = new String[GLOBAL_MAX];
	
	//	行番号です
	public static int iLineNum = 0;
	

	/***********************************
	 * 変数名$xxxまたは%xxxのデータを取得します。
	 * 
	 * @param name 変数名 $xxx or %xxx
	 * @return Stringに変換した変数の中身（int,string)
	 * @return 取得できない場合は"0"もしくは""
	 ***********************************/
	public static String getValue(String name){
		String str = "";
		int index;
		try{
			//	変数番号を取得する
			index = Integer.parseInt(name.substring(1));
		
			if(name.charAt(0)=='%')
				str = ""+mValInt[index];
				
			else if(name.charAt(0)=='$')
				str = mValStr[index];
				
		}catch(Exception e){}
		return str;
	}

	/**************************************
	 * 値をセット・演算をします
	 * @param name 変数名 %xxx
	 * @param int 値
	 **************************************/
	public static void setValue(String name,int value){
		int index = Integer.parseInt(name.substring(1));
		mValInt[index] = value;
	}
	
	public static void addValue(String name,String value){
		int index = Integer.parseInt(name.substring(1));
		mValStr[index]+=value;
	}
	public static void addValue(String name,int value){
		int index = Integer.parseInt(name.substring(1));
		mValInt[index]+=value;
	}
	public static void subValue(String name,int value){
		int index = Integer.parseInt(name.substring(1));
		mValInt[index]-=value;
	}
	
	/***************************************
	 * 値をセットします
	 * @param name 変数名 $xxx
	 ***************************************/
	public static void setValue(String name,String value){
		int index = Integer.parseInt(name.substring(1));
		mValStr[index] = value;
	}
	
	
	/*******************************************************
	 * 指定されたストリームから行番号と変数をセットします
	 * 
	 * ファイルがない場合は何も行いません。
	 *****************************************************/
	public static void setData(InputStream stream){
		try {
			if(stream==null) return;
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			
			//	1行目は行番号
			iLineNum = Integer.parseInt(br.readLine());
			
			//	それ以降は変数の内容
			String line = "";

			
			//	ローカル値データを取得
			for(int i=0; i<200; i++){
				line = br.readLine();
				int x =Integer.parseInt(line);
				mValInt[i] = x;
			}
			
			//	ローカル文字データを取得
			for(int i=0; i<200; i++){
				line = br.readLine();
				mValStr[i] = line;
			}
			
			
			br.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
	}
	
	/**
	 * ストリームに現在の内容を書き出します。
	 * @param stream
	 */
	public static void writeData(OutputStream stream){
		try {
			if(stream==null) return;
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream));
			
			//	1行目は行番号
			bw.write(iLineNum);
			bw.newLine();
			
			//	それ以降は変数の内容
			
			//	ローカル値データを取得
			for(int i=0; i<200; i++){
				bw.write(mValInt[i]);
				bw.newLine();
			}
			
			//	ローカル文字データを取得
			for(int i=0; i<200; i++){
				bw.write(mValStr[i]);
				bw.newLine();
			}
			
			
			bw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
	public static void release(){
		mValInt = null;
		mValStr = null;
	}
	
	
	
}
