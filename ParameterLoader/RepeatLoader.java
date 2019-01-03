import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.*;

public class RepeatLoader{
	Workbook wb;
	RepeatModifier rm;
	
	public RepeatLoader(RepeatModifier rm,String srcExcel){
		this.rm=rm;
		try{
			wb = WorkbookFactory.create(new FileInputStream(srcExcel));
			
		}catch(IOException e){
			System.out.println(e.toString());
			System.exit(0);
		}
	}
	
	public void loadExcel(String sheetName){
		Sheet sheet=wb.getSheet(sheetName);
		for(int rowIndex=0;rowIndex<=sheet.getLastRowNum();rowIndex++){	//rowの最大値は最大index
			Row row=sheet.getRow(rowIndex);
			if(row==null)continue;
			Cell checkCell=row.getCell(0);
			if(checkCell!=null && checkCell.getCellType()!=CellType.BLANK)continue;
			Cell repeatStrCell=row.getCell(1);
			if(repeatStrCell==null || repeatStrCell.getCellType()!=CellType.STRING)continue;
			String repeatStr=repeatStrCell.getStringCellValue();
			
			//リピート回数の取得
			if(repeatStr.equals("Repeat:")){
				int repeatNum=1;
				if(row.getCell(2).getCellType()==CellType.STRING)repeatNum=Integer.parseInt(row.getCell(2).getStringCellValue());
				else if(row.getCell(2).getCellType()==CellType.NUMERIC)repeatNum=(int)row.getCell(2).getNumericCellValue();
				rm.setGlobalRepeatNum(repeatNum);
				continue;
			}
			
			if(!repeatStr.matches("#[A-Z]+"))continue;
			
			//項目の格納
			LinkedList<String> koumokuList=new LinkedList<String>();
			for(int cellIndex=2;cellIndex<row.getLastCellNum();cellIndex++){	//2から開始
				Cell cell=row.getCell(cellIndex);
				if(cell==null)break;
				if(cell.getCellType()!=CellType.STRING && cell.getCellType()!=CellType.NUMERIC)break;	//値以外が来たら検索終了
				
				if(cell.getCellType()==CellType.STRING)koumokuList.add(cell.getStringCellValue());
				else if(cell.getCellType()==CellType.NUMERIC)koumokuList.add(String.valueOf((int)cell.getNumericCellValue()));
			}
			
			if(koumokuList.size()>1){
				RepeatListData tmpListData=new RepeatListData(repeatStr);
				for(String curStr:koumokuList){
					tmpListData.add(curStr);
				}
				
				rm.addRepeatData(tmpListData);
			}else if(koumokuList.size()==1){
				String tmpStr=koumokuList.get(0);
				int startValue=0;
				int maxValue=Integer.MAX_VALUE;
				int delta=1;
				int zeroPad=1;
				if(tmpStr.matches(".*,\\d+")){
					String[] word=tmpStr.split(",");
					delta=Integer.parseInt(word[1]);
					int pos=tmpStr.indexOf(",");
					tmpStr=tmpStr.substring(0,pos);
				}
				if(tmpStr.matches("\\d+-\\d+")){
					String[] word=tmpStr.split("-");
					startValue=Integer.parseInt(word[0]);
					zeroPad=word[0].length();
					maxValue=Integer.parseInt(word[1]);
				}else{
					startValue=Integer.parseInt(tmpStr);
					zeroPad=tmpStr.length();
				}
				
				RepeatNumericData tmpNumeric=new RepeatNumericData(repeatStr,startValue,maxValue,delta,zeroPad);
				rm.addRepeatData(tmpNumeric);
			}
		}
	}
}