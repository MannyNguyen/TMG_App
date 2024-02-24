package object;

public class News {
	private int id;
	private String news_id;
	private String news_title;
	private String news_subtitle;
	private String news_contents;
	private String news_image_url;

	public News(String news_id, String news_title,String subtitle,String news_contents, String news_image_url) {
		super();
		this.news_id = news_id;
		this.news_title = news_title;
		this.news_subtitle = subtitle;
		this.news_contents = news_contents;
		this.news_image_url = news_image_url;
	}
	public News()
	{
		super();
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNews_id() {
		return news_id;
	}

	public void setNews_id(String news_id) {
		this.news_id = news_id;
	}

	public String getNews_title() {
		return news_title;
	}

	public void setNews_title(String news_title) {
		this.news_title = news_title;
	}
	

	public String getNews_contents() {
		return news_contents;
	}

	public void setNews_contents(String news_contents) {
		this.news_contents = news_contents;
	}

	public String getNews_image_url() {
		return news_image_url;
	}

	public void setNews_image_url(String news_image_url) {
		this.news_image_url = news_image_url;
	}
	public String getNews_subtitle() {
		return news_subtitle;
	}
	public void setNews_subtitle(String news_subtitle) {
		this.news_subtitle = news_subtitle;
	}

	

}
