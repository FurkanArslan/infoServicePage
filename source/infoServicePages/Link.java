package infoServicePages;

public class Link extends Item {

	private String url;
	private String text;
	
	public Link(int id, String[] key,String url,String text) {
		super(id, key);
		this.text=text;
		this.url=url;
	}
	
	public String getUrl() {
		return url;
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
