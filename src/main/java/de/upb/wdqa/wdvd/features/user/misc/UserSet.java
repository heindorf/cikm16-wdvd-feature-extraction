package de.upb.wdqa.wdvd.features.user.misc;

import java.util.HashSet;

public class UserSet {
	
	HashSet<String> users = new HashSet<String>();
	
	public UserSet(String[] names) {
		for(String name: names){
			String tmp = name.trim();
			tmp = tmp.toLowerCase();
			users.add(tmp);
		}	
	}

	
	public Boolean strContains(String name){
		if (name == null){
			return null;
		}
		
		return contains(name);
	}
	
	public boolean contains(String name){
		boolean result = false;
		if (name!= null){
			String tmp= name.trim();
			tmp = name.toLowerCase();
			result = users.contains(tmp);
		}
		
		return result;
	}

}
