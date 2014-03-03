package scripter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*****************************************************
 * テキスト解析クラス
 * テキストファイルを解析するときに使います
 * 
 * Androidに依存しない形で作っています。
 * 
 * 2014/2/24 update
 ************************************************************/
public class TextPharse {

	/*******************************************************
	 * コールバックインターフェース
	 * テキスト解析時、コマンドを実行します。
	 * コマンドについてはリファレンスを参照ください。
	 * 
	 * #BG ファイル名　：背景画像の変更
	 * #TL ファイル名　：テキストフレームの変更
	 * 
	 * Displayに持たせてください。
	 *
	 ***********************************************************/
	public interface CallBack{
		public void changeBackGround(String fileName);	//	背景画像の変更
		public void changeBackGroundFade(String fileName);	//	フェード有りで背景の変更
		public void changeBackGroundBlackOut(String fileName);	//	ブラックアウトで背景変更
		public void changeImgTextFrame(String fileName);	//	テキスト枠の変更
		public void changeBGM(String fileName);			//	BGMの変更
		public void changeText(String str);				//	テキストレイヤーに表示するテキストの変更
		public void showOption(ArrayList<String> str);	//	選択肢を開く
		public void enablePersonImg(ArrayList<String> str);	//	人物レイヤに表示
	}
	
	ArrayList<String> mLine;	//データ
	CallBack mCallBack;	//	コールバック

	
	int iLogPtr;	//ログの位置	
	
	/*************************************************************
	 * デバッグコンストラクタ
	 * テキストデータのストリームなしで作成する。
	 * デバッグ用です。下のテキストデータ用のストリームで初期化してください。
	 ***************************************************************/
	public TextPharse(){
		Variable.iLineNum = 0;
		mLine = new ArrayList<String>();		

		mLine.add("テキストデータがありません");
		mLine.add("エラーを確認しました");
		
	}		
	
	/*************************************************************
	 * コンストラクタ
	 * テキストデータのストリームを元に作成します。
	 * 通常はこちらのコンストラクタを使用してください。
	 * 
	 * @param is テキストデータのストリーム
	 * @param cb イベント処理時のコールバック関数
	 ***************************************************************/
	public TextPharse(InputStream is,CallBack cb){
		Variable.iLineNum = 0;
		mLine = new ArrayList<String>();
		mCallBack = cb;
		changeFile(is);
		
	}
	
