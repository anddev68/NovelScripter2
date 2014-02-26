package scripter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * 背景レイヤー
 * @author hide
 *
 */
public class BGLayer extends Layer{
	
	public interface CallBack{
		public void fadein();	//フェードインが終わったときの処理
	}
	
	int fade_count = 21;	//	フェードイン用カウント
	
	CallBack mCallBack;	//	コールバック
	
	public BGLayer(){
		super();
	}
	
	/**
	 * デフォルトの背景を指定して読み込みます。
	 * 
	 * @param b
	 */
	public BGLayer(Bitmap b){
		super();
		
	}
	
	/***************************************
	 * 描画関数
	 * フェードインにも対応しました
	 ****************************************/
	@Override
	public void draw(Canvas c){
		super.draw(c);
		
		if(isBOut()){		//	ブラックアウト中の処理
			int a = (int)((BLACKOUT_MAX_COUNT-blackout_count)*255/BLACKOUT_MAX_COUNT);
			Paint p = new Paint();
			c.drawColor(Color.BLACK);
			
		}else{				//	通常描画
			drawBitmap(c,bitmap,0,0,iWidth,iHeight);
		}
		
		/*
		if(fade_count < 20){
			fade_count++;
			drawBitmap(c,bitmap,0,0,iWidth,iHeight);
			drawBitmap(c,bitmap2,(fade_count-20)*iWidth/20,0,iWidth,iHeight);
			
		}else if(fade_count == 20){
			fade_count++;
			if(mCallBack !=null) mCallBack.fadein();	//	フェードインコールバック実行
			bitmap = bitmap2;		//	切り替える
			bitmap2 = null;
			
		}else{	//	通常の背景を描画
			drawBitmap(c,bitmap,0,0,iWidth,iHeight);
		}
		*/
		
		

		
	}
	

	
	/******************************************************
	 * 画像切り替えでフェードインする
	 ********************************************************/
	public void setBitmapFadeIn(Bitmap b,CallBack cb){
		//	フェードイン実行
		fade_count = 0;
		
		//	終わったときに実行
		mCallBack = cb;
		
		bitmap2 = b;
	}
	
	
	public void setBitmap(Bitmap b){
		bitmap = b;
	}
	
	

}
