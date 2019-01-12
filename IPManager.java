import java.io.*;
import java.util.*;
import java.util.regex.*;

public class IPManager extends Modifier{
	public IPManager(){
		setDbPath("ƒAƒhƒŒƒXˆê——");
	}

	public void doAfterLoad(){
	}
	
	public void addIP(String ip,String explain){
		put(getDbPath()+Modifier.DIVIDE_STR+ip+Modifier.DIVIDE_STR+"ip",ip);
		put(getDbPath()+Modifier.DIVIDE_STR+ip+Modifier.DIVIDE_STR+"explain",explain);
	}
}