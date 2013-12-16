package Brightedge;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
/*
 * Ying Li liying@cmu.edu
 * 
 */
public class Crawler {
	String baseURL = "http://www.kidswithfoodallergies.org/resourcespre.php?id=62&title=Peanut_allergy_avoidance_list";
		//	"http://www.peta.org/living/vegetarian-living/animal-ingredients-list.aspx";
	//String baseURL = "http://allspicerestaurant.com/";
	
	String db = "junkdb";
	MySQL ms = null;
	ArrayList<String> menus= new ArrayList<String>();
	private String contactURL;
	private String title;
	private String address;
	private String phone;

	private String table="vegan";
	
	public static void main(String[] args){
		Crawler html = new Crawler();
		//html.searchURLs();
		
		//html.getAddr();
		
		//html.createTable("vega");
		html.getItem();
		
	}
	
	public Crawler(){
		this.ms = new MySQL();
		
		
	}
	
	//document.select("a:matchesOwn((?i).*?menu.*?)").first().attr("href");
	public void getItem(){
		
		Document document = get(this.baseURL);

		/* failure response*/
		if (document == null) {
			System.out.println("null response failure.");
			return;
		}
		//System.out.println(document.html());
		/*Element app = document.getElementById("fragment-19681501");
		if(app == null){
			System.out.println("no  item found");
			
			System.exit(1);
			
		}
		
		*/
		
		Elements es=document.select(":matchesOwn(.*?Arachic oil.*?)");
		Elements app = es.select("td br");
		System.out.println(">>name "+es.html());
		//for(int i=1;i< es.size();i++){
		/*for(Element el:es){
			//Element el= es.get(i);
			
				String s=el.text();
				//int j=s.indexOf(".");
				//String text=s.substring(0, j);
				System.out.println(">>"+s);
				//insert(text);
			
			
		}*/
		
		
		/* using Jsoup Convert Java String to DOM document*/
		
		/* Find the elements containing number of results */
		//Element sortFiltersBox = document.getElementById("sortFiltersBox");
		//System.out.println("Searching Menu href .........");
	}
	
public void getMENUJavascript()  {
    final WebClient webClient = new WebClient();
    
    // Get the first page
     
	try {
		
		
		
		// ScriptableObject form = page1.getScriptObject();
		/*
		Document doc =get(this.menuURL);
		Element menu =doc.select("h1:matchesOwn((?i)menu)").first();
		String url = menu.parent().select("script").attr("src");
	   
	    Document jsdoc =get(url);
	    System.out.println("------"+jsdoc);*/
	    
	     System.exit(1);
	} catch (Exception  e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     
    // Get the form that we are dealing with and within that form, 
    // find the submit button and the field that we want to change.
   

    //final HtmlSubmitInput button = form.getInputByName("submitbutton");
    //final HtmlTextInput textField = form.getInputByName("userid");

    // Change the value of the text field
    //textField.setValueAttribute("root");

    // Now submit the form by clicking the button and get back the second page.
   // final HtmlPage page2 = button.click();

    webClient.closeAllWindows();
}
private void getMENUSub(Element app) {
	Element parent = app.parent();
	String parentTag = null;
	if(!(parentTag = parent.tagName()).equals("div")){
		parent = parent.parent();
		
	}
		
	/*save each subpage uri into an ArrayList<String> menus*/
	for(Element ele: parent.select("a"))
		menus.add(ele.attr("href"));
	
	for(String href:menus){
		String url = this.baseURL+href;
		Document doc = get(url);

		/* failure response*/
		if (doc == null) {
			System.out.println("null response failure.");
			return;
		}
		
		Elements apps  =	doc.select(parentTag);
		for(Element hh:apps){
			String text = hh.html();
			
			insert(text);
		}
	}

	}

	private void insert(String text){
		
		
				try {String createTable = //"DROP TABLE REST;" +
						"CREATE TABLE IF NOT EXISTS "+ this.db+"."+this.table+
						
						"(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
						"text char(100) not null" +
									 ");";
				ms.update(createTable);
				
				
					String st = "INSERT INTO "+this.db+"."+table+" (text) " +
							 " VALUES(\'"+text+"\'"+");";
					System.out.println(st);
					ms.update(st);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	}
	public void dropTable(String table){
		
		try{
			String st = "DROP TABLE IF EXISTS "+this.db+"."+ table+";" ;
		System.out.println(st);
		ms.update(st);
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void createTable(){
		try {
			//dropTable("restaurant");
			String createTable = //"DROP TABLE REST;" +
					"CREATE TABLE IF NOT EXISTS "+ this.db+"."+this.table+
					
					"(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
					"item char(100), "+
					//+			 "address char(100),"+
						//		 "name char(100) not null" +
								 ");";
			ms.update(createTable);
			
			/*String st = "INSERT INTO "+this.db+"."+"restaurant (phone,address,name) VALUES(\'"
					+this.phone+"\',\'"+this.address+"\',\'"+ this.title+"\');";
			System.out.println(st);
			ResultSet rs = ms.update(st).getGeneratedKeys();
		    rs.next();
			this.rid = rs.getInt(1);
			System.out.println("-----\n restaurant id is "+this.rid);*/
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Matcher regExFind(String pat, String  mat){
		 	Pattern pattern = Pattern.compile(pat);
		 
            Matcher matcher = 
            pattern.matcher(mat);
 
            
            if(matcher.find()){
            	
				//System.out.println("regexp found >>"+matcher.group());
				return matcher;
				 
            }
			return null;
	}
	private void getAddr(){
		/* searching url */
		//+"&KW="+keyword.replace(" ", "%20");;
		if(this.contactURL == null)
			return;
		Document document = get(this.contactURL);

		/* failure response*/
		if (document == null) {
			System.out.println("null response failure.");
			return ;
		}
		String addrPat = "\\d{1,}.*\\s[a-zA-Z]{2,30},\\s[a-zA-Z]{2,15}\\s?";
		
		String phonePat = "\\(?(\\d{3})\\).?-?(\\d{3})-(\\d{4})";
		Element phone= document.select(":matchesOwn((?i)"+phonePat+")").first();
		String text = phone.text();
		//Matcher matcher = regExFind(,text);
		//String[] res = text.split("CA", 0);
		this.address = regExFind(addrPat,text).group();
		this.phone = regExFind(phonePat,text).group();
		System.out.println("phone found >"+this.phone);
		System.out.println("address found >"+this.address);
		return;
	}
	
	
	private int searchURLs(){
		/* searching url */
		String url = baseURL;
		//+"&KW="+keyword.replace(" ", "%20");;
				
		Document document = get(url);

		/* failure response*/
		if (document == null) {
			System.out.println("null response failure.");
			return 0;
		}

		/* using Jsoup Convert Java String to DOM document*/
		
		/* Find the elements containing number of results */
		//Element sortFiltersBox = document.getElementById("sortFiltersBox");
		
		
		this.title = document.title();
		//System.out.println(this.title);
		//elements directly contains ex, case insensitve
		//System.out.println("----\n"+document.select("a:matchesOwn((?i).*?menu.*?)").first().attr("href"));
		
		//this.menuURL = baseURL+document.select("a:matchesOwn((?i).*?menu.*?)").first().attr("href");
				
		this.contactURL=baseURL+document.select("a:matchesOwn((?i).*?contact us.*?)").first().attr("href");
				
		
		return 0;
	}
	
	
	
	public Document  get(String baseURL){
		Document doc =  null;
		try {
			
			//.data("content-type","application/javascript")
			doc = Jsoup.connect(baseURL).get();
			String title = doc.title();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return doc;
	}
	
}

	

