import java.io.*;
import java.util.*;
import java.util.regex.*;

public class IPManager extends Modifier{
	public IPManager(){
		setDbPath("アドレス一覧");
	}

	public void doAfterLoad(){
	}
	
	public void addIP(String ip,String explain){
		put(getDbPath()+Modifier.DIVIDE_STR+ip+Modifier.DIVIDE_STR+"ip",ip);
		put(getDbPath()+Modifier.DIVIDE_STR+ip+Modifier.DIVIDE_STR+"explain",explain);
	}
	
	public void searchIP(){
		LinkedList<String> aftList=new LinkedList<String>();	//編集＆戻り用リスト
		for(String curStr:getTargetList()){
			if(curStr.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"))curStr+="/32";
			if(!curStr.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}/\\d{1,2}"))continue;
			Address searchIP=new Address(curStr);
			Iterator<Node> it=iterator(getDbPath());
			while(it.hasNext()){
				Node curNode=it.next();
				//System.out.println(curNode.getValue(curNode.getName()+"_ip"));
				Address taisyouAddr=new Address(curNode.getValue(curNode.getName()+"_ip"));
				if(searchIP.containsAddress(taisyouAddr))aftList.add(curNode.getValue(curNode.getName()+"_explain"));
			}
		}
		
		setTargetList(aftList);
	}
}