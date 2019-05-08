import java.io.*;
import java.util.*;

public class Jikkou02_DBOutputTemplate{
	public static void main(String args[]) throws IOException{
		PrintWriter wr=new PrintWriter(new FileWriter("Œ‹‰Ê.csv"));
		DBManager dbm=new DBManager();
		dbm.loadDB("CiscoConfig");
		Iterator<Node> it=dbm.iterator("ciscoConfig");
		while(it.hasNext()){
			Node curNode=it.next();
			//System.out.println(curNode.getName());
			if(curNode.existsKey(curNode.getName()+"_System_SerialNumber")){
				System.out.println(curNode.getName()+"_System_SerialNumber,"+curNode.getValue(curNode.getName()+"_System_SerialNumber"));
				wr.println(curNode.getName()+"_System_SerialNumber,"+curNode.getValue(curNode.getName()+"_System_SerialNumber"));
			}
		}
		wr.close();
	}
}
