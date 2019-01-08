import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.*;

public class GrepLoader{
	Workbook wb;
	GrepModifier gm;
	
	public GrepLoader(GrepModifier gm,String srcExcel){
		this.gm=gm;
		try{
			wb = WorkbookFactory.create(new FileInputStream(srcExcel));
			
		}catch(IOException e){
			System.out.println(e.toString());
			System.exit(0);
		}
	}
	
	public void loadExcel(String sheetName){
		Sheet sheet=wb.getSheet(sheetName);
		for(int rowIndex=0;rowIndex<=sheet.getLastRowNum();rowIndex++){	//row‚ÌÅ‘å’l‚ÍÅ‘åindex
			Row row=sheet.getRow(rowIndex);
			if(row==null)continue;
			boolean grepFlag=false;
			boolean jogaiFlag=false;
			Cell checkCell1=row.getCell(0);
			Cell checkCell2=row.getCell(2);
			if(checkCell1==null)grepFlag=true;
			if(checkCell1!=null && checkCell1.getCellType()==CellType.BLANK)grepFlag=true;
			if(checkCell2==null)jogaiFlag=true;
			if(checkCell2!=null && checkCell2.getCellType()==CellType.BLANK)jogaiFlag=true;
			
			if(grepFlag){
				Cell grepCell=row.getCell(1);
				if(grepCell==null){
					
				}else if(grepCell.getCellType()==CellType.STRING){
					String grepStr=grepCell.getStringCellValue();
					gm.addGrepWord(grepStr);
				}else if(grepCell.getCellType()==CellType.NUMERIC){
					String grepStr=String.valueOf((int)grepCell.getNumericCellValue());
					gm.addGrepWord(grepStr);
				}
			}
			
			if(jogaiFlag){
				Cell jogaiCell=row.getCell(1);
				if(jogaiCell==null){
					
				}else if(jogaiCell.getCellType()==CellType.STRING){
					String jobaiStr=jogaiCell.getStringCellValue();
					gm.addJogaiWord(jobaiStr);
				}else if(jogaiCell.getCellType()==CellType.NUMERIC){
					String jobaiStr=String.valueOf((int)jogaiCell.getNumericCellValue());
					gm.addJogaiWord(jobaiStr);
				}
			}
		}
	}
}