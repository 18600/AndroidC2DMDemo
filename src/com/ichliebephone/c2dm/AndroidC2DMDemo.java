package com.ichliebephone.c2dm;

import com.google.android.c2dm.C2DMessaging;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


/**
 * please read readMe()
 * @author water
 *
 */
public class AndroidC2DMDemo extends Activity {
    /** Called when the activity is first created. */
	private static final String TAG = "AndroidC2DMDemo";
//	public static final String SENDER_ID = "android.c2dm.demo@gmail.com"; //使用C2DM服务的用户账户
	public static final String SENDER_ID = "geowok.golf.android@gmail.com";
	public static final String MESSAGE_KEY_ONE = "msg";   //和服务器商量好的接收消息的键值key
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.v(TAG, "Start");
        //向C2DM服务器注册
        C2DMessaging.register(this, SENDER_ID);
    }
    
    public void readMe(){
    	/**
    	 * 1.因为C2DM功能只有2.2及以上的Android系统才支持，因此创建一个2.2及以上的AVD，
    	 *   然后在”设置->账户与同步”里还要设置好Google Account.
    	 * 2. 在程序运行的过程中获得registration_id。
    	 * 3. 有了registration_id，我们的服务器端就可以向C2DM端发送需要Push的数据了，
    	 *    这里进行简单化处理下，在Ubuntu下直接使用curl命令来模拟服务器功能向C2DM发送数据。
    	 *    
    	 *  1.先来获取C2DM的ClientLogin权限Auth，在Ubuntu终端下输入：
    	 *     curl -d "accountType=HOSTED_OR_GOOGLE&Email=XXXXX@gmail.com&Passwd=XXXXX&service=ac2dm&source=bupt-c2dmdemo-1.0" https://www.google.com/accounts/ClientLogin  
    	 *     这个表示以POST的方式向https://www.google.com/accounts/ClientLogin发送数据，
    	 *     其中把Email和Passwd换成你自己在C2DM网页上注册的邮箱号和密码。
    	 *  2.如果你的邮箱已在C2DM网页上注册，并且密码没有错误的话就会返回需要的Auth内容：
    	 *         SID=DQAAAKYAAADcTtHbBBNcZJEOfkfVRycD_ZOIidwsQ3UwIY7cSrYWaY6uhlfo0l9gRPB-mQxP4K2T5tWiG--vWVmSTeq5p8SPwgnsYvfzj7bkNiPPIy4xRimVVfBmAHnZgLohw7gHMKi5DS6kK-Ut5tNzdTkI0I2tUDF0ryQ7MnPpI6Sj-gUCyBXmvKatHHDnNTTV78XdGIx7FYej1DyqGsPsYo3bCstHgltjv3cd2Hs7D4yrpUWHZw  
    	 *		   LSID=DQAAAKgAAABCpaoUE4XvxM24Cofntw1IUGx5fKxX-m7aqTL0zhunP0OjzJ2sn9ywmPa1BMZ2cF2IchuxHFLVzaSQfydAmiHZJGXLgaUorpIN6yz1e0VFWKmS6j4wGjZOos3QoJ9rkha0jKbOiHfBesADjxk-qjJ24TJ0RL-xkZHQyzS69YlA1KyzqIKjAMCzgqaDfCwhqxylJzizJksO2h8xpAFXZ38d_grm8XYZtzejiCiAMAR65A  
         *         Auth=DQAAAKoAAACRF4pgYULnXULoWgbwfdqmMiRhfZYa1l-LW_rwGD7cofov4L4c2bVrtCOXbEbkju_hhqdAonpMkrb5icptt28fU8c-s-u1y2MXNYDxPIdQzfA2t6oI3NTmyj35MpsR1NKL4TN7ZVEn6z9NueuiKAqLHukZYh1YMGkGC8M6rVvA7AWPW36064XCQED7KLVNp_pGT00lrni7UdZKZWEy0FT-EVR-OxDyHWw6C-5Kmfkisw 
         *     返回的内容包括SID，LSID和Auth三个部分，其中Auth是我们需要的内容。 
         *     
         *  3.有了Auth和registration_id值后，我们就可以继续用curl命令模拟我们自己服务器的功能向C2DM发送要推送的数据：
         *     curl -H "Authorization:GoogleLogin auth=DQAAAKoAAACRF4pgYULnXULoWgbwfdqmMiRhfZYa1l-LW_rwGD7cofov4L4c2bVrtCOXbEbkju_hhqdAonpMkrb5icptt28fU8c-s-u1y2MXNYDxPIdQzfA2t6oI3NTmyj35MpsR1NKL4TN7ZVEn6z9NueuiKAqLHukZYh1YMGkGC8M6rVvA7AWPW36064XCQED7KLVNp_pGT00lrni7UdZKZWEy0FT-EVR-OxDyHWw6C-5Kmfkisw" -d "registration_id=APA91bGUBoSvt3G5Ny9t0IGLmIKAKYX6G6VHwSQHh3tP2fqcaQ0N4GPdKh5B3RDUHFCFF06YwT8ifOP_cOy5BAWyCLHL8d8NpuIW9AqXt9h2JSBVF2MitZA&collapse_key=1&data.msg=ichliebejiajia" https://android.apis.google.com/c2dm/send  
         *   
         *     
         *     其中发送的数据部分为data.msg=ichliebejiajia，表示发送的数据内容为ichliebejiajia，键值为msg，
         *     键值得和Android终端上的程序统一好，以便终端上可以获取。如果发送成功，会返回一个id值，比如：
         *     id=0:1308623423080544%6c5c15c200000031  
         *     
         *  4.这时我们的服务器就已经把数据发送给C2DM服务器了，Android设备上一会就能接收到C2DM服务器Push的数据,
         *    状态栏上会有对应的通知显示.
         *     
         *     
         *     
         *     
         *     
    	 */
    }
    
    
}