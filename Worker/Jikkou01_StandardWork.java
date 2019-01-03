import java.io.*;
import java.util.*;

public class Jikkou01_StandardWork{
	private static RepeatModifier rm;
	private static ReplaceModifier tm;
	private static TargetLoader tl;

	public static void main(String args[]){
		//����
		String srcPath="input/�e�X�g�\�[�X.xls";
		LinkedList<String> idList=new LinkedList<String>();
		idList.addAll(Arrays.asList("���s01","���s02","���s03","���s04","���s05","���s06","���s07","���s08"));
		String repeatSheetName="�J��Ԃ�";
		String replaceSheetName="�e�X�g�p�����[�^";
		String targetSheetName="�^�[�Q�b�g";
		LinkedList<String> specifyList=new LinkedList<String>();
		specifyList.addAll(Arrays.asList("##ID##"));
		boolean blanSkip=false;
		
		//RepeatModifier����
		rm=new RepeatModifier();
		loadRepeatParameter(srcPath,repeatSheetName,replaceSheetName);
		
		//ReplaceModifier����
		tm=new ReplaceModifier();
		loadReplaceParameter(srcPath,replaceSheetName);
		
		//RepeatModifier�pTargetLoader����
		tl=new TargetLoader(rm,srcPath);
		
		for(String curId:idList){
			System.out.println("ID:"+curId);
			
			//�J��Ԃ�����
			String columnName=tm.getValue(curId+Modifier.DIVIDE_STR+"��");
			//System.out.println(columnName);
			tl.loadExcel(targetSheetName,columnName,blanSkip);
			rm.partialRepeat();
			LinkedList<String> targetList=rm.getTargetList();
			tm.setTargetList(targetList);
			
			//����u������
			if(specifyList.size()>0){
				for(String curStr:specifyList){
					tm.put(curStr,curId);	//ID�ɕϊ�
				}
				tm.specifyReplace(specifyList);
			}
			
			//�u������
			tm.diamondReplace();
			targetList=tm.getTargetList();
			
			//�t�@�C���o��
			String dstFileName=tm.getValue(curId+Modifier.DIVIDE_STR+"�o�̓t�@�C����");
			writeText(targetList,dstFileName);
			
//			for(String curStr:targetList){
//				System.out.println(curStr);
//			}
			//break;
		}
		
	}
	
	public static void loadRepeatParameter(String srcPath,String repeatSheetName,String replaceSheetName){
		RepeatLoader rl=new RepeatLoader(rm,srcPath);
		rl.loadExcel(repeatSheetName);
		NodeLoader nl=new NodeLoader(rm);
		nl.loadItemExcel(srcPath,replaceSheetName);

		//System.out.println(rm.getTreeStr());
	}
	
	public static void loadReplaceParameter(String srcPath,String replaceSheetName){
		NodeLoader nl=new NodeLoader(tm);
		nl.loadItemExcel(srcPath,replaceSheetName);
	}
	
	public static void writeText(LinkedList<String> targetList,String dstFileName){
		try {
			PrintWriter wr=new PrintWriter(new FileWriter(dstFileName));
			
			for(String curStr:targetList){
				//System.out.println(curStr);
				wr.println(curStr);
			}
			wr.close();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
