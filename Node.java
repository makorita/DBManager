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
		String[] word=key.split(DBManager.DIVIDE_STR);
		if(!word[0].equals(getName()))return;
		if(word.length==1){
			childMap.clear();
		}else if(childMap.containsKey(word[1])){	//子が存在する場合
			Node childNode=childMap.get(word[1]);
			key=key.replace(getName()+DBManager.DIVIDE_STR,"");
			childNode.clear(key);
		}
	}

	public boolean existsKey(String key){
		String[] word=key.split(DBManager.DIVIDE_STR);
		if(!word[0].equals(getName()))return false;
		if(word.length==1) return true;
		else if(word.length==2){	//子に持つ時
			if(childMap.containsKey(word[1]))return true;
			else return false;
		}else if(word.length>2){
			if(!childMap.containsKey(word[1]))return false;
			Node childNode=childMap.get(word[1]);
			key=key.replace(getName()+DBManager.DIVIDE_STR,"");
			return childNode.existsKey(key);
		}
		
		return false;
	}
	
	public Iterator<Node> iterator(String key){
		String[] word=key.split(DBManager.DIVIDE_STR);
		if(!word[0].equals(getName()))return null;
		if(word.length==1){
			return childMap.values().iterator();
		}else if(childMap.containsKey(word[1])){	//子が存在する場合
			Node childNode=childMap.get(word[1]);
			key=key.replace(getName()+DBManager.DIVIDE_STR,"");
			return childNode.iterator(key);
		}
		
		return null;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getValue(String key){
		String[] word=key.split(DBManager.DIVIDE_STR);
		if(!word[0].equals(getName()))return null;
		if(word.length==1){	//リーフの場合
			return value;
		}else if(childMap.containsKey(word[1])){	//子が存在する場合
			Node childNode=childMap.get(word[1]);
			key=key.replace(getName()+DBManager.DIVIDE_STR,"");
			return childNode.getValue(key);
		}
		
		return null;
	}

	public Node get(String key){
		String[] word=key.split(DBManager.DIVIDE_STR);
		if(!word[0].equals(getName()))return null;
		if(word.length==1){	//リーフの場合
			return this;
		}else if(childMap.containsKey(word[1])){	//子が存在する場合
			Node childNode=childMap.get(word[1]);
			key=key.replace(getName()+DBManager.DIVIDE_STR,"");
			return childNode.get(key);
		}
		
		return null;
	}

	public void put(String key,String valueStr){
		//System.out.println("debug:"+key+","+valueStr);
		String[] word=key.split(DBManager.DIVIDE_STR);
		if(!word[0].equals(getName()))return;
		if(word.length==1){	//リーフの場合
			value=valueStr;
		}else if(childMap.containsKey(word[1])){	//既に子が存在する場合
			Node childNode=childMap.get(word[1]);
			key=key.replace(getName()+DBManager.DIVIDE_STR,"");
			childNode.put(key,valueStr);
		}else{	//子がまだいない場合
			Node childNode=new Node(word[1]);
			childMap.put(childNode.getName(),childNode);
			key=key.replace(getName()+DBManager.DIVIDE_STR,"");
			childNode.put(key,valueStr);
		}
	}
	
	public void remove(String key){
		String[] word=key.split(DBManager.DIVIDE_STR);
		if(!word[0].equals(getName()))return;
		if(word.length==2){	//削除対象を子に持つ時
			if(!childMap.containsKey(word[1]))return;
			childMap.remove(word[1]);
		}else if(word.length>2){
			if(!childMap.containsKey(word[1]))return;
			Node childNode=childMap.get(word[1]);
			key=key.replace(getName()+DBManager.DIVIDE_STR,"");
			childNode.remove(key);
		}
	}
	
	public void addChild(Node childNode){
		childMap.put(childNode.getName(),childNode);
	}
	
	public String getTreeStr(String path){
		String returnStr=null;
		String newPath=null;
		if(!getName().equals(DBManager.ROOT_NAME)){
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
}
