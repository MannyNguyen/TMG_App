package object;

public class RowItem {

	private int imageId;
	private String content;
	private boolean isHeader;
	private Class<?> intentClass;

	
	public RowItem(Class<?> intentClass,int imageId, String content, boolean isHeader) {
		this.intentClass = intentClass;
		this.imageId = imageId;
		this.content = content;
		this.isHeader = isHeader;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public boolean getIsHeader() {
		return isHeader;
	}

	public void setHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}

	public Class<?> getIntentClass() {
		return intentClass;
	}

	public void setIntentClass(Class<?> intentClass) {
		this.intentClass = intentClass;
	}

	

}
