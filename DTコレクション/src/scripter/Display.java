package scripter;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * 【タイトル】
 * ノベルスクリプター on Android 試作品
 * 
 * 【製作者】
 * Twitter @anddev68
 * 
 * 【使い方】
 * これは自作のノベルスクリプターです。SurfaceViewのメインルーチンにDrawを入れてやれば動きます。
 * 細かい仕様につきましては後日HP、Twitterで公開する予定です。
 * Configで各種設定ができます。今のところは変数名とコメント文でご理解ください。
 * 
 * 
 * 
 * 【免責】
 * まだバグも多いので自己責任でおねがいします。
 * 著作権は@anddev68にあります。個人利用は連絡なしでも構いませんが、商用利用の場合はご連絡ください。
 *

 *
 * 2014/2/24
 * レイヤーの要素番号に依存しないよう、各レイヤーを取得するメソッドを作成しました。
 * 今後はそれをご使用ください。
 * 
 *
 *
 * 2014/2/22
 * レイヤーの番号は0をテキストレイヤーにしています。これはnextText()に対応するためです。
 * これはgetClass()を利用するなりして番号に依存しない形にする予定です。
 * 
 * テキスト番号からセーブ・ロードできるようにしたい
 * その場合、場面切り替えまで遡れるような機能もいる
 * 
 * 画面比率に合わせたレイヤー配置にしなくてはならない
 * これも注意すること
 *
 * 現在のレイヤー割り当ては以下の通りです
 * 0:テキストレイヤー
 * 1:背景レイヤー
 *
 *
 */

public class Display implements TextPharse.CallBack,TextLayer.CallBack{

	//	各レイヤー
	Layer[] mLayer;

	//	テキスト解析
	TextPharse mPharse;
	
	//	画面情報
	int iWidth,iHeight;
	
	//	コンテキスト
	Context mContext;
	
	//	ユーザ処理待ち状態
	boolean bWait;
	
	//	画面全消し更新フラグ（レスポンス向上用）
	boolean bScreenUpdate;
	
	//	自動再生（オートモード）かどうか
	boolean bAutoMode;

	//	スキップモードかどうか
	boolean bSkipMode;
	

	/*******************************
	 * コンストラクタ
	 *******************************/
	public Display(Context context){
		mContext = context;
		iWidth = iHeight = 0;	//	この時点では取得できない
		bWait = false;			//	ユーザ入力受け付ける
		bScreenUpdate = true;	//	全消しを行う
		
		//	各レイヤーを割り振ります
		//	レイヤー継承済みクラスで初期化してください
		mLayer = new Layer[5];
		
		//	テキストレイヤーで初期化
		mLayer[0] = new TextLayer(this);
		
		//	背景レイヤーで初期化
		mLayer[1] = new BGLayer();
		
		//	バックログレイヤで初期化
		mLayer[2] = new BackLogLayer();
		
		//	選択肢レイヤを作成
		mLayer[3] = new OptionLayer();
		
		
		mLayer[4] = new PersonLayer();
		
		//	テキスト解析クラスを初期化します
		try {
			//	エラー無しの場合
			mPharse = new TextPharse(getStream("sample3.sn"),this);
		} catch (IOException e) {
			//	エラーありの場合
			mPharse = new TextPharse();
		}
		
		
		//	テキスト及び画像ファイルの設定等をここで行います
		
		
		
		//	デフォルトでレイヤーを有効化します
		mLayer[0].display = 1;	//	テキストレイヤーを有効化する
		mLayer[1].display = 9;	//	背景レイヤーを有効化する
	}

	//	レイヤー無効化		
	public void DisableLayer(int num){
		mLayer[num].display = 0;
	}

	//	レイヤー有効化
	public void EnableLayer(int num){
		mLayer[num].display = 1;
	}

	//	レイヤーの優先順序を設定
	public void SetPriorLayer(int num,int prior){
		mLayer[num].display = prior;
	}

