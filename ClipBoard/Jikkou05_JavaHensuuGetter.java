import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class Jikkou05_JavaHensuuGetter{
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
			for(String curStr:clipList){
				curStr=curStr.replaceAll("\t","");
				if(curStr.length()==0)continue;
				if(Character.isUpperCase(curStr.charAt(0))){
					String[] word=curStr.split(" ");
					if(word.length==1)continue;
					String variableStr=word[1];
					variableStr=variableStr.replaceAll("=.*","");
					variableStr=variableStr.replaceAll(";","");
					//System.out.println(variableStr);
					ccm.put(dbKey+"_"+variableStr.replaceAll("_","#"),variableStr);
				}else if(curStr.matches(".*\\(.+? .+?\\).*")){
					if(curStr.matches(".*matches.*"))continue;
					if(curStr.matches(".*replace.*"))continue;
					Pattern p = Pattern.compile("\\(.+? .+?\\)");
					Matcher m = p.matcher(curStr);
					if(m.find()){
						String matchStr=m.group();
						matchStr=matchStr.replace("(","");
						matchStr=matchStr.replace(")","");
						String[] word1=matchStr.split(",");
						for(String tmpStr:word1){
							String[] word2=tmpStr.split(" ");
							//System.out.println(curStr+"\t"+tmpStr);
							String matchStr2=word2[1];
							if(matchStr2.length()<=1)continue;
							matchStr2=matchStr2.replace("[","");
							matchStr2=matchStr2.replace("]","");
							matchStr2=matchStr2.replaceFirst(":.*","");
							ccm.put(dbKey+"_"+matchStr2.replaceAll("_","#"),matchStr2);
						}
					}
				}
			}

			ccm.saveDB();
			System.out.println(ccm.getTreeStr());
		}
		
		ccm.writeCharu3Config(dbKey);
	}
}