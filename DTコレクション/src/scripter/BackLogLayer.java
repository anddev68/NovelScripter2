package scripter;

import java.util.ArrayList;

import android.R.color;
import android.graphics.Canvas;
import android.graphics.Paint;

public class BackLogLayer extends Layer{
	
	ArrayList<String> log;	//	ログリスト
	int iTextTop;				//	ログの位置
	int iTextOffset;			//	テキストの位置のオフセット
	boolean bInitLog;			//	ログの位置初期化フラグ
	
	//	文字の位置指定
	int txt_top;
	int txt_left;
	int txt_bottom;
	int txt_right;
	int txt_margin;
	
	public BackLogLayer(){
		super();
		
	}
	
	@Override
	public void draw(Canvas c){
		super.draw(c);
	

		
		
		if(bInitLog){
			//	初期座標
			txt_top = (int) (iHeight*0.2f);
			txt_left = (int) (iWidth * 0.1f);
			txt_bottom = (int) (iHeight * 0.8f);
			txt_right = (int)(iWidth * 0.8f);
			txt_margin = 35;
			iTextTop = txt_bottom - txt_margin * log.size();
			bInitLog = false;
			
		}
		
		//	背景部分
		Paint p = new Paint();
		p.setColor(color.black);
		p.setAlpha(200);
		c.drawRect(iWidth * 0.1f, iHeight*0.1f, iWidth*0.9f, iHeight*0.9f, p);
		
		//	テキスト部分
		txt_top = (int) (iHeight*0.2f);
		txt_left = (int) (iWidth * 0.1f);
		txt_bottom = (int) (iHeight * 0.8f);
		txt_right = (int)(iWidth * 0.8f);
		txt_margin = 35;
		
		//	文字部分を描画
		for(int i=0; i<log.size(); i++){
			int t = iTextTop + txt_margin * i + iTextOffset;
			if(t>=txt_top){
				c.drawText(log.get(i), txt_left, t, Config.textLayerFont());
			}
			if(t > txt_bottom){
				break;
			}
		}
		
		
		
	}
	
	public void setLog(ArrayList<String> str){
		log = new ArrayList<String>();
		bInitLog = true;
		iTextOffset = 0;
		
		
		//	改行は1行開け、最大文字数以上の場合は複数行にまたがって表示するようにする
		int i=0,j;
		while(i<str.size()){
			String[] s = split(str.get(i),Config.BACKLOG_LAYER_MAX_LEN);
			for(j=0;j<s.length; j++)
				log.add(s[j]);
			log.add("");
			i++;
		}	

	}
	
	//	指定文字数で分割する
	//	最後が足りなくなる場合がある
	private String[] split(String str,int num){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<str.length(); i++){
			sb.append(str.charAt(i));
			if(i!=0&&i%num==0)
			sb.append(':');
		}
		return sb.toString().split(":");
	}
	
	/*************
	 * ログを1行前に
	 ***************/
	public void upLog(){
		iTextOffset-=10;
	}
	
	/**************
	 * ログを1行下に
	 * ******************/
	public void downLog(){
		iTextOffset+=10;
		
	}
	
}
