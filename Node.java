import java.io.*;
import java.util.*;

public class Node implements Serializable{
	private String name;
	private String value;
	private TreeMap<String,Node> childMap;
	
	public Node(String name){
		this.name=name;
		childMap=new TreeMap<String,Node>();
	}
	
	public String getName(){
		return name;
	}

	public void clear(String key){
		String[] word=key.split(Modifier.DIVIDE_STR);
		if(!word[0].equals(getName()))return;
		if(word.length==1){
			childMap.clear();
		}else if(childMap.containsKey(word[1])){	//子が存在する場合
			Node childNode=childMap.get(word[1]);
			key=key.replace(getName()+Modifier.DIVIDE_STR,"");
			childNode.clear(key);
		}
	}

	public boolean existsKey(String key){
		String[] word=key.split(Modifier.DIVIDE_STR);
		if(!word[0].equals(getName()))return false;
		if(word.length==1) return true;
		else if(word.length==2){	//子に持つ時
			if(childMap.containsKey(word[1]))return true;
			else return false;
		}else if(word.length>2){
			if(!childMap.containsKey(word[1]))return false;
			Node childNode=childMap.get(word[1]);
			key=key.replace(getName()+Modifier.DIVIDE_STR,"");
			return childNode.existsKey(key);
		}
		
		return false;
	}
	
	public Iterator<Node> iterator(String key){
		String[] word=key.split(Modifier.DIVIDE_STR);
		if(!word[0].equals(getName()))return null;
		if(word.length==1){
			return childMap.values().iterator();
		}else if(childMap.containsKey(word[1])){	//子が存在する場合
			Node childNode=childMap.get(word[1]);
			key=key.replace(getName()+Modifier.DIVIDE_STR,"");
			return childNode.iterator(key);
		}
		
		return null;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getValue(String key){
		String[] word=key.split(Modifier.DIVIDE_STR);
		if(!word[0].equals(getName()))return null;
		if(word.length==1){	//リーフの場合
			return value;
		}else if(childMap.containsKey(word[1])){	//子が存在する場合
			Node childNode=childMap.get(word[1]);
			key=key.replace(getName()+Modifier.DIVIDE_STR,"");
			return childNode.getValue(key);
		}
		
		return null;
	}

	public Node get(String key){
		String[] word=key.split(Modifier.DIVIDE_STR);
		if(!word[0].equals(getName()))return null;
		if(word.length==1){	//リーフの場合
			return this;
		}else if(childMap.containsKey(word[1])){	//子が存在する場合
			Node childNode=childMap.get(word[1]);
			key=key.replace(getName()+Modifier.DIVIDE_STR,"");
			return childNode.get(key);
		}
		
		return null;
	}

	public void put(String key,String valueStr){
		//System.out.println("debug:"+key+","+valueStr);
		String[] word=key.split(Modifier.DIVIDE_STR);
		if(!word[0].equals(getName()))return;
		if(word.length==1){	//リーフの場合
			value=valueStr;
		}else if(childMap.containsKey(word[1])){	//既に子が存在する場合
			Node childNode=childMap.get(word[1]);
			key=key.replace(getName()+Modifier.DIVIDE_STR,"");
			childNode.put(key,valueStr);
		}else{	//子がまだいない場合
			Node childNode=new Node(word[1]);
			childMap.put(childNode.getName(),childNode);
			key=key.replace(getName()+Modifier.DIVIDE_STR,"");
			childNode.put(key,valueStr);
		}
	}
	
	public void remove(String key){
		String[] word=key.split(Modifier.DIVIDE_STR);
		if(!word[0].equals(getName()))return;
		if(word.length==2){	//削除対象を子に持つ時
			if(!childMap.containsKey(word[1]))return;
			childMap.remove(word[1]);
		}else if(word.length>2){
			if(!childMap.containsKey(word[1]))return;
			Node childNode=childMap.get(word[1]);
			key=key.replace(getName()+Modifier.DIVIDE_STR,"");
			childNode.remove(key);
		}
	}
	
	public void addChild(Node childNode){
		childMap.put(childNode.getName(),childNode);
	}
	
	public String getTreeStr(String path){
		String returnStr=null;
		String newPath=null;
		if(!getName().equals(Modifier.ROOT_NAME)){
			newPath=getName();
			if(path!=null)newPath=path+"\t"+newPath;
		}
		if(value!=null)returnStr=newPath+"\t:::"+value+"\n";
		for(String key:childMap.keySet()){
			Node curChild=childMap.get(key);
			String tmpStr=curChild.getTreeStr(newPath);
			if(tmpStr==null)continue;
			if(returnStr==null)returnStr=tmpStr;
			else returnStr+=tmpStr;
		}
		
		return returnStr;
	}
	
	public String getChar3ConfigStr(int level){
		String returnStr=null;
		String tabStr=getTabStr(level);
		if(childMap.size()>0){
			String tmpStr="_|CHARUTXT-FLD|"+getName()+"|_\n";
			if(tabStr!=null)tmpStr=tabStr+tmpStr;
			returnStr=tmpStr;
			for(String key:childMap.keySet()){
				Node childNode=childMap.get(key);
				returnStr+=childNode.getChar3ConfigStr(level+1);
			}
			tmpStr="_|CHARUTXT-FED|_\n";
			if(tabStr!=null)tmpStr=tabStr+tmpStr;
			returnStr+=tmpStr;
		}else{
			String tmpStr="_|CHARUTXT-DAT|"+getName()+"|_\n";
			if(tabStr!=null)tmpStr=tabStr+tmpStr;
			returnStr=tmpStr;
			returnStr+=getValue()+"\n";
			tmpStr="_|CHARUTXT-DED|_\n";
			if(tabStr!=null)tmpStr=tabStr+tmpStr;
			returnStr+=tmpStr;
		}
		
		return returnStr;
	}
	
	private String getTabStr(int level){
		String returnStr=null;
		for(int i=0;i<level;i++){
			if(i==0)returnStr="\t";
			else returnStr+="\t";
		}
		
		return returnStr;
	}
}
