import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.datatransfer.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.*;

public class Jikkou01_StandardReplace{
	public static void main(String args[]){
		String srcPath="D:/ãŒë”ÅEåí îÔ/TaskChute1.xls";
		String sheetName="íuä∑";
		
		LinkedList<String> clipList=new LinkedList<String>();
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		{
			Transferable object = clipboard.getContents(null);
			try {
				String clipBoardStr = (String)object.getTransferData(DataFlavor.stringFlavor);
				String[] word=clipBoardStr.split("\n");
				for(String curStr:word){
					//System.out.println(curStr);
					clipList.add(curStr);
				}
				
			} catch(UnsupportedFlavorException e){
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		ReplaceModifier tm=new ReplaceModifier();
		tm.setTargetList(clipList);
		{
			Workbook wb=null;
			try{
				wb = WorkbookFactory.create(new FileInputStream(srcPath));
				
			}catch(IOException e){
				System.out.println(e.toString());
				System.exit(0);
			}

			Sheet sheet=wb.getSheet(sheetName);
			for(int rowIndex=0;rowIndex<=sheet.getLastRowNum();rowIndex++){	//rowÇÃç≈ëÂílÇÕç≈ëÂindex
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
				else if(aftCell.getCellType()==CellType.BLANK)aftCellStr="";
				else continue;

				tm.put(befCellStr,aftCellStr);
			}
		}
		
		tm.standardReplace();
		
		{
			String clipBoardStr=null;
			LinkedList<String> tagetList=tm.getTargetList();
			for(String curStr:tagetList){
				if(clipBoardStr==null)clipBoardStr=curStr+"\n";
				else clipBoardStr+=curStr+"\n";
			}
			StringSelection selection = new StringSelection(clipBoardStr);
			clipboard.setContents(selection, null);
		}
	}
}