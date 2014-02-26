package jp.tohoh.DTCollection;

//	汎用FPSコントロールクラス
public class FPSControl {
	int fps;
	int frame_count;
	long time;
	
	public FPSControl(){
		fps = 0;
		frame_count = 0;
		time = System.currentTimeMillis();
	}
	
	/**
	 * 毎ループに挟んで使用してください
	 * @return true fpsを更新した場合　false 更新しなかった場合
	 */
	public boolean calc(){
		//	FPS測定
      	if(System.currentTimeMillis() >= time+1000){
      		fps = frame_count;
      		frame_count = 0;
      		time = System.currentTimeMillis();
      		return true;
      	}else{
      		frame_count++;
      		return false;
      	}
	}
	
	/**
	 * 現在のFPSを取得します
	 * @return
	 */
	public int getFps(){ return fps; }
	
}
