import java.io.*;
import java.util.*;

public class IPLoader{
	private IPManager ipm;
	private LinkedList<File> configList;
	
	public IPLoader(IPManager ipm){
		this.ipm=ipm;
	}
	
	public void loadRecursive(String srcDir){
		configList=new LinkedList<File>();
		File rootDir=new File(srcDir);
		recursiveCheck(rootDir);
		for(File curFile:configList){
			loadIP(curFile);
			break;
		}
	}

	public void recursiveCheck(File curDir){
		File[] childList=curDir.listFiles();
		for(File curFile:childList){
			if(curFile.isDirectory())recursiveCheck(curFile);
			else if(curFile.isFile())configList.add(curFile);
		}
	}
	
	private void loadIP(File curFile){
		try {
			BufferedReader br = new BufferedReader(new FileReader(curFile));
			String line=null;
			String hostname=null;
			String i_f=null;
			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				if(line.matches("hostname .*")){
					hostname=line;
					hostname=hostname.replace("hostname ","");
				}
				if(hostname==null)continue;
				if(line.matches("interface .*")){
					i_f=line;
					i_f=i_f.replace("interface ","");
					if(i_f.matches("FastEthernet.*"))i_f=i_f.replace("FastEthernet","Fa");
					if(i_f.matches("GigabitEthernet.*"))i_f=i_f.replace("GigabitEthernet","Gi");
				}
				if(i_f==null)continue;
				if(line.matches(" ip address .*")){
					String ip=line;
					ip=ip.replace(" ip address ","");
					String[] word=ip.split(" ");
					String address=word[0];
					String mask=word[1];
					String explain=address+"/"+Address.getMaskLength(mask)+"\n";
					explain+=hostname+" "+i_f+"\n";
					ipm.addIP(address,explain);
				}
				/*
				if(line.matches(" standby \\d+ ip .*")){
					String vip=line;
					String[] word=vip.split(" ");
					//System.out.println(word[2]);
					//System.out.println(word[4]);
					ipm.put(DBKEY+"_"+hostname+"_interface_"+i_f+"_vip_"+word[4]+"_address",word[4]);
					ipm.put(DBKEY+"_"+hostname+"_interface_"+i_f+"_vip_"+word[4]+"_hsrpGrp",word[2]);
					
				}
				*/
			}
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}