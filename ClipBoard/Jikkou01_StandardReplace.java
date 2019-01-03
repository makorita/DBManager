import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class Jikkou01_StandardReplace{
	public static void main(String args[]){
		String srcPath="F:/document/TaskChute1.xls";
		String sheetName="置換";
		
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
		
		//ReplaceModifierのセット
		//srcPath,sheetName⇒tm
		ReplaceModifier tm=new ReplaceModifier();
		{
			tm.setTargetList(clipList);
			NodeLoader nl=new NodeLoader(tm);
			nl.loadExcel(srcPath,sheetName);
			tm.standardReplace();
		}
		
		///クリップボードのセット
		///clipBoardStr,clipboard⇒クリップボード
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