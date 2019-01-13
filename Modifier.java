import java.io.*;
import java.util.*;
import java.net.*;

public abstract class Modifier{
	public static final String DIVIDE_STR="_";
	private static String DB_DIR;
	private String dbPath;
	private static final String DEFAULT_DB="DefaultDB";
	public static final String ROOT_NAME="rootNode";

	private LinkedList<String> targetList;	//成形対象
	private Node rootNode;	//パラメータ
	
	public Modifier(){	
		rootNode=new Node(ROOT_NAME);
		DB_DIR=getSelfPath()+"output/";
		//System.out.println(DB_DIR);
	}

	public String getDbPath(){
		return dbPath;
	}
	
	public void setDbPath(String dbPath){
		this.dbPath=dbPath;
	}

	public LinkedList<String> getTargetList(){
		return targetList;
	}
	
	public void setTargetList(LinkedList<String> targetList){
		this.targetList=targetList;
	}

	public void clear(String key){
		key=ROOT_NAME+DIVIDE_STR+key;
		rootNode.clear(key);
	}
	
	public boolean existsKey(String key){
		key=ROOT_NAME+DIVIDE_STR+key;
		return rootNode.existsKey(key);
	}
	
	public String getValue(String key){
		key=ROOT_NAME+DIVIDE_STR+key;
		return rootNode.getValue(key);
	}
	
	public void put(String key,String value){
		key=ROOT_NAME+DIVIDE_STR+key;
		rootNode.put(key,value);
		System.out.println(key+","+value);
	}
	
	public void remove(String key){
		key=ROOT_NAME+DIVIDE_STR+key;
		rootNode.remove(key);
	}
	
	public Iterator<Node> iterator(String key){
		if(key==null)key=ROOT_NAME;
		else key=ROOT_NAME+DIVIDE_STR+key;
		return rootNode.iterator(key);
	}
	
	public String getTreeStr(){	//引数拡張する時はもう1個メソッド作る
		return rootNode.getTreeStr(null);
	}
	
	public String getChar3ConfigStr(String key){
		if(key==null)key=ROOT_NAME;
		else key=ROOT_NAME+DIVIDE_STR+key;
		return rootNode.get(key).getChar3ConfigStr(0);
	}
	
	public boolean existsDB(String dbName){
		File dbFile=new File(DB_DIR+dbName+".bin");
		return dbFile.exists();
	}
	
	public void loadDB(String dbName){
		if(!existsDB(dbName))return;
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(DB_DIR+dbName+".bin"));
			Node tmpNode=(Node)in.readObject();
			in.close();
			
			Iterator<Node> it=tmpNode.iterator(ROOT_NAME);
			while(it.hasNext()){
				rootNode.addChild(it.next());
			}
			
			doAfterLoad();
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			if(true)System.exit(0);
		}
	}
	
	public void loadDB(){
		loadDB(DEFAULT_DB);
	}
	
	public abstract void doAfterLoad();
	
	public void saveDB(String dbName){
		try{
			//System.out.println("debug:"+DB_DIR);
			ObjectOutputStream wr = new ObjectOutputStream(new FileOutputStream(DB_DIR+dbName+".bin"));
			wr.writeObject(rootNode);
			wr.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			if(true)System.exit(0);
		}
	}
	
	public void saveDB(){
		saveDB(DEFAULT_DB);
	}
	
	private String getSelfPath(){
		String returnStr=this.getClass().getResource("Modifier.class").getPath().replaceAll("\\+", "%2b");
		try{
			returnStr=URLDecoder.decode(returnStr, "UTF-8");
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			if(true)System.exit(0);
		}
		returnStr=returnStr.replaceFirst("/","");
		returnStr=returnStr.replaceFirst("Modifier.class","");
		//System.out.println(returnStr);
		
		return returnStr;
    }
}