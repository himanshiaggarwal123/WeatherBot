import java.util.*;
public class WeatherDetails {
	private String Location;
	private String Date;
	private String Time;
	private Date date;
	private double Temperature;
	private double Max_Temperature;
	private String weather;
	private double Min_Temperature;
	private double Pressure;
	private double Humidity;
	private double clouds;
	private long epoch;
	private String dateformat;
	public WeatherDetails(){
		Location ="Richardson,Tx";
		Date="";
		Time="";
		date=new Date();
	}
	public void setLocation(String Location){
		this.Location=Location;
	}
	public String getLocation(){
		return Location;
	}
	public void setDate(String Date){
		this.Date=Date;
	}
	public String getDate(){
		return Date;
	}
	public void setTime(String Time){
		this.Time=Time;
	}
	public String getTime(){
		return Time;
	}
	public void setTemperature(double Temperature){
		this.Temperature=Temperature;
	}
	public double getTemperature(){
		return Temperature;
	}
	public void setMax_Temperature(double Max_Temperature){
		this.Max_Temperature=Max_Temperature;
	}
	public double getMax_Temperature(){
		return Max_Temperature;
	}
	public void setMin_Temperature(double Min_Temperature){
		this.Min_Temperature=Min_Temperature;
	}
	public double getMin_Temperature(){
		return Min_Temperature;
	}
	public void setHumidity(double Humidity){
		this.Humidity=Humidity;
	}
	public double getHumidity(){
		return Humidity;
	}
	public void setPressure(double Pressure){
		this.Pressure=Pressure;
	}
	public double getPressure(){
		return Pressure;
	}
	public void setdate(Date date){
		this.date=date;
	}
	public Date getdate(){
		return date;
	}
	public void setClouds(double clouds){
		this.clouds=clouds;
	}
	public double getClouds(){
		return clouds;
	}
	public void setWeather(String weather){
		this.weather=weather;
	}
	public String getWeather(){
		return weather;
	}
	public void setEpoch(long epoch){
		this.epoch=epoch;
	}
	public long getEpoch(){
		return epoch;
	}
	public void setDateFormat(String dateformat){
		this.dateformat=dateformat;
	}
	public String getDateFormat(){
		return dateformat;
	}
}
