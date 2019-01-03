import java.io.*;
import java.util.*;

public class PrefixModifier extends Modifier{
	private static final String ModStrDBKey="prefix"+Modifier.DIVIDE_STR+"modifyStr";
	private static final String PREFIX_DBNAME="prefixDB";
	private String modifyStr;
	
	public PrefixModifier(){
	}
	
	public String getModifyStr(){
		return modifyStr;
	}
	
	public void setModifyStr(String modifyStr){
		this.modifyStr=modifyStr;
		put(ModStrDBKey,getModifyStr());
	}
	
	public void doAfterLoad(){
		if(existsKey(ModStrDBKey))modifyStr=getValue(ModStrDBKey);	//set‚ðŽg‚¤‚ÆDB˜AŒg‚³‚ê‚é
	}
	
	public void loadDB(){
		if(existsDB(PREFIX_DBNAME))loadDB(PREFIX_DBNAME);
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