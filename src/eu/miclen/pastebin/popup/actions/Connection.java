package eu.miclen.pastebin.popup.actions;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.internal.Workbench;

import eu.miclen.pastebin.Activator;
import eu.miclen.pastebin.preferences.PreferenceConstants;

class Connection {
	private String url = "http://pastebin.com/api/api_post.php";
	private String urlLogin = "http://pastebin.com/api/api_login.php";
	private String dev_key = "insert you dev api key";
	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	private URL baseConnection;
	private String postData;
	public Connection() {
		try {
			baseConnection = new URL(url);
		} catch (MalformedURLException e) {
			MessageDialog.openError(Workbench.getInstance().getActiveWorkbenchWindow().getShell(), "Error URL", "Url not valid");
		} 
		postData ="";
	}
	public String sendContent(String code,String nameFile) {
		try {
			HttpURLConnection con = (HttpURLConnection) baseConnection.openConnection();
			resetPostContent();
			createPostData("api_option","paste");
			createPostData("api_user_key",store.getString(PreferenceConstants.P_USERKEY));
			int privato = store.getBoolean(PreferenceConstants.P_PRIVATE) ? 1 : 0;
			createPostData("api_paste_private",""+privato);
			createPostData("api_paste_name",URLEncoder.encode(nameFile,"UTF-8"));
			createPostData("api_paste_expire_date",store.getString(PreferenceConstants.P_EXPIRYDATE));
			String format = nameFile.substring(nameFile.lastIndexOf(".")+1);
			createPostData("api_paste_format",format);
			createPostData("api_dev_key",dev_key);
			createPostData("api_paste_code",URLEncoder.encode(code,"UTF-8"));
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(getPostData());
			wr.flush();
			wr.close();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			return response.toString();
			
			
		} catch (IOException e) {
			MessageDialog.openError(Workbench.getInstance().getActiveWorkbenchWindow().getShell(), "Error URL", "Connection error");
			return "Error";
		}
	}
	private void resetPostContent() {
		postData = "";
	}
	private String createPostData(String name,String value) {
		if(postData.equals(""))
			postData = postData.concat(name).concat("=").concat(value);
		else
			postData = postData.concat("&").concat(name).concat("=").concat(value);
		return postData;
	}
	private String getPostData() {
		return postData;
	}
	
	public String getUserKey(String username, String password) {
		try {
			URL privateConnection = new URL(urlLogin);
			HttpURLConnection con = (HttpURLConnection) privateConnection.openConnection();
			resetPostContent();
			createPostData("api_dev_key",dev_key);
			createPostData("api_user_name",URLEncoder.encode(username, "UTF-8"));
			createPostData("api_user_password",URLEncoder.encode(password, "UTF-8"));
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(getPostData());
			wr.flush();
			wr.close();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			return response.toString();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
