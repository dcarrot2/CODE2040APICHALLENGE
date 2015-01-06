import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class StageIII {
	private final static String prefixURL = "http://challenge.code2040.org/api/prefix";
	private final static String prefixValidateURL = "http://challenge.code2040.org/api/validateprefix";
	
	public static void main(String[] args) throws Exception {
		Registration registration = new Registration();
		String token = registration.getToken();
		StageIII stageIII = new StageIII();
		ArrayList<String> noPrefixes = stageIII.getNoPrefixes(token);
		stageIII.sendPrefixes(noPrefixes, token);
	}
	
	public boolean sendPrefixes(ArrayList<String> noPrefixes, String token) throws Exception{
		if(noPrefixes ==  null || token == null){
			return false;
		}
		
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		
		System.out.println(noPrefixes.toString());
		
		JSONObject jsonObj = new JSONObject();
		JSONArray mArray = new JSONArray();
		jsonObj.put("token", token);
		for(String string : noPrefixes)
			mArray.put(string);
		jsonObj.put("array", mArray);
		
		HttpPost httpPost = new HttpPost(prefixValidateURL);
		StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		String body = responseHandler.handleResponse(response);
		System.out.println(body);
		return true;
	}
	
	
	public ArrayList<String> getNoPrefixes(String token) throws Exception{
		if(token == null) return null;
		int index = 0;
		ArrayList<String> noPrefixes = new ArrayList();
		
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("token", token);
		
		HttpPost httpPost = new HttpPost(prefixURL);
		StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		String body = responseHandler.handleResponse(response);
		
		// manually parsing. Only the first and last elements needed extra parsing
		String[] array = body.split(",");
		String prefix = array[array.length - 1];
		String firstIndex = array[0];
		String lastIndex = array[array.length - 2];
		firstIndex = firstIndex.substring(20);
		lastIndex = lastIndex.substring(0, lastIndex.length() - 1);
		array[0] = firstIndex;
		array[array.length - 2] = lastIndex;
		String parsedPrefix = prefix.substring(9, prefix.length() - 4);
		for(int i = 0; i < array.length - 1; i++){
			if(!array[i].startsWith(parsedPrefix))
				noPrefixes.add(array[i].substring(1, array[i].length() - 1));
		}
		return noPrefixes;
	}
}
