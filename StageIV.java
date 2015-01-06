import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.time.Instant;

public class StageIV {
	private static final String timeURL = "http://challenge.code2040.org/api/time";
	private static final String validateTime = "http://challenge.code2040.org/api/validatetime";

	public static void main(String[] args) throws Exception {
		Registration registration = new Registration();
		String token = registration.getToken();
		StageIV stageIV = new StageIV();
		String getNewDate = stageIV.getDate(token);
		System.out.println(stageIV.sendDate(token, getNewDate));
	}

	public String sendDate(String token, String date) throws Exception {
		if(token == null || date == null) return null;
		ResponseHandler<String> responseHandler = new BasicResponseHandler();

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("token", token);
		jsonObj.put("datestamp", date);

		HttpPost httpPost = new HttpPost(validateTime);
		StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		String body = responseHandler.handleResponse(response);
		return body;
	}

	public String getDate(String token) throws Exception {
		if (token == null) {
			return null;
		}

		ResponseHandler<String> responseHandler = new BasicResponseHandler();

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("token", token);

		HttpPost httpPost = new HttpPost(timeURL);
		StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		String body = responseHandler.handleResponse(response);
		String[] array = body.split(",");
		String datestamp = array[0].substring(24, array[0].length() - 1);
		String interval = array[1].substring(11, array[1].length() - 3);
		return addInterval(datestamp, interval);
	}

	private String addInterval(String date, String interval) {
		if (date == null || interval == null)
			return null;
		Instant timeStamp = Instant.parse(date);
		timeStamp = timeStamp.plusSeconds(Long.parseLong(interval));
		return timeStamp.toString();
	}
}
