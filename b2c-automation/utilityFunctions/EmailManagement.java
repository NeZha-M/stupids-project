package utilityFunctions;

import java.util.Vector;

import org.jsoup.Jsoup;

import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;

public class EmailManagement {
	public static void main(String argv[]) throws Throwable {
		
	}
	public static String GetPassword(String bpNumber) throws Throwable{
		String password = "";
		PSTFile pstFile = new PSTFile("C:\\Users\\ui171010\\AppData\\Local\\Microsoft\\Outlook\\santhoshkumar.chelumpalli@essent.nl.ost");
		PSTFolder pstFolder = pstFile.getRootFolder();
		Vector<PSTFolder> folder = new Vector<PSTFolder>();
		PSTFolder inboxFolder = null;
		boolean inboxFound = false;
		folder = pstFolder.getSubFolders();
		for(int i=0;i<pstFolder.getSubFolderCount();i++)
		{
			Vector<PSTFolder> personalFolder = new Vector<PSTFolder>();
			personalFolder = folder.get(i).getSubFolders();
			for(int j=0;j<personalFolder.size();j++)
			{
				for (PSTFolder pstFolder2 : personalFolder) {
					Vector<PSTFolder> folder3 = new Vector<PSTFolder>();
					folder3=pstFolder2.getSubFolders();
					for(int x=0;x<folder3.size();x++){
						if(folder3.get(x).getDisplayName().equals("Others")){
							inboxFolder = folder3.get(x);
							break;
						}
					}
					if(inboxFound){
						break;
					}
				}
			}
			if(inboxFound){
				break;
			}
		}
		for(int mailNum = 0;mailNum<100;mailNum++){
			PSTMessage emailItem = (PSTMessage) inboxFolder.getNextChild();
			if(emailItem==null){
				break;
			}
			String emailBody = emailItem.getBodyHTML();
			emailBody = Jsoup.parse(emailBody).text();
			if(emailBody.contains(bpNumber) && emailBody.contains("F231_1")){
				String[] emailBodyStrings = emailBody.split(" ");
				for(int emailString=0;emailString<emailBodyStrings.length-1;emailString++){
					if(emailBodyStrings[emailString].equals("Uw") && emailBodyStrings[emailString+1].equals("wachtwoord") && emailBodyStrings[emailString+2].equals("is:")){
						password = emailBodyStrings[emailString+3];
					}
				}
			}
			
		}
		if(password.equals("")){
			System.out.println("No New Password Email is triggered");
		}
		else{
			System.out.println("New Password : "+password);
		}
		return password;
	}
	public static String GetUserName(String bpNumber) throws Throwable{
		String userName = "";
		PSTFile pstFile = new PSTFile("C:\\Users\\ui171010\\AppData\\Local\\Microsoft\\Outlook\\santhoshkumar.chelumpalli@essent.nl.ost");
		PSTFolder pstFolder = pstFile.getRootFolder();
		Vector<PSTFolder> folder = new Vector<PSTFolder>();
		PSTFolder inboxFolder = null;
		boolean inboxFound = false;
		folder = pstFolder.getSubFolders();
		for(int i=0;i<pstFolder.getSubFolderCount();i++)
		{
			Vector<PSTFolder> personalFolder = new Vector<PSTFolder>();
			personalFolder = folder.get(i).getSubFolders();
			for(int j=0;j<personalFolder.size();j++)
			{
				for (PSTFolder pstFolder2 : personalFolder) {
					Vector<PSTFolder> folder3 = new Vector<PSTFolder>();
					folder3=pstFolder2.getSubFolders();
					for(int x=0;x<folder3.size();x++){
						if(folder3.get(x).getDisplayName().equals("Others")){
							inboxFolder = folder3.get(x);
							inboxFound = true;
							break;
						}
					}
					if(inboxFound){
						break;
					}
				}
			}
			if(inboxFound){
				break;
			}
		}
		for(int mailNum = 0;mailNum<100;mailNum++){
			PSTMessage emailItem = (PSTMessage) inboxFolder.getNextChild();
			if(emailItem==null){
				break;
			}
			String emailBody = emailItem.getBodyHTML();
			emailBody = Jsoup.parse(emailBody).text();
			if(emailBody.contains(bpNumber) && emailBody.contains("F232_1")){
				String[] emailBodyStrings = emailBody.split(" ");
				for(int emailString=0;emailString<emailBodyStrings.length-1;emailString++){
					if(emailBodyStrings[emailString+1].equals("gebruikersnaam") && emailBodyStrings[emailString+2].equals("is:")){
						userName = emailBodyStrings[emailString+3];
					}
				}
			}
			
		}
		System.out.println("Done with Email Manager");
		return userName;
	}
}
