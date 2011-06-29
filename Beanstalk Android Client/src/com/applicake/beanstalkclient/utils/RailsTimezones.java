package com.applicake.beanstalkclient.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

public class RailsTimezones {

	@SuppressWarnings("serial")
	private static final Map<String, String> railsTZtoJavaTZ = new LinkedHashMap<String, String>() {
		{
			put("", "no time zone");
			put("International Date Line West", "Pacific/Midway");
			put("Midway Island", "Pacific/Midway");
			put("Samoa", "Pacific/Pago_Pago");
			put("Hawaii", "Pacific/Honolulu");
			put("Alaska", "America/Juneau");
			put("Pacific Time (US & Canada)", "America/Los_Angeles");
			put("Tijuana", "America/Tijuana");
			put("Mountain Time (US & Canada)", "America/Denver");
			put("Arizona", "America/Phoenix");
			put("Chihuahua", "America/Chihuahua");
			put("Mazatlan", "America/Mazatlan");
			put("Central Time (US & Canada)", "America/Chicago");
			put("Saskatchewan", "America/Regina");
			put("Guadalajara", "America/Mexico_City");
			put("Mexico City", "America/Mexico_City");
			put("Monterrey", "America/Monterrey");
			put("Central America", "America/Guatemala");
			put("Eastern Time (US & Canada)", "America/New_York");
			put("Indiana (East)", "America/Indiana/Indianapolis");
			put("Bogota", "America/Bogota");
			put("Lima", "America/Lima");
			put("Quito", "America/Lima");
			put("Atlantic Time (Canada)", "America/Halifax");
			put("Caracas", "America/Caracas");
			put("La Paz", "America/La_Paz");
			put("Santiago", "America/Santiago");
			put("Newfoundland", "America/St_Johns");
			put("Brasilia", "America/Sao_Paulo");
			put("Buenos Aires", "America/Argentina/Buenos_Aires");
			put("Georgetown", "America/Argentina/San_Juan");
			put("Greenland", "America/Godthab");
			put("Mid-Atlantic", "Atlantic/South_Georgia");
			put("Azores", "Atlantic/Azores");
			put("Cape Verde Is.", "Atlantic/Cape_Verde");
			put("Dublin", "Europe/Dublin");
			put("Edinburgh", "Europe/Dublin");
			put("Lisbon", "Europe/Lisbon");
			put("London", "Europe/London");
			put("Casablanca", "Africa/Casablanca");
			put("Monrovia", "Africa/Monrovia");
			put("UTC", "Etc/UTC");
			put("Belgrade", "Europe/Belgrade");
			put("Bratislava", "Europe/Bratislava");
			put("Budapest", "Europe/Budapest");
			put("Ljubljana", "Europe/Ljubljana");
			put("Prague", "Europe/Prague");
			put("Sarajevo", "Europe/Sarajevo");
			put("Skopje", "Europe/Skopje");
			put("Warsaw", "Europe/Warsaw");
			put("Zagreb", "Europe/Zagreb");
			put("Brussels", "Europe/Brussels");
			put("Copenhagen", "Europe/Copenhagen");
			put("Madrid", "Europe/Madrid");
			put("Paris", "Europe/Paris");
			put("Amsterdam", "Europe/Amsterdam");
			put("Berlin", "Europe/Berlin");
			put("Bern", "Europe/Berlin");
			put("Rome", "Europe/Rome");
			put("Stockholm", "Europe/Stockholm");
			put("Vienna", "Europe/Vienna");
			put("West Central Africa", "Africa/Algiers");
			put("Bucharest", "Europe/Bucharest");
			put("Cairo", "Africa/Cairo");
			put("Helsinki", "Europe/Helsinki");
			put("Kyev", "Europe/Kiev");
			put("Riga", "Europe/Riga");
			put("Sofia", "Europe/Sofia");
			put("Tallinn", "Europe/Tallinn");
			put("Vilnius", "Europe/Vilnius");
			put("Athens", "Europe/Athens");
			put("Istanbul", "Europe/Istanbul");
			put("Minsk", "Europe/Minsk");
			put("Jerusalem", "Asia/Jerusalem");
			put("Harare", "Africa/Harare");
			put("Pretoria", "Africa/Johannesburg");
			put("Moscow", "Europe/Moscow");
			put("St. Petersburg", "Europe/Moscow");
			put("Volgograd", "Europe/Moscow");
			put("Kuwait", "Asia/Kuwait");
			put("Riyadh", "Asia/Riyadh");
			put("Nairobi", "Africa/Nairobi");
			put("Baghdad", "Asia/Baghdad");
			put("Tehran", "Asia/Tehran");
			put("Abu Dhabi", "Asia/Muscat");
			put("Muscat", "Asia/Muscat");
			put("Baku", "Asia/Baku");
			put("Tbilisi", "Asia/Tbilisi");
			put("Yerevan", "Asia/Yerevan");
			put("Kabul", "Asia/Kabul");
			put("Ekaterinburg", "Asia/Yekaterinburg");
			put("Islamabad", "Asia/Karachi");
			put("Karachi", "Asia/Karachi");
			put("Tashkent", "Asia/Tashkent");
			put("Chennai", "Asia/Kolkata");
			put("Kolkata", "Asia/Kolkata");
			put("Mumbai", "Asia/Kolkata");
			put("New Delhi", "Asia/Kolkata");
			put("Kathmandu", "Asia/Katmandu");
			put("Astana", "Asia/Dhaka");
			put("Dhaka", "Asia/Dhaka");
			put("Sri Jayawardenepura", "Asia/Colombo");
			put("Almaty", "Asia/Almaty");
			put("Novosibirsk", "Asia/Novosibirsk");
			put("Rangoon", "Asia/Rangoon");
			put("Bangkok", "Asia/Bangkok");
			put("Hanoi", "Asia/Bangkok");
			put("Jakarta", "Asia/Jakarta");
			put("Krasnoyarsk", "Asia/Krasnoyarsk");
			put("Beijing", "Asia/Shanghai");
			put("Chongqing", "Asia/Chongqing");
			put("Hong Kong", "Asia/Hong_Kong");
			put("Urumqi", "Asia/Urumqi");
			put("Kuala Lumpur", "Asia/Kuala_Lumpur");
			put("Singapore", "Asia/Singapore");
			put("Taipei", "Asia/Taipei");
			put("Perth", "Australia/Perth");
			put("Irkutsk", "Asia/Irkutsk");
			put("Ulaan Bataar", "Asia/Ulaanbaatar");
			put("Seoul", "Asia/Seoul");
			put("Osaka", "Asia/Tokyo");
			put("Sapporo", "Asia/Tokyo");
			put("Tokyo", "Asia/Tokyo");
			put("Yakutsk", "Asia/Yakutsk");
			put("Darwin", "Australia/Darwin");
			put("Adelaide", "Australia/Adelaide");
			put("Canberra", "Australia/Melbourne");
			put("Melbourne", "Australia/Melbourne");
			put("Sydney", "Australia/Sydney");
			put("Brisbane", "Australia/Brisbane");
			put("Hobart", "Australia/Hobart");
			put("Vladivostok", "Asia/Vladivostok");
			put("Guam", "Pacific/Guam");
			put("Port Moresby", "Pacific/Port_Moresby");
			put("Magadan", "Asia/Magadan");
			put("Solomon Is.", "Asia/Magadan");
			put("New Caledonia", "Pacific/Noumea");
			put("Fiji", "Pacific/Fiji");
			put("Kamchatka", "Asia/Kamchatka");
			put("Marshall Is.", "Pacific/Majuro");
			put("Auckland", "Pacific/Auckland");
			put("Wellington", "Pacific/Auckland");
			put("Nuku'alofa", "Pacific/Tongatapu");
		}
	};

