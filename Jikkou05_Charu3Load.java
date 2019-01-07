import java.io.*;
import java.util.*;

public class Jikkou05_Charu3Load{
	public static void main(String args[]){
		Charu3ConfigModifier c3m=new Charu3ConfigModifier();
		c3m.loadDB();
		//System.out.println(c3m.getTreeStr());
		//System.out.println(c3m.getChar3ConfigStr());
		c3m.writeCharu3Config(null);
	}
}
