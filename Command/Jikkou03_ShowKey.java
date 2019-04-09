import java.io.*;
import java.util.*;

public class Jikkou03_ShowKey{
	public static void main(String args[]) throws IOException,FileNotFoundException{
		String rootPath=null;
		String hasChild=null;
		String notHasChild=null;
		for(int i=0;i<args.length;i++){
			if(args[i].equals("-rp"))rootPath=args[i+1];
			if(args[i].equals("-hc"))hasChild=args[i+1];
			if(args[i].equals("-nhc"))notHasChild=args[i+1];
		}
		//System.out.println(hasChild+","+notHasChild);
		
		BufferedReader br = new BufferedReader(new FileReader("env.txt"));
		String dbName=br.readLine();
		//System.out.println(dbName);
		dbName=dbName.replace("dbName:","");
		br.close();
		
		DBManager dbm=new DBManager();
		dbm.loadDB(dbName);
		Iterator<Node> it=dbm.iterator(rootPath);
		while(it.hasNext()){
			Node childNode=it.next();
			if(hasChild!=null && childNode.existsKey(childNode.getName()+DBManager.DIVIDE_STR+hasChild))System.out.println(childNode.getName());
			else if(notHasChild!=null && !childNode.existsKey(childNode.getName()+DBManager.DIVIDE_STR+notHasChild))System.out.println(childNode.getName());
			else if(hasChild==null && notHasChild==null)System.out.println(childNode.getName());
		}
	}
}