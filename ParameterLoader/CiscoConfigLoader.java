import java.io.*;
import java.util.*;

public class CiscoConfigLoader{
	public static final String DBKEY="ciscoConfig";
	private DBManager dbm;
	private LinkedList<File> configList;
	private boolean detailFlag;
	private boolean ifFlag;
	private boolean routingFlag;
	private boolean aclFlag;
	private boolean serialFlag;
	
	public CiscoConfigLoader(DBManager dbm){
		this.dbm=dbm;
		configList=new LinkedList<File>();
		detailFlag=true;
		ifFlag=true;
		routingFlag=true;
		aclFlag=true;
		serialFlag=true;
	}
	
	public void loadRecursive(String srcDir){
		File rootDir=new File(srcDir);
		recursiveCheck(rootDir);
		for(File curFile:configList){
			System.out.println(curFile.getName());
			checkConfig(curFile);
			//break;
		}

	}
	
	public void setDetailFlag(boolean detailFlag){
		this.detailFlag=detailFlag;
	}
	
	public void setIfFlag(boolean ifFlag){
		this.ifFlag=ifFlag;
	}
	
	public void setRoutingFlag(boolean routingFlag){
		this.routingFlag=routingFlag;
	}
	
	public void setAclFlag(boolean aclFlag){
		this.aclFlag=aclFlag;
	}
	
	public void setSerialFlag(boolean serialFlag){
		this.serialFlag=serialFlag;
	}
	
	private void checkConfig(File curFile){
		try {
			BufferedReader br = new BufferedReader(new FileReader(curFile));
			String line=null;
			String hostname=null;
			while ((line = br.readLine()) != null) {	//hostname�̎擾
				//System.out.println(line);
				if(line.matches("hostname .*")){
					hostname=line;
					hostname=hostname.replace("hostname ","");
					break;
				}
			}
			
			br = new BufferedReader(new FileReader(curFile));
			String i_f=null;
			TreeMap<String,LinkedList<String>> aclMap=null;
			boolean aclMode=false;
			String stackName=null;
			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				if(ifFlag){
					if(line.matches("interface .*")){
						i_f=line;
						i_f=i_f.replace("interface ","");
						String shortName=null;
						if(i_f.matches("FastEthernet.*"))shortName=i_f.replace("FastEthernet","Fa");
						if(i_f.matches("GigabitEthernet.*"))shortName=i_f.replace("GigabitEthernet","Gi");
						dbm.put(DBKEY+"_"+hostname+"_interface_"+i_f+"_name",i_f);
						dbm.put(DBKEY+"_"+hostname+"_interface_"+i_f+"_name",i_f);
						if(shortName!=null)dbm.put(DBKEY+"_"+hostname+"_interface_"+i_f+"_shortName",shortName);
					}
					if(i_f==null)continue;
					if(line.matches(" ip address .*") && !line.equals(" ip address negotiated")){
						//System.out.println(line);
						String ip=line;
						ip=ip.replace(" ip address ","");
						String[] word=ip.split(" ");
						String address=word[0];
						String mask=word[1];
						dbm.put(DBKEY+"_"+hostname+"_interface_"+i_f+"_ip_address",address);
						dbm.put(DBKEY+"_"+hostname+"_interface_"+i_f+"_ip_mask",mask);
					}
					if(line.matches(" standby \\d+ ip .*")){
						String vip=line;
						String[] word=vip.split(" ");
						//System.out.println(word[2]);
						//System.out.println(word[4]);
						dbm.put(DBKEY+"_"+hostname+"_interface_"+i_f+"_vip_"+word[4]+"_address",word[4]);
						dbm.put(DBKEY+"_"+hostname+"_interface_"+i_f+"_vip_"+word[4]+"_hsrpGrp",word[2]);
						
					}
				}
				if(routingFlag){
					if(line.matches("ip route .*")){
						dbm.put(DBKEY+"_"+hostname+"_routing_staticRoute_"+line,line);
					}
				}
				if(aclFlag){
					if(!aclMode && line.matches("access-list .*")){
						aclMap=new TreeMap<String,LinkedList<String>>();
						aclMode=true;
						String[] word=line.split(" ");
						LinkedList<String> aclList=new LinkedList<String>();
						aclList.add(line);
						aclMap.put(word[1],aclList);
					}else if(aclMode && line.matches("access-list .*")){
						String[] word=line.split(" ");
						if(aclMap.containsKey(word[1])){
							LinkedList<String> aclList=aclMap.get(word[1]);
							aclList.add(line);
						}else{
							LinkedList<String> aclList=new LinkedList<String>();
							aclList.add(line);
							aclMap.put(word[1],aclList);
						}
					}else if(aclMode){
						for (String key : aclMap.keySet()) {
							LinkedList<String> curList=aclMap.get(key);
							String valueStr=null;
							for(String curStr:curList){
								if(valueStr==null)valueStr=curStr+"\n";
								else valueStr+=curStr+"\n";
							}
							dbm.put(DBKEY+"_"+hostname+"_acl_acl"+key,valueStr);
						}
					}
				}
				if(serialFlag){
					if(line.matches("System serial number.*")){
						String[] word=line.split(": ");
						if(stackName==null)dbm.put(DBKEY+"_"+hostname+"_System_SerialNumber",word[1]);
						else dbm.put(DBKEY+"_"+hostname+"_System#"+stackName+"_SerialNumber",word[1]);
						stackName=null;
					}else if(line.matches("Motherboard serial number.*")){
						String[] word=line.split(": ");
						if(stackName==null)dbm.put(DBKEY+"_"+hostname+"_Motherboard_SerialNumber",word[1]);
						else dbm.put(DBKEY+"_"+hostname+"_Motherboard#"+stackName+"_SerialNumber",word[1]);
					}else if(line.matches("Daughterboard serial number.*")){
						String[] word=line.split(": ");
						if(stackName==null)dbm.put(DBKEY+"_"+hostname+"_Daughterboard_SerialNumber",word[1]);
						else dbm.put(DBKEY+"_"+hostname+"_Daughterboard#"+stackName+"_SerialNumber",word[1]);
					}else if(line.matches("Switch \\d+")){
						stackName=line;
						stackName=stackName.replace(" ","");
					}else if(line.matches("Processor board ID .*")){
						String sn=line;
						sn=sn.replace("Processor board ID ","");
						dbm.put(DBKEY+"_"+hostname+"_System_SerialNumber",sn);
					}else if(line.matches("Serial Number: .*")){
						String sn=line;
						sn=sn.replace("Serial Number: ","");
						dbm.put(DBKEY+"_"+hostname+"_System_SerialNumber",sn);
					}
				}
			}
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void recursiveCheck(File curDir){
		File[] childList=curDir.listFiles();
		for(File curFile:childList){
			if(curFile.isDirectory())recursiveCheck(curFile);
			else if(curFile.isFile())configList.add(curFile);
		}
	}
}