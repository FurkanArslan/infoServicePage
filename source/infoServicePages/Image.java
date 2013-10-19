package infoServicePages;

public class Image extends Item {

	private String imgPath;
	private int width;
	private int height;
	public Image(int id, String[] key,String imgPath, int width, int height) {
		super(id, key);
		this.imgPath=imgPath;
		this.width=width;
		this.height=height;
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public String getImgPath() {
		return imgPath;
	}
	public int getId(){
		return super.getId();
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
