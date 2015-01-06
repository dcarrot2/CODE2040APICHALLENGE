import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

public class Registration {
	
	public static void main(String[] args) throws Exception {
		Registration registration = new Registration();
		
		registration.sendPost();
	}
	
	// HTTP POST request
		private void sendPost() throws Exception {
	 
			String url = "http://challenge.code2040.org/api/register";
			String email = "diazjfdaniel@gmail.com";
			String github = "https://github.com/dcarrot2/CODE2040APICHALLENGE";
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
	 
			
			JSONObject jsonObj = new JSONObject();

			    jsonObj.put("email", email);
			    jsonObj.put("github", github);
			
			HttpPost httpPost = new HttpPost(url);
			StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(httpPost);
			String body = responseHandler.handleResponse(response);
			System.out.println(body);
		}
}
