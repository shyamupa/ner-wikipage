package fine2coarse;

public class LabeledToken {
	private String token;
	private String label;
	public LabeledToken(String token,String label)
	{
		this.token = token;
		this.label = label;
		
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

}
