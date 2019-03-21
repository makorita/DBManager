import java.io.*;
import java.util.*;
import java.net.*;

public class Moldifier extends DBManager{
	private LinkedList<String> targetList;
	
	public Moldifier(){	
	}
	
	public LinkedList<String> getTargetList(){
		return targetList;
	}
	
	public void setTargetList(LinkedList<String> targetList){
		this.targetList=targetList;
	}
}