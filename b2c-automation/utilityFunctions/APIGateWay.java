package utilityFunctions;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader; 
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;


public class APIGateWay {

	public static String USER_AGENT = "Mozilla/5.0";
	List<String> cookies = null;
	public static String[][] SendAPIDataRequest(String userName,String password) throws Exception {
		System.out.println("Sending request for UserName : "+userName+"and Password : "+password);
		String[][] timeLineItems = null;
		try{
			URL authURL = new URL("https://api-tst.energiedirect.nl/selfservice/user/authenticateUser");
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.rwe.com", 8080));
			HttpsURLConnection dataConnection = (HttpsURLConnection) authURL.openConnection(proxy);
			dataConnection.setRequestMethod("POST");
			dataConnection.setRequestProperty("User-Agent", USER_AGENT);
			dataConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");	
			dataConnection.setRequestProperty("Access-Control-Allow-Credentials", "true");
			dataConnection.setRequestProperty("Access-Control-Allow-Origin", "*");
			String urlParameters = "<authenticateUser><request><username>"+userName+"</username><password>"+password+"</password><ControlParameters>"
					+ "<GetContracts>false</GetContracts></ControlParameters> </request></authenticateUser>";
			dataConnection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(dataConnection.getOutputStream());
			wr.writeBytes(urlParameters);
			BufferedReader inputBuffer = new BufferedReader(
					new InputStreamReader(dataConnection.getInputStream()));
			String inputLine;
			List<String> cookies = dataConnection.getHeaderFields().get("Set-Cookie");
			//System.out.println(dataConnection.getHeaderFields());
			StringBuffer response = new StringBuffer();
			while ((inputLine = inputBuffer.readLine()) != null) {
				response.append(inputLine);
			}
			inputBuffer.close();		
			URL dataReqURL = new URL("https://api-tst.energiedirect.nl/selfservice/customer/getCustomerTimeLineItems");
			HttpURLConnection dataConnection2 = (HttpURLConnection) dataReqURL.openConnection(proxy);		
			for (String cookie : cookies) {
				dataConnection2.setRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}	
			dataConnection2.setRequestMethod("GET");
			dataConnection2.setRequestProperty("User-Agent", USER_AGENT);
			dataConnection2.setRequestProperty("Accept-Language", "en-US,en;q=0.5");	
			dataConnection2.setRequestProperty("Access-Control-Allow-Credentials", "true");
			dataConnection2.setRequestProperty("Access-Control-Allow-Origin", "*");
			dataConnection2.setDoOutput(true);	
			BufferedReader in = new BufferedReader(
					new InputStreamReader(dataConnection2.getInputStream()));
			response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			//System.out.println(response);
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(response.toString())));
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Result output = new StreamResult(new File("C:\\ED100Online\\APIResponses\\"+userName+".xml"));
			Source input = new DOMSource(doc);
			transformer.transform(input, output);
			org.w3c.dom.NodeList timeLineItem = doc.getElementsByTagName("TimeLineItem");
			timeLineItems = new String[timeLineItem.getLength()][3];
			org.w3c.dom.Node attributesList = null;
			String nodeName = "";
			String attributes = "";
			for(int item=0;item<timeLineItem.getLength();item++){
				attributes = "";
				for(int timeLineNode=0;timeLineNode<timeLineItem.item(item).getChildNodes().getLength();timeLineNode++){
					nodeName = timeLineItem.item(item).getChildNodes().item(timeLineNode).getNodeName();
					if(nodeName.equals("ItemType")){
						timeLineItems[item][0] = timeLineItem.item(item).getChildNodes().item(timeLineNode).getTextContent();
					}
					else if(nodeName.equals("ItemDate")){
						timeLineItems[item][1] = timeLineItem.item(item).getChildNodes().item(timeLineNode).getTextContent();
					}
					else if(nodeName.equals("Attributes")){
						attributesList = timeLineItem.item(item).getChildNodes().item(timeLineNode);
						for(int attribute=0;attribute<attributesList.getChildNodes().getLength();attribute++){
							if(!attributesList.getChildNodes().item(attribute).getTextContent().trim().replaceAll(" +", " ").equalsIgnoreCase("")){
								attributes = attributes + attributesList.getChildNodes().item(attribute).getTextContent().trim().replaceAll(" +", " ")+";";
							}	
						}
					}
					else if(!(nodeName.equals("#text"))){
						System.out.println(nodeName);
						System.out.println("Unknown Node name encountered");
					}
				}
				String[] timelineItemAttibutes = attributes.split(";");
				if(timelineItemAttibutes!=null && timelineItemAttibutes.length>0){
					for (String string : timelineItemAttibutes) {
						if(string.contains("INVOICEAMOUNT")){
							String[] invoiceAttributeDetails = string.split(" ");
							if(invoiceAttributeDetails[1].contains(".")){
								String[] attributeDetail = invoiceAttributeDetails[1].split("\\.");
								if(attributeDetail[1].length()==1){
									attributes = attributes.replace(invoiceAttributeDetails[1], invoiceAttributeDetails[1]+"0");
								}
							}
							else{
								attributes = attributes.replace(invoiceAttributeDetails[0], invoiceAttributeDetails[0]+".00");
							}
						}
					}
				}
				timeLineItems[item][2] = attributes;
			}
		}
		catch(Throwable t){
			System.out.println("Oops, something went wrong");
			System.out.println(t.toString());
		}
		return timeLineItems;
	}
	public static String[][] RleaseMeterReadingsFromEDSN(String eanNumber) throws Exception {
		String[][] timeLineItems = null;
		try{
			URL authURL = new URL("https://edsngateway-tst.energiedirect.nl/security/authentication/logon.normal.php");
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.rwe.com", 8080));
			HttpsURLConnection dataConnection = (HttpsURLConnection) authURL.openConnection(proxy);
			dataConnection.setRequestMethod("POST");
			dataConnection.setRequestProperty("User-Agent", USER_AGENT);
			dataConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");	
			dataConnection.setRequestProperty("Access-Control-Allow-Credentials", "true");
			dataConnection.setRequestProperty("Access-Control-Allow-Origin", "*");
			String urlParameters = "user=T_B2C_ENE_Emulator&password=T_B2C_ENE_Emulator";
			dataConnection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(dataConnection.getOutputStream());
			wr.writeBytes(urlParameters);
			BufferedReader inputBuffer = new BufferedReader(
					new InputStreamReader(dataConnection.getInputStream()));
			String inputLine;
			Map<String,List<String>> bullshit = dataConnection.getHeaderFields();
			System.out.println(bullshit);
			String cookies = dataConnection.getHeaderField("Cookie");
			//System.out.println(dataConnection.getHeaderFields());
			StringBuffer response = new StringBuffer();
			while ((inputLine = inputBuffer.readLine()) != null) {
				response.append(inputLine);
			}
			inputBuffer.close();		
			URL dataReqURL = new URL("https://api-tst.energiedirect.nl/selfservice/customer/getCustomerTimeLineItems");
			HttpURLConnection dataConnection2 = (HttpURLConnection) dataReqURL.openConnection(proxy);	
			dataConnection2.setRequestProperty("Cookie", cookies);
			dataConnection2.setRequestMethod("GET");
			dataConnection2.setRequestProperty("User-Agent", USER_AGENT);
			dataConnection2.setRequestProperty("Accept-Language", "en-US,en;q=0.5");	
			dataConnection2.setRequestProperty("Access-Control-Allow-Credentials", "true");
			dataConnection2.setRequestProperty("Access-Control-Allow-Origin", "*");
			dataConnection2.setDoOutput(true);	
			BufferedReader in = new BufferedReader(
					new InputStreamReader(dataConnection2.getInputStream()));
			response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			//System.out.println(response);
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(response.toString())));
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Result output = new StreamResult(new File("C:\\ED100Online\\APIResponses\\"+""+".xml"));
			Source input = new DOMSource(doc);
			transformer.transform(input, output);
			org.w3c.dom.NodeList timeLineItem = doc.getElementsByTagName("TimeLineItem");
			timeLineItems = new String[timeLineItem.getLength()][3];
			org.w3c.dom.Node attributesList = null;
			String nodeName = "";
			String attributes = "";
			for(int item=0;item<timeLineItem.getLength();item++){
				attributes = "";
				for(int timeLineNode=0;timeLineNode<timeLineItem.item(item).getChildNodes().getLength();timeLineNode++){
					nodeName = timeLineItem.item(item).getChildNodes().item(timeLineNode).getNodeName();
					if(nodeName.equals("ItemType")){
						timeLineItems[item][0] = timeLineItem.item(item).getChildNodes().item(timeLineNode).getTextContent();
					}
					else if(nodeName.equals("ItemDate")){
						timeLineItems[item][1] = timeLineItem.item(item).getChildNodes().item(timeLineNode).getTextContent();
					}
					else if(nodeName.equals("Attributes")){
						attributesList = timeLineItem.item(item).getChildNodes().item(timeLineNode);
						for(int attribute=0;attribute<attributesList.getChildNodes().getLength();attribute++){
							if(!attributesList.getChildNodes().item(attribute).getTextContent().trim().replaceAll(" +", " ").equalsIgnoreCase("")){
								attributes = attributes + attributesList.getChildNodes().item(attribute).getTextContent().trim().replaceAll(" +", " ")+";";
							}	
						}
					}
					else if(!(nodeName.equals("#text"))){
						System.out.println(nodeName);
						System.out.println("Unknown Node name encountered");
					}
				}
				String[] timelineItemAttibutes = attributes.split(";");
				if(timelineItemAttibutes!=null && timelineItemAttibutes.length>0){
					for (String string : timelineItemAttibutes) {
						if(string.contains("INVOICEAMOUNT")){
							String[] invoiceAttributeDetails = string.split(" ");
							if(invoiceAttributeDetails[1].contains(".")){
								String[] attributeDetail = invoiceAttributeDetails[1].split("\\.");
								if(attributeDetail[1].length()==1){
									attributes = attributes.replace(invoiceAttributeDetails[1], invoiceAttributeDetails[1]+"0");
								}
							}
							else{
								attributes = attributes.replace(invoiceAttributeDetails[0], invoiceAttributeDetails[0]+".00");
							}
						}
					}
				}
				timeLineItems[item][2] = attributes;
			}
		}
		catch(Throwable t){
			System.out.println("Oops, something went wrong");
			System.out.println(t.toString());
		}
		return timeLineItems;
	}
	public static void SendPasswordResetRequest(String emailAddress,String userName,String environment,String customerType_Local) throws Throwable{
		URL authURL = null;
		if(environment.equalsIgnoreCase("Acceptance")){
			authURL = new URL("https://api-acc."+customerType_Local+".nl/selfservice/user/resetPassword");
		}
		else{
			authURL = new URL("https://api-tst."+customerType_Local+".nl/selfservice/user/resetPassword");
		}
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.rwe.com", 8080));
		HttpsURLConnection dataConnection = (HttpsURLConnection) authURL.openConnection(proxy);
		dataConnection.setRequestMethod("POST");
		dataConnection.setRequestProperty("User-Agent", USER_AGENT);
		dataConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");	
		dataConnection.setRequestProperty("Access-Control-Allow-Credentials", "true");
		dataConnection.setRequestProperty("Access-Control-Allow-Origin", "*");
		String urlParameters = "<ResetPassword><request><EmailAddress>"+emailAddress+"</EmailAddress><UserID>"+userName+"</UserID></request></ResetPassword>";
		dataConnection.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(dataConnection.getOutputStream());
		wr.writeBytes(urlParameters);
		dataConnection.getInputStream();
		System.out.println("Password reset triggered successfully");
	}
	public static void CreateOnlineAccount(String businessPartnerNumber,String password,String emailAddress,String environment,String customerType) throws Throwable{
		URL authURL = null;
		if(environment.equalsIgnoreCase("Acceptance")){
			authURL = new URL("https://api-acc."+customerType+".nl/selfservice/user/registerOnlineAccount");
		}
		else{
			authURL = new URL("https://api-tst."+customerType+".nl/selfservice/user/registerOnlineAccount");
		}
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.rwe.com", 8080));
		HttpsURLConnection dataConnection = (HttpsURLConnection) authURL.openConnection(proxy);
		dataConnection.setRequestMethod("POST");
		dataConnection.setRequestProperty("User-Agent", USER_AGENT);
		dataConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");    
		dataConnection.setRequestProperty("Access-Control-Allow-Credentials", "true");
		dataConnection.setRequestProperty("Access-Control-Allow-Origin", "*");
		String urlParameters = "<RegisterOnlineAccount><request><Logon><UserID><![CDATA["+businessPartnerNumber+"]]></UserID><Password><![CDATA["+password+"]]></Password></Logon><RegistrationBy><Validation><PartnerID>"+businessPartnerNumber+"</PartnerID></Validation></RegistrationBy><Email EmailAddress=\""+emailAddress+"\" DoNotContact=\"false\" /></request></RegisterOnlineAccount>";
		System.out.println(urlParameters);
		dataConnection.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(dataConnection.getOutputStream());
		wr.writeBytes(urlParameters);
		dataConnection.getInputStream();
		System.out.println("Create Online Account Request Triggered Successfully");
	}
	public static void main(String[] args) throws Throwable{

	}
	public static ArrayList<String> GetMeterDetails(String customerBrand,String environment,String postCode,String houseNumber,String houseExtension){
		ArrayList<String> connectionDetails = new ArrayList<String>();
		if(customerBrand.toLowerCase().contains("ed")){
			customerBrand = "energiedirect";
		}
		else{
			customerBrand = "essent";
		}
		if(environment.toLowerCase().contains("acc")){
			environment = "acc";
		}
		else{
			environment = "tst";
		}
		try{
			URL authURL = new URL("https://api-"+environment+"."+customerBrand+".nl/selfservice/user/authenticateUser");
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.rwe.com", 8080));
			HttpsURLConnection dataConnection = (HttpsURLConnection) authURL.openConnection(proxy);
			dataConnection.setRequestMethod("POST");
			dataConnection.setRequestProperty("User-Agent", USER_AGENT);
			dataConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");	
			dataConnection.setRequestProperty("Access-Control-Allow-Credentials", "true");
			dataConnection.setRequestProperty("Access-Control-Allow-Origin", "*");
			String urlParameters = "";
			if(environment.equals("acc")){
				if(customerBrand.equals("energiedirect")){
					urlParameters = "<authenticateUser><request><username>0612345678</username><password>786e2a34d</password><ControlParameters>"
							+ "<GetContracts>false</GetContracts></ControlParameters> </request></authenticateUser>";
				}
				else{
					urlParameters = "<authenticateUser><request><username>0171635896</username><password>Test123</password><ControlParameters>"
							+ "<GetContracts>false</GetContracts></ControlParameters> </request></authenticateUser>";
				}
			}
			else{
				if(customerBrand.equals("energiedirect")){
					urlParameters = "<authenticateUser><request><username>0132134741</username><password>464d437ad</password><ControlParameters>"
							+ "<GetContracts>false</GetContracts></ControlParameters> </request></authenticateUser>";
				}
				else{
					urlParameters = "<authenticateUser><request><username>0170687912</username><password>Test123</password><ControlParameters>"
							+ "<GetContracts>false</GetContracts></ControlParameters> </request></authenticateUser>";
				}
			}
			dataConnection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(dataConnection.getOutputStream());
			wr.writeBytes(urlParameters);
			BufferedReader inputBuffer = new BufferedReader(
					new InputStreamReader(dataConnection.getInputStream()));
			String inputLine;
			List<String> cookies = dataConnection.getHeaderFields().get("Set-Cookie");
			//System.out.println(dataConnection.getHeaderFields());
			StringBuffer response = new StringBuffer();
			while ((inputLine = inputBuffer.readLine()) != null) {
				response.append(inputLine);
			}
			inputBuffer.close();		
			URL dataReqURL = new URL("https://api-"+environment+"."+customerBrand+".nl/selfservice/customer/getMeterDetails");
			HttpURLConnection dataConnection2 = (HttpURLConnection) dataReqURL.openConnection(proxy);		
			for (String cookie : cookies) {
				dataConnection2.setRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}	
			dataConnection2.setRequestMethod("GET");
			dataConnection2.setRequestProperty("User-Agent", USER_AGENT);
			dataConnection2.setRequestProperty("Accept-Language", "en-US,en;q=0.5");	
			dataConnection2.setRequestProperty("Access-Control-Allow-Credentials", "true");
			dataConnection2.setRequestProperty("Access-Control-Allow-Origin", "*");
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			Date todayDate = new Date();
			String todayDateAsString = dateFormatter.format(todayDate);
			urlParameters = "<GetMeterDetails><request><KeyDate>"+todayDateAsString+"</KeyDate><Address><HouseNr>"+houseNumber+"</HouseNr><HouseNrExt>"+houseExtension+"</HouseNrExt><Postcode>"+postCode+"</Postcode><Country>NL</Country></Address></request></GetMeterDetails>";
			dataConnection2.setDoOutput(true);	
			wr = new DataOutputStream(dataConnection2.getOutputStream());
			wr.writeBytes(urlParameters);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(dataConnection2.getInputStream()));
			response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			//System.out.println(response);
			in.close();
			//System.out.println(response);
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(response.toString())));
			org.w3c.dom.NodeList eanConnections = doc.getElementsByTagName("ConnectEAN");
			org.w3c.dom.NodeList energyType = doc.getElementsByTagName("EnergyType");
			APIGateWay apiGateWay = new APIGateWay();
			for(int connectionNumber=0;connectionNumber<eanConnections.getLength();connectionNumber++){
				String connectionInformation = apiGateWay.GetAddressOfEANNumber(customerBrand, environment, eanConnections.item(connectionNumber).getTextContent());
				if(connectionInformation.contains("SLM")){
					connectionDetails.add(eanConnections.item(connectionNumber).getTextContent()+";"+"SLM"+";"+energyType.item(connectionNumber).getAttributes().getNamedItem("code").getNodeValue());
				}
				else{
					connectionDetails.add(eanConnections.item(connectionNumber).getTextContent()+";"+"CVN"+";"+energyType.item(connectionNumber).getAttributes().getNamedItem("code").getNodeValue());
				}
			}
		}
		catch(Throwable t){
			System.out.println("Oops, something went wrong");
			System.out.println(t.toString());
		}
		return connectionDetails;
	}
	public String GetAddressOfEANNumber(String customerBrand,String environment,String eanNumber){
		String connectionDetails = "";
		if(customerBrand.toLowerCase().contains("ed")){
			customerBrand = "energiedirect";
		}
		else{
			customerBrand = "essent";
		}
		if(environment.toLowerCase().contains("acc")){
			environment = "acc";
		}
		else{
			environment = "tst";
		}
		try{
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.rwe.com", 8080));
			String urlParameters = "";
			DataOutputStream wr = null;
			String inputLine;
			StringBuffer response;
			if(this.cookies==null){
				URL authURL = new URL("https://api-"+environment+"."+customerBrand+".nl/selfservice/user/authenticateUser");
				HttpsURLConnection dataConnection = (HttpsURLConnection) authURL.openConnection(proxy);
				dataConnection.setRequestMethod("POST");
				dataConnection.setRequestProperty("User-Agent", USER_AGENT);
				dataConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");	
				dataConnection.setRequestProperty("Access-Control-Allow-Credentials", "true");
				dataConnection.setRequestProperty("Access-Control-Allow-Origin", "*");	
				if(environment.equals("acc")){
					if(customerBrand.equals("energiedirect")){
						urlParameters = "<authenticateUser><request><username>0612345678</username><password>786e2a34d</password><ControlParameters>"
								+ "<GetContracts>false</GetContracts></ControlParameters> </request></authenticateUser>";
					}
					else{
						urlParameters = "<authenticateUser><request><username>0171635896</username><password>Test123</password><ControlParameters>"
								+ "<GetContracts>false</GetContracts></ControlParameters> </request></authenticateUser>";
					}
				}
				else{
					if(customerBrand.equals("energiedirect")){
						urlParameters = "<authenticateUser><request><username>0132134741</username><password>464d437ad</password><ControlParameters>"
								+ "<GetContracts>false</GetContracts></ControlParameters> </request></authenticateUser>";
					}
					else{
						urlParameters = "<authenticateUser><request><username>0170687912</username><password>Test123</password><ControlParameters>"
								+ "<GetContracts>false</GetContracts></ControlParameters> </request></authenticateUser>";
					}
				}
				dataConnection.setDoOutput(true);
				wr = new DataOutputStream(dataConnection.getOutputStream());
				wr.writeBytes(urlParameters);
				BufferedReader inputBuffer = new BufferedReader(
						new InputStreamReader(dataConnection.getInputStream()));
				cookies = dataConnection.getHeaderFields().get("Set-Cookie");
				//System.out.println(dataConnection.getHeaderFields());
				response = new StringBuffer();
				while ((inputLine = inputBuffer.readLine()) != null) {
					response.append(inputLine);
				}
				inputBuffer.close();
			}
			URL dataReqURL = new URL("https://api-"+environment+"."+customerBrand+".nl/selfservice/customer/getMeterDetails");
			HttpURLConnection dataConnection2 = (HttpURLConnection) dataReqURL.openConnection(proxy);		
			for (String cookie : cookies) {
				dataConnection2.setRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}	
			dataConnection2.setRequestMethod("GET");
			dataConnection2.setRequestProperty("User-Agent", USER_AGENT);
			dataConnection2.setRequestProperty("Accept-Language", "en-US,en;q=0.5");	
			dataConnection2.setRequestProperty("Access-Control-Allow-Credentials", "true");
			dataConnection2.setRequestProperty("Access-Control-Allow-Origin", "*");
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			Date todayDate = new Date();
			String todayDateAsString = dateFormatter.format(todayDate);
			urlParameters = "<GetMeterDetails><request><KeyDate>"+todayDateAsString+"</KeyDate><ConnectEAN>"+eanNumber+"</ConnectEAN></request></GetMeterDetails>";
			dataConnection2.setDoOutput(true);	
			wr = new DataOutputStream(dataConnection2.getOutputStream());
			wr.writeBytes(urlParameters);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(dataConnection2.getInputStream()));
			response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			//System.out.println(response);
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(response.toString())));
			org.w3c.dom.NodeList postCode = doc.getElementsByTagName("Postcode");
			org.w3c.dom.NodeList houseNumber = doc.getElementsByTagName("HouseNr");
			org.w3c.dom.NodeList houseNumberExtension = doc.getElementsByTagName("HouseNrExt");
			org.w3c.dom.NodeList meterType = doc.getElementsByTagName("IsSmartMeter");
			if(houseNumberExtension.item(0).getTextContent().equals("")){
				if(meterType.item(0).getTextContent().equals("false")){
					connectionDetails = connectionDetails+(postCode.item(0).getTextContent()+" "+houseNumber.item(0).getTextContent()+" CVN");
				}
				else{
					connectionDetails = connectionDetails+(postCode.item(0).getTextContent()+" "+houseNumber.item(0).getTextContent()+" SLM");
				}
			}
			else{
				if(meterType.item(0).getTextContent().equals("false")){
					connectionDetails = connectionDetails+ (postCode.item(0).getTextContent()+" "+houseNumber.item(0).getTextContent()+"/"+houseNumberExtension.item(0).getTextContent()+" CVN");
				}
				else{
					connectionDetails = connectionDetails+ (postCode.item(0).getTextContent()+" "+houseNumber.item(0).getTextContent()+"/"+houseNumberExtension.item(0).getTextContent()+" SLM");
				}
			}
		}
		catch(Throwable t){
			System.out.println("Oops, something went wrong");
			System.out.println(t.toString());
		}
		return connectionDetails;
	
	}
}