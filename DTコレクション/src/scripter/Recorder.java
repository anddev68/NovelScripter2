package scripter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	

	/*************************************************
	 * 指定されたストリーム（セーブデータ.data）を解析し、
	 * 現在の変数、行番号などをセットし、返します。
	 *************************************************/
	public static RecordData read(InputStream stream){
		try {
			RecordData data = new RecordData();
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			
			//	1行目は行番号
			data.iLineNum = Integer.parseInt(br.readLine());
			
			//	それ以降は変数の内容
			String line = "";

			
			//	ローカル値データを取得
			for(int i=0; i<200; i++){
				line = br.readLine();
				int x =Integer.parseInt(line);
				data.mVar.mValInt[i] = x;
			}
			
			//	ローカル文字データを取得
			for(int i=0; i<200; i++){
				line = br.readLine();
				data.mVar.mValStr[i] = line;
			}
			
			
			br.close();
			return data;
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return null;
		}
	}
	
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
	 ***************************************************/
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
