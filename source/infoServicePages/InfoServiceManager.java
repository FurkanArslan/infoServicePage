package infoServicePages;

import java.util.ArrayList;

import test.InfoServiceTemplate;

public class InfoServiceManager extends InfoServiceTemplate {

	private ArrayList<Article> articles = new ArrayList<Article>(); 
	private ArrayList<Item> referance= new ArrayList<Item>();
	private ArrayList<Image> images= new ArrayList<Image>();
	private ArrayList<Link> links= new ArrayList<Link>();
	
	public boolean addArticle(int id, String[] keys, String filePath) {
		Boolean idControl=idControl(id);
		if(idControl==true){
			Article newArticle= new Article(id, keys, filePath);
			articles.add(newArticle);
			return true;
		}
		else
			return false;
	}
	public boolean addImage(int id, String[] keys, String imgPath, int width,int height) {
		Boolean idControl=idControl(id);
		if(idControl==true){	
			Image newImage = new Image(id, keys, imgPath,width,height);
			images.add(newImage);
			return true;
		}
		else
			return false;
	}
	public boolean addLink(int id, String[] keys, String url, String text) {
		Boolean idControl=idControl(id);
		if(idControl==true){
			Link newLink = new Link(id,keys,url,text);
			links.add(newLink);
			return true;
		}
		else
			return false;
	}
	public String addArticleReference(int articleId, int referenceId) {
		int articleLocation;
		int checkingImage,checkingLink,checkingArticle;
		
		articleLocation=checkingArticles(articleId);
		if(articleLocation==-1)						//checking whether article id belong to one of other item or not
		{	
			checkingImage=checkingImages(articleId);
			checkingLink=checkingLinks(articleId);
			if(checkingImage!=-1 || checkingLink!=-1)
				return articleId + " is not article";
			else									//if article id does not belong to any item, returns article not found
				return articleId + " is not found";
		}
		
		checkingImage=checkingImages(referenceId);	    //determine referance's location in images 
		checkingLink=checkingLinks(referenceId);		//determine referance's location in links 
		checkingArticle=checkingArticles(referenceId);	//determine referance's location in articles
		if(checkingImage!=-1){
			articles.get(articleLocation).addReferance(images.get(checkingImage));
			return articleId + " referenced by " + referenceId;
		}
		else if(checkingLink!=-1){
			articles.get(articleLocation).addReferance(links.get(checkingLink));
			return articleId + " referanced by " + referenceId;
		}
		else if(checkingArticle!=-1){
			articles.get(articleLocation).addReferance(articles.get(checkingArticle));
			return articleId + " referanced by " + referenceId;
		}
		else
			return referenceId + " is not found";
	}
	public int searchPage(String resultFile, String[] keys, ItemType itemType,SearchType algType) {
		OutputDelegate output = new OutputDelegate(resultFile, 0);
		output.write(PageStartTag);
		int count=0;
		if(itemType== ItemType.ARTICLE)
			count=searchPageArticle(algType, keys, output);
		
		else if(itemType== ItemType.IMAGE)
			count=searchPageImage(algType, keys, output);
		
		else if(itemType== ItemType.LINK)
			count=searchPageLink(algType, keys, output);
		
		else						//it's means that ItemType equals to ALL
		{
			count+=searchPageArticle(algType, keys, output);
			count+=searchPageImage(algType, keys, output);
			count+=searchPageLink(algType, keys, output);	
		}
		output.write(PageEndTag);
		return count;
	}
	public int infoPage(String resultFile, int id) {
		OutputDelegate output= new OutputDelegate(resultFile, 0);
		output.write(PageStartTag);
		int[] usedReferance=new int[100];		//ý used this array for determine which variable's write.
		int usedAmount=0;						//for determine how many referance was written 
		usedReferance[usedAmount++]=id;
		usedAmount=searchReferance(id, output,usedReferance,usedAmount,null,0);
		output.write(PageEndTag);
		return usedAmount;
	}
	public int extendedInfoPage(String resultFile, int id) {
		OutputDelegate output= new OutputDelegate(resultFile, 0);
		output.write(PageStartTag);
		int[] usedArticle=new int[100];			//ý used this array for determine which variable's write.
		int usedArticleAmount=0;				//for determine how many referance was written
		usedArticle[usedArticleAmount++]=id;
		int endTagCount=0;
	    usedArticleAmount=searchReferance(id, output,usedArticle, usedArticleAmount, "extended",endTagCount);
		output.write(PageEndTag);
		return usedArticleAmount;
	}
	private int searchReferance(int articleId,OutputDelegate output,int[] usedReferance, int usedReferanceAmount, String type, int endTagCount){
		int articleLocation=checkingArticles(articleId);
		output.write(PrepareArticle(articles.get(articleLocation).getText()));		//write article text
		output.write(ReferanceStartTag);											//write reference tag
		
		referance=articles.get(articleLocation).getReferance();						//determine reference
		for (int referanceLocation = 0; referanceLocation < referance.size(); referanceLocation++) {
			int referanceId=referance.get(referanceLocation).getId();					//determine reference id
			boolean control=usedControl(usedReferance, usedReferanceAmount, referanceId);//controlling whether reference's element has used or not.
			
			if(control==false)			//if used,method return false.And The item does not write
				continue;
			usedReferance[usedReferanceAmount++]=referanceId;
			int checkingArticle=checkingArticles(referanceId);// controlling whether reference's element is article or not
			if(checkingArticle!=-1)
			{
				if(type=="extended")		//if the process is extended and if necessary,the slope may use.
				{
					usedReferanceAmount=searchReferance(articles.get(checkingArticle).getId(),output, usedReferance, usedReferanceAmount, "extended",endTagCount);
					referance=articles.get(articleLocation).getReferance();
					continue;
				}
				output.write(PrepareArticle(articles.get(checkingArticle).getText())); //writing article's text
				continue;
			}
			
			int checkingImages=checkingImages(referanceId);	// controlling whether reference's element is image or not
			if(checkingImages!=-1)			//if reference's element is image, method return location in images 
			{		
				output.write(PrepareImage(images.get(checkingImages).getImgPath(), images.get(checkingImages).getWidth(), images.get(checkingImages).getHeight()));
				continue;
			}
			
			int checkingLinks=checkingLinks(referanceId);	// controlling whether reference's element is link or not
			if(checkingLinks!=-1)			//if reference's element is link, method return location in links 
			{
				output.write(PrepareLink(links.get(checkingLinks).getUrl(), links.get(checkingLinks).getText()));
			}
		}
		output.write(ReferanceEndTag);			//write referance end tag
		return usedReferanceAmount;
	}
	private boolean usedControl(int[] usedReferance, int usedReferanceAmount, int id) {
		for (int loop = 0; loop < usedReferanceAmount; loop++) {
			if(usedReferance[loop]==id)
				return false;
		}
		return true;
	}
	private int checkingImages(int id) {
		for (int check = 0; check < images.size(); check++) {
			if(images.get(check).getId()== id)
				return check;
		}
		return -1;
	}
	private int checkingLinks(int id){
		for (int check = 0; check < links.size(); check++) {
			if(links.get(check).getId()== id)
				return check;
		}
		return -1;	
	}
	private int checkingArticles(int articleId){
		int Location=0;
		for (; Location < articles.size(); Location++) {
			if(articles.get(Location).getId()==articleId){
				return Location;
			}
		}
		return -1;
	}
	private int searchPageArticle(SearchType algType,String[] keys,OutputDelegate output){
		boolean control;
		int count=0;
		for (int articleLocation = 0; articleLocation < articles.size(); articleLocation++) {
			if(algType== SearchType.OR)
				control=articles.get(articleLocation).searchPageOr(keys);
			else
				control=articles.get(articleLocation).searchPageAnd(keys);
			if(control==true){
				count++;
				output.write(PrepareArticle(articles.get(articleLocation).getText()));
			}
		}
		return count;
	}
	private int searchPageImage(SearchType algType,String[] keys,OutputDelegate output){
		boolean control;
		int count=0;
		for (int imageLocation = 0; imageLocation < images.size(); imageLocation++) {
			if(algType== SearchType.OR)
				control=images.get(imageLocation).searchPageOr(keys);
			else
				control=images.get(imageLocation).searchPageAnd(keys);
			if(control==true){
				count++;
				output.write(PrepareImage(images.get(imageLocation).getImgPath(),images.get(imageLocation).getWidth(),images.get(imageLocation).getHeight()));
			}
		}
		return count;
	}
	private int searchPageLink(SearchType algType,String[] keys,OutputDelegate output){
		boolean control;
		int count=0;
		for (int linkLocation = 0; linkLocation < links.size(); linkLocation++) {
			if(algType== SearchType.OR)
				control=links.get(linkLocation).searchPageOr(keys);
			else
				control=links.get(linkLocation).searchPageAnd(keys);
			if(control==true){
				count++;
				output.write(PrepareLink(links.get(linkLocation).getUrl(), links.get(linkLocation).getText()));
			}
		}
		return count;
	}
	private Boolean idControl(int id) {
		int checkingArticle=checkingArticles(id);
		int checkingImage=checkingImages(id);
		int checkingLink=checkingLinks(id);
		if(checkingArticle==-1 && checkingImage==-1 && checkingLink==-1)
			return true;
		else
			return false;
	}
}