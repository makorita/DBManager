import java.io.*;
import java.util.*;

public class Jikkou01_OpenDB{
	public static void main(String args[]) throws IOException{
		String dbName=args[0];
		PrintWriter wr=new PrintWriter(new FileWriter("env.txt"));
		wr.println("dbName:"+dbName);
		System.out.println(dbName+":‚ğƒI[ƒvƒ“‚µ‚Ü‚µ‚½");
		wr.close();
	}
}