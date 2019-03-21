import java.io.*;
import java.util.*;
import java.net.*;

public class Modifier extends DBManager{
	private LinkedList<String> targetList;
	
	public Modifier(){	
	}
	
	public LinkedList<String> getTargetList(){
		return targetList;
	}
	
	public void setTargetList(LinkedList<String> targetList){
		this.targetList=targetList;
	}
}