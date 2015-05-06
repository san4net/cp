package com.copypaste.impls;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.copypaste.logic.CopyPasteLogic;

public class MemoryMappedCopyPasteImpl<T> implements CopyPasteLogic<T> {
	volatile boolean copied = false;
	T sourceFile, destFile;
	private long size = 0;

	public MemoryMappedCopyPasteImpl() {
	}

	public MemoryMappedCopyPasteImpl(T srcFile, T destFile2) {
		super();
		this.sourceFile = srcFile;
		this.destFile = destFile2;
	}

	@Override
	public T copyPaste() throws Exception {
		long status = -1;
		RandomAccessFile rafIn = new RandomAccessFile((String) sourceFile, "rw");
		RandomAccessFile rafOut = new RandomAccessFile((String) destFile, "rw");
		size = rafIn.length();
		
		if (rafIn.getChannel() == null || rafOut.getChannel() == null) {
			return (T) Long.valueOf(status);
		}
		

		try (FileChannel fcIn = rafIn.getChannel();
				FileChannel fcOut = rafOut.getChannel();) {
			long timeIn = System.currentTimeMillis();
			ByteBuffer byteBuffIn = fcIn.map(FileChannel.MapMode.READ_WRITE, 0,
					(int) fcIn.size());
			fcIn.read(byteBuffIn);
			byteBuffIn.flip();
			ByteBuffer writeMap = fcOut.map(FileChannel.MapMode.READ_WRITE, 0,
					(int) fcIn.size());
			writeMap.put(byteBuffIn);
			long timeOut = System.currentTimeMillis();
			System.out.println("Memory mapped copy Time for a file of size :"
					+ (int) fcIn.size() + " in sec : " + (timeOut - timeIn)
					/ 1000);
			status = size ;
			copied = true;

		} catch (Exception e) {
			e.printStackTrace();
			status = 0;
		}
		
		return (T) String.valueOf(status);
	}

	@Override
	public boolean isCopied() {
		return copied;
	}
	
	public T getSourceFile() {
		return sourceFile;
	}
	
	public void setSourceFile(T sourceFile) {
		this.sourceFile = sourceFile;
	}
	
	public T getDestFile() {
		return destFile;
	}
	
	public void setDestFile(T destFile) {
		this.destFile = destFile;
	}
	/**
	 * This is size of the file
	 */
	@Override
	public long size() {
		// TODO Auto-generated method stub
		return size;
	}
	
}
