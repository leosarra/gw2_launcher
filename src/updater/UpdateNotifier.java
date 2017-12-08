package updater;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vdurmont.semver4j.Semver;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.swing.JOptionPane;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateNotifier implements Runnable {
	private final String version;
	private final String repoName;
	private final String username;
	
    public UpdateNotifier(String version, String repoName, String username){
    	this.version=version;
    	this.repoName=repoName;
    	this.username=username;
        
    }

    @Override
    public void run() {
        Logger logger = Logger.getLogger("mis_update_notifier");
        String output= getJSONData();
        if (output.startsWith("[")){
            JSONArray jarray;
			try {
				jarray = new JSONArray(output);
	            if (jarray.length()>0){
	                JSONObject last_release=jarray.getJSONObject(0);
	                if (last_release.has("tag_name")){
	                    Semver sem = new Semver(last_release.getString("tag_name"));
	                    //System.out.println(last_release.getString("tag_name"));
	                    if (sem.isGreaterThan(version)) {
	                    	String URI="http://www.github.com/"+username+"/"+repoName+"/releases";
	                        logger.log(Level.SEVERE,"An update is avaiable. Please get the update at "+URI);
	                        int dialogButton=0;
	                        JOptionPane.showConfirmDialog(null,"An update for the launcher is avaiable. Would you like to go to the download page?","An update is avaiable",dialogButton);
	                        if (dialogButton==1){
	                        	try {
									java.awt.Desktop.getDesktop().browse(new java.net.URI(URI));
								} catch (IOException | URISyntaxException e) {
									e.printStackTrace();
								}
	                    	}
	                    }
	                    else logger.log(Level.INFO,"MIS is updated");
	                }
	            else logger.log(Level.WARNING,"Unable to find updates on Github");
	                }
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
			else logger.log(Level.WARNING,"Unable to find updates on Github");
    }


    private String getJSONData(){
        Client client = Client.create();
        WebResource webResource = client.resource(UriBuilder.fromUri("https://api.github.com/repos/"+username+"/"+repoName+"/releases").build());
        ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
        String output = response.getEntity(String.class);
        return output;
    }
}
