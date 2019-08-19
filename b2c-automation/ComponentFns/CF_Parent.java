package ComponentFns;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

import resources.ComponentFns.CF_ParentHelper;

import com.rational.test.ft.*;
import com.rational.test.ft.object.interfaces.*;
import com.rational.test.ft.object.interfaces.SAP.*;
import com.rational.test.ft.object.interfaces.WPF.*;
import com.rational.test.ft.object.interfaces.dojo.*;
import com.rational.test.ft.object.interfaces.siebel.*;
import com.rational.test.ft.object.interfaces.flex.*;
import com.rational.test.ft.object.interfaces.generichtmlsubdomain.*;
import com.rational.test.ft.script.*;
import com.rational.test.ft.value.*;
import com.rational.test.ft.vp.*;
import com.rational.test.ft.vp.impl.TestDataTable;
import com.ibm.rational.test.ft.object.interfaces.sapwebportal.*;
/**
 * Description   : Functional Test Script
 * @author kamal_singla
 */
public class CF_Parent extends CF_ParentHelper
{
	public void testMain(Object[] args)
	{
		
	}
	public static int iValidObjectIndex=0;
	public static int iLoopCount=30;
	public static boolean nonMappableObject = false;
	public static boolean ignoreLefOffset = false;
	public enum PropertyType{
		Class,
		ClassName,
		ClassIndex,
		Id,
		Name,
		Text,
		ControlName,
		Value,
		Tooltip,
		Type,
		PlaceHolder,
		Tag,
		Method,
		ContentText,
		Title,
		datawidget,
		dataOption,
		dataAction,
		Cclass,
		DefaultValue,
		HREF,
		FieldAlias,
		ScreenLeft,
		Alt,
		URL,
		Source,
	}

