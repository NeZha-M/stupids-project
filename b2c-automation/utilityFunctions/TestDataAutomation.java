package utilityFunctions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class TestDataAutomation {
	public String DESTINATION_NAME = "ABAP_AS_WITH_POOL";
	Properties connectProperties = new Properties();
	int noOfBusinessPartnersRequired = 1;
	public String wusDBURL = "";
	public String environment = "";
	public LinkedHashMap<String, String> businessPartnerInfo = new LinkedHashMap<String, String>();
	public String tableName = "";
	public String[] inputFields = null;
	public String[] inputValues = null;
	public String[] outputFields = null;
	public String[][] resultSet = null;
	public boolean lastFilter = false;
	public ArrayList<String> bpNumbers;
	public String[] productFilter = new String[]{"",""};
	public String meterType = CONSTANTS.notApplicable;
	public String meterReadingUnit = "";
	public ArrayList<String> eanNumbers = null;
	public String smartMeterAdditionalFilter = CONSTANTS.notApplicable;
	public TestDataAutomation(String dataRequestEvnironment,boolean isItSapCRM,String customerType){
		this.environment = dataRequestEvnironment;
		String brandValue = "040";
		if(customerType.equalsIgnoreCase("ed")){
			brandValue = "050";
		}
		if(dataRequestEvnironment.equalsIgnoreCase("Acceptance")){
			if(isItSapCRM){
				connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "s031l0040.rwe.com");
				connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "41");
				connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, brandValue);
				connectProperties.setProperty(DestinationDataProvider.JCO_USER,   CONSTANTS.sapUserName);
				connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, CONSTANTS.sapPassword);
				connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "en");
				wusDBURL = "jdbc:mysql://a-wus.cnujmblovh0h.eu-west-1.rds.amazonaws.com:3306/";
			}
			else{
				connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "s031l0042.rwe.com");
				connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "51");
				connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, brandValue);
				connectProperties.setProperty(DestinationDataProvider.JCO_USER,   CONSTANTS.sapUserName);
				connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, CONSTANTS.sapPassword);
				connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "en");
				wusDBURL = "jdbc:mysql://a-wus.cnujmblovh0h.eu-west-1.rds.amazonaws.com:3306/";
			}
		}
		else{
			if(isItSapCRM){
				connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "s031l0044");
				connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "81");
				connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, brandValue);
				connectProperties.setProperty(DestinationDataProvider.JCO_USER,   CONSTANTS.sapUserName);
				connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, CONSTANTS.sapPassword);
				connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "en");
				wusDBURL = "jdbc:mysql://t-wus.cnujmblovh0h.eu-west-1.rds.amazonaws.com:3306/";
			}
			else{
				connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "logonT05e");
				connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "95");
				connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, brandValue);
				connectProperties.setProperty(DestinationDataProvider.JCO_USER,   CONSTANTS.sapUserName);
				connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, CONSTANTS.sapPassword);
				connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "en");
				wusDBURL = "jdbc:mysql://t-wus.cnujmblovh0h.eu-west-1.rds.amazonaws.com:3306/";
			}
		}
		CreateDestinationDataFile(this.DESTINATION_NAME, connectProperties);
	}
	public void getEANs(String BusinessPartner,String installationStartDate,String installationEndDate ){
		this.eanNumbers =new ArrayList<String>();
		String[] installations = GetUtilityInstallations(GetContractNumber(BusinessPartner),installationStartDate,installationEndDate);
		int count = 0;
		for (String string : installations) {
			if(count==0){
				count = 10;
				continue;

			}
			System.out.println(GetPointOfDelivery(string));
			this.eanNumbers.add(GetPointOfDelivery(string));
		}
	}
	public void getEANs(String BusinessPartner){
		this.eanNumbers =new ArrayList<String>();
		String[] installations = GetUtilityInstallations(GetContractNumber(BusinessPartner));
		int count = 0;
		for (String string : installations) {
			if(count==0){
				count = 10;
				continue;

			}
			System.out.println(GetPointOfDelivery(string));
			this.eanNumbers.add(GetPointOfDelivery(string));
		}
	}
	public ArrayList<String> GetCustomerForPeriodicInvoice(){
		Calendar calendarInstance = Calendar.getInstance();
		calendarInstance.add(Calendar.DATE, 1);	
		String futureMoveOutDate = new SimpleDateFormat("yyyyMMdd").format(calendarInstance.getTime());
		calendarInstance.add(Calendar.YEAR, -1);
		String moveInStartDate = new SimpleDateFormat("yyyyMMdd").format(calendarInstance.getTime());
		calendarInstance.add(Calendar.DATE, 25);
		String moveInEndDate = new SimpleDateFormat("yyyyMMdd").format(calendarInstance.getTime());
		this.TestDataRequestProcessor("CTYPE HH MINS "+moveInStartDate+" MINE "+moveInEndDate+" MOTS "+futureMoveOutDate+" MOTE NA Both 200 2 Test INV_N 01;"+CONSTANTS.notApplicable);

		return null;
	}
	public  String GetContractNumber(String businessPartner){
		this.tableName = "FKKVKP";
		//GPART = '0131136915'
		this.inputFields = new String[]{"GPART"};
		this.inputValues = new String[]{" = '"+businessPartner+"'"};
		this.outputFields = new String[]{"VKONT"};
		this.ReadTable(10);
		if(this.resultSet !=null){
			return this.resultSet[0][1];
		}
		else{
			return null;
		}
	}
	public  String GetMeterReadingUnit(String installationNumber){
		this.tableName = "EANLH";
		//GPART = '0131136915'
		this.inputFields = new String[]{"ANLAGE","BIS"};
		this.inputValues = new String[]{" = '"+installationNumber+"'"," >= '"+new SimpleDateFormat("yyyyMMdd").format(new java.util.Date())+"'"};
		this.outputFields = new String[]{"ABLEINH"};
		this.ReadTable(10);
		if(this.resultSet !=null){
			return this.resultSet[0][1];
		}
		else{
			return null;
		}
	}
	public  String GetMeterReadingDate(String meterReadingUnit){
		this.tableName = "TE418";
		//GPART = '0131136915'
		this.inputFields = new String[]{"TERMSCHL"};
		this.inputValues = new String[]{" = '"+meterReadingUnit+"'"};
		this.outputFields = new String[]{"TERMTDAT"};
		this.ReadTable(10);
		if(this.resultSet !=null){
			return Calendar.getInstance().get(Calendar.YEAR)+this.resultSet[0][1].substring(4);
		}
		else{
			return null;
		}
	}
	public boolean IsAddressEmpty(String postCode,String houseNumber,String extension){
		this.tableName = "ADRC";
		//GPART = '0131136915'
		if(!postCode.contains(" ")){
			postCode = postCode.substring(0, 4)+" "+postCode.substring(4);
			//System.out.println(postCode);
		}
		if(extension.equals("")){
			this.inputFields = new String[]{"POST_CODE1","HOUSE_NUM1"};
			this.inputValues = new String[]{" = '"+postCode+"'"," = '"+houseNumber+"'"};
		}
		else{
			this.inputFields = new String[]{"POST_CODE1","HOUSE_NUM1","HOUSE_NUM2"};
			this.inputValues = new String[]{" = '"+postCode+"'"," = '"+houseNumber+"'"," = '"+extension+"'"};
		}
		this.outputFields = new String[]{"ADDRNUMBER"};
		this.ReadTable(10);
		if(this.resultSet !=null){
			return false;
		}
		else{
			return true;
		}
	}
	public ArrayList<String> GetEmptyAddresses(String meterType,String customerType) throws Throwable{
		ArrayList<String> addressDetails = new ArrayList<String>();
		BufferedReader textFileReader = null;
		if(meterType.toLowerCase().contains("slm")){
			textFileReader = new BufferedReader(new FileReader(CONSTANTS.smartMeterAddressLocation));
		}
		else{
			textFileReader = new BufferedReader(new FileReader(CONSTANTS.nlPostCodesLocation));
		}
		ArrayList<String> availableAddresses = new ArrayList<String>();
		String addressLine = "";
		int emptyAddressCount = 0;
		Random randomNumberGenerator = new Random();
		while(true){
			addressLine = textFileReader.readLine();
			if(addressLine==null){
				textFileReader.close();
				break;
			}
			else{
				availableAddresses.add(addressLine);
			}
		}
		int numberOfAddressLine =  availableAddresses.size()-1;
		while(emptyAddressCount==0){
			int randomAddress = randomNumberGenerator.nextInt(numberOfAddressLine-1);
			System.out.println("Random Address: "+availableAddresses.get(randomAddress));
			String[] houseNumbers = availableAddresses.get(randomAddress).split(" ");
			if(!meterType.equals(CONSTANTS.notApplicable)){
				for(int houseNumber=1;houseNumber<houseNumbers.length;houseNumber++){
					String[] splitHouseNumber = houseNumbers[houseNumber].split("/");
					String houseNumberExtension = "";
					if(splitHouseNumber.length==2){
						houseNumberExtension = splitHouseNumber[1];
					}
					if(this.IsAddressEmpty(houseNumbers[0], splitHouseNumber[0], houseNumberExtension)){
						ArrayList<String> getMeterDetails = APIGateWay.GetMeterDetails(customerType, this.environment, houseNumbers[0], splitHouseNumber[0], houseNumberExtension);
						ArrayList<String> meterTypes = new ArrayList<String>();
						for (String meterDetail : getMeterDetails) {
							String[] splitMeterDetails = meterDetail.split(";");
							APIGateWay apiGateWay = new APIGateWay();
							String connectionInformation = apiGateWay.GetAddressOfEANNumber(customerType, this.environment, splitMeterDetails[0]);
							if(connectionInformation.contains("SLM")){
								meterTypes.add("SLM");
							}
							else{
								meterTypes.add("CVN");
							}
						}
						if(getMeterDetails.size()==0 || getMeterDetails.size()>2){
							continue;
						}
						boolean meterTypeCorrect = true;
						for (String getMeterType : meterTypes) {
							if(!getMeterType.contains(meterType)){
								meterTypeCorrect = false;
								break;
							}
						}
						if(meterTypeCorrect){
							addressDetails.add(houseNumbers[0]+" "+houseNumbers[houseNumber]);
							emptyAddressCount++;
							if(emptyAddressCount>0){
								break;
							}
						}
					}
				}
			}
			else{
				for(int houseNumber=1;houseNumber<houseNumbers.length;houseNumber++){
					String[] splitHouseNumber = houseNumbers[houseNumber].split("/");
					String houseNumberExtension = "";
					if(splitHouseNumber.length==2){
						houseNumberExtension = splitHouseNumber[1];
					}
					if(this.IsAddressEmpty(houseNumbers[0], splitHouseNumber[0], houseNumberExtension)){
						addressDetails.add(houseNumbers[0]+" "+houseNumbers[houseNumber]);
						emptyAddressCount++;
						if(emptyAddressCount>10){
							break;
						}
					}
				}
			}
		}
		return addressDetails;
	}
	public  String GetPremiseId(String installationNumber){
		this.tableName = "EANL";
		this.inputFields = new String[]{"ANLAGE"};
		this.inputValues = new String[]{" = '"+installationNumber+"'"};
		this.outputFields = new String[]{"VSTELLE"};
		this.ReadTable(10);
		if(this.resultSet !=null){
			return this.resultSet[0][1];
		}
		else{
			return null;
		}
	}
	public  String[] GetUtilityInstallations(String contractNumber){
		this.tableName = "EVER";
		this.inputFields = new String[]{"VKONTO"};
		this.inputValues = new String[]{" = '"+contractNumber+"'"};
		this.outputFields = new String[]{"ANLAGE"};
		this.ReadTable(10);
		if(this.resultSet !=null){
			return this.resultSet[0];
		}
		else{
			return null;
		}
	}
	public  String[][] GetUtilityInstallationInformation(String contractNumber){
		this.tableName = "EVER";
		this.inputFields = new String[]{"VKONTO"};
		this.inputValues = new String[]{" = '"+contractNumber+"'"};
		this.outputFields = new String[]{"ANLAGE","EINZDAT","AUSZDAT"};
		this.ReadTable(10);
		if(this.resultSet !=null){
			return this.resultSet;
		}
		else{
			return null;
		}
	}
	public  String[] GetUtilityInstallations(String contractNumber,String installationStartDate,String installationEndDate){
		this.tableName = "EVER";
		if(installationEndDate.equals(CONSTANTS.notApplicable)){
			this.inputFields = new String[]{"VKONTO","EINZDAT","AUSZDAT"};
			this.inputValues = new String[]{" = '"+contractNumber+"'"," >= '"+installationStartDate+"'"," >= '"+"20160101"+"'"};
		}
		else{
			this.inputFields = new String[]{"VKONTO","EINZDAT","AUSZDAT"};
			this.inputValues = new String[]{" = '"+contractNumber+"'"," >= '"+installationStartDate+"'"," >= '"+installationEndDate+"'"};
		}
		this.outputFields = new String[]{"ANLAGE"};
		this.ReadTable(10);
		if(this.resultSet !=null){
			return this.resultSet[0];
		}
		else{
			return null;
		}
	}
	public  String GetUserCredentials(String bpNumber) throws Throwable{
		String credentials = "";
		Class.forName("com.mysql.jdbc.Driver");
		Connection wusConnection = DriverManager.getConnection(wusDBURL+"B2B20","wus-for-dev","w5sdev$5enl");
		wusConnection.setAutoCommit(false);
		Statement wusStatement = wusConnection.createStatement();
		ResultSet resultSet=wusStatement.executeQuery("SELECT UserID, EmailAddress FROM WebUserStore WHERE `ExternalReferenceID_SAP-CRM` = '"+bpNumber+"'");
		int webUserEntryCount = 0;
		if(resultSet.next() == true){
			webUserEntryCount++;
			resultSet.getString(1);
			credentials = resultSet.getString("UserID")+";"+resultSet.getString("EmailAddress");
			System.out.println("Email address of the user : "+resultSet.getString("EmailAddress"));
			return credentials;
		}
		if(webUserEntryCount==0){
			credentials = "wusError_NoEntry";
		}
		return credentials;
	}
	public void GetBusinessPartnerDetails(String businessPartnerNumber) throws Throwable{
		this.businessPartnerInfo.put("Business Parter Number", businessPartnerNumber);
		this.businessPartnerInfo.put("Business Parter Type", this.GetBusinessPartnerType(businessPartnerNumber));
		this.businessPartnerInfo.put("Contract Number", this.GetContractNumber(businessPartnerNumber));
		this.businessPartnerInfo.put("Payment Type", this.GetPaymentType(businessPartnerNumber));
		String[][] utitlityInstallationInformation = this.GetUtilityInstallationInformation(this.businessPartnerInfo.get("Contract Number"));
		String[] installationNumbers = Arrays.copyOfRange(utitlityInstallationInformation[0], 1, utitlityInstallationInformation[0].length); 
		String[] installationStartDates = Arrays.copyOfRange(utitlityInstallationInformation[1], 1, utitlityInstallationInformation[1].length);
		String[] installationEndDates = Arrays.copyOfRange(utitlityInstallationInformation[2], 1, utitlityInstallationInformation[2].length);
		for(int installationDate=0;installationDate<installationStartDates.length;installationDate++){
			installationStartDates[installationDate] = installationStartDates[installationDate].substring(6,8)+"."+installationStartDates[installationDate].substring(4,6)+"."+installationStartDates[installationDate].substring(0,4);
			installationEndDates[installationDate] = installationEndDates[installationDate].substring(6,8)+"."+installationEndDates[installationDate].substring(4,6)+"."+installationEndDates[installationDate].substring(0,4);
		}
		this.businessPartnerInfo.put("Premise ID", this.GetPremiseId(installationNumbers[0]));
		this.businessPartnerInfo.put("Installations", StringUtils.join(",", installationNumbers));
		this.businessPartnerInfo.put("Move In Date", StringUtils.join(",", installationStartDates));		
		this.businessPartnerInfo.put("Move Out Date", StringUtils.join(",", installationEndDates));		
		String[] pointOfDelivery = new String[installationNumbers.length];String[] meterReadingUnits = new String[installationNumbers.length];String[] meterType = new String[installationNumbers.length];
		String[] productType = new String[installationNumbers.length];
		for(int installation=0;installation<installationNumbers.length;installation++){
			pointOfDelivery[installation] = this.GetPointOfDelivery(installationNumbers[installation]);
			meterType[installation] = this.GetMeterType(installationNumbers[installation]);
			productType[installation] = this.GetProductTypeFromUtilityInstallation(installationNumbers[installation]);
			meterReadingUnits[installation] = this.GetMeterReadingUnit(installationNumbers[installation]);
		}
		this.businessPartnerInfo.put("Point Of Delivery", StringUtils.join(",", pointOfDelivery));
		this.businessPartnerInfo.put("Meter Type", StringUtils.join(",", meterType));
		this.businessPartnerInfo.put("Product Type", StringUtils.join(",", productType));
		this.businessPartnerInfo.put("Meter Reading Unit", StringUtils.join(",", meterReadingUnits));
		this.businessPartnerInfo.put("Budget Bill Amount", this.GetBudgetBillAmount(businessPartnerNumber, this.businessPartnerInfo.get("Contract Number")));
		
	}
	public String GetNewCredentialsForUser(String bpNumber,String customerType_Local) throws Throwable{
		System.out.println("Warning, user email will be reset");
		String newCredentials = "";
		String credentials = GetUserCredentials(bpNumber);
		if(credentials.equals("wusError_NoEntry")){
			this.UpdateTable(bpNumber, CONSTANTS.userMailAddress);
			if(customerType_Local.equalsIgnoreCase("ed")){
				APIGateWay.CreateOnlineAccount(bpNumber, "Test123", CONSTANTS.userMailAddress, this.environment,"energiedirect");
			}
			else{
				APIGateWay.CreateOnlineAccount(bpNumber, "Test123", CONSTANTS.userMailAddress, this.environment,"essent");
			}
			newCredentials = bpNumber+";Test123";
		}
		else{
			this.UpdateTable(bpNumber, CONSTANTS.userMailAddress);
			newCredentials = (credentials.split(";"))[0];
			if(customerType_Local.equalsIgnoreCase("ed")){
				APIGateWay.SendPasswordResetRequest(CONSTANTS.userMailAddress,(credentials.split(";"))[0],this.environment,"energiedirect");	
			}
			else{
				APIGateWay.SendPasswordResetRequest(CONSTANTS.userMailAddress,(credentials.split(";"))[0],this.environment,"essent");	
			}
			for(int passwordTrail = 0;passwordTrail<10;passwordTrail++){
				if(EmailManagement.GetPassword(bpNumber).equals("")){
					Thread.sleep(10000);
					continue;
				}
				else{
					newCredentials = newCredentials+";"+EmailManagement.GetPassword(bpNumber);
					break;
				}
			}
		}
		return newCredentials;
	}
	public void ReadTable(int maxNumberOfResults){	
		JCoDestination jcoDestination = null;
		try{
			if(tableName==null || tableName.equals("")){
				System.out.println("Please provide a table name");
			}
			//Set the query table information
			jcoDestination = JCoDestinationManager.getDestination(this.DESTINATION_NAME);
			JCoContext.begin(jcoDestination);
			JCoRepository sapRepository = jcoDestination.getRepository();
			JCoFunctionTemplate template = sapRepository.getFunctionTemplate("RFC_READ_TABLE");
			JCoFunction jcoFunction = template.getFunction();
			jcoFunction.getImportParameterList().setValue("QUERY_TABLE", tableName);
			jcoFunction.getImportParameterList().setValue("DELIMITER", ";");
			jcoFunction.getImportParameterList().setValue("ROWSKIPS", Integer.valueOf(0));
			jcoFunction.getImportParameterList().setValue("ROWCOUNT", Integer.valueOf(maxNumberOfResults));

			//Set filter criteria
			JCoTable returnOptions = jcoFunction.getTableParameterList().getTable("OPTIONS");
			returnOptions.appendRow();
			if(inputFields==null || inputFields.length==0){
				System.out.println("No filter will be applied on the results, Please be careful with large number of results");
			}
			else{
				int numberOfFilterFields = inputFields.length;
				int numberOFFilterValues = inputValues.length;
				if(numberOfFilterFields != numberOFFilterValues){
					System.out.println("Filter information is incorrect");
				}
				else{
					for(int filterCount=0;filterCount<numberOfFilterFields;filterCount++){
						if(filterCount==0){
							returnOptions.setValue("TEXT", inputFields[filterCount]+inputValues[filterCount]);
							returnOptions.appendRow();
						} 
						else{
							returnOptions.setValue("TEXT", "AND "+inputFields[filterCount]+inputValues[filterCount]);
							returnOptions.appendRow();
						}
					}
				}

			}
			//returnOptions.setValue("TEXT", filterQuery);		
			JCoTable returnFields = jcoFunction.getTableParameterList().getTable("FIELDS");
			if(outputFields==null || outputFields.length==0){
				System.out.println("No Output fields specified");
				resultSet = null;
			}
			else{
				int noOfOutputFileds = outputFields.length;		
				for(int outputFieldCount = 0;outputFieldCount<noOfOutputFileds;outputFieldCount++){
					returnFields.appendRow();
					returnFields.setValue("FIELDNAME", outputFields[outputFieldCount]);
				}
			}
			try{
				jcoFunction.execute(jcoDestination);
				JCoTable jcoTabled = jcoFunction.getTableParameterList().getTable("DATA");
				int noOfOutputRows = jcoTabled.getNumRows();
				if(noOfOutputRows==0){
					//System.out.println("No results for specified criteria");
					resultSet = null;
				}
				else{
					int noOfOutputFileds = outputFields.length;
					resultSet = new String[noOfOutputFileds][noOfOutputRows+1];				
					for(int outputFieldCount = 0;outputFieldCount<noOfOutputFileds;outputFieldCount++){
						resultSet[outputFieldCount][0] = outputFields[outputFieldCount];
					}
					int outputRowNumber = 1;
					do {
						for (JCoFieldIterator fieldIterator = jcoTabled.getFieldIterator();fieldIterator.hasNextField();)
						{
							JCoField tabField = fieldIterator.nextField();
							String[] outputRow = tabField.getString().split(";");
							for(int rowField=0;rowField<outputRow.length;rowField++){
								resultSet[rowField][outputRowNumber] = outputRow[rowField];
							}
						}
						outputRowNumber++;
					}
					while (jcoTabled.nextRow() == true);
				}	
			}
			catch(Throwable t){
				System.out.println("Unable to execute JCO Query, Please check the query again");
				System.out.println(t.toString());
			}	
		}
		catch(Throwable t){		
			System.out.println("Oops, something went wrong");
			System.out.println(t.toString());
		}
		finally{
			try {
				JCoContext.end(jcoDestination);
			} catch (JCoException e) {
				System.out.println("JCO Destination doesn't exist");
			}	
		}
	}
	public String GetBudgetBillAmount(String businessPartnerNumber,String contractNumber) throws Throwable{ 
		JCoDestination jcoDestination;
		JCoRepository sapRepository;
		jcoDestination = JCoDestinationManager.getDestination(this.DESTINATION_NAME);
		JCoDestinationManager.getDestination(this.DESTINATION_NAME);
		try {
			sapRepository = jcoDestination.getRepository();
			if (sapRepository == null) {
				System.out.println("Couldn't get repository!");
				System.exit(0);
			} 
			JCoFunctionTemplate template = sapRepository.getFunctionTemplate("ZSR234_BBP_AMOUNT_FOR_PARTNER");
			JCoFunction function = template.getFunction();
			function.getImportParameterList().setValue("IV_PARTNER", businessPartnerNumber);
			function.getImportParameterList().setValue("IV_CONTRACT_ACCOUNT", contractNumber);
			function.execute(jcoDestination);
			JCoStructure budgetBillInformation = function.getExportParameterList().getStructure("ES_PARTNER_DETAILS");		
			return budgetBillInformation.getString("BBPAMOUNT");
		} 
		catch (Exception e) {
			System.out.println("Unable to get bbplans");
		} 
		finally {
			try {
				JCoContext.end(jcoDestination);
			} 
			catch (JCoException e) {
				System.out.println("JCO Destination doesn't exist");
			}
		}
		return "Not Availablex";
	}
	public void UpdateTable(String  businessPartnerNumber,String emailAddress){		
		JCoDestination jcoDestination = null;
		try{
			jcoDestination = JCoDestinationManager.getDestination(this.DESTINATION_NAME);
			JCoContext.begin(jcoDestination);
			JCoRepository sapRepository = jcoDestination.getRepository();
			JCoFunctionTemplate template = sapRepository.getFunctionTemplate("ZBAPI_SW005_UPDATE_PARTNER");
			JCoFunction jcoFunction = template.getFunction();

			//Set filter criteria
			JCoStructure returnOptions = jcoFunction.getImportParameterList().getStructure("IS_DETAIL");
			returnOptions.setValue("PARTNER", businessPartnerNumber);

			JCoStructure returnOptions2 = returnOptions.getStructure("EMAIL");
			returnOptions2.setValue("EMAIL_ADDRESS", emailAddress);
			returnOptions2.setValue("DO_NOT_CONTACT", "X");

			JCoStructure returnOptions3 = jcoFunction.getImportParameterList().getStructure("IS_DETAILX");
			returnOptions3.setValue("EMAIL", "X");
			try{
				jcoFunction.execute(jcoDestination);
				Thread.sleep(10000);
				System.out.println("Email updated");
			}
			catch(Throwable t){
				System.out.println("Unable to execute JCO Query, Please check the query again");
				System.out.println(t.toString());
			}	
		}
		catch(Throwable t){		
			System.out.println("Oops, something went wrong");
			System.out.println(t.toString());
		}
		finally{
			try {
				JCoContext.end(jcoDestination);
			} catch (JCoException e) {
				System.out.println("JCO Destination doesn't exist");
			}	
		}
	}
	static void CreateDestinationDataFile(String destinationName, Properties connectProperties)
	{
		File destCfg = new File(destinationName+".jcoDestination");
		try
		{
			FileOutputStream fos = new FileOutputStream(destCfg, false);
			connectProperties.store(fos, "for tests only !");
			fos.close();
		}
		catch (Exception e)
		{
			throw new RuntimeException("Unable to create the destination files", e);
		}
	}
	public static void main(String[] args) throws Throwable{
		
	}
	public void TestDataRequestProcessor(String request){
		System.out.println("In Request processor");
		System.out.println("Request : "+request);
		String[] requestStrings = request.split(";");
		int numberOfFilters = requestStrings.length;
		String requestString = "";
		for (int filterRequest=0;filterRequest<numberOfFilters;filterRequest++) {
			requestString = requestStrings[filterRequest];
			if(filterRequest == numberOfFilters-1){
				this.lastFilter = true;
			}
			if(!requestString.equals(CONSTANTS.notApplicable)){
				ProcessRequestString(requestString);
			}
		}
		if(requestString.equals(CONSTANTS.notApplicable)){
			System.out.println("No online accounts extracted");
		}
		else{
			for(int bpNumber=0;bpNumber<this.bpNumbers.size();bpNumber++){
				String userDetails = "";
				try{
					userDetails = GetUserCredentials(this.bpNumbers.get(bpNumber));
					if(userDetails.equals("wusError_NoEntry")){
						this.bpNumbers.set(bpNumber, this.bpNumbers.get(bpNumber)+";NoOnlineAccount;NoOnlineAccount");
					}
					else{
						this.bpNumbers.set(bpNumber, this.bpNumbers.get(bpNumber)+";"+userDetails);
					}
				}
				catch(Throwable t){
					this.bpNumbers.set(bpNumber, this.bpNumbers.get(bpNumber)+";Unknown;Unknown");
				}
			}
		}
	}
	public String GetProductType(String installationNumber){
		this.tableName = "EVERH";
		this.inputFields = new String[]{"VERTRAG"};
		this.inputValues = new String[]{" = '"+installationNumber+"'"};
		this.outputFields = new String[]{"CRM_PRODUCT"};
		this.ReadTable(10);
		if(this.resultSet!=null && this.resultSet.length != 0){
			return this.resultSet[0][resultSet[0].length-1];
		}
		else{
			return null;
		}
	}
	public String GetProductTypeFromUtilityInstallation(String installationNumber){
		this.tableName = "EVERH";
		this.inputFields = new String[]{"ANLAGE"};
		this.inputValues = new String[]{" = '"+installationNumber+"'"};
		this.outputFields = new String[]{"CRM_PRODUCT"};
		this.ReadTable(10);
		if(this.resultSet!=null && this.resultSet.length != 0){
			return this.resultSet[0][resultSet[0].length-1];
		}
		else{
			return null;
		}
	}
	public String GetPaymentType(String businessPartnerNumber){
		this.tableName = "FKKVKP";
		this.inputFields = new String[]{"GPART"};
		this.inputValues = new String[]{" = '"+businessPartnerNumber+"'"};
		this.outputFields = new String[]{"EZAWE"};
		this.ReadTable(10);
		if(this.resultSet !=null){
			return this.resultSet[0][1];
		}
		else{
			return null;
		}


	}
	public ArrayList<String> GetProductDuration(String installationNumber){
		ArrayList<String> productStrings = new ArrayList<String>();
		this.tableName = "ETTIFN";
		this.inputFields = new String[]{"ANLAGE","OPERAND"};
		this.inputValues = new String[]{" = '"+installationNumber+"'"," = 'E-PROPOSIT'"};
		this.outputFields = new String[]{"STRING2","STRING3","STRING4"};
		this.ReadTable(10);
		if(this.resultSet!=null && this.resultSet.length != 0){
			for(int productString=0;productString<resultSet.length;productString++){
				productStrings.add(resultSet[productString][1]);
			}
			//return this.resultSet[0][1];
			System.out.println(productStrings);
			return productStrings;
		}
		else{
			return null;
		}
	}
	public String GetPointOfDelivery(String installationNumber){
		this.tableName = "EUIINSTLN";
		this.inputFields = new String[]{"ANLAGE"};
		this.inputValues = new String[]{" = '"+installationNumber+"'"};
		this.outputFields = new String[]{"INT_UI"};
		this.ReadTable(10);
		//Unique ID - this.resultSet[0][1]
		//System.out.println(this.resultSet[0][1]);
		this.tableName = "EUITRANS";
		this.inputFields = new String[]{"INT_UI"};
		this.inputValues = new String[]{" = '"+this.resultSet[0][1]+"'"};
		this.outputFields = new String[]{"EXT_UI"};
		this.ReadTable(10);
		if(this.resultSet !=null){
			return this.resultSet[0][1];
		}
		else{
			return null;
		}
	}
	public String GetMeterType(String installationNumber){
		this.tableName = "ETTIFN";
		this.inputFields = new String[]{"ANLAGE","OPERAND"};
		this.inputValues = new String[]{" = '"+installationNumber+"'"," = 'E-MTR-TYPE'"};
		this.outputFields = new String[]{"STRING1"};
		this.ReadTable(10);
		if(this.resultSet!=null && this.resultSet.length != 0){
			return this.resultSet[0][resultSet[0].length-1];
		}
		else{
			this.tableName = "ETTIFN";
			this.inputFields = new String[]{"ANLAGE","OPERAND"};
			this.inputValues = new String[]{" = '"+installationNumber+"'"," = 'G-MTR-TYPE'"};
			this.outputFields = new String[]{"STRING1"};
			this.ReadTable(10);
			if(this.resultSet!=null && this.resultSet.length != 0){
				return this.resultSet[0][resultSet[0].length-1];
			}
			return CONSTANTS.notApplicable;
		}
	}
	public boolean VerifySmartMeterAdditionalFilter(String installationNumber){
		if(this.smartMeterAdditionalFilter.contains("SMN")){
			String meterReadingType = this.GetMeterReadingType(installationNumber);
			if(meterReadingType.equals("SMN")){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			String[] splitSmartMeterFilter = this.smartMeterAdditionalFilter.split(";");
			String meterReadingType = this.GetMeterReadingType(installationNumber);
			System.out.println(meterReadingType);
			if(meterReadingType.equals("SMU")){
				if(splitSmartMeterFilter[1].equals(CONSTANTS.notApplicable)){
					return true;
				}
				else{
					String meterStatus = this.GetMeterStatus(installationNumber);
					System.out.println(meterStatus);
					if(meterStatus.equals(splitSmartMeterFilter[1])){
						return true;
					}
					else{
						return false;
					}
				}
			}
		}
		return false;
	}
	public String GetMeterReadingType(String installationNumber){
		this.tableName = "ETTIFN";
		this.inputFields = new String[]{"ANLAGE","OPERAND"};
		this.inputValues = new String[]{" = '"+installationNumber+"'"," = 'E-MET-READ'"};
		this.outputFields = new String[]{"STRING1"};
		this.ReadTable(10);
		if(this.resultSet!=null && this.resultSet.length != 0){
			return this.resultSet[0][resultSet[0].length-1];
		}
		else{
			this.tableName = "ETTIFN";
			this.inputFields = new String[]{"ANLAGE","OPERAND"};
			this.inputValues = new String[]{" = '"+installationNumber+"'"," = 'G-MET-READ'"};
			this.outputFields = new String[]{"STRING1"};
			this.ReadTable(10);
			if(this.resultSet!=null && this.resultSet.length != 0){
				return this.resultSet[0][resultSet[0].length-1];
			}
			return CONSTANTS.notApplicable;
		}
	}
	public String GetMeterStatus(String installationNumber){
		this.tableName = "ETTIFN";
		this.inputFields = new String[]{"ANLAGE","OPERAND"};
		this.inputValues = new String[]{" = '"+installationNumber+"'"," = 'E-MTR-STAT'"};
		this.outputFields = new String[]{"STRING1"};
		this.ReadTable(10);
		if(this.resultSet!=null && this.resultSet.length != 0){
			return this.resultSet[0][resultSet[0].length-1];
		}
		else{
			this.tableName = "ETTIFN";
			this.inputFields = new String[]{"ANLAGE","OPERAND"};
			this.inputValues = new String[]{" = '"+installationNumber+"'"," = 'G-MTR-STAT'"};
			this.outputFields = new String[]{"STRING1"};
			this.ReadTable(10);
			if(this.resultSet!=null && this.resultSet.length != 0){
				return this.resultSet[0][resultSet[0].length-1];
			}
			return CONSTANTS.notApplicable;
		}
	}
	public String GetBusinessPartnerType(String bpNumber){
		this.tableName = "BUT000";
		this.inputFields = new String[]{"PARTNER"};
		this.inputValues = new String[]{" = '"+bpNumber+"'"};
		this.outputFields = new String[]{"BPKIND"};
		this.ReadTable(10);
		if(this.resultSet!=null && this.resultSet.length != 0){
			return this.resultSet[0][1];
		}
		else{
			return null;
		}
	}
	public String[] GetInstallations(String contractNumber){
		this.tableName = "EVER";
		this.inputFields = new String[]{"VKONTO"};
		this.inputValues = new String[]{" = '"+contractNumber+"'"};
		this.outputFields = new String[]{"VERTRAG"};
		this.ReadTable(10);
		if(this.resultSet !=null){
			return this.resultSet[0];
		}
		else{
			return null;
		}
	}
	public String[] GetInstallationNumbers(String contractNumber){
		this.tableName = "EVER";
		this.inputFields = new String[]{"VKONTO"};
		this.inputValues = new String[]{" = '"+contractNumber+"'"};
		this.outputFields = new String[]{"ANLAGE"};
		this.ReadTable(10);
		if(this.resultSet !=null){
			return this.resultSet[0];
		}
		else{
			return null;
		}
	}
	public String[] GetInstallations(String contractNumber,String installationStartDate,String installationEndDate){
		this.tableName = "EVER";
		if(installationEndDate.equals(CONSTANTS.notApplicable)){
			this.inputFields = new String[]{"VKONTO","EINZDAT","AUSZDAT"};
			this.inputValues = new String[]{" = '"+contractNumber+"'"," >= '"+installationStartDate+"'"," >= '"+"20160101"+"'"};
		}
		else{
			this.inputFields = new String[]{"VKONTO","EINZDAT","AUSZDAT"};
			this.inputValues = new String[]{" = '"+contractNumber+"'"," >= '"+installationStartDate+"'"," >= '"+installationEndDate+"'"};
		}
		this.outputFields = new String[]{"VERTRAG"};
		this.ReadTable(10);
		if(this.resultSet !=null){
			return this.resultSet[0];
		}
		else{
			return null;
		}
	}
	public String GetBusinessParterNumber(String contractNumber){
		this.tableName = "FKKVKP";
		this.inputFields = new String[]{"VKONT"};
		this.inputValues = new String[]{" = '"+contractNumber+"'"};
		this.outputFields = new String[]{"GPART"};
		this.ReadTable(10);
		if(this.resultSet !=null){
			return this.resultSet[0][1];
		}
		else{
			return null;
		}
	}
	public void ProcessRequestString(String requestString){
		String[] requestParameters = requestString.split(" ");
		if(requestString.contains("CTYPE")){
			this.noOfBusinessPartnersRequired = Integer.parseInt(requestParameters[12]);
			this.bpNumbers = new ArrayList<String>();
			this.environment = requestParameters[13];
			if(requestParameters[14].equalsIgnoreCase("INV_F")){
				this.tableName = "ERDK";
				this.outputFields = new String[]{"PARTNER"};
				this.inputFields = new String[]{"ABRVORG","INVOICED"};
				this.inputValues = new String[]{" = '"+requestParameters[15]+"'"," = 'X'"};
				if(this.lastFilter){
					this.ReadTable(this.noOfBusinessPartnersRequired);
				}
				else{
					this.ReadTable(Integer.parseInt(requestParameters[11]));
				}
				for(int bpNumber=1;bpNumber<this.resultSet[0].length;bpNumber++){
					if(this.bpNumbers.contains(this.resultSet[0][bpNumber])){
						//System.out.println("BP is already in the results");
					}
					else{
						this.bpNumbers.add(this.resultSet[0][bpNumber]);
					}	
				}
			}
			else{
				if(requestParameters[10].equals("MultipleContracts")){
					this.bpNumbers.clear();
					this.tableName = "FKKVKP";
					this.outputFields = new String[]{"GPART"};
					this.ReadTable(Integer.parseInt(requestParameters[11]));
					ArrayList<String> temporaryBusinessParnterList = new ArrayList<String>();
					for(int bpNumber=1;bpNumber<this.resultSet[0].length;bpNumber++){
						if(temporaryBusinessParnterList.contains(this.resultSet[0][bpNumber])){
							if(!this.bpNumbers.contains(this.resultSet[0][bpNumber])){
								this.bpNumbers.add(this.resultSet[0][bpNumber]);
							}
						}
						else{
							temporaryBusinessParnterList.add(this.resultSet[0][bpNumber]);
						}
						if(this.bpNumbers.size()>=Integer.parseInt(requestParameters[12])){
							break;
						}
					}
				}
				else{
					this.tableName = "EVER";
					this.outputFields = new String[]{"VERTRAG","VKONTO"};
					if(requestParameters[7].equals(CONSTANTS.notApplicable)){
						if(requestParameters[9].equals(CONSTANTS.notApplicable)){
							this.inputFields = new String[]{"EINZDAT","EINZDAT"};
							this.inputValues = new String[]{" >= '"+requestParameters[3]+"'"," <= '"+requestParameters[5]+"'"};
							this.ReadTable(Integer.parseInt(requestParameters[11]));
						}
						else{
							this.inputFields = new String[]{"AUSZDAT","EINZDAT","EINZDAT"};
							this.inputValues = new String[]{" <= '"+requestParameters[9]+"'"," >= '"+requestParameters[3]+"'"," <= '"+requestParameters[5]+"'"};
							this.ReadTable(Integer.parseInt(requestParameters[11]));
						}
					}
					else{
						if(requestParameters[9].equals(CONSTANTS.notApplicable)){
							this.inputFields = new String[]{"AUSZDAT","EINZDAT","EINZDAT"};
							this.inputValues = new String[]{" >= '"+requestParameters[7]+"'"," >= '"+requestParameters[3]+"'"," <= '"+requestParameters[5]+"'"};
							this.ReadTable(Integer.parseInt(requestParameters[11]));
						}
						else{
							this.inputFields = new String[]{"AUSZDAT","AUSZDAT","EINZDAT","EINZDAT"};
							this.inputValues = new String[]{" >= '"+requestParameters[7]+"'"," <= '"+requestParameters[9]+"'"," >= '"+requestParameters[3]+"'"," <= '"+requestParameters[5]+"'"};
							this.ReadTable(Integer.parseInt(requestParameters[11]));
						}
					}		
					String[] installationNumbers = this.resultSet[0];
					String[] contractNumbers = this.resultSet[1];
					if(requestParameters[10].equals("Electricity")){
						for(int installation=1;installation<installationNumbers.length;installation++){
							String[] contractInstallations = GetInstallations(contractNumbers[installation]);
							if(contractInstallations!=null && contractInstallations.length==2){
								contractInstallations = GetInstallationNumbers(contractNumbers[installation]);
								String prodType = "";
								//Filter Meter Type
								boolean meterTypeFilterCorrect = true;
								boolean productTypeCorrect = false;
								boolean meterReadingUnitFilterCorrect = true;
								if(this.meterType.equals(CONSTANTS.notApplicable)){
									//System.out.println("No filter on Meter Type");
								}
								else{
									String customerMeterType = GetMeterType(contractInstallations[1]);
									if(!(customerMeterType.equals(this.meterType))){
										meterTypeFilterCorrect = false;
									}
									if(this.meterType.equals("SLM")){
										if(!this.smartMeterAdditionalFilter.equals(CONSTANTS.notApplicable)){
											if(!this.VerifySmartMeterAdditionalFilter(contractInstallations[1])){
												meterTypeFilterCorrect = false;
											}
										}
									}
								}
								if(meterTypeFilterCorrect){
									prodType = GetProductType(installationNumbers[installation]);
									System.out.println("ProductType : "+prodType);
									if(!this.productFilter[0].equals("")){
										if(prodType.equals(this.productFilter[0])){
											if(!this.productFilter[1].equals("")){
												ArrayList<String> productDuration = GetProductDuration(contractInstallations[1]);
												for (String productDurationString : productDuration) {
													if(productDurationString!=null && productDurationString.contains(this.productFilter[1])){
														productTypeCorrect = true;
														break;
													}
												}
											}
											else{
												productTypeCorrect = true;
											}
										}
									}
									else{
										productTypeCorrect = true;
									}
								}
								if(meterTypeFilterCorrect && productTypeCorrect){
									if(this.meterReadingUnit.equals("")){
										System.out.println("No Meter reading filter");
									}
									else{
										String meterReadingUnit = GetMeterReadingUnit(contractInstallations[1]);
										if(!(meterReadingUnit.equals(this.meterReadingUnit))){
											meterReadingUnitFilterCorrect = false;
										}
									}

								}
								if(productTypeCorrect && meterTypeFilterCorrect && meterReadingUnitFilterCorrect){
									if(prodType.startsWith("E")){
										String busingessPartnerNumber = GetBusinessParterNumber(contractNumbers[installation]);
										String custType = GetBusinessPartnerType(busingessPartnerNumber);
										if(custType.equalsIgnoreCase(requestParameters[1])){
											if(this.bpNumbers.contains(busingessPartnerNumber)){
												//System.out.println("BP is already in the results");
											}
											else{
												this.bpNumbers.add(busingessPartnerNumber);
											}										}
										if(this.lastFilter && this.bpNumbers.size()==(this.noOfBusinessPartnersRequired)){
											break;
										}
									}
								}
							}
						}
					}
					else if(requestParameters[10].equals("Gas")){
						for(int installation=1;installation<installationNumbers.length;installation++){
							String[] contractInstallations = GetInstallations(contractNumbers[installation]);
							if(contractInstallations!=null && contractInstallations.length==2){
								String prodType = "";
								contractInstallations = GetInstallationNumbers(contractNumbers[installation]);
								//Filter Meter type
								boolean meterTypeFilterCorrect = true;
								boolean productTypeCorrect = false;
								boolean meterReadingUnitFilterCorrect = true;
								if(this.meterType.equals(CONSTANTS.notApplicable)){
									//System.out.println("No filter on Meter Type");
								}
								else{
									String customerMeterType = GetMeterType(contractInstallations[1]);
									if(!(customerMeterType.equals(this.meterType))){
										meterTypeFilterCorrect = false;
									}
									if(this.meterType.equals("SLM")){
										if(!this.smartMeterAdditionalFilter.equals(CONSTANTS.notApplicable)){
											if(!this.VerifySmartMeterAdditionalFilter(contractInstallations[1])){
												meterTypeFilterCorrect = false;
											}
										}
									}
								}
								if(meterTypeFilterCorrect){
									prodType = GetProductType(installationNumbers[installation]);
									System.out.println("Product Type "+prodType);
									if(!this.productFilter[0].equals("")){
										if(prodType.equals(this.productFilter[0])){
											if(prodType.equals(this.productFilter[0])){
												if(!this.productFilter[1].equals("")){
													ArrayList<String> productDuration = GetProductDuration(contractInstallations[1]);
													for (String productDurationString : productDuration) {
														if(productDurationString!=null && productDurationString.contains(this.productFilter[1])){
															productTypeCorrect = true;
															break;
														}
													}
												}
												else{
													productTypeCorrect = true;
												}
											}
										}
									}
									else{
										productTypeCorrect = true;
									}
								}	
								if(meterTypeFilterCorrect && productTypeCorrect){
									if(this.meterReadingUnit.equals("")){
										System.out.println("No Meter reading filter");
									}
									else{
										String meterReadingUnit = GetMeterReadingUnit(contractInstallations[1]);
										if(!(meterReadingUnit.equals(this.meterReadingUnit))){
											meterReadingUnitFilterCorrect = false;
										}
									}

								}
								if(productTypeCorrect && meterTypeFilterCorrect && meterReadingUnitFilterCorrect){
									if(prodType.startsWith("G")){
										String busingessPartnerNumber = GetBusinessParterNumber(contractNumbers[installation]);
										String custType = GetBusinessPartnerType(busingessPartnerNumber);
										if(custType.equalsIgnoreCase(requestParameters[1])){
											if(this.bpNumbers.contains(busingessPartnerNumber)){
												//System.out.println("BP is already in the results");
											}
											else{
												this.bpNumbers.add(busingessPartnerNumber);
											}										}
										if(this.lastFilter && this.bpNumbers.size()==(this.noOfBusinessPartnersRequired)){
											break;
										}
									}
								}
							}
						}
					}
					else if(requestParameters[10].equals("Both")){
						for(int installation=1;installation<installationNumbers.length;installation++){
							String[] contractInstallations = GetInstallations(contractNumbers[installation]);
							if(contractInstallations!=null && contractInstallations.length==3){
								String prodType = "";
								contractInstallations = GetInstallationNumbers(contractNumbers[installation]);
								System.out.println("ProductType : "+prodType);
								//Filter Meter type
								boolean meterTypeFilterCorrect = true;
								boolean productTypeCorrect = false;
								boolean meterReadingUnitFilterCorrect = true;
								if(this.meterType.equals(CONSTANTS.notApplicable)){
									//System.out.println("No filter on Meter Type");
								}
								else{
									String customerMeterType = GetMeterType(contractInstallations[1]);
									if(!(customerMeterType.equals(this.meterType))){
										meterTypeFilterCorrect = false;
									}
									if(this.meterType.equals("SLM")){
										if(!this.smartMeterAdditionalFilter.equals(CONSTANTS.notApplicable)){
											if(!this.VerifySmartMeterAdditionalFilter(contractInstallations[1])){
												meterTypeFilterCorrect = false;
											}
										}
									}
								}
								if(meterTypeFilterCorrect){
									prodType = GetProductType(installationNumbers[installation]);
									if(!this.productFilter[0].equals("")){
										if(prodType.equals(this.productFilter[0])){
											if(prodType.equals(this.productFilter[0])){
												if(!this.productFilter[1].equals("")){
													ArrayList<String> productDuration = GetProductDuration(contractInstallations[1]);
													for (String productDurationString : productDuration) {
														if(productDurationString!=null && productDurationString.contains(this.productFilter[1])){
															productTypeCorrect = true;
															break;
														}
													}
												}
												else{
													productTypeCorrect = true;
												}
											}
										}
									}
									else{
										productTypeCorrect = true;
									}
								}	
								if(meterTypeFilterCorrect && productTypeCorrect){
									if(this.meterReadingUnit.equals("")){
										System.out.println("No Meter reading filter");
									}
									else{
										String meterReadingUnit = GetMeterReadingUnit(contractInstallations[1]);
										if(!(meterReadingUnit.equals(this.meterReadingUnit))){
											meterReadingUnitFilterCorrect = false;
										}
									}

								}
								if(productTypeCorrect && meterTypeFilterCorrect && meterReadingUnitFilterCorrect){
									if(prodType.startsWith("G")){
										String busingessPartnerNumber = GetBusinessParterNumber(contractNumbers[installation]);
										String custType = GetBusinessPartnerType(busingessPartnerNumber);
										if(custType.equalsIgnoreCase(requestParameters[1])){
											if(this.bpNumbers.contains(busingessPartnerNumber)){
												//System.out.println("BP is already in the results");
											}
											else{
												this.bpNumbers.add(busingessPartnerNumber);
											}										}
										if(this.lastFilter && this.bpNumbers.size()==(this.noOfBusinessPartnersRequired)){
											break;
										}
									}
								}
							}
						}

					}
					else if(requestParameters[10].equals("MultiplePremise")){
						for(int installation=1;installation<installationNumbers.length;installation++){
							String[] contractInstallations = GetInstallations(contractNumbers[installation],requestParameters[3],requestParameters[7]);
							if(contractInstallations!=null && contractInstallations.length>3){
								String prodType = GetProductType(installationNumbers[installation]);
								System.out.println("ProductType : "+prodType);
								String busingessPartnerNumber = GetBusinessParterNumber(contractNumbers[installation]);
								String custType = GetBusinessPartnerType(busingessPartnerNumber);
								if(custType.equalsIgnoreCase(requestParameters[1])){
									if(this.bpNumbers.contains(busingessPartnerNumber)){
										//System.out.println("BP is already in the results");
									}
									else{
										this.bpNumbers.add(busingessPartnerNumber);
									}									}
								if(this.lastFilter && this.bpNumbers.size()==(this.noOfBusinessPartnersRequired)){
									break;
								}
							}
						}
					}
					else{
						for(int contractNumber=1;contractNumber<contractNumbers.length;contractNumber++){
							String[] contractInstallations_Dummy = null;
							contractInstallations_Dummy = GetInstallationNumbers(contractNumbers[contractNumber]);
							boolean meterTypeFilterCorrect = true;
							boolean productTypeCorrect = false;
							boolean meterReadingUnitFilterCorrect = true;
							if(this.meterType.equals(CONSTANTS.notApplicable)){
								//System.out.println("No filter on Meter Type");
							}
							else{
								String customerMeterType = GetMeterType(contractInstallations_Dummy[1]);
								if(!(customerMeterType.equals(this.meterType))){
									meterTypeFilterCorrect = false;
								}
								if(this.meterType.equals("SLM")){
									if(!this.smartMeterAdditionalFilter.equals(CONSTANTS.notApplicable)){
										if(!this.VerifySmartMeterAdditionalFilter(contractInstallations_Dummy[1])){
											meterTypeFilterCorrect = false;
										}
									}
								}
							}
							if(meterTypeFilterCorrect){
								if(!this.productFilter[0].equals("")){
									String[] contractInstallations = GetInstallations(contractNumbers[contractNumber],requestParameters[3],requestParameters[7]);
									for (int contractInstallation = 1;contractInstallation<contractInstallations.length;contractInstallation++) {
										String prodType = GetProductType(contractInstallations[contractInstallation]);
										if(prodType!=null && prodType.equals(this.productFilter[0])){
											if(prodType.equals(this.productFilter[0])){
												if(!this.productFilter[1].equals("")){
													ArrayList<String> productDuration = GetProductDuration(contractInstallations_Dummy[1]);
													if(productDuration!=null){
														for (String productDurationString : productDuration) {
															if(productDurationString!=null && productDurationString.contains(this.productFilter[1])){
																productTypeCorrect = true;
																break;
															}
														}
													}
												}
												else{
													productTypeCorrect = true;
												}
											}
										}
									}
								}
								else{
									productTypeCorrect = true;
								}
							}
							if(meterTypeFilterCorrect && productTypeCorrect){
								if(this.meterReadingUnit.equals("")){
									//System.out.println("No Meter reading filter");
								}
								else{
									String meterReadingUnit = GetMeterReadingUnit(contractInstallations_Dummy[1]);
									System.out.println("Meter Reading Unit"+meterReadingUnit);
									if(meterReadingUnit==null){
										meterReadingUnitFilterCorrect = false;
									}
									if(!(meterReadingUnit.equals(this.meterReadingUnit))){
										meterReadingUnitFilterCorrect = false;
									}
								}

							}
							if(productTypeCorrect  && meterTypeFilterCorrect && meterReadingUnitFilterCorrect){
								String busingessPartnerNumber = GetBusinessParterNumber(contractNumbers[contractNumber]);
								String custType = GetBusinessPartnerType(busingessPartnerNumber);
								if(custType.equalsIgnoreCase(requestParameters[1])){
									if(this.bpNumbers.contains(busingessPartnerNumber)){
										//System.out.println("BP is already in the results");
									}
									else{
										this.bpNumbers.add(busingessPartnerNumber);
									}								}
								if(this.lastFilter && this.bpNumbers.size()==(this.noOfBusinessPartnersRequired)){
									break;
								}
							}
						}
					}
				}	
			}					
		}
		else if(requestParameters[0].equalsIgnoreCase("INV_Y")){
			ArrayList<String> filteredBusinessPartners = new ArrayList<String>();
			for (String businessPartner : this.bpNumbers) {
				this.tableName = "ERDK";
				this.outputFields = new String[]{"PARTNER"};
				this.inputFields = new String[]{"ABRVORG","INVOICED","PARTNER"};
				this.inputValues = new String[]{" = '"+requestParameters[1]+"'"," = 'X'"," = '"+businessPartner+"'"};
				this.ReadTable(100);
				if(!(this.resultSet==null)){
					filteredBusinessPartners.add(businessPartner);
				}
				if(this.lastFilter && filteredBusinessPartners.size()==(this.noOfBusinessPartnersRequired)){
					break;
				}
			}
			this.bpNumbers = filteredBusinessPartners;
		}
	}
}
