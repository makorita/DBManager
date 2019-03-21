import java.io.*;
import java.util.*;

public class PrefixModifier extends Modifier{
	private static final String PREFIX_DB_PATH="prefix";
	private static final String PREFIX_DBNAME="prefixDB";
	
	public PrefixModifier(){
	}
	
	public String getModifyStr(){
		return getValue(PREFIX_DB_PATH+DBManager.DIVIDE_STR+"modifyStr");
	}
	
	public void setModifyStr(String modifyStr){
		put(PREFIX_DB_PATH+DBManager.DIVIDE_STR+"modifyStr",modifyStr);
	}
	
	public void doAfterLoad(){
	}
	
	public void loadDB(){
		loadDB(PREFIX_DBNAME);
	}
	
	public void saveDB(){
		saveDB(PREFIX_DBNAME);
	}
	
	public void addPrefix(){
		LinkedList<String> aftList=new LinkedList<String>();
		for(String curStr:getTargetList()){
			if(curStr.matches(getModifyStr()+".*"))curStr=curStr.replaceFirst(getModifyStr(),"");
			else curStr=getModifyStr()+curStr;
			aftList.add(curStr);
		}
		
		setTargetList(aftList);
	}
	
	public void addSuffix(){
		LinkedList<String> aftList=new LinkedList<String>();
		for(String curStr:getTargetList()){
			if(curStr.matches(".*"+getModifyStr()))curStr=curStr.replaceFirst(getModifyStr()+"$","");
			else curStr=curStr+getModifyStr();
			aftList.add(curStr);
		}
		
		setTargetList(aftList);
	}
}