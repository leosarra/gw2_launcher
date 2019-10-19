package updater;


import com.vdurmont.semver4j.Semver;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateNotifier implements Runnable {
    private final String version;
    private final String repoName;
    private final String username;

    public UpdateNotifier(String version, String repoName, String username) {
        this.version = version;
        this.repoName = repoName;
        this.username = username;

    }

    @Override
    public void run() {
        Logger logger = Logger.getLogger("mis_update_notifier");
        String output = getJSONData();
        if (output != null && output.startsWith("[")) {
            JSONArray jarray;
            try {
                jarray = new JSONArray(output);
                if (jarray.length() > 0) {
                    JSONObject last_release = jarray.getJSONObject(0);
                    if (last_release.has("tag_name")) {
                        Semver sem = new Semver(last_release.getString("tag_name"));
                        //System.out.println(last_release.getString("tag_name"));
                        if (sem.isGreaterThan(version)) {
                            String URI = "http://www.github.com/" + username + "/" + repoName + "/releases";
                            logger.log(Level.SEVERE, "An update is avaiable. Please get the update at " + URI);
                            int dialogButton = JOptionPane.YES_NO_OPTION;
                            int dialogResult = JOptionPane.showConfirmDialog(null, "An update for the launcher is avaiable. Would you like to go to the download page?", "An update is avaiable", dialogButton);
                            if (dialogResult == JOptionPane.YES_OPTION) {
                                try {
                                    java.awt.Desktop.getDesktop().browse(new java.net.URI(URI));
                                } catch (IOException | URISyntaxException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else logger.log(Level.INFO, "GW2_Launcher is updated");
                    } else logger.log(Level.WARNING, "Unable to find updates on Github");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else logger.log(Level.WARNING, "Unable to find updates on Github");
    }


    private String getJSONData() {
        Request req = new Request.Builder().url("https://api.github.com/repos/" + username + "/" + repoName + "/releases").header("Accept", "application/json")
                .header("Content-Type", "application/json").build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).retryOnConnectionFailure(true)
                .build();
        Response resp = null;
        String body = null;
        try {
            resp = client.newCall(req).execute();
            if (resp.body() == null) return null;
            body = resp.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }
}
