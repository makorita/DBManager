import java.io.*;
import java.util.*;
import java.util.regex.*;

public class ReplaceModifier extends Modifier{
	public ReplaceModifier(){
		
	}

	public void doAfterLoad(){
	}
	
	public void standardReplace(){
		LinkedList<String> aftList=new LinkedList<String>();
		for(String curStr:getTargetList()){
			//System.out.println(curStr);
			Iterator<Node> it=iterator(null);
			while(it.hasNext()){
				Node tmpNode=it.next();
				if(tmpNode.getValue()!=null)curStr=curStr.replaceAll(tmpNode.getName(),tmpNode.getValue());
			}
			aftList.add(curStr);
		}
		setTargetList(aftList);
	}
	
	public void diamondReplace(){
		LinkedList<String> aftList=new LinkedList<String>();
		Pattern p = Pattern.compile("<.+?>");
		for(String curStr:getTargetList()){
			Matcher m = p.matcher(curStr);
			while(m.find()){
				String befStr=m.group();
				//System.out.println(befStr);
				String replaceStr=befStr;
				replaceStr=replaceStr.replaceFirst("<","");
				replaceStr=replaceStr.replaceFirst(">","");
				String aftStr=getValue(replaceStr);
				if(aftStr!=null)curStr=curStr.replaceFirst(befStr,aftStr);
			}
			
			aftList.add(curStr);
		}
		
		setTargetList(aftList);
	}
	
	public void specifyReplace(LinkedList<String> specifyList){
		LinkedList<String> aftList=new LinkedList<String>();
		for(String curStr:getTargetList()){
			for(String befStr:specifyList){
				if(!existsKey(befStr))continue;
				String aftStr=getValue(befStr);
				if(aftStr!=null)curStr=curStr.replaceAll(befStr,aftStr);
			}
			
			aftList.add(curStr);
		}
		
		setTargetList(aftList);
	}
}