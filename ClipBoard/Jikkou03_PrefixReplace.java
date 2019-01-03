import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class Jikkou03_PrefixReplace{
	public static void main(String args[]){
		boolean suffixFlag=false;
		if(args.length==1 && args[0].equals("suffix"))suffixFlag=true;
		
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
		}
		
		///データマネージャー生成。パラメータワークブック読み込み
		///clipList⇒dm
		PrefixModifier pm=new PrefixModifier();
		{
			pm.setTargetList(clipList);
			
			pm.loadDB();
			if(suffixFlag)pm.addSuffix();
			else pm.addPrefix();
			
			/*
			LinkedList<String> tmpList=pm.getTargetList();
			for(String tmpStr:tmpList){
				System.out.println(tmpStr);
			}
			*/
		}
		
		///クリップボードのセット
		///clipBoardStr,clipboard⇒クリップボード
		{
			String clipBoardStr=null;
			LinkedList<String> tagetList=pm.getTargetList();
			for(String curStr:tagetList){
				if(clipBoardStr==null)clipBoardStr=curStr+"\n";
				else clipBoardStr+=curStr+"\n";
			}
			StringSelection selection = new StringSelection(clipBoardStr);
			clipboard.setContents(selection, null);
		}
	}
}