package com.thecodewarrior.guides.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.thecodewarrior.guides.GuideMod;

public class GuideMatcher {
	
	public static final Logger l = GuideMod.logChild("GuideMatcher");
	
	public String id;
	public List<MetaMatcher> metaList;
	public String guideName;
	
	public GuideMatcher(String id, String metaStr, String guideName) {
		super();
		this.guideName = guideName;
		this.id = id;
		this.metaList = new ArrayList<MetaMatcher>();
		String[] arr = metaStr.split(",");
		for(int i = 0; i < arr.length; i++) {
			String s = arr[i];
			MetaMatcher meta = null;
			if(s.matches("[0-9]+-[0-9]+")) {
				String[] nums = s.split("-");
				int n1 = Integer.parseInt(nums[0]);
				int n2 = Integer.parseInt(nums[1]);
				meta = new MetaMatcher(n1, n2, MetaMatchType.Range);
				
			} else if(s.matches("<[0-9]+")) {
				int n1 = Integer.parseInt(s.substring(1));
				meta = new MetaMatcher(n1, 0, MetaMatchType.Less);
				
			} else if(s.matches("<=[0-9]+")) {
				int n1 = Integer.parseInt(s.substring(2));
				meta = new MetaMatcher(n1, 0, MetaMatchType.LessOrEqual);
				
			} else if(s.matches(">[0-9]+")) {
				int n1 = Integer.parseInt(s.substring(1));
				meta = new MetaMatcher(n1, 0, MetaMatchType.Greater);
				
			} else if(s.matches(">=[0-9]+")) {
				int n1 = Integer.parseInt(s.substring(2));
				meta = new MetaMatcher(n1, 0, MetaMatchType.GreaterOrEqual);
			} else if(s.matches("[0-9]+")) {
				int n1 = Integer.parseInt(s);
				meta = new MetaMatcher(n1, 0, MetaMatchType.Equal);
			}
			if(meta != null)
				this.metaList.add(meta);
		}
		l.info("Created matcher for guide '" + guideName + "' with the id of '" + id + "' and a meta value of '" + metaStr + "'");
	}

	public Match match(String id, int meta) {
		Match bestMatch = new Match();
		if(!id.equalsIgnoreCase(this.id)) {
			l.info("ID doesn't match. Our ID=" + this.id + ", Passed ID=" + id);
			return bestMatch;
		}
		if(metaList.size() == 0) {
			bestMatch.hasMatch = true;
			bestMatch.typeSpecifity = 0;
		} else {
			for (int i = 0; i < metaList.size(); i++) {
				MetaMatcher mat = metaList.get(i);
				l.info(mat.type);
				if(mat.doesMatch(meta)) {
					if(
							(mat.getTypeSpecifity() == bestMatch.typeSpecifity && mat.getItemSpecifity() > bestMatch.itemSpecifity) ||
							(mat.getTypeSpecifity() >  bestMatch.typeSpecifity)
						){
						bestMatch = mat.getMatch();
					}
				}
			}
		}
		bestMatch.guideName = this.guideName;
		l.info("Got match with ID " + id + ", the guideName is " + this.guideName + " and the match guide name is " + bestMatch.guideName + " and the hasMatch = " + bestMatch.hasMatch);
		return bestMatch;
	}
	
	static class Match {
		public boolean hasMatch;
		public int typeSpecifity;
		public int itemSpecifity;
		public String guideName;
		
		public Match() {
			hasMatch = false;
			typeSpecifity = -1;
			itemSpecifity = -1;
			guideName = "";
		}
		
		public Match(int typeSpecifity, int itemSpecifity, String guideName) {
			this.typeSpecifity = typeSpecifity;
			this.itemSpecifity = itemSpecifity;
			this.guideName = guideName;
			this.hasMatch = true;
		}
	}
	
	static class MetaMatcher {
		int number1 = 0, number2 = 0;
		MetaMatchType type = MetaMatchType.Equal;
		public MetaMatcher(int number1, int number2, MetaMatchType type) {
			super();
			if(type == MetaMatchType.Less) {
				this.number1 = 0;
				this.number2 = number1-1;
				this.type = MetaMatchType.Range;
				
			} else if(type == MetaMatchType.LessOrEqual) {
				this.number1 = 0;
				this.number2 = number1;
				this.type = MetaMatchType.Range;
			} else {
				this.number1 = number1;
				this.number2 = number2;
				
				this.type = type;
			}
			
		}
		
		public boolean doesMatch(int meta) {
			switch(this.type) {
			case Greater:
				return meta > number1;
			case GreaterOrEqual:
				return meta >= number1;
			case Range:
				if(number1 <= number2)
					return meta >= number1 && meta <= number2;
				else
					return meta >= number2 && meta <= number1;
			case Equal:
				return meta == number1;
			default:
				return false;
			}
		}
		
		public Match getMatch() {
			return new Match(getTypeSpecifity(), getItemSpecifity(), "-");
		}
		
		public int getTypeSpecifity() {
			switch(this.type) {
			case Greater:
			case GreaterOrEqual:
				return 1;
			case Range:
				return 2;
			case Equal:
				return 3;
			default:
				return 0;
			}
		}
		
		public int getItemSpecifity() {
			switch(this.type) {
			case Greater:
				return -(number1+1);
			case GreaterOrEqual:
				return -(number1);
			case Range:
				return -Math.abs(number1-number2);
			case Equal:
				return 1;
			default:
				return Integer.MIN_VALUE;
			}
		}
	}
	
	public enum MetaMatchType {
		Less,
		Greater,
		LessOrEqual,
		GreaterOrEqual,
		Equal,
		Range
	}
	
}
