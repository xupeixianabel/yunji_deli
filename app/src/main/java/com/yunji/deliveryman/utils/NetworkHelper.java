package com.yunji.deliveryman.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;


public class NetworkHelper {

	private static final String tag  = "IPForwardNetworkHelper";
    /**
     * A String denoting TCP.
     */
    public static final String TCP = "TCP";

    /**
     * A String denoting UDP.
     */
    public static final String UDP = "UDP";

    /**
     * A String denoting BOTH.
     */
    public static final String BOTH = "BOTH";

    /**
     * Return whether or not an IPv4 Address is valid.
     * @param address The IPv4 Address
     * @return true if valid, false if not valid.
     */
    @Deprecated
    public static boolean isValidIpv4Address(String address){
        if(address != null & address.length() > 0){
            return true;
        }

        return false;
    }
    
    /**
     * 获取共享给水滴分配的IP地址
     * @return
     */
    @SuppressWarnings("resource")
	public static String getWaterIP()
    {
    	try 
    	{
			File file = new File("/proc/net/arp");
	        if (file.isFile() && file.exists()) 
	        {
	            InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
	            BufferedReader br = new BufferedReader(isr);
	            String lineTxt = null;
	            while((lineTxt = br.readLine()) != null) 
	            {
	                if(lineTxt.contains("rndis0") || lineTxt.contains("usb0")) 
	                {
	                	return lineTxt.substring(0, lineTxt.indexOf(" "));
	                }
	            }  
	            isr.close();
	            br.close();
	        }
		} catch (Exception e) {

		}
    	
        return "";
    }
    
    /**
     * 打开USB共享功能
     * @param //manager
     * @param value
     * @return
     */
    public static boolean setUsbTethering(Context ctx, boolean value)
	{
    	ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			Method method = manager.getClass().getDeclaredMethod("setUsbTethering",boolean.class);
			
			if (null != method) 
			{
				try{

					method.setAccessible(true);
					Object obj = method.invoke(manager, value);
				}catch (Exception e){
					e.printStackTrace();
				}

			}
			else
			{

			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
   }

}
