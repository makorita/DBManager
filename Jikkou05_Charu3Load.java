import java.io.*;
import java.util.*;

public class Jikkou05_Charu3Load{
	public static void main(String args[]){
		String charu3ConfigPath=args[0];
		Charu3ConfigModifier c3m=new Charu3ConfigModifier(charu3ConfigPath);
		c3m.loadDB();
		//System.out.println(c3m.getTreeStr());
		//System.out.println(c3m.getChar3ConfigStr());
		c3m.writeCharu3Config();
	}
}
