package infoServicePages;

import java.util.StringTokenizer;
import tr.edu.hacettepe.cs.minio.MinioReader;


public class InputDelegate {
	
	private MinioReader fileReader;
	
	public InputDelegate(String arguman) {
		fileReader= MinioReader.getFileReader(arguman);
	}

	public String read(){
		String line=null;
		if(fileReader.inputAvailable()){
			line=fileReader.readLine();
		}
		
		return line;
	}
	
	public void close(){
		fileReader.close();
	}
}
