package com.yunji.deliveryman.utils;

import java.io.DataOutputStream;
import java.io.IOException;


public class ProcessHelper {
	
	private static final String tag = "ProcessHelper";
	
	public static boolean execCommand(String command)
	{
		Process process = null;
		DataOutputStream dos = null;

        try 
        {
            process = Runtime.getRuntime().exec("sh");
            dos = new DataOutputStream(process.getOutputStream());
            dos.writeBytes(command);
            dos.flush();
            dos.close();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return true;
	}


}
