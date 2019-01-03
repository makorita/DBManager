import java.io.*;
import java.util.*;

public class RepeatListData extends RepeatData{
	private LinkedList<String> repeatList;
	
	private int curIndex;
	
	public RepeatListData(String replaceStr){
		setReplaceStr(replaceStr);
		repeatList=new LinkedList<String>();
		init();
	}
	
	public void init(){
		curIndex=0;
	}
	
	public String getType(){
		return "LIST";
	}
	
	public void add(String curStr){
		repeatList.add(curStr);
	}
	
	public String get(){
		return repeatList.get(curIndex);
	}
	
	public void next(){
		if(curIndex>=repeatList.size()-1)init();
		else curIndex++;
	}
	
	public Iterator<String> iterator(){
		return repeatList.iterator();
	}
	
	public String getStrExp(){
		String returnStr=getReplaceStr()+"::";
		for(String curStr:repeatList){
			returnStr+="\t"+curStr;
		}
		returnStr=returnStr.replaceFirst("\t","");
		
		return returnStr;
	}
}
