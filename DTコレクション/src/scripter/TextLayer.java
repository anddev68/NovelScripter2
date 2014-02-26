package scripter;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/****************************************************************
 * テキストが表示されるテキストレイヤー
 * 
 * 位置はいまのところ固定
 * テキストのフォント、サイズ、色のみConfigにもたせてます
 ****************************************************************/
public class TextLayer extends Layer{
	
	//	背景画像
	Bitmap bitmap;
	
	//	表示するテキスト
	String text;

	//	テキストを滑らかに進める処理
	int text_ptr;		//	表示している文字数
	Timer text_timer;	//	テキストのタイマーです
	
	public TextLayer(){
		super();
		setText("テキストレイヤーに文字が渡されていません");
	}
	
	public TextLayer(String txt){
		super();
		setText(txt);
	}
	
	
	@Override
	public void draw(Canvas c){
		int width = c.getWidth();
		int height = c.getHeight();
		
		//	テキスト枠の位置・大きさを指定します
		int frame_top = (int)(height * 0.6);
		int frame_left = (int)(width * 0.05);
		int frame_width = (int)(width * 0.9);
		int frame_height = (int)(height * 0.35);
		
		//	テキストの位置
		int txt_top = (int)(height * 0.72);
		int txt_left = (int)(width * 0.1);
		
		//	テキストの行間隔
		int txt_margin = 35;
		
		//	枠の大きさを変更し、描画する
		//	分岐は高速化のための処理
		if(bitmap!=null){
			if(bitmap.getWidth()!=frame_width || bitmap.getHeight()!=frame_height)
				bitmap = Bitmap.createScaledBitmap(bitmap, frame_width, frame_height, true);
			c.drawBitmap(bitmap, frame_left, frame_top,Config.textLayerFrame());
		}
		
		int low,start,end,left,top;
		low = text_ptr / Config.TEXT_LAYER_MAX_LEN;	//	現在の行を取得
		
		//	恐らくテキスト送りとdraw()の同期処理ができていないのが原因かと思われる
		//	IndexOutエラーが起きるため、エラー時は出力をしない
		try{
			//	円滑処理後の行の表示
			for(int i=0; i<low; i++){
				top = txt_top + txt_margin * i;
				left = txt_left;
				start = i * Config.TEXT_LAYER_MAX_LEN;
				end = (i+1)*Config.TEXT_LAYER_MAX_LEN;
				String str = text.substring(start,end);
				c.drawText(str,left,top,Config.textLayerFont());
			}
		
			//	円滑処理中の行の表示
			start = low * Config.TEXT_LAYER_MAX_LEN;	//	開始位置を決定
			end = start + (text_ptr % Config.TEXT_LAYER_MAX_LEN);	//	終了位置を求める
			left = txt_left;
			top = txt_top + txt_margin * low;
			String str = text.substring(start,end);
			c.drawText(str,left,top,Config.textLayerFont());
			
		}catch(Exception e){
			
		}
	}
	
	
	
	//	テキスト円滑化タイマーの初期化
	private void initTextTimer(){
		text_timer = new Timer();
		text_timer.schedule(new TimerTask(){
			@Override
			public void run() {
				nextText();
			}
		}, 0, Config.TEXT_LAYER_SPEED);
	}
	
	//	テキストを1文字進める
	private void nextText(){
		String str = text;
		if( text_ptr < str.length()){

			text_ptr++;
		}else{
			text_timer.cancel();
		}
		
	}

	//	テキストを置き換える
	public void setText(String str){
		text_ptr = 1;
		text = str;
		initTextTimer();
	}
	
	public void setBitmap(Bitmap b){
		bitmap = b;
	}
	
}
