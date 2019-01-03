import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class Jikkou05_JavaMethodGetter{
	public static void main(String args[]){
		String dbKey="javaMember";

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

		//クリップボードの検査
		Charu3ConfigModifier ccm=new Charu3ConfigModifier();
		ccm.loadDB();
		{
			String className=null;
			String tourokuName=null;
			for(String curStr:clipList){
				curStr=curStr.replaceAll("\t","");
				if(curStr.matches("(public|private|protected).*")){
					//System.out.println(curStr);
					if(curStr.matches("public .*class.*")){
						curStr=curStr.replaceFirst("abstract ","");
						curStr=curStr.replaceFirst("public class ","");
						curStr=curStr.replaceFirst("\\{","");
						String[] word=curStr.split(" ");
						className=word[0];
						tourokuName=className.substring(0,1).toLowerCase()+className.substring(1);
						//System.out.println(className);
						ccm.put(dbKey+"_"+tourokuName+"_className",className);
						continue;
					}
					
					if(className==null)continue;
					curStr=curStr.replaceAll("\\{","");
					curStr=curStr.replaceAll("public ","");
					curStr=curStr.replaceAll("private ","");
					curStr=curStr.replaceAll("protected ","");
					curStr=curStr.replaceAll("static ","");
					curStr=curStr.replaceAll("final ","");
					curStr=curStr.replaceAll("=.*","");
					curStr=curStr.replaceAll("//.*","");
					curStr=curStr.replaceAll(";","");
					int pos=curStr.indexOf(" ");
					if(pos<0)continue;
					String returnStr=curStr.substring(0,pos);
					String nameStr=curStr.substring(pos+1);
					if(returnStr.matches(className+".*"))continue;
					//System.out.println(returnStr+","+nameStr);
					String keyStr=null;
					String valueStr=null;
					if(nameStr.matches(".*\\(.*\\)")){
						int kakkoPos=nameStr.indexOf("(");
						valueStr=nameStr.substring(0,kakkoPos);
						keyStr=nameStr+"->"+returnStr;
						//System.out.println(keyStr);
					}else{
						keyStr=nameStr+"=>"+returnStr;
						keyStr=keyStr.replaceAll("_","#");
						valueStr=nameStr;
					}
					ccm.put(dbKey+"_"+tourokuName+"_"+keyStr,valueStr);
				}
				
			}
			ccm.saveDB();
			System.out.println(ccm.getTreeStr());
		}
		
		ccm.writeCharu3Config(dbKey);
	}
}