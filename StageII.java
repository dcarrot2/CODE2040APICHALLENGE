import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;


public class StageII {
	private static final String haystackURL = "http://challenge.code2040.org/api/haystack";
	private static final String validateNeedleURL = "http://challenge.code2040.org/api/validateneedle";
	
	public static void main(String[] args) throws Exception {
		StageII stageII = new StageII();
		Registration registration = new Registration();
		String token = registration.getToken();
		int index = stageII.getNeedleAndHay(token);
		stageII.sendIndex(String.valueOf(index), token);
	}
	
	public boolean sendIndex(String index, String token) throws Exception{
		if(index == null) return false;
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("token", token);
		jsonObj.put("needle", index);
		
		HttpPost httpPost = new HttpPost(validateNeedleURL);
		StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		String body = responseHandler.handleResponse(response);
		System.out.println(body);
		return false;
	}
	
	public int getNeedleAndHay(String token) throws Exception{
		if(token == null) return -1;
		int index = 0;
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("token", token);
		
		HttpPost httpPost = new HttpPost(haystackURL);
		StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		String body = responseHandler.handleResponse(response);
		
		// split by comma since it was a more comman delimiter
		String[] hay = body.split(",");
		// parse last element for needle
		String needle = hay[hay.length - 1].substring(9, hay[hay.length - 1].length() - 3);
		// removed garbage from last element of the list
		hay[hay.length - 2] = hay[hay.length -2].substring(0, hay[hay.length -2].length() - 1);
		for(int i = 1; i < hay.length - 1; i++){
			if(needle.equals(hay[i])){
				index = i;
				break;
			}
		}
		return index;
	}
}
