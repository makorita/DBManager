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
	
	public void loadDB(){
		loadDB(PREFIX_DBNAME);
	}
	
	public void saveDB(){
		saveDB(PREFIX_DBNAME);
	}
	
	public void addPrefix(){
		LinkedList<String> aftList=new LinkedList<String>();
		for(String curStr:getTargetList()){
			curStr=getModifyStr()+curStr;
			aftList.add(curStr);
		}
		
		setTargetList(aftList);
	}
	
	public void delPrefix(){
		LinkedList<String> aftList=new LinkedList<String>();
		for(String curStr:getTargetList()){
			curStr=curStr.replaceFirst(getModifyStr(),"");
			aftList.add(curStr);
		}
		
		setTargetList(aftList);
	}
}