package infoServicePages;

import tr.edu.hacettepe.cs.minio.*;

public class OutputDelegate {
	
	private MinioWriter fileWriter;
	private int lineNumber;
	
	public OutputDelegate(String arguman,int lineNumber) {
		fileWriter = MinioWriter.getFileWriter(arguman);
		this.lineNumber=lineNumber;
	}
	public void write(String write)
	{
		fileWriter.println(write);
	}
	
	public void split()
	{
		for(int number=1;number<lineNumber;number++)
		{
		fileWriter.print("-");
		}
		fileWriter.println("-");
	}
}
