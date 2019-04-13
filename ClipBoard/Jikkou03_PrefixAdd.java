import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class Jikkou03_PrefixAdd{
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
		}
		
		///�f�[�^�}�l�[�W���[�����B�p�����[�^���[�N�u�b�N�ǂݍ���
		///clipList��dm
		PrefixModifier pm=new PrefixModifier();
		{
			pm.setTargetList(clipList);
			
			pm.loadDB();
			pm.addPrefix();
		}
		
		///�N���b�v�{�[�h�̃Z�b�g
		///clipBoardStr,clipboard�˃N���b�v�{�[�h
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