package scripter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/*****************************
 * データ管理を行います。
 * 
 * 【セーブデータの形式】
 * ・セーブした行番号\n
 * ・%0の内容\n
 * ・%1の内容・・・
 * ・$0の内容・・・
 * 
 *  $,%はローカルの数だけ継続
 *  
 ****************************/
public class Recorder {
	


	
	/************************************************
	 * 指定されたストリームに対して指定のフォーマットで
	 * 現在の行番号などをセットします
	 ************************************************/
	public static void write(OutputStream stream,RecordData data){
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream));
			
			//	行番号を出力
			bw.write(""+data.iLineNum);
			bw.newLine();
			
			//	ローカル値変数データを出力
			for(int i=0; i<200; i++){
				bw.write(""+data.mVar.mValInt[i]);
				bw.newLine();
			}
			
			//	ローカル文字変数データの出力
			for(int i=0; i<200; i++){
				bw.write(data.mVar.mValStr[i]);
				bw.newLine();
			}
			
			
			bw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
	}
	
	
	/***************************************************
	 * セーブデータ/ロードデータ一覧画面に表示するデータ
	 * 
	 * ListView用にAdapterで吐き出します
	 *****************************************************/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
