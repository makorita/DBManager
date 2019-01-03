import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class Jikkou04_DeleteNodeFromDefDB{
	public static void main(String args[]){
		///�N���b�v�{�[�h�̓ǂݍ���
		///�N���b�v�{�[�h��clibList
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
		
		///�f�[�^�}�l�[�W���[�����B�p�����[�^���[�N�u�b�N�ǂݍ���
		///clipList��tm
		ReplaceModifier tm=new ReplaceModifier();
		{
			tm.loadDB();
			for(String curStr:clipList){
				curStr=curStr.replaceAll("\t",Modifier.DIVIDE_STR);	//�^�u����؂蕶����Ƃ��Ĉ���
				tm.remove(curStr);
			}
			tm.saveDB();
		}
	}
}