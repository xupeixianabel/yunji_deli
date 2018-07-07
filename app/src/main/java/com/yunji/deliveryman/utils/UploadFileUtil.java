package com.yunji.deliveryman.utils;

import android.app.Activity;

import com.yunji.deliveryman.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

public class UploadFileUtil {
        public static String uploadFile(Activity activity,String actionUrl, String[] uploadFilePaths , Map<String, String> map) {
            String end = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            DataOutputStream ds = null;
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader reader = null;
            StringBuffer resultBuffer = new StringBuffer();
            String tempLine = null;

            try {
                // 统一资源
                URL url = new URL(actionUrl);
                // 连接类的父类，抽象类
                URLConnection urlConnection = url.openConnection();
                // http的连接类
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

                // 设置是否从httpUrlConnection读入，默认情况下是true;
                httpURLConnection.setDoInput(true);
                // 设置是否向httpUrlConnection输出
                httpURLConnection.setDoOutput(true);
                // Post 请求不能使用缓存
                httpURLConnection.setUseCaches(false);
                // 设定请求的方法，默认是GET
                httpURLConnection.setRequestMethod("POST");
                // 设置字符编码连接参数
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                // 设置字符编码
                httpURLConnection.setRequestProperty("Charset", "UTF-8");
                // 设置请求内容类型
                httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                // 设置DataOutputStream
                ds = new DataOutputStream(httpURLConnection.getOutputStream());


                if (map != null && !map.isEmpty()) { // 这时请求中的普通参数，键值对类型的，相当于上面分析的请求中的username，可能有多个
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        String key = entry.getKey(); // 键，相当于上面分析的请求中的username
                        String value = map.get(key); // 值，相当于上面分析的请求中的sdafdsa
                        ds.writeBytes(twoHyphens + boundary + end); // 像请求体中写分割线，就是前缀+分界线+换行
                        ds.writeBytes("Content-Disposition: form-data; "
                                + "name=\"" + key + "\"" + end); // 拼接参数名，格式就是Content-Disposition: form-data; name="key" 其中key就是当前循环的键值对的键，别忘了最后的换行
                        ds.writeBytes(end); // 空行，一定不能少，键和值之间有一个固定的空行
                        ds.writeBytes(URLEncoder.encode(value.toString(), "utf-8")); // 将值写入
                        // 或者写成：dos.write(value.toString().getBytes(charset));
                        ds.writeBytes(end); // 换行
                    } // 所有循环完毕，就把所有的键值对都写入了
                }



                for (int i = 0; i < uploadFilePaths.length; i++) {
//                    String uploadFile = uploadFilePaths[i];
//                    String filename = uploadFile.substring(uploadFile.lastIndexOf(File.separatorChar) + 1);
//                    ds.writeBytes(twoHyphens + boundary + end);
//                    ds.writeBytes("Content-Disposition: form-data; " + "name=\"file" + i + "\";filename=\"" + filename
//                            + "\"" + end);
//                    ds.writeBytes(end);
//                    FileInputStream fStream = new FileInputStream(uploadFile);



                    ds.writeBytes(twoHyphens + boundary + end);
                    ds.writeBytes("Content-Disposition: form-data; " + "name=\"file" + i + "\";filename=\"" + "cruise_1"
                            + "\"" + end);
                    ds.writeBytes(end);
                    InputStream fStream=activity.getResources().openRawResource(R.raw.cruise_1);


                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int length = -1;
                    while ((length = fStream.read(buffer)) != -1) {
                        ds.write(buffer, 0, length);
                    }
                    ds.writeBytes(end);
                    /* close streams */
                    fStream.close();
                }
                ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
                /* close streams */
                ds.flush();
                if (httpURLConnection.getResponseCode() >= 300) {
                    throw new Exception(
                            "HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
                }

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream);
                    reader = new BufferedReader(inputStreamReader);
                    tempLine = null;
                    resultBuffer = new StringBuffer();
                    while ((tempLine = reader.readLine()) != null) {
                        resultBuffer.append(tempLine);
                        resultBuffer.append("\n");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ds != null) {
                    try {
                        ds.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return resultBuffer.toString();
            }
        }

    }