	/*****************************************************************
	 * シナリオファイルの変更
	 * シナリオファイルを変更します
	 * 
	 * @param is テキストデータのストリーム
	 *****************************************************************/
	public void changeFile(InputStream is){
		mLine.clear();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while((line = br.readLine()) != null){
				mLine.add(line);
			}
			br.close();
			checkHeader();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*****************************************************************
	 * シナリオファイル変更時、インスタンス生成時にヘッダー情報・タグの確認を行います。
	 * 各情報があった場合はコールバックで返します。
	 * 
	 * 今のところ得に何も行いません。
	 * 1行目から本文まで読み進め、本文1行目をレイヤーに反映させます。
	 *****************************************************************/
	private void checkHeader(){
		nextNS();
	}
	
	/*******************************************
	 * シナリオファイルの行を進めます。
	 * コマンド行は実行し、次の行へ。
	 * 本文であればテキストレイヤーに渡します。
	 * 空行、コメント行は無視して次の行へ。
	 * 
	 ******************************************/
	public void nextNS(){
		boolean loop = true;
		ArrayList<String> strs = null;	//	コマンド解析用
	
		while(loop){
			if(isEmptyLow()) Variable.iLineNum++;	//	空行無視
			else if(isComment()) Variable.iLineNum++;		//	コメント文無視
			else if(isLabel()) Variable.iLineNum++;			//	ラベル無視
			else if((strs=pharseCommand())!=null){	//	各コマンドの処理
				if(strs.get(0).equals("setwindow")) setTextWindow(strs);
				else if(strs.get(0).equals("bg")){	//	背景
					setBackGround(strs); 
					if(!strs.get(2).equals("1")) return;	//	背景切り替えの処理待ちがいる場合は抜ける
				}else if(strs.get(0).equals("select")){	//	選択肢を開く処理
					mCallBack.showOption(strs);				//	処理待ち
					return;
				}else if(strs.get(0).equals("goto")){
					jumpLabel(strs.get(1));				//	指定のラベルに飛ぶ
				}else if(strs.get(0).equals("mov")){	//	変数を代入する場合
					setValue(strs);						//	変数のセット
				}else if(strs.get(0).equals("add")){	//	変数に値を追加する
					addValue(strs);
				}else if(strs.get(0).equals("ld")){	//	人物レイヤーにセット
					mCallBack.enablePersonImg(strs);
				}
				Variable.iLineNum++;
			}else{		//	本文の処理
				//	改行待ち機能は今のところ実装していません
				mCallBack.changeText(replaceVariable(mLine.get(Variable.iLineNum)));	//	変数は値を代入して読み込む
				Variable.iLineNum++;
				
				loop = false;
			}	//	else end
		}	
					
	}
	
	/******************************************
	 * 強制的に一行進めます
	 * 背景の切り替え処理が終わった際に利用します。
	 ******************************************/
	public void nextForce(){
		Variable.iLineNum++;
	}
	
	/***********************************************************
	 * 指定のラベルに飛びます。選択肢が選ばれた時に使用します。
	 * 現在の行から後ろに検索します。前には検索しません。
	 * （2/27訂正）
	 * 前から検索します。不都合の場合は訂正してください。
	 * 存在しない場合には警告するだけで何も行いません。
	 **********************************************************/
	public void jumpLabel(String labelName){
		int start = 0;	//	iLineNumに変更すれば現在の番号から開始します
		for(int i=start; i<mLine.size(); i++){
			if(mLine.get(i).equals(labelName)){
				Variable.iLineNum = i;
				return;
			}
		}
		System.out.println("["+labelName+"]が見つかりませんでした。");
	}
	
	/*****************************************************************
	 * 各コマンド判定を行います
	 * NScripterに準拠しています
	 ******************************************************************/
	private boolean isComment(){ if(mLine.get(Variable.iLineNum).charAt(0)==';') return true; return false;}
	private boolean isEmptyLow(){ if(mLine.get(Variable.iLineNum).equals("")) return true; return false;}	
	private boolean isLabel(){if(mLine.get(Variable.iLineNum).charAt(0)=='*') return true; return false;}
	
	
	/*********************************************************************
	 * 現在の行をコマンドであると仮定し、コマンド名と引数を取得します。
	 * @return array コマンド名と1以上の引数を配列として返します。
	 * @return null コマンドとして解析できなかった場合に帰ります
	 *********************************************************************/
	private ArrayList<String> pharseCommand(){
		try{
			//	現在の行
			String cur = mLine.get(Variable.iLineNum);
			//	空白の位置
			int ptr = cur.indexOf(' ');
			//	空白までがコマンド名
			String name = cur.substring(0, ptr);
			//	引数を配列で取得
			String[] args = cur.substring(ptr+1).split(",");
			//	リストで返す
			ArrayList<String> str = new ArrayList<String>();
			str.add(name);
			for(int i=0; i<args.length; i++)
				str.add(args[i]);
			return str;
		}catch(Exception e){
			return null;
		}
	}
	
	/**********************************************************************
	 * テキストウインドウ（テキストレイヤ）を設定します
	 * 引数はファイル名のみにしてあります。NScripterとは違うので注意
	 * NScripter:setwindow
	 **********************************************************************/
	private void setTextWindow(ArrayList<String> strs){
		mCallBack.changeImgTextFrame(strs.get(1));
	}
	
	/***********************************************************************
	 * コマンド解析の結果、背景を設定する
	 **********************************************************************/
	private void setBackGround(ArrayList<String> strs){
		switch( Integer.parseInt(strs.get(2)) ){
		case 1: mCallBack.changeBackGround(strs.get(1)); break;
		case 17: 
			mCallBack.changeBackGroundBlackOut(strs.get(1)); 
			break;
		}
	}

	/***********************************************************************
	 * コマンド解析の結果、変数の代入を行います。
	 * 文字列かどうかは自動判定にしてあります。
	 * 将来的にはダブルクオートに対応したいと思います。
	 **********************************************************************/
	private void setValue(ArrayList<String> strs){
		if(strs.get(1).charAt(0)=='%'){
			int num = Integer.parseInt(strs.get(2));
			Variable.setValue(strs.get(1),  num);
		
		}else
			Variable.setValue(strs.get(1), strs.get(2) );	
	}
	
	/**********************************************************************
	 * コマンド解析の結果、変数に値を足します。
	 **********************************************************************/
	private void addValue(ArrayList<String> strs){
		if(strs.get(1).charAt(0)=='%')
			Variable.addValue(strs.get(1), Integer.parseInt(strs.get(2)) );
		else
			Variable.addValue(strs.get(1), strs.get(2) );	
	}
	
	
	/**********************************************************************
	 * 文章解析を行い、変数を置き換えたものを返します。
	 * 今のところは%xxx、整数のみ対応しています。
	 **********************************************************************/
	private String replaceVariable(String str){
		//	作業用バッファを用意する
		String buffer = new String(str);
		
		//	正規表現で整数変数を示す%xxx部分を抜き出します。
		String regex = "%\\d+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		
		//	%xxxが抜き出せたら、それぞれに値を代入していきます。
		while( m.find() ){
			//	マッチした文字列（例：%0）を取得
			String xxx = m.group();
			//	値を取り出す
			String s = Variable.getValue(xxx);
			//	置き換える
			buffer = buffer.replaceFirst(xxx, s);
		}
		return buffer;
	}
	
	
	
	/***************************************************************************
	 * ログ表示用にログデータを取得します
	 * 
	 * 
	 * @param max 最大行数（最終行-最大行数が最初の行）
	 * @param offset 現在の行数からいくつ戻るか（この行を最終行とする）
	 ***************************************************************************/
	public ArrayList<String> getLog(){
		ArrayList<String> buffer = new ArrayList<String>();
		//	とりあえずそのまま現在のテキストまで返します
		for(int i=0; i<Variable.iLineNum; i++){
			buffer.add(mLine.get(i));
		}
		return buffer;
	}
	
	
}
