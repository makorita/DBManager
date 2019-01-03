import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.*;

public class TargetLoader{
	Modifier modi;
	Workbook wb;
	
	public TargetLoader(Modifier modi,String srcPath){
		this.modi=modi;
		try{
			wb = WorkbookFactory.create(new FileInputStream(srcPath));
			
		}catch(IOException e){
			System.out.println(e.toString());
			System.exit(0);
		}
	}
	
	public void loadExcel(String sheetName,String ColumnName,boolean blankSkip){
		Sheet sheet=wb.getSheet(sheetName);
		//ターゲット列検索
		int targetColumnNum=-1;
		Row koumokuRow=sheet.getRow(0);
		for(int cellIndex=0;cellIndex<koumokuRow.getLastCellNum();cellIndex++){
			Cell cell=koumokuRow.getCell(cellIndex);
			if(cell==null)continue;
			//if(cell.getCellType()==Cell.CELL_TYPE_STRING)System.out.println(cell.getStringCellValue());
			if(cell.getCellType()==CellType.STRING && cell.getStringCellValue().equals(ColumnName)){
				targetColumnNum=cellIndex;
				break;
			}
		}
		
		//ターゲットリスト読み込み
		LinkedList<String> targetList=new LinkedList<String>();
		for(int rowIndex=1;rowIndex<=sheet.getLastRowNum();rowIndex++){	//スタートは1行目
			Row row=sheet.getRow(rowIndex);
			if(row==null)continue;
			Cell cell=row.getCell(targetColumnNum);
			if(cell==null || cell.getCellType()==CellType.BLANK){
				if(!blankSkip)targetList.add("");
				continue;
			}
			if(cell.getStringCellValue().matches(";.*"))continue;
			
			targetList.add(cell.getStringCellValue());
		}
		
		modi.setTargetList(targetList);
	}
}