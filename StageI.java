import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;


public class StageI {
	private static final String getUrl = "http://challenge.code2040.org/api/getstring";
	private static final String postUrl = "http://challenge.code2040.org/api/validatestring";
	
	public static void main(String[] args) throws Exception {
		Registration registration = new Registration();
		StageI stageI = new StageI();
		String key = registration.getToken();
		System.out.println("Key: " + key);
		String toReverse = stageI.getString(key);
		System.out.println(toReverse);
		String reverse = new StringBuffer(toReverse).reverse().toString();
		System.out.println(reverse);
		stageI.sendString(reverse, key);
	}
	
	public String getString(String token) throws Exception{
		if(token == null) return null;
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		 
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("token", token);
		
		HttpPost httpPost = new HttpPost(getUrl);
		StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		String body = responseHandler.handleResponse(response);
		String parse[] = body.split(":");
		String straight = parse[1];
		return straight.substring(1, straight.length() - 3);
	}
	
	public boolean sendString(String reversed, String token) throws Exception{
		if(reversed == null || token == null) return false;
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		 
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("token", token);
		jsonObj.put("string", reversed);
		
		HttpPost httpPost = new HttpPost(postUrl);
		StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		String body = responseHandler.handleResponse(response);
		System.out.println("Response: " + body);
		return true;
	}
}
