import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.datatransfer.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.*;

public class Jikkou06_MultiGrep{
	public static void main(String args[]) throws IOException{
		String mode="grep";
		if(args.length==1 && args[0].equals("ungrep"))mode="ungrep";
		else if(args.length==1 && args[0].equals("match"))mode="match";
		
		String srcPath="F:/document/TaskChute1.xls";
		String sheetName="Grep";
		
		///クリップボードの読み込み
		///クリップボード⇒clibList,clipboard
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
		
		//RepatModifierのセット
		//srcPath,sheetName⇒gm
		GrepModifier gm=new GrepModifier();
		gm.setTargetList(clipList);
		{
			Workbook wb = WorkbookFactory.create(new FileInputStream(srcPath));
			Sheet sheet=wb.getSheet(sheetName);
			for(int rowIndex=0;rowIndex<=sheet.getLastRowNum();rowIndex++){	//rowの最大値は最大index
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

			if(mode.equals("grep"))gm.grep();
			else if(mode.equals("ungrep"))gm.ungrep();
			else if(mode.equals("match"))gm.matchGrep();
		}
		
		///クリップボードのセット
		///clipBoardStr,clipboard⇒クリップボード
		{
			String clipBoardStr=null;
			LinkedList<String> tagetList=gm.getTargetList();
			for(String curStr:tagetList){
				if(clipBoardStr==null)clipBoardStr=curStr+"\n";
				else clipBoardStr+=curStr+"\n";
			}
			StringSelection selection = new StringSelection(clipBoardStr);
			clipboard.setContents(selection, null);
		}
	}
}