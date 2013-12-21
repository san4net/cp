package com.copypaste;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DoCopy {
	
	public Integer doCopying(String srcPath, String destPath) {
		Integer returnCode = -1;
		InputStream inStream = null;
		OutputStream outStream = null ;
		File afile = new File(srcPath);
		File bfile = new File(destPath);
		
		if(!afile.exists() ||bfile.exists()){
			return 2;
		}
		try {
			
			inStream = new FileInputStream(afile);
			outStream = new FileOutputStream(bfile);

			byte[] buffer = new byte[1024];

			int length;
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}
			returnCode=0;

		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				inStream.close();
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return returnCode;
	}
	

}
