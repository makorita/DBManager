import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class Jikkou01_DiamondReplace{
	public static void main(String args[]){
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
					clipList.add(curStr);
				}

				
			} catch(UnsupportedFlavorException e){
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//System.out.println(clipBoardStr);
		}
		
		///�f�[�^�}�l�[�W���[�����B�p�����[�^���[�N�u�b�N�ǂݍ���
		///clipList��tm
		ReplaceModifier tm=new ReplaceModifier();
		{
			tm.loadDB();
			tm.setTargetList(clipList);
			tm.diamondReplace();
		}
		
		///�N���b�v�{�[�h�̃Z�b�g
		///clipBoardStr,clipboard�˃N���b�v�{�[�h
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