	/*****************************************************
	 * メイン描画メソッド
	 * レイヤーの優先順序にしたがって描画を行います。
	 * レイヤーの優先順序にはまだ対応していません。ご了承ください。
	 * 
	 * レスポンスの都合上、背景レイヤは毎回再描画を行わないようにしました。
	 * テキストレイヤだけ再描画しています。
	 * 背景レイヤは処理待ち状態と画面更新フラグのときのみ描画を行っています
	 * @param c
	 ******************************************************/
	public void draw(Canvas c){
		if(bScreenUpdate||bWait){
			bScreenUpdate = false;
			c.drawColor(Color.WHITE);
			bScreenUpdate = false;
			c.drawColor(Color.WHITE);

			//	背景表示
			if(getBGLayer().display > 0) getBGLayer().draw(c);

			//	人物レイヤ表示
			if(getPersonLayer().display > 0) getPersonLayer().draw(c);
			
			//	テキストレイヤ表示
			if(getTextLayer().display > 0) getTextLayer().draw(c);
			
			//	選択肢レイヤ表示
			if(getOptionLayer().display > 0) getOptionLayer().draw(c);

			//	バックログレイヤ表示
			if(getBackLogLayer().display > 0) getBackLogLayer().draw(c);

			//	上に小さくオートモード表示
			if(bAutoMode){
				c.drawText("AUTO", 50, 50, Config.whiteFont());
			}
			
		}else{
			if(getTextLayer().display > 0)
				getTextLayer().draw(c);
			
		}
	}
	
	
	/*********************************************
	 * 画面情報が変わったとき、画面サイズが取得できる場合に画面情報の変更を行う
	 * @param width
	 * @param height
	 **********************************************/
	public void setScreenSize(int width,int height){
		iWidth = width;
		iHeight = height;
	}

	/******************************************************************************
	 * Assetを操作するメソッド類です。
	 * ファイル名を指定すれば任意のファイルを取り出すことができます。
	 ****************************************************************************/
	private InputStream getStream(String fileName) throws IOException{
		AssetManager asset = mContext.getAssets();
		return asset.open(fileName);
	}
	
	private Bitmap getBitmap(String fileName){
		try {
			return BitmapFactory.decodeStream(getStream(fileName));
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return null;
		}
	}
	
	/******************************************************************
	 * 各レイヤーの取得
	 * Layer[]から番号を指定しなくても取得できるようにしました。
	 ******************************************************************/
	private TextLayer getTextLayer(){
		for(int i=0;i<mLayer.length;i++){
			if(mLayer[i].getClass() == TextLayer.class)
				return (TextLayer) mLayer[i];
		}
		return null;
	}
	
	private BGLayer getBGLayer(){
		for(int i=0;i<mLayer.length;i++){
			if(mLayer[i].getClass() == BGLayer.class)
				return (BGLayer) mLayer[i];
		}
		return null;
	}
	
	private BackLogLayer getBackLogLayer(){
		for(int i=0;i<mLayer.length;i++){
			if(mLayer[i].getClass() == BackLogLayer.class)
				return (BackLogLayer) mLayer[i];
		}
		return null;
	}
	
	private OptionLayer getOptionLayer(){
		for(int i=0;i<mLayer.length;i++){
			if(mLayer[i].getClass() == OptionLayer.class)
				return (OptionLayer) mLayer[i];
		}
		return null;
	}
	
	private PersonLayer getPersonLayer(){
		for(int i=0;i<mLayer.length;i++){
			if(mLayer[i].getClass() == PersonLayer.class)
				return (PersonLayer) mLayer[i];
		}
		return null;
	}
	
	
	/******************************************************************
	 * 各種ユーザー入力に対する処理
	 * Androidの場合、SurfaceViewで呼んでください。
	 * GestureEventとか使用するといいかも。
	 * 
	 * disableTextLayer:テキストレイヤーを消す
	 * enableTextLayer:テキストレイヤーをつける
	 * toggleTextLayer:テキストレイヤーのON/OFF切り替え
	 * nextText:テキストをすすめる処理
	 * 
	 * toggleBackLogLayer:バックログレイヤーの切り替え
	 * enable/disable on/off
	 * upBackLoglayer:ログを上に動かす
	 * downBackLogLayer:ログを下に動かす
	 * 
	 * clickOptionLayer: 選択肢を選んだ場合
	 * 
	 * update: 強制的に画面の更新を行います（Activityのrestart時等に試用してください）
	 * 
	 *******************************************************************/
	public void disableTextLayer(){
		DisableLayer(0);
	}
	
