import java.io.*;
import java.util.*;

public class Jikkou01_StandardWork{
	private static RepeatModifier rm;
	private static ReplaceModifier tm;
	private static TargetLoader tl;

	public static void main(String args[]){
		//準備
		String srcPath="input/テストソース.xls";
		LinkedList<String> idList=new LinkedList<String>();
		idList.addAll(Arrays.asList("試行01","試行02","試行03","試行04","試行05","試行06","試行07","試行08"));
		String repeatSheetName="繰り返し";
		String replaceSheetName="テストパラメータ";
		String targetSheetName="ターゲット";
		LinkedList<String> specifyList=new LinkedList<String>();
		specifyList.addAll(Arrays.asList("##ID##"));
		boolean blanSkip=false;
		
		//RepeatModifier生成
		rm=new RepeatModifier();
		loadRepeatParameter(srcPath,repeatSheetName,replaceSheetName);
		
		//ReplaceModifier生成
		tm=new ReplaceModifier();
		loadReplaceParameter(srcPath,replaceSheetName);
		
		//RepeatModifier用TargetLoader生成
		tl=new TargetLoader(rm,srcPath);
		
		for(String curId:idList){
			System.out.println("ID:"+curId);
			
			//繰り返し処理
			String columnName=tm.getValue(curId+Modifier.DIVIDE_STR+"列名");
			//System.out.println(columnName);
			tl.loadExcel(targetSheetName,columnName,blanSkip);
			rm.partialRepeat();
			LinkedList<String> targetList=rm.getTargetList();
			tm.setTargetList(targetList);
			
			//特定置換処理
			if(specifyList.size()>0){
				for(String curStr:specifyList){
					tm.put(curStr,curId);	//IDに変換
				}
				tm.specifyReplace(specifyList);
			}
			
			//置換処理
			tm.diamondReplace();
			targetList=tm.getTargetList();
			
			//ファイル出力
			String dstFileName=tm.getValue(curId+Modifier.DIVIDE_STR+"出力ファイル名");
			writeText(targetList,dstFileName);
			
//			for(String curStr:targetList){
//				System.out.println(curStr);
//			}
			//break;
		}
		
	}
	
	public static void loadRepeatParameter(String srcPath,String repeatSheetName,String replaceSheetName){
		RepeatLoader rl=new RepeatLoader(rm,srcPath);
		rl.loadExcel(repeatSheetName);
		NodeLoader nl=new NodeLoader(rm);
		nl.loadItemExcel(srcPath,replaceSheetName);

		//System.out.println(rm.getTreeStr());
	}
	
	public static void loadReplaceParameter(String srcPath,String replaceSheetName){
		NodeLoader nl=new NodeLoader(tm);
		nl.loadItemExcel(srcPath,replaceSheetName);
	}
	
	public static void writeText(LinkedList<String> targetList,String dstFileName){
		try {
			PrintWriter wr=new PrintWriter(new FileWriter(dstFileName));
			
			for(String curStr:targetList){
				//System.out.println(curStr);
				wr.println(curStr);
			}
			wr.close();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
