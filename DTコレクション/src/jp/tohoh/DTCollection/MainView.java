package jp.tohoh.DTCollection;




import scripter.RecordData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class MainView extends SurfaceView implements Runnable,Callback,
	GestureDetector.OnGestureListener,OnDoubleTapListener{
	
	//	ノベルスクリプーた
	scripter.Display display;
	
	//	ジェスチャーデコーダ
	GestureDetector gesture;
	
	//	前回の位置
	float oldX1,oldY1,oldX2,oldY2;
	
	//	バッファ
	Bitmap buffer;
	
	SurfaceHolder surfaceHolder;
	boolean loop;
	Thread thread = null;
	
	public MainView(Context context) {
		super(context);
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
	
		display = new scripter.Display(context);
		
		gesture = new GestureDetector(context, this);
		
		
		
	}

	@Override
	public void run() {
		while(loop){
			
			//	ここで描画処理
			Canvas canvas = surfaceHolder.lockCanvas();
			if(canvas==null) continue;
			
			//	ダブルバッファリングで書いてみる
			if( buffer!=null ){
				Canvas c2 = new Canvas(buffer);
				display.draw(c2);	//	スクリプターを更新する
				canvas.drawBitmap(buffer, 0, 0, null);
			}
			
			surfaceHolder.unlockCanvasAndPost(canvas);
			
			//	ここでその他の処理
            
		}
	}
	
	/***********************************
	 * 読み書きするデータを取得します
	 * @return 
	 **********************************/
	public RecordData getData(){
		return display.getData();
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		buffer = 
				Bitmap.createBitmap(arg2, arg3, Bitmap.Config.ARGB_8888);
		display.setScreenSize(arg2, arg3);
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
	
		thread = new Thread(this);
		thread.start();
		loop = true;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		loop = false;
		thread = null;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent me){
		if(me.getPointerCount() > 1){	//	マルチタッチの時
			int id1 = me.getPointerId( 0 );
			int id2 = me.getPointerId( 1 );
			int index1 = me.findPointerIndex(id1);
			int index2 = me.findPointerIndex(id2);
			float y1 = me.getY(index1);
			float y2 = me.getY(index2);
			
			switch(me.getAction()){
			case MotionEvent.ACTION_MOVE:
				if( y1+40>oldY1&& y2+40>oldY2){
					//	2本とも下へ
					System.out.println("ダブル下スライド！");
					display.disableBacklogLayer();	//	バックログOFF
					
				}else if( y1-40 < oldY1 && y2-40 < oldY2){
					//	2本とも上へ
					System.out.println("ダブル上スライド！");
					display.enableBacklogLayer();	//	バックログをON
				}
				
				break;
			}
			oldY1 = y1;
			oldY2 = y2;
			return true;
		}
		gesture.onTouchEvent(me);
		return true;
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		display.clickOptionLayer((int)arg0.getX(), (int)arg0.getY());
		System.out.println("onDown");
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		System.out.println("onLongPress");
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		display.toggleTextLayer();
		System.out.println("onLongPress");
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		int rangeY = (int) (arg0.getRawY() - arg1.getRawY());
		if(rangeY > 30){
			//	下方向にスライド
			display.upBackLogLayer();
		}else if(rangeY < -30){
			//	上方向にスライド
			display.downBackLogLayer();
		}
		
		System.out.println("onScroll");
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		System.out.println("onShowPress");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		System.out.println("SingleTap");
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		//	テキストを読み進める
		display.nextText();
		System.out.println("SingleTap");
		return false;
	
	
	}
	
	




}
