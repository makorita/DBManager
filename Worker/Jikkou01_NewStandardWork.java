import java.io.*;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.*;

public class Jikkou01_NewStandardWork{
	static String idStr="##ID##";
	public static void main(String args[]) throws IOException{
		//準備
//		String id="testRT01";
//		String srcPath="input/新テストソース.xls";
//		String replaceSheetName="テストパラメータ";
//		String targetSheetName="ターゲット";
//		String targetColumn="試行01";
		String id=args[0];
		String srcPath=args[1];
		String replaceSheetName=args[2];
		String targetSheetName=args[3];
		String targetColumn=args[4];
		//System.out.println(id+","+srcPath+","+replaceSheetName+","+targetSheetName+","+targetColumn);

		//ターゲットリストの読み込み
		LinkedList<String> targetList=new LinkedList<String>();
		{
			Workbook wb = WorkbookFactory.create(new FileInputStream(srcPath));
			Sheet sheet=wb.getSheet(targetSheetName);
			//ターゲット列検索
			int targetColumnNum=-1;
			Row koumokuRow=sheet.getRow(0);
			for(int cellIndex=0;cellIndex<koumokuRow.getLastCellNum();cellIndex++){
				Cell cell=koumokuRow.getCell(cellIndex);
				if(cell==null)continue;
				//if(cell.getCellType()==CellType.STRING)System.out.println(cell.getStringCellValue()+","+targetColumn);
				if(cell.getCellType()==CellType.STRING && cell.getStringCellValue().equals(targetColumn)){
					//System.out.println(cellIndex);
					targetColumnNum=cellIndex;
					break;
				}
			}
			
			//ターゲットリスト読み込み
			for(int rowIndex=1;rowIndex<=sheet.getLastRowNum();rowIndex++){	//スタートは1行目
				Row row=sheet.getRow(rowIndex);
				if(row==null)continue;
				Cell cell=row.getCell(targetColumnNum);
				if(cell==null)continue;
				if(cell.getCellType()==CellType.BLANK)targetList.add("");
				if(cell.getStringCellValue().matches(";;.*"))continue;
				
				//System.out.println(cell.getStringCellValue());
				targetList.add(cell.getStringCellValue());
			}
		}
		
		ReplaceModifier tm=null;
		//パラメータ読み込み
		//targetList⇒tm
		{
			tm=new ReplaceModifier();
			tm.setTargetList(targetList);
			NodeLoader nl=new NodeLoader(tm);
			nl.loadTabExcel(srcPath,replaceSheetName);
			//System.out.println(tm.getTreeStr());
		}
		
		//ID置換
		{
			LinkedList<String> specifyList=new LinkedList<String>();
			specifyList.add(idStr);
			tm.put(idStr,id);
			tm.specifyReplace(specifyList);
		}
		
		//繰り返し置換
		{
			tm.repeatReplace();
		}
		
		//ダイヤモンド置換
		{
			tm.diamondReplace();
		}
		
		//ファイル出力
		{
			targetList=tm.getTargetList();
			String dstFileName=tm.getValue(id+"_出力ファイル名");
			PrintWriter wr=new PrintWriter(new FileWriter(dstFileName));
			
			for(String curStr:targetList){
				//System.out.println(curStr);
				wr.println(curStr);
			}
			wr.close();
		}
	}
}
