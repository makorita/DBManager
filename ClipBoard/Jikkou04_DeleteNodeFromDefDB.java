import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class Jikkou04_DeleteNodeFromDefDB{
	public static void main(String args[]){
		///クリップボードの読み込み
		///クリップボード⇒clibList
		LinkedList<String> clipList=new LinkedList<String>();
		{
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable object = clipboard.getContents(null);
			try {
				String clipBoardStr = (String)object.getTransferData(DataFlavor.stringFlavor);
				String[] word=clipBoardStr.split("\n");
				for(String curStr:word){
					clipList.add(curStr);
				}
				
			} catch(UnsupportedFlavorException e){
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//System.out.println(clipBoardStr);
		}
		
		///データマネージャー生成。パラメータワークブック読み込み
		///clipList⇒tm
		ReplaceModifier tm=new ReplaceModifier();
		{
			tm.loadDB();
			for(String curStr:clipList){
				curStr=curStr.replaceAll("\t",Modifier.DIVIDE_STR);	//タブも区切り文字列として扱う
				tm.remove(curStr);
			}
			tm.saveDB();
		}
	}
}