/**
 * @author Himanshi Aggarwal
 * ChatBot
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.*;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.Triple;

public class Main{
	WeatherDetails wd;
	private String query; 
	private final String serializedClassifier;
	private final  AbstractSequenceClassifier<CoreLabel> classifier ;	
	public Main(String query) throws ClassCastException, ClassNotFoundException, IOException{
		wd=new WeatherDetails();
		this.query=query;
		serializedClassifier= "classifiers/english.muc.7class.distsim.crf.ser.gz";
		classifier=	CRFClassifier.getClassifier(serializedClassifier);
	}
	public Main() throws ClassCastException, ClassNotFoundException, IOException{
		serializedClassifier= "classifiers/english.muc.7class.distsim.crf.ser.gz";
		classifier=	CRFClassifier.getClassifier(serializedClassifier);
	}
	public void fetchResults(){
		try{
			final String Query = "http://api.openweathermap.org/data/2.5/forecast?q="+wd.getLocation()+"&appid=a49c9961777e1e0e12d58e956f6656b3";		
			final URL url = new URL(Query);
			final URLConnection connection = url.openConnection();
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), StandardCharsets.UTF_8));
			String inputLine;
			final StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			JSONObject weather = null;
			JSONArray dataArray = null;
			int flag=0;
			if (response != null) {
				JSONObject obj = new JSONObject(response.toString());
				 dataArray=obj.getJSONArray("list"); 
				 for(int i=0;i<dataArray.length();i++){
					 JSONObject rec = dataArray.getJSONObject(i);
					 if(rec.getString("dt_txt").compareTo(wd.getDateFormat())>0){
						flag=1;
						weather = rec.getJSONObject("main"); 
						 wd.setTemperature(convertKtoF(weather.getDouble("temp")));
						 wd.setMax_Temperature(convertKtoF(weather.getDouble("temp_max")));
						 wd.setMin_Temperature(convertKtoF(weather.getDouble("temp_min")));
						 wd.setPressure(weather.getDouble("pressure"));
						 wd.setHumidity(weather.getDouble("humidity"));
						 JSONArray w=rec.getJSONArray("weather");
						 wd.setWeather(w.getJSONObject(0).getString("description"));
						 wd.setClouds(rec.getJSONObject("clouds").getDouble("all"));
						 display();
						 break;
					 }
				 }
			}
			if(flag==0){
				System.out.println("Bot:\tSorry no weather conditions found");
			}
		}
		catch(Exception e){
			System.out.println("Bot:\tSorry no weather data available.");
		}
			
	}
	public void fetchResults_today() throws IOException, JSONException, ClassCastException, ClassNotFoundException{
		try{
			final String Query = "http://api.openweathermap.org/data/2.5/weather?q="+wd.getLocation()+"&appid=a49c9961777e1e0e12d58e956f6656b3";		
			final URL url = new URL(Query);
			final URLConnection connection = url.openConnection();
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), StandardCharsets.UTF_8));
			String inputLine;
			final StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			JSONObject weather = null;
			JSONArray dataArray = null;
			if (response != null) {
				JSONObject obj = new JSONObject(response.toString());
				 weather = obj.getJSONObject("main"); 
				 dataArray=obj.getJSONArray("weather");
				 JSONObject rec = dataArray.getJSONObject(0);
				 wd.setTemperature(convertKtoF(weather.getDouble("temp")));
				 wd.setMax_Temperature(convertKtoF(weather.getDouble("temp_max")));
				 wd.setMin_Temperature(convertKtoF(weather.getDouble("temp_min")));
				 wd.setPressure(weather.getDouble("pressure"));
				 wd.setHumidity(weather.getDouble("humidity"));
				 wd.setWeather(rec.getString("description"));
				 wd.setClouds(obj.getJSONObject("clouds").getDouble("all"));
				 display_today();
			}
		}
		catch(Exception e){
			System.out.println("Bot:\tSorry no weather data available.");
		}
		
	}
	private double convertKtoF(double temperature) {
		double f;
		f=(9/5)*(temperature-273)+32;
		return f;
	}
	public void display_today(){
		if(query.toLowerCase().contains("rain")){
			display_rainy(query);
		}
		else if(query.toLowerCase().contains("cloud")){
			display_cloudy(query);
		}
		else{
			System.out.println("Bot:\tWeather in "+wd.getLocation()+" on "+wd.getdate()+" will be:");
			System.out.println("\tTemperature: "+wd.getTemperature()+"F\n\tPressure: "+wd.getPressure()+"\n\tHumidity: "+wd.getHumidity()
								+"\n\tMin Temperature: "+wd.getMin_Temperature()+"F\n\tMax Temperature: "+wd.getMax_Temperature()+"F\n\tConditions: "+wd.getWeather());
		}
	}
	public void display(){
		if(query.toLowerCase().contains("rain")){
			display_rainy(query);
		}
		else if(query.toLowerCase().contains("cloud")){
			display_cloudy(query);
		}
		else if(wd.getDate().equals("")&& wd.getTime().equals("")){
			System.out.println("Bot:\tWeather in "+wd.getLocation()+" on "+wd.getdate()+" will be:");
			System.out.println("\tTemperature: "+wd.getTemperature()+"F\n\tPressure: "+wd.getPressure()+"\n\tHumidity: "+wd.getHumidity()
								+"\n\tMin Temperature: "+wd.getMin_Temperature()+"F\n\tMax Temperature: "+wd.getMax_Temperature()+"F\n\tConditions: "+wd.getWeather());
		}
		else{
			System.out.println("Bot:\tWeather in "+wd.getLocation()+" on "+wd.getDate()+" at "+wd.getTime()+" will be:");
			System.out.println("\tTemperature: "+wd.getTemperature()+"F\n\tPressure: "+wd.getPressure()+"\n\tHumidity: "+wd.getHumidity()
								+"\n\tMin Temperature: "+wd.getMin_Temperature()+"F\n\tMax Temperature: "+wd.getMax_Temperature()+"F\n\tConditions: "+wd.getWeather());

		}
		
	}
	public void checkEntity() throws ClassCastException, ClassNotFoundException, IOException, JSONException{
        try{
        	List<Triple<String,Integer,Integer>> triples = classifier.classifyToCharacterOffsets(query);
            for (Triple<String,Integer,Integer> trip : triples) {
                if(trip.first().equals("DATE")){
                	wd.setDate(query.substring(trip.second(),trip.third));
                }
                if(trip.first().equals("TIME")){
                	wd.setTime(query.substring(trip.second(),trip.third));
                }
                if(trip.first().equals("LOCATION")){
                	wd.setLocation(query.substring(trip.second(),trip.third));
                }
             }
            
            if(query.toLowerCase().contains("today")){
            	if(wd.getTime().equals("")){
                	wd.setdate(new Date());
            		fetchResults_today();
            	}
            	else{
            		wd.setDate(new LocalDate().toString());
            		convertDateformat(wd.getTime(),"time");
            		fetchResults();
            	}
            }
            else if(query.toLowerCase().contains("tomorrow")){
            	if(wd.getTime().equals("")){
                	Date d=new Date();
                	Calendar c=Calendar.getInstance();
                	c.setTime(d);
                	c.add(Calendar.DATE, 1);
                	wd.setdate(c.getTime()); 
                	convertDateformat();
                	fetchResults();
                	}
            	else{
            		wd.setDate(new LocalDate().plusDays(1).toString());
            		convertDateformat(wd.getTime(),"time");
            		fetchResults();
            	}
            }
            else if(wd.getTime().equals("")){
            	if(wd.getDate().equals("")){
            		wd.setdate(new Date());
                	fetchResults_today();
            	}
            	else{
                	wd.setTime(new LocalTime().toString());
                	convertDateformat(wd.getDate(),"date");
                	fetchResults();
            	}
            }
            else{
            	convertDateformat(wd.getDate(),wd.getTime());
            	fetchResults();
            }
            
            
        }
        catch(Exception e){
        	System.out.println("Bot:\tProper format not entered ");
        }
		}
	private void convertDateformat(String convert, String string) throws ParseException {
		try{
			if(string=="date"){
				SimpleDateFormat spf = new SimpleDateFormat("MMM dd, yyyy");
			    Date newDate = spf.parse(convert);
			    spf = new SimpleDateFormat("yyyy-MM-dd");
			    wd.setDateFormat(spf.format(newDate)+" "+wd.getTime());
			}
			else if(string=="time"){
				SimpleDateFormat spf = new SimpleDateFormat("hh:mm a");
			    Date newDate = spf.parse(convert);
			    spf = new SimpleDateFormat("hh:mm:ss");
			    wd.setDateFormat(wd.getDate()+" "+spf.format(newDate));
			}
			else{
				SimpleDateFormat spf = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
			    Date newDate = spf.parse(convert+" "+string);
			    spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			    wd.setDateFormat(spf.format(newDate));
			}
		}catch(Exception e){
			System.out.println("Bot:\tProper format not entered");
		}
		
	}
	private void convertDateformat() throws ParseException{
		try{
			SimpleDateFormat spf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
		    Date newDate = spf.parse(wd.getdate().toLocaleString());
		    spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    wd.setDateFormat(spf.format(newDate));
	
		}
		catch(Exception e){
			System.out.println("Bot:\tWeather details are not available.");
		}
	   }
	private void display_rainy(String query) {
		if(wd.getWeather().toLowerCase().contains("rain")){
			System.out.println("Bot:\tYes, It might rain. Dont forget to carry your umbrella.");
			System.out.println("\tClouds: "+wd.getClouds());
		}
		else if(wd.getWeather().toLowerCase().contains("cloud")){
			System.out.println("Bot:\tYes, It might rain. Dont forget to carry your umbrella.");
			System.out.println("\tClouds: "+wd.getClouds());
		}
		else{
			System.out.println("Bot:\tNo it will be "+wd.getWeather());
			System.out.println("\tClouds: "+wd.getClouds());
		}
	}
	private void display_cloudy(String query) {
		if(wd.getWeather().toLowerCase().contains("cloud")){
			System.out.println("Bot:\tYes, It will be cloudy");
			System.out.println("\tClouds: "+wd.getClouds());
		}
		else{
			System.out.println("Bot:\tNo it will be "+wd.getWeather());
			System.out.println("\tClouds: "+wd.getClouds());
		}
	}
	public static void main(String args[]) throws Exception{
		Scanner scan=new Scanner(System.in);
		System.out.println("Bot:\tHello My Name is WeatherBot. \n\tWhat can I help you with?\n (Format Example:time: 00:00 pm day: Apr 23, 2017 or today).\n type exit to Exit" );
		while(true){
			System.out.print("User:\t");
			String query = scan.nextLine();
			if(query.toLowerCase().equals("exit")){
				System.out.println("Have a Wonderful Day!!");
				scan.close();
				System.exit(0);
			}
			Main m=new Main(query);
			m.checkEntity();
		}
	}
	
}
