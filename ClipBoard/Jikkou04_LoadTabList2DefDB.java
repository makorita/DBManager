import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class Jikkou04_LoadTabList2DefDB{
	public static void main(String args[]){
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
			tm.loadDB();
			NodeLoader nl=new NodeLoader(tm);
			nl.loadTabList(clipList);
			//System.out.println(tm.getTreeStr());
			tm.saveDB();
			System.out.println(tm.getTreeStr());
		}
	}
}