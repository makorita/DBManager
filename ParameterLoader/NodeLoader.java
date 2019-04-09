import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.*;

public class NodeLoader{
	DBManager dbm;
	
	public NodeLoader(DBManager dbm){
		this.dbm=dbm;
	}
	
	public void loadTabList(LinkedList<String> srcList){
		for(String curStr:srcList){
			String[] word=curStr.split("\t");
			if(word.length==1)continue;
			String key=null;
			for(int i=0;i<word.length-1;i++){	//最後のタブ以外は区切り文字に変換
				if(i==0)key=word[0];
				else key=key+DBManager.DIVIDE_STR+word[i];
			}
			dbm.put(key,word[word.length-1]);
		}
	}
	
	public void loadItemExcel(String srcFilePath,String sheetName){
		Workbook wb=null;
		try{
			wb = WorkbookFactory.create(new FileInputStream(srcFilePath));
			
		}catch(IOException e){
			System.out.println(e.toString());
			System.exit(0);
		}

		Sheet sheet=wb.getSheet(sheetName);
		LinkedList<String> koumokuList=null;
		String mode="NONE";
		for(int rowIndex=0;rowIndex<=sheet.getLastRowNum();rowIndex++){	//行回し
			//フィルタ処理
			Row row=sheet.getRow(rowIndex);
			if(row==null){
				koumokuList=null;
				mode="NONE";
				continue;
			}
			
			String idStr=null;
			LABEL:for(int cellIndex=0;cellIndex<row.getLastCellNum();cellIndex++){
				Cell cell=row.getCell(cellIndex);
				if(cell==null)continue;
				
				String cellStr=null;
				if(cell.getCellType()==CellType.STRING)cellStr=cell.getStringCellValue();
				else if(cell.getCellType()==CellType.NUMERIC)cellStr=String.valueOf((int)(cell.getNumericCellValue()));
				else if(cell.getCellType()==CellType.FORMULA){
					try{
						cellStr=cell.getStringCellValue();
					}catch(IllegalStateException e){
						cellStr=String.valueOf((int)(cell.getNumericCellValue()));
					}
				}else if(cell.getCellType()==CellType.BLANK && cellIndex==0){
					koumokuList=null;
					mode="NONE";
				}
				
				if(cellStr==null)continue;
				if(cellStr.matches(";;.*"))continue LABEL;
				cellStr=cellStr.replaceAll("_","#UB#");
				
				if(mode.equals("NONE")){
					if(koumokuList==null)koumokuList=new LinkedList<String>();
					koumokuList.add(cellStr);	//項目の代入
					//System.out.println(cellStr);
					
				}else if(mode.equals("INSERT")){
					if(cellIndex==0)idStr=cellStr;
					//System.out.println(idStr);
				
					dbm.put(idStr+DBManager.DIVIDE_STR+koumokuList.get(cellIndex),cellStr);
				}
			}
			//モード変更
			if(mode.equals("NONE") && koumokuList!=null)mode="INSERT";
		}
	}
	
	public void loadTabExcel(String srcFilePath,String sheetName){
		Workbook wb=null;
		try{
			wb = WorkbookFactory.create(new FileInputStream(srcFilePath));
			
		}catch(IOException e){
			System.out.println(e.toString());
			System.exit(0);
		}

		Sheet sheet=wb.getSheet(sheetName);
		for(int rowIndex=0;rowIndex<=sheet.getLastRowNum();rowIndex++){	//行回し
			Row row=sheet.getRow(rowIndex);
			if(row==null)continue;
			
			//1回リストに格納
			LinkedList<String> cellList=new LinkedList<String>();
			for(int cellIndex=0;cellIndex<row.getLastCellNum();cellIndex++){
				Cell cell=row.getCell(cellIndex);
				if(cell==null)continue;
				
				String cellStr=null;
				if(cell.getCellType()==CellType.STRING)cellStr=cell.getStringCellValue();
				else if(cell.getCellType()==CellType.NUMERIC)cellStr=String.valueOf((int)(cell.getNumericCellValue()));
				
				if(cellStr!=null && !cellStr.matches(";;.*"))cellList.add(cellStr);
			}
			
			//フィルタ
			if(cellList.size()<2)continue;
			
			//key生成
			String key=null;
			for(int i=0;i<cellList.size()-1;i++){	//最終値はキーに追加しない
				if(i==0)key=cellList.get(0);
				else key+=DBManager.DIVIDE_STR+cellList.get(i);
			}
			
			dbm.put(key,cellList.get(cellList.size()-1));
		}		
	}
}