	public void enableTextLayer(){
		EnableLayer(0);
	}
	
	public void toggleTextLayer(){
		if(mLayer[0].display > 0) disableTextLayer();
		else enableTextLayer();
	}
	
	public void nextText(){
		if(!bWait){
			//	mPharse.next();
			mPharse.nextNS();
		}
	}
	
	public void toggleBackLogLayer(){
		BackLogLayer layer = getBackLogLayer();
		if(layer.display > 0) layer.display = 0;	//	最前面
		else layer.display = 9;	//	最前面
	}
	
	public void enableBacklogLayer(){
		getBackLogLayer().setLog(mPharse.getLog());	//	ログ取得
		getBackLogLayer().display = 9;
		bWait = true;		//	待機状態
	}
	
	public void disableBacklogLayer(){
		getBackLogLayer().display = 0;
		bWait = false;	//	待機終わり
		bScreenUpdate = true;
	}
	
	public void upBackLogLayer(){
		getBackLogLayer().upLog();
	}
	
	public void downBackLogLayer(){
		getBackLogLayer().downLog();
	}
	
	public void clickOptionLayer(int x,int y){
		if(getOptionLayer().display > 0) getOptionLayer().onClick(x, y);
	}
	

	public void enableAutoMode(){
		bAutoMode = true;
		bScreenUpdate = true;
	}

	public void disableAutoMode(){
		bAutoMode = false;
		bScreenUpdate = true;
	}
	
	public void update(){
		bWait = false;
		bScreenUpdate = true;
	}
	
	/****************************************************************
	 * TextPharseから受け取ったコールバックメソッドの実装
	 * テキストを解析した時に、それぞれ行う処理を記述します。
	 * 
	 * changeBackGround:指定されたファイル名の背景画像に変更する
	 * changeBGM:指定されたファイル名のBGMに変更する
	 ******************************************************************/
	@Override
	public void changeBackGround(String fileName) {
		getBGLayer().setBitmap(getBitmap(fileName));
		bScreenUpdate = true;
	}

	@Override
	public void changeBGM(String fileName) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	@Override
	public void changeImgTextFrame(String fileName){
		TextLayer textLayer = (TextLayer)mLayer[0];
		textLayer.setBitmap(getBitmap(fileName));
	}

	@Override
	public void changeText(String str){
		getTextLayer().setText(str);
	}

	@Override
	public void changeBackGroundFade(String fileName) {
		
	}

	@Override
	public void changeBackGroundBlackOut(String fileName) {
		bWait = true;	//	終わるまで処理を待つ
		bScreenUpdate = true;	//	画面更新
		
		getBGLayer().startBlackOut(getBitmap(fileName), new Layer.BlackOutCallBack() {
			
			@Override
			public void onFinish() {
				bWait = false;			//	処理街状態を抜ける
				bScreenUpdate = true;	//	画面更新
				mPharse.nextForce();		//	1行すすめる
			}
			
		});
	}

	@Override
	public void showOption(ArrayList<String> str) {
		
		//	選択肢レイヤーを有効にし、コールバックと選択肢を設定する
		getOptionLayer().display = 9;
		getOptionLayer().setOption(str, new OptionLayer.CallBack(){

			@Override
			public void onClick(String label) {
				getOptionLayer().display = 0;
				bWait = false;
				bScreenUpdate = true;
				System.out.println(label+"へ飛びます。");
				mPharse.jumpLabel(label);
			}
		
		});
		
		bWait = true;	//	終わるまで処理を待つ
		bScreenUpdate = true;	//	画面更新
		
		
		
	}
	
	@Override
	public void enablePersonImg(ArrayList<String> str) {
		//	人物レイヤーに画像をセットします
		//	中央限定です。
		getPersonLayer().display = 1;
		getPersonLayer().setBitmap(getBitmap(str.get(2)));

	}
	
	
	
	/*******************************************
	 * テキストレイヤーが1行表示しきった時の処理
	 * @see TextLayer
	 ******************************************/
	@Override
	public void onFinish() {
		//	オートモードなら勝手にすすめる
		if(bAutoMode) nextText();
	}




	
	
}
