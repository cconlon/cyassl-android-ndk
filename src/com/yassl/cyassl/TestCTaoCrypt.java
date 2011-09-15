/* TestCTaoCrypt.java
 *
 * Copyright (C) 2006-2011 Sawtooth Consulting Ltd.
 *
 * This file is part of CyaSSL.
 *
 * CyaSSL is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * CyaSSL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA
 */
package com.yassl.cyassl;

import java.io.*;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.content.res.AssetManager;

public class TestCTaoCrypt extends Activity
{	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        CopyCerts();
        
        TextView tv = new TextView(this);
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setTextSize(12);	

        tv.append("CTaoCrypt Test Results:\n");
        tv.append("---------------------------------\n\n");
        
        // Run through native CTaoCrypt tests (originally from cyassl/ctaocrypt/test/test.c)
        
        int ret = md5_test();
        tv = appendResult(tv, ret, "MD5");
        
        ret = md4_test();
        tv = appendResult(tv, ret, "MD4");
        
        ret = sha_test();
        tv = appendResult(tv, ret, "SHA");
        
        ret = sha256_test();
        tv = appendResult(tv, ret, "SHA256");
        
        ret = sha512_test();
        tv = appendResult(tv, ret, "SHA512");
        
        ret = ripemd_test();
        tv = appendResult(tv, ret, "RIPEMD");
        
        ret = hmac_test();
        tv = appendResult(tv, ret, "HMAC");
        
        ret = arc4_test();
        tv = appendResult(tv, ret, "ARC4");
        
        ret = hc128_test();
        tv = appendResult(tv, ret, "HC128");

        ret = rabbit_test();
        tv = appendResult(tv, ret, "RABBIT  ");
        
        ret = des_test();
        tv = appendResult(tv, ret, "DES");
        
        ret = des3_test();
        tv = appendResult(tv, ret, "DES3");
        
        ret = aes_test();
        tv = appendResult(tv, ret, "AES");
        
        ret = random_test();
        tv = appendResult(tv, ret, "RANDOM");

        ret = rsa_test();
        tv = appendResult(tv, ret, "RSA");
        
        ret = dh_test();
        tv = appendResult(tv, ret, "DH");
        
        ret = dsa_test();
        tv = appendResult(tv, ret, "DSA");
        
        ret = pwdbased_test();
        tv = appendResult(tv, ret, "PWDBASED");
        
        ret = openssl_test();
        tv = appendResult(tv, ret, "OPENSSL");
        
        ret = ecc_test();
        tv = appendResult(tv, ret, "ECC");
        
        setContentView(tv);
    }
    
    /*
     * This function appends the correct result string to our
     * TextView depending on the crypto test return value
     */ 
    public TextView appendResult(TextView tv, int ret, String cipherName){
    	int padLength = 20 - cipherName.length();
    	cipherName = String.format("%1$-" + padLength + "s", cipherName);
    	if(ret == 1)
    		tv.append(cipherName + "\t test failed!\n");
    	else if(ret == 2)
    		tv.append(cipherName + "\t NOT ENABLED\n");
    	else
    		tv.append(cipherName + "\t test passed!\n");
    	
    	return tv;
    }

    /* 
     * Delcare native JNI functions 
     * - These are implemented in TestCTaoCrypt.c 
     */
    public native int testCtc();
    public native int md5_test();
    public native int md4_test();
    public native int sha_test();
    public native int sha256_test();
    public native int sha512_test();
    public native int ripemd_test();
    public native int hmac_test();
    public native int arc4_test();
    public native int hc128_test();
    public native int rabbit_test();
    public native int des_test();
    public native int des3_test();
    public native int aes_test();
    public native int random_test();
    public native int rsa_test();
    public native int dh_test();
    public native int dsa_test();
    public native int pwdbased_test();
    public native int openssl_test();
    public native int ecc_test();

    /*
     * Load native library with above JNI functions and CTaoCrypt tests.
     */
    static {
        System.loadLibrary("ctctest");
    }

	private void CopyCerts() {
	    AssetManager assetManager = getAssets();
	    String[] certs = null;
	    
	    // Desired location to install certificates (on sdcard)
	    String certLocation = "/sdcard/certs/";
	    
	    try {
	        certs = assetManager.list("certs");
	    } catch (IOException e) {
	        Log.e("tag", e.getMessage());
	    }
	    
	    // Try to create our desired cert directory
	    File f = new File(certLocation);
	    if(!f.exists()) {
	    	if (!f.mkdir()) {
	    		// We can't create the cert directory, so lets try
	    		// to put our certs in the sdcard root directory
	    		certLocation = "/sdcard/";
	    	}
	    }
	    
	    // Copy all our certs over to the SD card
	    for(String filename : certs) {
	        InputStream in = null;
	        OutputStream out = null;
	        try {
	          in = assetManager.open("certs/"+filename);
	          Log.i("---CYASSL---", "Filename = " + filename);
	          out = new FileOutputStream(certLocation + filename);
	          copyFile(in, out);
	          in.close();
	          in = null;
	          out.flush();
	          out.close();
	          out = null;
	        } catch(Exception e) {
	            Log.e("tag", e.getMessage());
	        }       
	    }
	}
	private void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
}
