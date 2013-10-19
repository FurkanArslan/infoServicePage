package infoServicePages;


import java.util.ArrayList;

public class Article extends Item {
	
	private String filePad;
	private String text;
	private ArrayList<Item> referances= new ArrayList<Item>();
	
	public Article(int id, String[] key,String filePad) {
		super(id, key);
		this.filePad=filePad;
		this.text=readText(filePad);
	}

	private String readText(String filePad2) {
		InputDelegate input=new InputDelegate(filePad2);
		String text="";
		String read=input.read();
		while(read!=null)
		{
			text+=read+"<br/>";
			read=input.read();
		}
		return text;
	}

	public void addReferance(Item referance){
		referances.add(referance);
	}
	
	public String getFilePad() {
		return filePad;
	}
	public void setFilePad(String filePad) {
		this.filePad = filePad;
	}
	public int getId(){
		return super.getId();
	}
	public String[] getKey(){
		return super.getKey();
	}
	public ArrayList<Item> getReferance(){
		return referances;
	}
	public String getText() {
		return text;
	}
	public boolean searchPageOr(String[] keys) {
		for (int keyLocation = 0; keyLocation < keys.length; keyLocation++) {
			boolean control=search(keys[keyLocation]);
			if(control==true)
				return true;
		}
		return false;
	}
	private boolean search(String searchKey){
		String[] key=this.getKey();
		for (int Location = 0; Location < key.length; Location++) {
			if(key[Location].equals(searchKey))
				return true;
		}
		return false;
	}

	public boolean searchPageAnd(String[] keys) {
		for (int keyLocation = 0; keyLocation < keys.length; keyLocation++) {
			boolean control=search(keys[keyLocation]);
			if(control==false)
				return false;
		}
		return true;
	}
}
