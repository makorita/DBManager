import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class Jikkou02_AllRepeat{
	public static void main(String args[]){
		String srcPath="F:/document/TaskChute1.xls";
		String sheetName="繰り返し";
		
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
		//srcPath,sheetName⇒rm
		RepeatModifier rm=new RepeatModifier();
		{
			rm.setTargetList(clipList);
			RepeatLoader rl=new RepeatLoader(rm,srcPath);
			rl.loadExcel(sheetName);
			rm.allRepeat();
		}
		
		///クリップボードのセット
		///clipBoardStr,clipboard⇒クリップボード
		{
			String clipBoardStr=null;
			LinkedList<String> tagetList=rm.getTargetList();
			for(String curStr:tagetList){
				if(clipBoardStr==null)clipBoardStr=curStr+"\n";
				else clipBoardStr+=curStr+"\n";
			}
			StringSelection selection = new StringSelection(clipBoardStr);
			clipboard.setContents(selection, null);
		}
	}
}