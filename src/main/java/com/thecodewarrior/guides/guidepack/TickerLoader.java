package com.thecodewarrior.guides.guidepack;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.thecodewarrior.guides.ConfigOptions;
import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.Reference;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.gui.ticker.Ticker;
import com.thecodewarrior.guides.gui.ticker.TickerButtonLink;

public class TickerLoader {

	public static final Logger l = GuideMod.logChild("TickerLoader");
	
	public static String  protocol = "http";
	
	public static void downloadTickers() {
		String s = null;
		try {
			s = IOUtils.toString ( new URL(protocol, ConfigOptions.serverHost, ConfigOptions.serverPort,
					"/api/ticker/"+Reference.VERSION+"/tickers.json"));
		} catch (MalformedURLException e) {
			l.error("Malformed url when downloading manifest file. URL:" + 
					protocol + "://" + ConfigOptions.serverHost + ":" + ConfigOptions.serverPort + "/api/ticker/"+Reference.VERSION+"/tickers.json"
					);
		} catch (IOException e) {
			l.error("IOException when opening stream to server");
		}
		if(s == null)
			return;
		JsonParser parser = new JsonParser();
		JsonArray rootArr = null;
		try{
			rootArr = parser.parse(s).getAsJsonArray();
		} catch (JsonSyntaxException e) {
			l.info("JSON Syntax error in ticker result on line " + e.getLocalizedMessage());
		}
		if(rootArr == null)
			return;
		
		for(Iterator<JsonElement> tickerIter = rootArr.iterator(); tickerIter.hasNext(); ) {
			JsonObject ticker = (JsonObject) tickerIter.next();
			String text = ticker.get("text").getAsString();
			String colorStr = ticker.get("color").getAsString();
			if(colorStr.startsWith("0x")) {
				colorStr = colorStr.substring(2);
			}
			if(colorStr.length() == 6) {
				colorStr = colorStr+"00";
			}
			int color = Integer.parseUnsignedInt(colorStr, 16);
			Ticker t = new Ticker(color, text);
			int timeout = ConfigOptions.defaultTickerTimeout;
			if( ticker.has("timeout") ) {
				timeout = ticker.get("timeout").getAsInt();
			}
			t.setTimeout(timeout);
			
			JsonArray buttons = ticker.getAsJsonArray("buttons");
			for(Iterator<JsonElement> buttonIter = buttons.iterator(); buttonIter.hasNext(); ) {
				JsonObject button = (JsonObject) buttonIter.next();
				String buttonText = button.get("text").getAsString();
				String link = button.get("link").getAsString();
				TickerButtonLink l = new TickerButtonLink(buttonText, link);
				t.addButton(l);
			}
			
			GuiBookOfRevealing.pendingTickers.add(t);
		}
		
	}

}