	@SuppressWarnings("serial")
	public static final Map<String, String> railsTZtoRailsWithGMT = new LinkedHashMap<String, String>() {
		{
			put("", "no time zone");
			put("International Date Line West",
					"(GMT-11:00) International Date Line West");
			put("Midway Island", "(GMT-11:00) Midway Island");
			put("Samoa", "(GMT-11:00) Samoa");
			put("Hawaii", "(GMT-10:00) Hawaii");
			put("Alaska", "(GMT-09:00) Alaska");
			put("Pacific Time (US & Canada)", "(GMT-08:00) Pacific Time (US & Canada)");
			put("Tijuana", "(GMT-08:00) Tijuana");
			put("Arizona", "(GMT-07:00) Arizona");
			put("Chihuahua", "(GMT-07:00) Chihuahua");
			put("Mazatlan", "(GMT-07:00) Mazatlan");
			put("Mountain Time (US & Canada)", "(GMT-07:00) Mountain Time (US & Canada)");
			put("Central America", "(GMT-06:00) Central America");
			put("Central Time (US & Canada)", "(GMT-06:00) Central Time (US & Canada)");
			put("Guadalajara", "(GMT-06:00) Guadalajara");
			put("Mexico City", "(GMT-06:00) Mexico City");
			put("Monterrey", "(GMT-06:00) Monterrey");
			put("Saskatchewan", "(GMT-06:00) Saskatchewan");
			put("Bogota", "(GMT-05:00) Bogota");
			put("Eastern Time (US & Canada)", "(GMT-05:00) Eastern Time (US & Canada)");
			put("Indiana (East)", "(GMT-05:00) Indiana (East)");
			put("Lima", "(GMT-05:00) Lima");
			put("Quito", "(GMT-05:00) Quito");
			put("Caracas", "(GMT-04:30) Caracas");
			put("Atlantic Time (Canada)", "(GMT-04:00) Atlantic Time (Canada)");
			put("La Paz", "(GMT-04:00) La Paz");
			put("Santiago", "(GMT-04:00) Santiago");
			put("Newfoundland", "(GMT-03:30) Newfoundland");
			put("Brasilia", "(GMT-03:00) Brasilia");
			put("Buenos Aires", "(GMT-03:00) Buenos Aires");
			put("Georgetown", "(GMT-03:00) Georgetown");
			put("Greenland", "(GMT-03:00) Greenland");
			put("Mid-Atlantic", "(GMT-02:00) Mid-Atlantic");
			put("Azores", "(GMT-01:00) Azores");
			put("Cape Verde Is.", "(GMT-01:00) Cape Verde Is.");
			put("Casablanca", "(GMT+00:00) Casablanca");
			put("Dublin", "(GMT+00:00) Dublin");
			put("Edinburgh", "(GMT+00:00) Edinburgh");
			put("Lisbon", "(GMT+00:00) Lisbon");
			put("London", "(GMT+00:00) London");
			put("Monrovia", "(GMT+00:00) Monrovia");
			put("UTC", "(GMT+00:00) UTC");
			put("Amsterdam", "(GMT+01:00) Amsterdam");
			put("Belgrade", "(GMT+01:00) Belgrade");
			put("Berlin", "(GMT+01:00) Berlin");
			put("Bern", "(GMT+01:00) Bern");
			put("Bratislava", "(GMT+01:00) Bratislava");
			put("Brussels", "(GMT+01:00) Brussels");
			put("Budapest", "(GMT+01:00) Budapest");
			put("Copenhagen", "(GMT+01:00) Copenhagen");
			put("Ljubljana", "(GMT+01:00) Ljubljana");
			put("Madrid", "(GMT+01:00) Madrid");
			put("Paris", "(GMT+01:00) Paris");
			put("Prague", "(GMT+01:00) Prague");
			put("Rome", "(GMT+01:00) Rome");
			put("Sarajevo", "(GMT+01:00) Sarajevo");
			put("Skopje", "(GMT+01:00) Skopje");
			put("Stockholm", "(GMT+01:00) Stockholm");
			put("Vienna", "(GMT+01:00) Vienna");
			put("Warsaw", "(GMT+01:00) Warsaw");
			put("West Central Africa", "(GMT+01:00) West Central Africa");
			put("Zagreb", "(GMT+01:00) Zagreb");
			put("Athens", "(GMT+02:00) Athens");
			put("Bucharest", "(GMT+02:00) Bucharest");
			put("Cairo", "(GMT+02:00) Cairo");
			put("Harare", "(GMT+02:00) Harare");
			put("Helsinki", "(GMT+02:00) Helsinki");
			put("Istanbul", "(GMT+02:00) Istanbul");
			put("Jerusalem", "(GMT+02:00) Jerusalem");
			put("Kyev", "(GMT+02:00) Kyev");
			put("Minsk", "(GMT+02:00) Minsk");
			put("Pretoria", "(GMT+02:00) Pretoria");
			put("Riga", "(GMT+02:00) Riga");
			put("Sofia", "(GMT+02:00) Sofia");
			put("Tallinn", "(GMT+02:00) Tallinn");
			put("Vilnius", "(GMT+02:00) Vilnius");
			put("Baghdad", "(GMT+03:00) Baghdad");
			put("Kuwait", "(GMT+03:00) Kuwait");
			put("Moscow", "(GMT+03:00) Moscow");
			put("Nairobi", "(GMT+03:00) Nairobi");
			put("Riyadh", "(GMT+03:00) Riyadh");
			put("St. Petersburg", "(GMT+03:00) St. Petersburg");
			put("Volgograd", "(GMT+03:00) Volgograd");
			put("Tehran", "(GMT+03:30) Tehran");
			put("Abu Dhabi", "(GMT+04:00) Abu Dhabi");
			put("Baku", "(GMT+04:00) Baku");
			put("Muscat", "(GMT+04:00) Muscat");
			put("Tbilisi", "(GMT+04:00) Tbilisi");
			put("Yerevan", "(GMT+04:00) Yerevan");
			put("Kabul", "(GMT+04:30) Kabul");
			put("Ekaterinburg", "(GMT+05:00) Ekaterinburg");
			put("Islamabad", "(GMT+05:00) Islamabad");
			put("Karachi", "(GMT+05:00) Karachi");
			put("Tashkent", "(GMT+05:00) Tashkent");
			put("Chennai", "(GMT+05:30) Chennai");
			put("Kolkata", "(GMT+05:30) Kolkata");
			put("Mumbai", "(GMT+05:30) Mumbai");
			put("New Delhi", "(GMT+05:30) New Delhi");
			put("Sri Jayawardenepura", "(GMT+05:30) Sri Jayawardenepura");
			put("Kathmandu", "(GMT+05:45) Kathmandu");
			put("Almaty", "(GMT+06:00) Almaty");
			put("Astana", "(GMT+06:00) Astana");
			put("Dhaka", "(GMT+06:00) Dhaka");
			put("Novosibirsk", "(GMT+06:00) Novosibirsk");
			put("Rangoon", "(GMT+06:30) Rangoon");
			put("Bangkok", "(GMT+07:00) Bangkok");
			put("Hanoi", "(GMT+07:00) Hanoi");
			put("Jakarta", "(GMT+07:00) Jakarta");
			put("Krasnoyarsk", "(GMT+07:00) Krasnoyarsk");
			put("Beijing", "(GMT+08:00) Beijing");
			put("Chongqing", "(GMT+08:00) Chongqing");
			put("Hong Kong", "(GMT+08:00) Hong Kong");
			put("Irkutsk", "(GMT+08:00) Irkutsk");
			put("Kuala Lumpur", "(GMT+08:00) Kuala Lumpur");
			put("Perth", "(GMT+08:00) Perth");
			put("Singapore", "(GMT+08:00) Singapore");
			put("Taipei", "(GMT+08:00) Taipei");
			put("Ulaan Bataar", "(GMT+08:00) Ulaan Bataar");
			put("Urumqi", "(GMT+08:00) Urumqi");
			put("Osaka", "(GMT+09:00) Osaka");
			put("Sapporo", "(GMT+09:00) Sapporo");
			put("Seoul", "(GMT+09:00) Seoul");
			put("Tokyo", "(GMT+09:00) Tokyo");
			put("Yakutsk", "(GMT+09:00) Yakutsk");
			put("Adelaide", "(GMT+09:30) Adelaide");
			put("Darwin", "(GMT+09:30) Darwin");
			put("Brisbane", "(GMT+10:00) Brisbane");
			put("Canberra", "(GMT+10:00) Canberra");
			put("Guam", "(GMT+10:00) Guam");
			put("Hobart", "(GMT+10:00) Hobart");
			put("Melbourne", "(GMT+10:00) Melbourne");
			put("Port Moresby", "(GMT+10:00) Port Moresby");
			put("Sydney", "(GMT+10:00) Sydney");
			put("Vladivostok", "(GMT+10:00) Vladivostok");
			put("Magadan", "(GMT+11:00) Magadan");
			put("New Caledonia", "(GMT+11:00) New Caledonia");
			put("Solomon Is.", "(GMT+11:00) Solomon Is.");
			put("Auckland", "(GMT+12:00) Auckland");
			put("Fiji", "(GMT+12:00) Fiji");
			put("Kamchatka", "(GMT+12:00) Kamchatka");
			put("Marshall Is.", "(GMT+12:00) Marshall Is.");
			put("Wellington", "(GMT+12:00) Wellington");
			put("Nuku'alofa", "(GMT+13:00) Nuku'alofa");

		}
	};

	public static ArrayList<String> getDetailedRailsTimezonesArrayList() {
		ArrayList<String> valuesArray = new ArrayList<String>();
		for (String s : railsTZtoRailsWithGMT.values()) {
			valuesArray.add(s);
		}
		return valuesArray;
	}

	public static TimeZone getJavaTz(String railsTZ) {
		return TimeZone.getTimeZone(railsTZtoJavaTZ.get(railsTZ));
	}

	public static ArrayList<String> listOfRailsTimezones() {
		ArrayList<String> timezonesArray = new ArrayList<String>();
		for (String s : railsTZtoRailsWithGMT.keySet()) {
			timezonesArray.add(s);
		}

		return timezonesArray;

	}

}
