package cn.itcast.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import cn.itcast.utils.HttpClientUtil;

public class HttpClientTest {

	@Test
	public void testGet() throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();

		HttpGet httpGet = new HttpGet("http://localhost:8080/itcast297/loginAction_login?username=cgx&password=123456");
		CloseableHttpResponse response = client.execute(httpGet);

		HttpEntity entity = response.getEntity();
		String string = EntityUtils.toString(entity);
		System.out.println(string);
	}

	@Test
	public void testPost() throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost("http://localhost:8080/itcast297/loginAction_login");
		
		ArrayList<BasicNameValuePair> arrayList = new ArrayList<BasicNameValuePair>();
		arrayList.add(new BasicNameValuePair("username", "cgx"));
		arrayList.add(new BasicNameValuePair("password", "123456"));
		
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(arrayList);
		
		httpPost.setEntity(entity);
		
		CloseableHttpResponse response = client.execute(httpPost);

		HttpEntity entity1 = response.getEntity();
		String string = EntityUtils.toString(entity1);
		System.out.println(string);
	}
	
	@Test
	public void testHttpUtils(){
		HashMap map = new HashMap();
		map.put("username", "cgx");
		map.put("password", "123456");
		String doPost = HttpClientUtil.doPost("http://localhost:8080/itcast297/loginAction_login",map);
		String zTreeJson = HttpClientUtil.doGet("http://localhost:8080/itcast297/sysadmin/roleAction_genzTreeNodes?id=4028a1c34ec2e5c8014ec2ebf8430001");
		System.out.println(zTreeJson);
	}
}
