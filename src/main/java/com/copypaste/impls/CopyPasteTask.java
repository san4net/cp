package com.copypaste.impls;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.copypaste.Task;
import com.copypaste.adapter.CallableAdapter;
import com.copypaste.util.Constants.COPY_OPTION;
import com.copypaste.util.Constants.STATE;
import com.copypaste.util.Utils;

public class CopyPasteTask<T> implements Task<T> {
	private STATE currentState;
	private String sourceLocation;
	private String destLocation;
	private COPY_OPTION copyOption;
	private Object communicationChannel;

	CopyPasteTask(Object communicationChannel) {
		this.communicationChannel = communicationChannel;
	}

	// TODO
	ExecutorService service = Executors.newFixedThreadPool(2);

	@Override
	public void injectLogic(String logic) {
		// TODO
	}

	/**
	 * Start copy paste task
	 * 
	 */
	@Override
	public void start() {
		try {
			setState(STATE.DOING);
			synchronized (communicationChannel) {
				if (copyOption.compareTo(copyOption.FILE) == 0) {
					String pathname[] = sourceLocation.split("\\\\");
					destLocation = destLocation.endsWith("\\") ? destLocation
							+ pathname[pathname.length - 1] : destLocation
							+ "\\" + pathname[pathname.length - 1];

					Future<?> future = singleTask(sourceLocation, destLocation);
					/*
					 * service.submit(new CallableAdapter<>(new
					 * MemoryMappedCopyPasteImpl<String>( sourceLocation,
					 * destLocation)));
					 */
					System.out.println(future.get());
					Utils.showMsg("File copied src-" + sourceLocation
							+ "\n destination-" + destLocation);
					makeReady();
					communicationChannel.notifyAll();
				} else {
					doDirectoryCopyPaste();
					makeReady();
					communicationChannel.notifyAll();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Future<?> singleTask(String src, String dest) {

		return service.submit(new CallableAdapter<>(
				new MemoryMappedCopyPasteImpl<String>(src, dest)));
	}

	private void doDirectoryCopyPaste() {
		File f = new File(sourceLocation);
		if (!f.exists()) {
			Utils.showMsg("Source directory doesnot exist [" + sourceLocation
					+ "]");
			makeReady();
			return;
		}
		File[] fileArray = f.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return (!pathname.isDirectory());
			}
		});

		List<String> srcFileList = new ArrayList<>();
		List<String> destFileList = new ArrayList<>();

		if (!destLocation.endsWith("\\")) {
			destLocation.concat("\\");
		}

		for (File tempFile : fileArray) {
			srcFileList.add(tempFile.getAbsolutePath());
			destFileList.add(destLocation + tempFile.getName());
		}

		List<Future> futureList = new ArrayList<Future>();
		for (int index = 0; index < srcFileList.size(); index++) {
			futureList.add(singleTask(srcFileList.get(index), destFileList.get(index)));
		}

		Utils.showMsg(summaryBuilder(futureList, srcFileList));
	}

	private String summaryBuilder(List<Future> futureList,
			List<String> srcFileList) {
		StringBuilder success = new StringBuilder();
		StringBuilder failed = new StringBuilder();
		for (int index = 0; index < futureList.size(); index++) {
			try {
				Future f = futureList.get(index);
				String i = (String) f.get();
				if (i.equals(Utils.SUCCESS)) {
					success.append(srcFileList.get(index));
				} else {
					failed.append(srcFileList.get(index));
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return "Copied successfully [" + success + "] \n Failed [" + failed
				+ "]";
	}

	/**
	 * This makes the application ready for next work item
	 * 
	 */
	private void makeReady() {
		setState(STATE.DONE);
		setState(STATE.READEY);
	}

	private void setState(STATE state) {
		currentState = state;
	}

	@Override
	public boolean stop() {
		return false;
	}

	@Override
	public boolean terminate() {
		return false;
	}

	@Override
	public void finish() {

	}

	public STATE getCurrentState() {
		return currentState;
	}

	public void setCurrentState(STATE currentState) {
		this.currentState = currentState;
	}

	public String getSourceLocation() {
		return sourceLocation;
	}

	public void setSourceLocation(String sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	public String getDestLocation() {
		return destLocation;
	}

	public void setDestLocation(String destLocation) {
		this.destLocation = destLocation;
	}

	public COPY_OPTION getCopyOption() {
		return copyOption;
	}

	public void setFile(COPY_OPTION option) {
		this.copyOption = option;
	}

}
