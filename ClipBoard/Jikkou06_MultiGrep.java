import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class Jikkou06_MultiGrep{
	public static void main(String args[]){
		String mode="grep";
		if(args.length==1 && args[0].equals("ungrep"))mode="ungrep";
		else if(args.length==1 && args[0].equals("match"))mode="match";
		
		String srcPath="F:/document/TaskChute1.xls";
		String sheetName="Grep";
		
		///�N���b�v�{�[�h�̓ǂݍ���
		///�N���b�v�{�[�h��clibList,clipboard
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
		
		//RepatModifier�̃Z�b�g
		//srcPath,sheetName��gm
		GrepModifier gm=new GrepModifier();
		{
			gm.setTargetList(clipList);
			GrepLoader gl=new GrepLoader(gm,srcPath);
			gl.loadExcel(sheetName);
			if(mode.equals("grep"))gm.grep();
			else if(mode.equals("ungrep"))gm.ungrep();
			else if(mode.equals("match"))gm.matchGrep();
		}
		
		///�N���b�v�{�[�h�̃Z�b�g
		///clipBoardStr,clipboard�˃N���b�v�{�[�h
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