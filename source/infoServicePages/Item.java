package infoServicePages;
public abstract class  Item {

	private String key[];
	private int id;
	
	public Item(int id,String key[]){
		this.key=key;
		this.id=id;
	}
	
	protected String[] getKey() {
		return key;
	}
	
	public int getId() {
		return id;
	}
	
	public abstract boolean searchPageOr(String[] keys);
	public abstract boolean searchPageAnd(String[] keys);
	
}