	public enum Platform{
		SAP,
		WEB,
		WINDOW;
	}
	public static void ClickTab(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, TestObject parent)
	{
		if (platform.equals(Platform.SAP)){
			SAPGuiTabTestObject tab=(SAPGuiTabTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			tab.click();
		}
	}
	public static String ReadEditBoxText(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, TestObject parent)
	{
		String strTextBoxVal="";
		if (platform.equals(Platform.SAP)){
			SAPGuiTextTestObject editBox=(SAPGuiTextTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);

			if(!(editBox.getProperty(getPropertyType(PropertyType.Text, platform))==null)){
				strTextBoxVal= editBox.getProperty(getPropertyType(PropertyType.Text, platform)).toString();
			}

		}else if(platform.equals(Platform.WEB)){              
			TextGuiTestObject editBox=(TextGuiTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			if(!(editBox.getProperty(getPropertyType(PropertyType.Value, platform))==null)){
				strTextBoxVal= editBox.getProperty(getPropertyType(PropertyType.Value, platform)).toString();
			}      
		}
		return strTextBoxVal;
	}
	public static String ReadEditBoxText(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, RegularExpression propertyValue2, TestObject parent)
	{
		String strTextBoxVal="";
		if (platform.equals(Platform.SAP)){
			SAPGuiTextTestObject editBox=(SAPGuiTextTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);

			if(!(editBox.getProperty(getPropertyType(PropertyType.Text, platform))==null)){
				strTextBoxVal= editBox.getProperty(getPropertyType(PropertyType.Text, platform)).toString();
			}

		}else if(platform.equals(Platform.WEB)){              
			TextGuiTestObject editBox=(TextGuiTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			if(!(editBox.getProperty(getPropertyType(PropertyType.Value, platform))==null)){
				strTextBoxVal= editBox.getProperty(getPropertyType(PropertyType.Value, platform)).toString();
			}      
		}
		return strTextBoxVal;
	}
	public static void EnterText(Platform platform, PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, String textInput, TestObject parent)
	{
		if (platform.equals(Platform.SAP)){
			SAPGuiTextTestObject editBox=(SAPGuiTextTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.setText(textInput);

		}else if(platform.equals(Platform.WEB)){                                           
			TextGuiTestObject editBox=(TextGuiTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.setText(textInput);                      
		}
	}
	public static void SendText(Platform platform, PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, String textInput, TestObject parent)
	{
		if (platform.equals(Platform.SAP)){
			SAPGuiTextTestObject editBox=(SAPGuiTextTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.setText(textInput);

		}else if(platform.equals(Platform.WEB)){                                           
			GuiTestObject editBox=(GuiTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.click();
			for(int clearText=0;clearText<10;clearText++){
				getScreen().inputKeys("{BACKSPACE}");
			}
			getScreen().inputKeys(textInput);
		}
	}
	public static void EnterText(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, RegularExpression propertyValue2,String textInput, TestObject parent)
	{
		if (platform.equals(Platform.SAP)){
			SAPGuiTextTestObject editBox=(SAPGuiTextTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.setText(textInput);

		}else if(platform.equals(Platform.WEB)){              
			TextGuiTestObject editBox=(TextGuiTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.setText(textInput);             
		}
	}
	public static void ClickText(Platform platform, PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, TestObject parent)
	{
		if (platform.equals(Platform.SAP)){
			SAPGuiTextTestObject editBox=(SAPGuiTextTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.click();

		}else if(platform.equals(Platform.WEB)){                                           
			TextGuiTestObject editBox=(TextGuiTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.click();                      
		}
	}
	public static void ClickButton(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, TestObject parent)
	{
		if (platform.equals(Platform.SAP)){
			SAPGuiToggleTestObject button=(SAPGuiToggleTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.click();

		}else if(platform.equals(Platform.WEB)){                                           
			GuiTestObject button=(GuiTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.click();
		}
	}
	public static void ClickButton(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, RegularExpression propertyValue2, TestObject parent)
	{
		if (platform.equals(Platform.SAP)){
			SAPGuiToggleTestObject button=(SAPGuiToggleTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.click();

		}else if(platform.equals(Platform.WEB)){                                           
			GuiTestObject button=(GuiTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.click();
		}
	}
	public static void ClickButtonPoint(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, RegularExpression propertyValue2, TestObject parent,int pointX,int pointY)
	{
		if (platform.equals(Platform.SAP)){
			SAPGuiToggleTestObject button=(SAPGuiToggleTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.click();
		}else if(platform.equals(Platform.WEB)){                                           
			GuiTestObject button=(GuiTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.click(atPoint(pointX,pointY));
		}
	}
	public static void DoubleClickButtonPoint(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, RegularExpression propertyValue2, TestObject parent,int pointX,int pointY)
	{
		if (platform.equals(Platform.SAP)){
			SAPGuiToggleTestObject button=(SAPGuiToggleTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.doubleClick();
		}else if(platform.equals(Platform.WEB)){                                           
			GuiTestObject button=(GuiTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.doubleClick(atPoint(pointX,pointY));
		}
	}
	public static void ClickButtonPoint(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, TestObject parent,int pointX,int pointY)
	{
		if (platform.equals(Platform.SAP)){
			SAPGuiToggleTestObject button=(SAPGuiToggleTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.click();
		}else if(platform.equals(Platform.WEB)){                                           
			GuiTestObject button=(GuiTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.click(atPoint(pointX,pointY));
		}
	}
	public static void HoverControl(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, RegularExpression propertyValue2, TestObject parent)
	{
		if (platform.equals(Platform.SAP)){
			SAPGuiToggleTestObject button=(SAPGuiToggleTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.hover();

		}else if(platform.equals(Platform.WEB)){			
			GuiTestObject button=(GuiTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.hover();
		}
		else if(platform.equals(Platform.WINDOW)){			
			GuiTestObject button=(GuiTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.hover();
		}

	}
	public static void SelectCheckBox(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, TestObject parent)
	{

		if (platform.equals(Platform.SAP)){
			SAPGuiToggleTestObject checkBox=(SAPGuiToggleTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			do{
				checkBox.clickToState(SELECTED);
			}while(!checkBox.getState().toString().equalsIgnoreCase("SELECTED"));        

		}else if(platform.equals(Platform.WEB)){              
			ToggleGUITestObject checkBox=(ToggleGUITestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			do{
				checkBox.clickToState(SELECTED);
			}while(!checkBox.getState().toString().equalsIgnoreCase("SELECTED"));
		}
	}
	public static void SelectCheckBox(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, RegularExpression propertyValue2, TestObject parent)
	{

		if (platform.equals(Platform.SAP)){
			SAPGuiToggleTestObject checkBox=(SAPGuiToggleTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			do{
				checkBox.clickToState(SELECTED);
			}while(!checkBox.getState().toString().equalsIgnoreCase("SELECTED"));        

		}else if(platform.equals(Platform.WEB)){              
			ToggleGUITestObject checkBox=(ToggleGUITestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			do{
				checkBox.clickToState(SELECTED);
			}while(!checkBox.getState().toString().equalsIgnoreCase("SELECTED"));
		}
	}
	public static void UnSelectCheckBox(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, TestObject parent)
	{

		if (platform.equals(Platform.SAP)){
			SAPGuiToggleTestObject checkBox=(SAPGuiToggleTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			do{
				checkBox.deselect();
			}while(!checkBox.getState().toString().equalsIgnoreCase("NOT_SELECTED"));        

		}else if(platform.equals(Platform.WEB)){              
			ToggleGUITestObject checkBox=(ToggleGUITestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			do{
				checkBox.deselect();
			}while(!checkBox.getState().toString().equalsIgnoreCase("NOT_SELECTED"));
		}
	}
	public static void SelectDropDownValue(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2,String dropDownValue, TestObject parent)
	{

		if (platform.equals(Platform.SAP)){
			SAPGuiComboBoxTestObject editBox=(SAPGuiComboBoxTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.setValue(dropDownValue);

		}else if(platform.equals(Platform.WEB)){              
			SelectGuiSubitemTestObject editBox=(SelectGuiSubitemTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.select(dropDownValue);
		}
	}
	public static void SelectDropDownValue(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, RegularExpression propertyValue2,String dropDownValue, TestObject parent)
	{

		if (platform.equals(Platform.SAP)){
			SAPGuiComboBoxTestObject editBox=(SAPGuiComboBoxTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.setValue(dropDownValue);

		}else if(platform.equals(Platform.WEB)){              
			SelectGuiSubitemTestObject editBox=(SelectGuiSubitemTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.select(dropDownValue);
		}
	}
	public static void SelectRadioButton(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, TestObject parent)
	{
		if (platform.equals(Platform.SAP)){
			SAPGuiToggleTestObject radio=(SAPGuiToggleTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			do{
				radio.clickToState(SELECTED);
			}while(!radio.getState().toString().equalsIgnoreCase("SELECTED"));        

		}else if(platform.equals(Platform.WEB)){              
			ToggleGUITestObject radio=(ToggleGUITestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			do{
				radio.clickToState(SELECTED);
			}while(!radio.getState().toString().equalsIgnoreCase("SELECTED"));
		}
	}
	public static void SelectRadioButton(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, RegularExpression propertyValue2, TestObject parent)
	{
		if (platform.equals(Platform.SAP)){
			SAPGuiToggleTestObject radio=(SAPGuiToggleTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			do{
				radio.clickToState(SELECTED);
			}while(!radio.getState().toString().equalsIgnoreCase("SELECTED"));        

		}else if(platform.equals(Platform.WEB)){              
			ToggleGUITestObject radio=(ToggleGUITestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			do{
				radio.clickToState(SELECTED);
			}while(!radio.getState().toString().equalsIgnoreCase("SELECTED"));
		}
	}
	public static int getRowsofTable(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, TestObject parent)
	{
		int rows=0;
		if (platform.equals(Platform.SAP)){
			TestDataTable sapTable=(TestDataTable)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent).getTestData("list");
			rows=sapTable.getRowCount();
			System.out.println("SAP Rows"+rows);

		}else if(platform.equals(Platform.WEB)){

			ITestDataTable webTable=(ITestDataTable)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent).getTestData("contents");
			rows=webTable.getRowCount();
			System.out.println(rows);
		}
		return rows;
	}
	public static int getColumnsofTable(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, TestObject parent)
	{
		int columns=0;
		if (platform.equals(Platform.SAP)){
			TestDataTable sapTable=(TestDataTable)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent).getTestData("list");
			columns=sapTable.getColumnCount();
			System.out.println("SAP Columns"+columns);

		}else if(platform.equals(Platform.WEB)){

			ITestDataTable webTable=(ITestDataTable)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent).getTestData("contents");
			columns=webTable.getColumnCount();
			System.out.println(columns);
		}
		return columns;
	}
	public static TestObject getCurrentObject(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, TestObject parent){

		TestObject currentObject=null;
		try{

			int iCount=0;
			do{
				if(CheckForObject(parent)){

					TestObject[] testObjects;
					if(nonMappableObject)
						testObjects= (TestObject[])parent.find(atDescendant(getPropertyType(propertyType1,platform),propertyValue1,getPropertyType(propertyType2,platform),propertyValue2), false);
					else
						testObjects= (TestObject[])parent.find(atDescendant(getPropertyType(propertyType1,platform),propertyValue1,getPropertyType(propertyType2,platform),propertyValue2));

					System.out.println("Number of objects found in "+iCount+"th try for '"+ propertyValue2+ "' = "+testObjects.length); 
					if(testObjects.length==1){
						if(nonMappableObject){
							currentObject = testObjects[0];
							break;
						}
						else{
							if(getValidObjects(testObjects,platform).size()==0){
								sleep(3);
								iCount++;
								continue;
							}
							currentObject=getValidObjects(testObjects,platform).get(0);//testObjects[0];
							if(CheckForObject(currentObject)){
								break;                            
							}else{
								sleep(3);
								iCount++;
								continue;
							}
						}
					}else if(testObjects.length>1){
						ArrayList< TestObject> lstValid=getValidObjects(testObjects,platform);
						System.out.println("Number of valid objects for "+propertyValue2+" = "+lstValid.size());
						if(lstValid.size()==0){
							sleep(3);
							iCount++;
							continue;
						}
						currentObject= lstValid.get(iValidObjectIndex);                                    
						if(CheckForObject(currentObject)){
							break;                            
						}else{
							sleep(3);
							iCount++;
							continue;
						}
					}else{
						System.out.println("In else for  - "+propertyValue2);
						sleep(3);
						iCount++;
						continue;
					}
				}else{
					System.out.println("Parent not found for "+iCount+"th try");
					sleep(3);
					iCount++;
					continue;
				}
			}while(iCount<=iLoopCount);

		}catch(Throwable t){
			currentObject=null;
			t.printStackTrace();
		} finally{
			iValidObjectIndex=0;
			iLoopCount=30;
			nonMappableObject = false;
			ignoreLefOffset = false;
		}

		return currentObject;
	}
	public static TestObject getCurrentObject(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, RegularExpression propertyValue2, TestObject parent){

		TestObject currentObject=null;
		try{

			int iCount=0;
			do{
				if(CheckForObject(parent)){
					TestObject[] testObjects= (TestObject[])parent.find(atDescendant(getPropertyType(propertyType1,platform),propertyValue1,getPropertyType(propertyType2,platform),propertyValue2));

					System.out.println("Number of objects found in "+iCount+"th try for '"+ propertyValue2+ "' = "+testObjects.length); 
					if(testObjects.length==1){
						if(getValidObjects(testObjects,platform).size()==0){
							sleep(3);
							iCount++;
							continue;
						}
						currentObject=getValidObjects(testObjects,platform).get(0);//testObjects[0];
						if(CheckForObject(currentObject)){
							break;                            
						}else{
							sleep(3);
							iCount++;
							continue;
						}
					}else if(testObjects.length>1){
						ArrayList< TestObject> lstValid=getValidObjects(testObjects,platform);
						System.out.println("Number of valid objects for "+propertyValue2+" = "+lstValid.size());
						if(lstValid.size()==0){
							sleep(3);
							iCount++;
							continue;
						}
						currentObject= lstValid.get(iValidObjectIndex);                                    
						if(CheckForObject(currentObject)){
							break;                            
						}else{
							sleep(3);
							iCount++;
							continue;
						}
					}else{
						System.out.println("In else for  - "+propertyValue2);
						sleep(3);
						iCount++;
						continue;
					}
				}else{
					System.out.println("Parent not found for "+iCount+"th try");
					sleep(3);
					iCount++;
					continue;
				}
			}while(iCount<=iLoopCount);

		}catch(Throwable t){
			currentObject=null;
			t.printStackTrace();
		} finally{
			iValidObjectIndex=0;
			iLoopCount=30;
		}

		return currentObject;
	}
	public static ArrayList<TestObject>  getValidObjects(TestObject[] objList){

		ArrayList<TestObject> lstOutputObjects= new ArrayList<TestObject>();
		try{
			for (int i=0;i<objList.length;i++){

				if((!objList[i].getProperty(".offsetHeight").toString().equalsIgnoreCase("0"))&&(!objList[i].getProperty(".screenLeft").toString().equalsIgnoreCase("0")))
				{
					lstOutputObjects.add(objList[i]);
				}                   
			}
		}
		catch(Exception e){
			e.printStackTrace();
			lstOutputObjects.clear();
		}
		return lstOutputObjects;
	}
	public static ArrayList<TestObject>  getValidObjects(TestObject[] objList, Platform platform){

		ArrayList<TestObject> lstOutputObjects= new ArrayList<TestObject>();
		try{
			System.out.println("In getValidObjects");
			for (int i=0;i<objList.length;i++){

				if(platform.equals(Platform.WEB)){
					if((ignoreLefOffset || !objList[i].getProperty(".offsetHeight").toString().equalsIgnoreCase("0")))
					{
						if((ignoreLefOffset || !objList[i].getProperty(".screenLeft").toString().equalsIgnoreCase("0"))){
							lstOutputObjects.add(objList[i]);
						}
					} 
				}else{
					lstOutputObjects.add(objList[i]);
				}
			}

			System.out.println(lstOutputObjects);

		}
		catch(Exception e){
			e.printStackTrace();
			lstOutputObjects.clear();
		}
		return lstOutputObjects;
	}
	public static String getPropertyType(PropertyType strKey, Platform platform){

		try{
			HashMap<PropertyType,String> PropertyMap= new LinkedHashMap<PropertyType,String>();

			if (platform.equals(Platform.SAP)){
				PropertyMap.put(PropertyType.Class, ".class");
				PropertyMap.put(PropertyType.ClassName, ".className");
				PropertyMap.put(PropertyType.Text, "Text");
				PropertyMap.put(PropertyType.Name, "Name");
				PropertyMap.put(PropertyType.Tooltip, "Tooltip");
				PropertyMap.put(PropertyType.Id, "Id");
				PropertyMap.put(PropertyType.Title, "Title");

			}else if(platform.equals(Platform.WEB)){
				PropertyMap.put(PropertyType.Class, ".class");
				PropertyMap.put(PropertyType.Cclass, "class");
				PropertyMap.put(PropertyType.ClassIndex, ".classIndex");
				PropertyMap.put(PropertyType.ClassName, ".className");
				PropertyMap.put(PropertyType.Name, ".name");
				PropertyMap.put(PropertyType.Id, ".id");
				PropertyMap.put(PropertyType.Value, ".value");
				PropertyMap.put(PropertyType.Text, ".text");
				PropertyMap.put(PropertyType.Type, ".type");
				PropertyMap.put(PropertyType.PlaceHolder, "placeholder");
				PropertyMap.put(PropertyType.Tag, ".tag");
				PropertyMap.put(PropertyType.Method, "method");
				PropertyMap.put(PropertyType.ContentText, ".contentText"); 
				PropertyMap.put(PropertyType.Title, ".title"); 
				PropertyMap.put(PropertyType.datawidget,"data-widget");
				PropertyMap.put(PropertyType.dataOption,".data-option-product-type");
				PropertyMap.put(PropertyType.dataAction,"data-action");
				PropertyMap.put(PropertyType.DefaultValue,".defaultValue");
				PropertyMap.put(PropertyType.HREF,".href");
				PropertyMap.put(PropertyType.FieldAlias,"FieldAlias");
				PropertyMap.put(PropertyType.ScreenLeft, ".screenLeft");
				PropertyMap.put(PropertyType.Alt, ".alt");
				PropertyMap.put(PropertyType.URL, ".url");
				PropertyMap.put(PropertyType.Source, ".src");
			}



			return PropertyMap.get(strKey);
		}catch(Exception e){
			return null;
		}
	}
	public static boolean CheckForObject(TestObject control)
	{
		try{
			control.waitForExistence(5,1);

		}catch(Exception exception)
		{
			logError("Exception "+exception+" occured in CheckForObject");          
			return false;

		}

		boolean status = control.exists();

		if (status == true){
			return true;

		}else{
			return false;

		}

	}
	public static void SelectDropDownValue(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2,int dropDownValue, TestObject parent)
	{

		if (platform.equals(Platform.SAP)){
			SAPGuiComboBoxTestObject editBox=(SAPGuiComboBoxTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.setValue("");

		}else if(platform.equals(Platform.WEB)){              
			SelectGuiSubitemTestObject editBox=(SelectGuiSubitemTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.select(dropDownValue);
		}
		else if(platform.equals(Platform.WINDOW)){                                
			TextSelectGuiSubitemTestObject editBox=(TextSelectGuiSubitemTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.select(dropDownValue);
		}
	}
	public static void SelectDropDownValue(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, RegularExpression propertyValue2,int dropDownValue, TestObject parent)
	{

		if (platform.equals(Platform.SAP)){
			SAPGuiComboBoxTestObject editBox=(SAPGuiComboBoxTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.setValue("");

		}else if(platform.equals(Platform.WEB)){              
			SelectGuiSubitemTestObject editBox=(SelectGuiSubitemTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.select(dropDownValue);
		}
		else if(platform.equals(Platform.WINDOW)){                                
			TextSelectGuiSubitemTestObject editBox=(TextSelectGuiSubitemTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			editBox.select(dropDownValue);
		}
	}
	public static void DoubleClickButton(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, TestObject parent)
	{
		if (platform.equals(Platform.SAP)){
			SAPGuiTextTestObject button=(SAPGuiTextTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.doubleClick();

		}else if(platform.equals(Platform.WEB)){			
			GuiTestObject button=(GuiTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.doubleClick();
		}
	}
	public static boolean ObjectExists(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, TestObject parent)
	{
		if (platform.equals(Platform.SAP)){
			TestObject randomOject=(TestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			if(randomOject != null){
				return true;
			}  
			else{
				return false;
			}

		}else if(platform.equals(Platform.WEB)){              
			TestObject randomOject=(TestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			if(randomOject != null){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}
	public static boolean nonMappableObjectExists(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, TestObject parent)
	{
		if (platform.equals(Platform.SAP)){
			nonMappableObject = true;
			TestObject randomOject=(TestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			if(randomOject != null){
				return true;
			}  
			else{
				return false;
			}

		}else if(platform.equals(Platform.WEB)){
			nonMappableObject = true;
			TestObject randomOject=(TestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			if(randomOject != null){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}
	public static boolean ObjectExists(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, RegularExpression propertyValue2, TestObject parent)
	{
		if (platform.equals(Platform.SAP)){
			TestObject randomOject=(TestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			if(randomOject != null){
				return true;
			}  
			else{
				return false;
			}

		}else if(platform.equals(Platform.WEB)){              
			TestObject randomOject=(TestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			if(randomOject != null){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}
	public static int getObjectCount(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, TestObject parent){

		int iValidObjectCount=0;

		try{

			int iCount=0;
			do{
				if(CheckForObject(parent)){
					TestObject[] testObjects= (TestObject[])parent.find(atDescendant(getPropertyType(propertyType1,platform),propertyValue1,getPropertyType(propertyType2,platform),propertyValue2));

					System.out.println("Number of objects found in "+iCount+"th try for '"+ propertyValue2+ "' = "+testObjects.length); 
					if(testObjects.length==1){
						int iTempSize=getValidObjects(testObjects,platform).size();
						if(iTempSize==0){
							sleep(3);
							iCount++;
							continue;
						}  
						iValidObjectCount=iTempSize;
						break;
					}else if(testObjects.length>1){
						ArrayList< TestObject> lstValid=getValidObjects(testObjects,platform);
						System.out.println("Number of valid objects for "+propertyValue2+" = "+lstValid.size());
						if(lstValid.size()==0){
							sleep(3);
							iCount++;
							continue;
						} 
						iValidObjectCount=lstValid.size();
						break;
					}else{
						System.out.println("In else for  - "+propertyValue2);
						sleep(3);
						iCount++;
						continue;
					}
				}else{
					System.out.println("Parent not found for "+iCount+"th try");
					sleep(3);
					iCount++;
					continue;
				}
			}while(iCount<=iLoopCount);

		}catch(Throwable t){
			t.printStackTrace();
		}finally{
			iLoopCount=30;
		}

		return iValidObjectCount;
	}
	public static int getObjectCount(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, RegularExpression propertyValue2, TestObject parent){

		int iValidObjectCount=0;

		try{

			int iCount=0;
			do{
				if(CheckForObject(parent)){
					TestObject[] testObjects= (TestObject[])parent.find(atDescendant(getPropertyType(propertyType1,platform),propertyValue1,getPropertyType(propertyType2,platform),propertyValue2));

					System.out.println("Number of objects found in "+iCount+"th try for '"+ propertyValue2+ "' = "+testObjects.length); 
					if(testObjects.length==1){
						int iTempSize=getValidObjects(testObjects).size();
						if(iTempSize==0){
							sleep(3);
							iCount++;
							continue;
						}  
						iValidObjectCount=iTempSize;
						break;
					}else if(testObjects.length>1){
						ArrayList< TestObject> lstValid=getValidObjects(testObjects);
						System.out.println("Number of valid objects for "+propertyValue2+" = "+lstValid.size());
						if(lstValid.size()==0){
							sleep(3);
							iCount++;
							continue;
						} 
						iValidObjectCount=lstValid.size();
						break;
					}else{
						System.out.println("In else for  - "+propertyValue2);
						sleep(3);
						iCount++;
						continue;
					}
				}else{
					System.out.println("Parent not found for "+iCount+"th try");
					sleep(3);
					iCount++;
					continue;
				}
			}while(iCount<=iLoopCount);

		}catch(Throwable t){
			t.printStackTrace();
		}finally{
			iLoopCount=30;
		}

		return iValidObjectCount;
	}
	public static int getRowsofTable(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, RegularExpression propertyValue2, TestObject parent)
	{
		int rows=0;
		if (platform.equals(Platform.SAP)){
			TestDataTable sapTable=(TestDataTable)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent).getTestData("list");
			rows=sapTable.getRowCount();
			System.out.println("SAP Rows"+rows);

		}else if(platform.equals(Platform.WEB)){

			ITestDataTable webTable=(ITestDataTable)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent).getTestData("contents");
			rows=webTable.getRowCount();
			System.out.println(rows);
		}
		return rows;
	}
	public static void HoverControl(Platform platform,PropertyType propertyType1, String propertyValue1, PropertyType propertyType2, String propertyValue2, TestObject parent)
	{
		if (platform.equals(Platform.SAP)){
			SAPGuiToggleTestObject button=(SAPGuiToggleTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.hover();

		}else if(platform.equals(Platform.WEB)){                                         
			GuiTestObject button=(GuiTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.hover();
		}
		else if(platform.equals(Platform.WINDOW)){                                
			GuiTestObject button=(GuiTestObject)getCurrentObject( platform, propertyType1,  propertyValue1,  propertyType2,  propertyValue2,  parent);
			button.hover();
		}
	}
}