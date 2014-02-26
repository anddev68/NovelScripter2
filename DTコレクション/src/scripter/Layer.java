package scripter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/********************************************************
 * レイヤーのスーパークラスです。
 * このクラス自体のインスタンス生成はできません。派生してお使いください。
 * 
 * 2014/2/24 update
 * フェード機能とBitmap2枚をもたせました。
 * 人物レイヤー、背景レイヤー等で使用してください。
 *******************************************************/
public class Layer {
	
	/***************************************
	 * フェードイン終了時に行う処理を指定
	 *****************************************/
	public interface FadeInCallBack{
		public void onFinish();
	}
	
	/***************************************
	 * ブラックアウト・ブライトイン終了時に行う処理を指定
	 *****************************************/
	public interface BlackOutCallBack{
		public void onFinish();
	}

	//	画面切り替え用変数
	protected int fade_count;
	protected int blackout_count;
	protected final int FADE_MAX_COUNT = 20;		//	このフレーム数で書き換える
	protected final int BLACKOUT_MAX_COUNT = 20;	//	このフレーム数で書き換える
	protected FadeInCallBack mFadeInCallBack;
	protected BlackOutCallBack mBOutCallBack;
	
	//	画像
	protected Bitmap bitmap;		//	メインの画像
	protected Bitmap bitmap2;	//	画面切り替え時に使用するバッファ
	
	
	public int display;		//	表示フラグ（優先順序）
	public String name;		//	レイヤー名（必須ではない）
	public int alpha;			//	透過用
	
	protected int iWidth,iHeight;		//	描画先の大きさ
	
	protected Layer(){	//	コンストラクタを不可視にしておきます。
		iWidth = iHeight = 0;
		fade_count = FADE_MAX_COUNT+1;
		blackout_count = BLACKOUT_MAX_COUNT+1;
	}	
	
	/*************************************************************
	 *  描画関数
	 *  フェード機能と画面情報を使用する場合はオーバーライドの時にsuper()が必要です。
	 * ***********************************************************/
	public void draw(Canvas c){
		//	画面情報のセット
		iWidth = c.getWidth();
		iHeight = c.getHeight();	
		
		//	フェードアウトの更新
		if(fade_count < FADE_MAX_COUNT){
			fade_count++;
		}else if(fade_count==FADE_MAX_COUNT){	//	フェードアウト終わり
			if( mFadeInCallBack != null ) mFadeInCallBack.onFinish();	//	コールバックの実行
			fade_count++;
			bitmap = bitmap2;
			bitmap2 = null;
			blackout_count++;
		}
		
		//	ブラックアウトの更新
		if(blackout_count < BLACKOUT_MAX_COUNT){
			blackout_count++;
		}else if(blackout_count == BLACKOUT_MAX_COUNT){
			bitmap = bitmap2;
			bitmap2 = null;
			blackout_count++;
			if( mBOutCallBack != null ) mBOutCallBack.onFinish();	//	コールバック実行

		}
	}
	
	/******************************************************************
	 * 指定した大きさに変更してbitmapを描画する関数
	 * @param c
	 * @param bitmap
	 * @param left
	 * @param top
	 * @param width
	 * @param height
	 *******************************************************************/
	protected void drawBitmap(Canvas c,Bitmap bitmap,int left,int top,int width,int height){
		drawBitmap(c,bitmap,left,top,width,height,null);
	}
	protected void drawBitmap(Canvas c,Bitmap bitmap,int left,int top,int width,int height,Paint p){
		if(bitmap!=null){
			if( bitmap.getWidth()!=width || bitmap.getHeight() !=height )
				bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
			c.drawBitmap(bitmap, left, top, p);
		}
		
	}
	
	
	/*****************************************
	 * 画面切り替えの開始（フェードイン）
	 *****************************************/
	public void startFadeIn(Bitmap b,FadeInCallBack cb){
		fade_count = 0;	//	カウントを0にしてスタート
		bitmap2 = b;		//	書き換え用にセット
		this.mFadeInCallBack = cb;
	}
	
	/*******************************************
	 *	画面切り替えの開始（ブラックアウト）
	 *******************************************/
	public void startBlackOut(Bitmap b,BlackOutCallBack cb){
		blackout_count = 0;
		bitmap2 = b;
		this.mBOutCallBack = cb;
	}
	
	/*********************************************
	 * 画面切り替え中かどうか調べる
	 *********************************************/
	protected boolean isFade(){ if(fade_count<=FADE_MAX_COUNT) return true; return false; }
	protected boolean isBOut(){ if(blackout_count<=BLACKOUT_MAX_COUNT) return true; return false; }
	
	
	
}
