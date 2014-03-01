package scripter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/*****************************
 * データ管理を行います。
 * 
 * 【セーブデータの形式】
 * ・セーブした行番号\n
 * ・%0の内容\n
 * ・%1の内容・・・
 * ・
 * 
 ****************************/
public class Recorder {
	
	/*************************************************
	 * 指定されたストリーム（セーブデータ.data）を解析し、
	 * 現在の変数、行番号などにセットします。
	 *************************************************/
	public static void read(InputStream stream){
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));

	}
	
	/************************************************
	 * 指定されたストリームに対して指定のフォーマットで
	 * 現在の行番号などをセットします
	 ************************************************/
	public static void write(InputStream stream,int iLine,Variable var){
		
		
	}

}
