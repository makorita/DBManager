import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.*;

public class NodeLoader{
	Modifier modi;
	
	public NodeLoader(Modifier modi){
		this.modi=modi;
	}
	
	public void loadTabList(LinkedList<String> srcList){
		for(String curStr:srcList){
			String[] word=curStr.split("\t");
			if(word.length==1)continue;
			String key=null;
			for(int i=0;i<word.length-1;i++){	//最後のタブ以外は区切り文字に変換
				if(i==0)key=word[0];
				else key=key+Modifier.DIVIDE_STR+word[i];
			}
			modi.put(key,word[word.length-1]);
			networkExtend(key,word[word.length-1]);
		}
	}
	
	public void loadExcel(String srcFilePath,String sheetName){
		Workbook wb=null;
		try{
			wb = WorkbookFactory.create(new FileInputStream(srcFilePath));
			
		}catch(IOException e){
			System.out.println(e.toString());
			System.exit(0);
		}

		Sheet sheet=wb.getSheet(sheetName);
		for(int rowIndex=0;rowIndex<=sheet.getLastRowNum();rowIndex++){	//rowの最大値は最大index
			Row row=sheet.getRow(rowIndex);
			if(row==null)continue;
			Cell checkCell=row.getCell(0);
			if(checkCell!=null && checkCell.getCellType()!=CellType.BLANK)continue;
			
			Cell befCell=row.getCell(1);
			if(befCell==null || (befCell.getCellType()!=CellType.STRING && befCell.getCellType()!=CellType.NUMERIC))continue;
			String befCellStr=null;
			if(befCell.getCellType()==CellType.STRING)befCellStr=befCell.getStringCellValue();
			else if(befCell.getCellType()==CellType.NUMERIC)befCellStr=String.valueOf((int)befCell.getNumericCellValue());
			
			Cell aftCell=row.getCell(2);
			String aftCellStr=null;
			if(aftCell==null || aftCell.getCellType()==CellType.BLANK)aftCellStr="";
			else if(aftCell.getCellType()==CellType.STRING)aftCellStr=aftCell.getStringCellValue();
			else if(aftCell.getCellType()==CellType.NUMERIC)aftCellStr=String.valueOf((int)aftCell.getNumericCellValue());
			else continue;

			modi.put(befCellStr,aftCellStr);
			networkExtend(befCellStr,aftCellStr);
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
		String mode="NORMAL";
		LinkedList<String> koumokuList=null;
		for(int rowIndex=0;rowIndex<=sheet.getLastRowNum();rowIndex++){	//行回し
			//フィルタ処理
			Row row=sheet.getRow(rowIndex);
			if(row==null)continue;
			Cell koumokuCell=row.getCell(0);
			if(koumokuCell==null)continue;
			if(koumokuCell.getCellType()!=CellType.STRING && koumokuCell.getCellType()!=CellType.NUMERIC)continue;
			String koumokuStr=null;
			if(koumokuCell.getCellType()==CellType.STRING)koumokuStr=koumokuCell.getStringCellValue();
			else if(koumokuCell.getCellType()==CellType.NUMERIC)koumokuStr=String.valueOf((int)koumokuCell.getNumericCellValue());
			
			//行チェック
			if(koumokuStr.equals("項目")){
				mode="NORMAL";
			}else if(koumokuStr.equals("ID")){
				mode="EXTEND";
				koumokuList=new LinkedList<String>();
				for(int cellIndex=1;cellIndex<row.getLastCellNum();cellIndex++){	//1からスタート
					Cell itemCell=row.getCell(cellIndex);
					if(itemCell==null)break;
					if(itemCell.getCellType()!=CellType.STRING && itemCell.getCellType()!=CellType.NUMERIC)break;
					if(itemCell.getCellType()==CellType.STRING && itemCell.getStringCellValue().equals("備考"))break;	//"備考"は最終行扱い
					String itemStr=null;
					if(itemCell.getCellType()==CellType.STRING)itemStr=itemCell.getStringCellValue();
					else if(itemCell.getCellType()==CellType.NUMERIC)itemStr=String.valueOf((int)itemCell.getNumericCellValue());
					
					koumokuList.add(itemStr);
				}
				
			}else if(mode.equals("NORMAL")){
				///フィルタ処理
				Cell valueCell=row.getCell(1);
				if(valueCell==null)continue;
				if(valueCell.getCellType()!=CellType.STRING && valueCell.getCellType()!=CellType.NUMERIC)continue;
				
				///パラメータ登録
				///valueCell⇒value
				String value=null;
				if(valueCell.getCellType()==CellType.STRING)value=valueCell.getStringCellValue();
				else if(valueCell.getCellType()==CellType.NUMERIC)value=String.valueOf((int)(valueCell.getNumericCellValue()));
				modi.put(koumokuStr,value);
				networkExtend(koumokuStr,value);
			}else if(mode.equals("EXTEND")){
				for(int i=0;i<koumokuList.size();i++){	//値回し
					///フィルタ処理
					Cell valueCell=row.getCell(i+1);
					if(valueCell==null)continue;
					if(valueCell.getCellType()!=CellType.STRING && valueCell.getCellType()!=CellType.NUMERIC)continue;
					
					///パラメータ登録
					///valueCell⇒value
					String value=null;
					if(valueCell.getCellType()==CellType.STRING)value=valueCell.getStringCellValue();
					else if(valueCell.getCellType()==CellType.NUMERIC)value=String.valueOf((int)(valueCell.getNumericCellValue()));
					modi.put(koumokuStr+Modifier.DIVIDE_STR+koumokuList.get(i),value);
					networkExtend(koumokuStr+Modifier.DIVIDE_STR+koumokuList.get(i),value);
				}
			}
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
				
				if(cellStr!=null && !cellStr.matches(";.*"))cellList.add(cellStr);
			}
			
			//フィルタ
			if(cellList.size()<2)continue;
			
			//key生成
			String key=null;
			for(int i=0;i<cellList.size()-1;i++){	//最終値はキーに追加しない
				if(i==0)key=cellList.get(0);
				else key+=Modifier.DIVIDE_STR+cellList.get(i);
			}
			
			modi.put(key,cellList.get(cellList.size()-1));
			networkExtend(key,cellList.get(cellList.size()-1));
		}		
	}
	
	public void networkExtend(String koumokuStr,String valueStr){
		if(!valueStr.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}/\\d{1,2}"))return;
		String[] word=valueStr.split("/");
		String maskStr=Address.getMaskStr(Integer.parseInt(word[1]));
		modi.put(koumokuStr+Modifier.DIVIDE_STR+"ip",word[0]);
		modi.put(koumokuStr+Modifier.DIVIDE_STR+"masklen",word[1]);
		modi.put(koumokuStr+Modifier.DIVIDE_STR+"mask",maskStr);
	}
}