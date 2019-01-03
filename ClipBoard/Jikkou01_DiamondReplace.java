import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class Jikkou01_DiamondReplace{
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
			tm.setTargetList(clipList);
			tm.diamondReplace();
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