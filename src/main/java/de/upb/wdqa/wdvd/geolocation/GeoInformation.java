package de.upb.wdqa.wdvd.geolocation;

public class GeoInformation {
	private long startAdress;
	private long endAdress;
	private String countryCode;
	String continentCode;
	String timeZone;
	String regionCode;
	String cityName;
	String countyName;
	String latitude;
	String longitude;
	
	public GeoInformation(long startAdress, long endAdress, String countryCode, String continentCode,
			String timeZone, String regionCode, String cityName, String countyName){
		this.startAdress = startAdress;
		this.endAdress = endAdress;
		this.countryCode = countryCode;
		this.continentCode = continentCode;
		this.timeZone = timeZone;
		this.regionCode = regionCode;
		this.cityName = cityName;
		this.countyName = countyName;		
	}
	
	public long getStartAdress(){
		return startAdress;
	}
	
	public long getEndAdress(){
		return endAdress;
	}
	
	public String getCountryCode(){
		return countryCode;
	}
	
	public String getContinentCode(){
		return continentCode;
	}
	
	public String getTimeZone(){
		return timeZone;
	}
	
	public String getRegionCode(){
		return regionCode;
	}
	
	public String getCityName(){
		return cityName;
	}
	
	public String getCountyName(){
		return countyName;
	}
	
	public String toString(){
		return "" + startAdress + ", " + endAdress + ": " + countryCode;
	}
}
