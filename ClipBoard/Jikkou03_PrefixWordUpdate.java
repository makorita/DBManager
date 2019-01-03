import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class Jikkou03_PrefixWordUpdate{
	public static void main(String args[]){
		///クリップボードの読み込み
		///クリップボード⇒clibList,clipboard
		String clipBoardStr=null;
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		{
			Transferable object = clipboard.getContents(null);
			try {
				clipBoardStr = (String)object.getTransferData(DataFlavor.stringFlavor);
				
			} catch(UnsupportedFlavorException e){
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//クリップボード語句整形
		{
			clipBoardStr=clipBoardStr.replaceAll("\n","");
			//clipBoardStr=clipBoardStr.replaceAll("\r","");
		}
		
		PrefixModifier pm=new PrefixModifier();
		{
			pm.setModifyStr(clipBoardStr);
			pm.saveDB();
			//System.out.println(pm.getTreeStr());
		}
	}
}