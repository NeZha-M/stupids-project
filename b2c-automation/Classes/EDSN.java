package Classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import utilityFunctions.CONSTANTS;
import utilityFunctions.ExcelTestDataManager;
import ComponentFns.CF_Parent;
import ComponentFns.CF_Parent.Platform;
import ComponentFns.CF_Parent.PropertyType;
import ComponentFns.ExecutionManager;

import com.rational.test.ft.object.interfaces.GuiTestObject;
import com.rational.test.ft.object.interfaces.RootTestObject;
import com.rational.test.ft.object.interfaces.SelectGuiSubitemTestObject;
import com.rational.test.ft.object.interfaces.StatelessGuiSubitemTestObject;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.value.RegularExpression;
import com.rational.test.ft.vp.ITestDataTable;

public class EDSN extends ExecutionManager{
	public boolean Login(LinkedHashMap<String, String> testData,ExecutionManager executionManager){
		executionManager.testReporting.updateStepInfo("Login into EDSN", "User should be able to login into EDSN",executionManager);
		try{
			String edsnUser = "";
			if(executionManager.methodArguments.toLowerCase().contains(CONSTANTS.energieDirect.toLowerCase())){
				if(executionManager.methodArguments.toLowerCase().contains(CONSTANTS.acceptanceEnvironment.toLowerCase())){
					startBrowser(CONSTANTS.edsnEDAcceptanceURL);
					edsnUser ="A_B2C_ENE_Emulator";
				}
				else{
					startBrowser(CONSTANTS.edsnEDTestURL);
					edsnUser = "T_B2C_ENE_Emulator";
				}
			}
			else{
				if(executionManager.methodArguments.toLowerCase().contains(CONSTANTS.acceptanceEnvironment.toLowerCase())){
					startBrowser(CONSTANTS.edsnEssentAcceptanceURL);
					edsnUser ="A_B2C_Emulator";

				}
				else{
					startBrowser(CONSTANTS.edsnEssentTestURL);
					edsnUser ="T_B2C_Emulator";

				}
			}
			CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Name, "user", edsnUser, edsnHtmlDocument());
			CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.password", PropertyType.Name, "password", edsnUser, edsnHtmlDocument());
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.submit", PropertyType.Value, new RegularExpression(".*Login.*", false), edsnHtmlDocument());
			CF_Parent.iLoopCount = 2;
			if(!CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "EAN status", edsnHtmlDocument())){
				if(!CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "EAN status", edsnHtmlDocument())){
					executionManager.takeScreenShot("EDSN login unsuccessful", executionManager);
					executionManager.testReporting.updateActualResults("Login into EDSN failed",CONSTANTS.FAIL,executionManager);
					executionManager.testPassed = false;
					executionManager.stopScript = true;
					return false;
				}
			}
			executionManager.takeScreenShot("EDSN Login successful", executionManager);
			executionManager.testReporting.updateActualResults("User successfully logged in into "+executionManager.methodArguments,CONSTANTS.PASS,executionManager);
		}
		catch(Throwable t){
			executionManager.takeScreenShot("Login into EDSN failed", executionManager);
			executionManager.testReporting.updateActualResults("Login into EDSN failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			System.out.println(t.toString());
			return false;
		}
		return true;
	}
	public boolean RealeaseMessage(LinkedHashMap<String, String> testData,ExecutionManager executionManager){
		executionManager.testReporting.updateStepInfo("Release EDSN Message "+executionManager.methodArguments, "EDSN Message should be released successfully",executionManager);
		try{
			String[] edsnReleaseMessageEAN = ExcelTestDataManager.readExcelColumn(CONSTANTS.testDataLoc, testData.get("testCaseName"), "EANNumbers");
			String[] EANsToReleaseMessageFromEDSN = edsnReleaseMessageEAN[0].split(";");
			if(EANsToReleaseMessageFromEDSN.length==0){
				executionManager.testReporting.updateActualResults("No EANs available for Release in Excel sheet",CONSTANTS.PASS,executionManager);
			}
			else{
				for (String EANToReleaseFromEDSN : EANsToReleaseMessageFromEDSN) {
					SearchEANNumber(EANToReleaseFromEDSN);
					int messageIndex = -1;
					if(executionManager.methodArguments.contains("VTK")){
						messageIndex = GetEDSNMessageIndex(executionManager.methodArguments.split(";"), "Release","VTK");
					}
					else if(executionManager.methodArguments.contains("EVM")){
						messageIndex = GetEDSNMessageIndex(executionManager.methodArguments.split(";"), "Release","EVM");
					}
					else if(executionManager.methodArguments.contains("TPM")){
						messageIndex = GetEDSNMessageIndex(executionManager.methodArguments.split(";"), "Release","TPM");
					}
					else{
						messageIndex = GetEDSNMessageIndex(executionManager.methodArguments.split(";"), "Release","");
					}
					if(messageIndex>=0){
						TestObject[] htmlDivisons = edsnHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.DIV",CF_Parent.getPropertyType(PropertyType.Id,Platform.WEB),new RegularExpression(".*divContentId.*", false)));
						TestObject[] eanSearchResultsTable = htmlDivisons[0].find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.TABLE"));
						TestObject editAndReleaseMessageTableCell = (TestObject)((StatelessGuiSubitemTestObject)eanSearchResultsTable[0]).getSubitem(atCell(atRow(messageIndex),atColumn(2)));
						CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.IMG", PropertyType.Source, new RegularExpression(".*process.*", false), editAndReleaseMessageTableCell);
						executionManager.takeScreenShot("EDSN Message for "+EANToReleaseFromEDSN+" Released successfully", executionManager);
						executionManager.testReporting.updateActualResults("EDSN Message for "+EANToReleaseFromEDSN+" Released successfully",CONSTANTS.PASS,executionManager);
					}
					else{
						executionManager.takeScreenShot("EDSN Message release failed", executionManager);
						executionManager.testReporting.updateActualResults("Rleasing EDSN Message"+executionManager.methodArguments+" failed",CONSTANTS.FAIL,executionManager);
					}
				}
			}
		}
		catch(Throwable t){
			executionManager.takeScreenShot("EDSN Message release failed", executionManager);
			executionManager.testReporting.updateActualResults("Rleasing EDSN Message"+executionManager.methodArguments+" failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			System.out.println(t.toString());
			return false;
		}
		return true;
	}
	public boolean EditMessage(LinkedHashMap<String, String> testData,ExecutionManager executionManager){
		executionManager.testReporting.updateStepInfo("Edit EDSN Message "+executionManager.methodArguments, "EDSN Message should be edited successfully",executionManager);
		try{
			String[] edsnReleaseMessageEAN = ExcelTestDataManager.readExcelColumn(CONSTANTS.testDataLoc, testData.get("testCaseName"), "EANNumbers");
			if(edsnReleaseMessageEAN==null || edsnReleaseMessageEAN.length==0){
				executionManager.testReporting.updateActualResults("No EANs available for Release in Excel sheet",CONSTANTS.PASS,executionManager);
			}
			else{
				String[] EANsToReleaseMessageFromEDSN = edsnReleaseMessageEAN[0].split(";");
				for (String EANToReleaseFromEDSN : EANsToReleaseMessageFromEDSN) {
					SearchEANNumber(EANToReleaseFromEDSN);
					int messageIndex = -1;
					if(executionManager.methodArguments.contains("VTK")){
						messageIndex = GetEDSNMessageIndex(executionManager.methodArguments.split(";"), "Release","VTK");
					}
					else if(executionManager.methodArguments.contains("EVM")){
						messageIndex = GetEDSNMessageIndex(executionManager.methodArguments.split(";"), "Release","EVM");
					}
					else if(executionManager.methodArguments.contains("TPM")){
						messageIndex = GetEDSNMessageIndex(executionManager.methodArguments.split(";"), "Release","TPM");
					}
					else{
						messageIndex = GetEDSNMessageIndex(executionManager.methodArguments.split(";"), "Release","");
					}					System.out.println("Message Index : "+messageIndex);
					if(messageIndex>=0){
						TestObject[] htmlDivisons = edsnHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.DIV",CF_Parent.getPropertyType(PropertyType.Id,Platform.WEB),new RegularExpression(".*divContentId.*", false)));
						TestObject[] eanSearchResultsTable = htmlDivisons[0].find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.TABLE"));
						TestObject editAndReleaseMessageTableCell = (TestObject)((StatelessGuiSubitemTestObject)eanSearchResultsTable[0]).getSubitem(atCell(atRow(messageIndex),atColumn(2)));
						CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.IMG", PropertyType.Source, new RegularExpression(".*edit.*", false), editAndReleaseMessageTableCell);
						executionManager.takeScreenShot("EDSN Message for "+EANToReleaseFromEDSN+" Edited successfully", executionManager);
						executionManager.testReporting.updateActualResults("EDSN Message for "+EANToReleaseFromEDSN+" Edited successfully",CONSTANTS.PASS,executionManager);
					}
					else{
						executionManager.takeScreenShot("EDSN Message edit failed", executionManager);
						executionManager.testReporting.updateActualResults("Editing EDSN Message"+executionManager.methodArguments+" failed",CONSTANTS.FAIL,executionManager);
					}
				}
			}
		}
		catch(Throwable t){
			executionManager.takeScreenShot("EDSN Message edit failed", executionManager);
			executionManager.testReporting.updateActualResults("Editing EDSN Message"+executionManager.methodArguments+" failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			System.out.println(t.toString());
			return false;
		}
		return true;
	}
	public int GetEDSNMessageIndex(String[] messageArguments,String messageAction,String fromWhichEnvironment) throws Throwable{
		// -1 : Message Doesn't exist   // -2 : More than one entry for message
		TestObject[] htmlDivisons = edsnHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.DIV",CF_Parent.getPropertyType(PropertyType.Id,Platform.WEB),new RegularExpression(".*divContentId.*", false)));
		if(htmlDivisons.length==0){
			int searchResultWaitCount = 0;
			while(searchResultWaitCount!=10){
				htmlDivisons = edsnHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.DIV",CF_Parent.getPropertyType(PropertyType.Id,Platform.WEB),new RegularExpression(".*divContentId.*", false)));
				searchResultWaitCount++;
				if(htmlDivisons.length>0){
					break;
				}
			}
			if(searchResultWaitCount==10){
				return -1;
			}
		}
		TestObject[] eanSearchResultsTable = htmlDivisons[0].find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.TABLE"));	
		ITestDataTable eanSearchResultsData = (ITestDataTable)eanSearchResultsTable[0].getTestData("grid");
		int numberOfRows = eanSearchResultsData.getRowCount();
		int numberOfColumns = eanSearchResultsData.getColumnCount();
		System.out.println("Rows : "+numberOfRows+" Cols : "+numberOfColumns);
		String[][] messageDetails = new String[numberOfRows-1][numberOfColumns];
		//(Index Information) 0: In/Out 1: Message Description 2: Message Release Button 3: From Supplier 4: To Supplier 5: Message Info
		for(int rowNumber=0;rowNumber<numberOfRows-1;rowNumber++){
			for(int columnNumber=0;columnNumber<numberOfColumns-1;columnNumber++){
				if(columnNumber == 2){
					TestObject editAndReleaseMessageTableCell = (TestObject)((StatelessGuiSubitemTestObject)eanSearchResultsTable[0]).getSubitem(atCell(atRow(rowNumber),atColumn(columnNumber)));
					TestObject[] editAndReleaseMessages = editAndReleaseMessageTableCell.find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.IMG"),false);
					if(editAndReleaseMessages.length==0){
						messageDetails[rowNumber][columnNumber] = "No;No;No";
					}
					else{
						String infoMessage = "No";String editMessage = "No";String releaseMessage = "No";
						for (TestObject editRelease : editAndReleaseMessages) {
							if(editRelease.getProperty(".src").toString().contains("process.gif")){
								releaseMessage = "Yes";
							}
							if(editRelease.getProperty(".src").toString().contains("edit.gif")){
								editMessage = "Yes";
							}
							if(editRelease.getProperty(".src").toString().contains("infoSmall.gif")){
								infoMessage = "Yes";
							}
						}
						messageDetails[rowNumber][columnNumber] = infoMessage+";"+editMessage+";"+releaseMessage;
					}
					continue;
				}
				messageDetails[rowNumber][columnNumber] = eanSearchResultsData.getCell(rowNumber, columnNumber).toString();
				if(columnNumber == 5){
					messageDetails[rowNumber][columnNumber+1] = messageDetails[rowNumber][columnNumber].substring(0, 8);
				}
			}
		}
		ArrayList<Integer> messageIndexes = new ArrayList<Integer>();
		if(messageArguments[1].toLowerCase().equals("latest")){
			for(int messageDetail=0;messageDetail<messageDetails.length;messageDetail++){
				if(messageDetails[messageDetail][1].toLowerCase().equals(messageArguments[0].toLowerCase())){
					String[] messageEditReleaseDetails = messageDetails[messageDetail][2].split(";");
					if(messageAction.equals("Release")){
						if(messageEditReleaseDetails[2].equals("Yes")){
							messageIndexes.add(messageDetail);
						}
					}
					else if(messageAction.equals("Edit")){
						if(messageEditReleaseDetails[1].equals("Yes")){	
							messageIndexes.add(messageDetail);
						}
					}
				}
			}
			if(messageIndexes.size()==0){
				return -1;
			}
			else if(messageIndexes.size()==1){
				return messageIndexes.get(0);
			}
			else{
				System.out.println("More than one entry for message");
				int latestMessageIndex = messageIndexes.get(0);
				String latestMessageDate = messageDetails[messageIndexes.get(0)][numberOfColumns-1];
				SimpleDateFormat edsnMessageDateFormat = new SimpleDateFormat("dd-MM-yy");
				boolean firstMessageVerified = false;
				for (Integer messageIndex : messageIndexes) {
					if(!firstMessageVerified || edsnMessageDateFormat.parse(messageDetails[messageIndex][numberOfColumns-1]).after(edsnMessageDateFormat.parse(latestMessageDate))){
						if(fromWhichEnvironment.equals("")){
							latestMessageDate = messageDetails[messageIndex][numberOfColumns-1];
							latestMessageIndex = messageIndex;
						}
						else{
							TestObject edsnMessageTableCell = (TestObject)((StatelessGuiSubitemTestObject)eanSearchResultsTable[0]).getSubitem(atCell(atRow(messageIndex),atColumn(1)));
							TestObject[] edsnMessage = edsnMessageTableCell.find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.A"),false);
							((GuiTestObject)edsnMessage[0]).click(atPoint(10, 10));
							Thread.sleep(3000);
							htmlDivisons = edsnHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.IFRAME",CF_Parent.getPropertyType(PropertyType.Id,Platform.WEB),new RegularExpression(".*idBericht.*", false)));
							TestObject[] messageInformationTable = htmlDivisons[0].find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.TABLE"));
							String messageInformation = messageInformationTable[0].getProperty(".text").toString();
							if(messageInformation.contains(fromWhichEnvironment)){
								latestMessageDate = messageDetails[messageIndex][numberOfColumns-1];
								latestMessageIndex = messageIndex;
								firstMessageVerified = true;
							}
						}
					}
				}
				return latestMessageIndex;
			}
		}
		if(messageArguments[1].toLowerCase().equals("date")){
			for(int messageDetail=0;messageDetail<messageDetails.length;messageDetail++){
				if(messageDetails[messageDetail][1].toLowerCase().equals(messageArguments[0].toLowerCase())){
					String[] messageEditReleaseDetails = messageDetails[messageDetail][2].split(";");
					if(messageAction.equals("Release")){
						if(messageEditReleaseDetails[2].equals("Yes") && messageDetails[messageDetail][numberOfColumns-1].equals(messageArguments[2])){
							messageIndexes.add(messageDetail);
						}
					}
					else if(messageAction.equals("Edit")){
						if(messageEditReleaseDetails[1].equals("Yes") && messageDetails[messageDetail][numberOfColumns-1].equals(messageArguments[2])){
							messageIndexes.add(messageDetail);
						}
					}
				}
			}
			if(messageIndexes.size()==0){
				return -1;
			}
			else if(messageIndexes.size()==1){
				return messageIndexes.get(0);
			}
			else{
				System.out.println("More than one entry for message");
				return -2;
			}
		}
		return -1;
	}
	public void SearchEANNumber(String eanNumber){
		CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "EAN status", edsnHtmlDocument());
		sleep(2);
		CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Name, "connectionpoint", eanNumber, edsnHtmlDocument());
		CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.image", PropertyType.Source, new RegularExpression(".*search.*", false), edsnHtmlDocument());
	}
}
