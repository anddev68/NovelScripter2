package scripter;


/***************************************************
 * NScripter互換のグローバル変数・内部変数を扱うクラスです
 * 各変数に名前でアクセスできるように設定してあります。 
 * 
 * 変数にはグローバル領域・ローカルがあります。
 * $0が文字変数、%0が数値変数です。
 * それぞれ200以降はグローバルとなります。
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
 * @see TextPharse
 * 
 ****************************************************/
public class Variable {
	//	グローバルの最大値
	//	メモリの状況で変更してください。
	public static final int GLOBAL_MAX = 400;
	
	int[] mValInt;
	String[] mValStr;
	
	public Variable(){
		mValInt = new int[GLOBAL_MAX];
		mValStr = new String[GLOBAL_MAX];
		
	}

	/***********************************
	 * 変数名$xxxまたは%xxxのデータを取得します。
	 * 
	 * @param name 変数名 $xxx or %xxx
	 * @return Stringに変換した変数の中身（int,string)
	 * @return 取得できない場合は"0"もしくは""
	 ***********************************/
	public String getValue(String name){
		String str = "";
		int index;
		try{
			//	変数番号を取得する
			index = Integer.parseInt(name.substring(1));
		
			if(name.charAt(0)=='$')
				str = ""+mValInt[index];
				
			else if(name.charAt(0)=='%')
				str = mValStr[index];
				
		}catch(Exception e){}
		return str;
	}

	/**************************************
	 * 値をセット・演算をします
	 * @param name 変数名 %xxx
	 * @param int 値
	 **************************************/
	public void setValue(String name,int value){
		int index = Integer.parseInt(name.substring(1));
		mValInt[index] = value;
	}
	
	public void addValue(String name,String value){
		int index = Integer.parseInt(name.substring(1));
		mValStr[index]+=value;
	}
	public void addValue(String name,int value){
		int index = Integer.parseInt(name.substring(1));
		mValInt[index]+=value;
	}
	public void subValue(String name,int value){
		int index = Integer.parseInt(name.substring(1));
		mValInt[index]-=value;
	}
	
	/***************************************
	 * 値をセットします
	 * @param name 変数名 $xxx
	 ***************************************/
	public void setValue(String name,String value){
		int index = Integer.parseInt(name.substring(1));
		mValStr[index] = value;
	}
	
	
	/************************************
	 * グローバル変数を書き出します。
	 *************************************/
	public void writeGrobalValue(){
		
	}
	
	public void loadGrobalValue(){
		
	}
	
	/***************************************
	 * ローカル変数を書き出します。
	 ***************************************/
	public void writeLocalValue(){
		
	}
	
	public void loadLocalValue(){
		
	}
	
	
	
	public void release(){
		
		
		
	}
	
	
	
}
