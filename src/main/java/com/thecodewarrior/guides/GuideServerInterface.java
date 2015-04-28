package com.thecodewarrior.guides;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;

public class GuideServerInterface {
	public static String  protocol = "http";
	public static String  host     = "localhost";//"localhost";
	public static int     port     = 80;//3000;
	public static boolean enabled  = false;
	
	public static boolean dev      = false;
		
	public static Logger l;
	public static void registerLogger() {
		l = GuideMod.logChild("Guide Mod");
	}
	
	public static void updateMod(String modid, String version, File localfile) {
		
		if(!enabled) {
			return;
		}
		
		try {
			String remoteVersion = IOUtils.toString( new URL(protocol, host, port, "/guides/"+modid+"/"+version+"/version").openStream() );
			
			boolean shouldDownload = false;
			if(!remoteVersion.equals("X")) {
				if(dev) {
					shouldDownload = true;
				} else if(localfile.exists()) {
				
					ZipFile zipFile = new ZipFile(localfile);
		
					InputStream stream = zipFile.getInputStream(new ZipEntry("pack.info"));
					if(stream == null) {
						zipFile.close();
						return;
					}
					HashMap<String, String> info = getPackInfo(stream);
					String localVersion = "0.0.0";
					if(info != null) {
						info.get("version");
					}
					
				    zipFile.close();
				    
				    if(compareVersions(localVersion, remoteVersion) == -1) {
				    	shouldDownload = true;
				    }
				} else {
					shouldDownload = true;
				}
				if(shouldDownload) {
					downloadGuide(modid, version, localfile);
				}
			}
		    
		} catch (MalformedURLException e) {
			//l.error("Malformed URL when checking for update:");
			//l.catching(e);
		} catch (IOException e) {
			//l.error("IOException when checking for update:");
			//l.catching(e);
		}
//		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
//		FileOutputStream fos = new FileOutputStream("information.html");
//		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	}
	
	private static void downloadGuide(String modid, String version, File localfile) {
		URL remote;
		try {
			remote = new URL(protocol, host, port, "/guides/"+modid+"/"+version+"/dl");
			ReadableByteChannel rbc = Channels.newChannel(remote.openStream());
			FileOutputStream fos = new FileOutputStream(localfile);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
		} catch (MalformedURLException e) {
			//l.error("Malformed URL trying to download update:" + e.toString());
			//l.catching(e);
		} catch (IOException e) {
			//l.error("IOException trying to download update: " + e.toString());
			//l.catching(e);
		}
		
	}

	/**
	 *  compare two version numbers
	 * @param version1
	 * @param version2
	 * @return 0 if version2 == version1, -1 if version1 < version2, 1 if version2 < version1
	 */
	public static int compareVersions(String version1, String version2)
	{
	    String[] vals1 = version1.split("\\D");
	    String[] vals2 = version2.split("\\D");
	    int i = 0;
	    // set index to first non-equal ordinal or length of shortest version string
	    while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) 
	    {
	      i++;
	    }
	    // compare first non-equal ordinal number
	    if (i < vals1.length && i < vals2.length) 
	    {
	        int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
	        return Integer.signum(diff);
	    }
	    // the strings are equal or one string is a substring of the other
	    // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
	    else
	    {
	        return Integer.signum(vals1.length - vals2.length);
	    }
	}
	
	public static HashMap<String, String> getPackInfo(InputStream stream) {
		try {
			HashMap<String, String> rtrn = new HashMap<String,String>();
			Iterator<String> lines = IOUtils.lineIterator(stream, "UTF-8");
			while(lines.hasNext()) {
				String line = lines.next();
				String[] split = line.split(":");
				if(split.length != 2) {
					continue;
				}
				rtrn.put(split[0].trim(), split[1].trim());
			}
		} catch (IOException e) {
			l.error("IOException trying to read guidepack pack.info file");
			//l.catching(e);
		}
		return null;
	}
	
}
