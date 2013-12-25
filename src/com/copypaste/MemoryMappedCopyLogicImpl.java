package com.copypaste;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.copypaste.logic.CopyLogic;

public class MemoryMappedCopyLogicImpl implements CopyLogic {
  private volatile Integer status=-1;
	@Override
	public int startCopy(String src, String dest) throws Exception {
		return memoryMappedCopy(src, dest) ;
	}

	@Override
	public boolean isCopyDone() {
		return status==0;
	}

	public int memoryMappedCopy(String fromFile, String toFile)
			throws Exception {
		RandomAccessFile rafIn = new RandomAccessFile(fromFile, "rw");
		
		RandomAccessFile rafOut = new RandomAccessFile(toFile, "rw");
		if(rafIn.getChannel() == null  || rafOut.getChannel() == null){
			return -2;
		}
		try (FileChannel fcIn = rafIn.getChannel();
			FileChannel fcOut = rafOut.getChannel();) {
			long timeIn = System.currentTimeMillis();
			ByteBuffer byteBuffIn = fcIn.map(FileChannel.MapMode.READ_WRITE, 0,	(int) fcIn.size());
			fcIn.read(byteBuffIn);
			byteBuffIn.flip();
			ByteBuffer writeMap = fcOut.map(FileChannel.MapMode.READ_WRITE, 0,
					(int) fcIn.size());
			writeMap.put(byteBuffIn);
			long timeOut = System.currentTimeMillis();
			System.out.println("Memory mapped copy Time for a file of size :"
					+ (int) fcIn.size() + " in sec : " + (timeOut - timeIn)/1000);
			status=0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

}
