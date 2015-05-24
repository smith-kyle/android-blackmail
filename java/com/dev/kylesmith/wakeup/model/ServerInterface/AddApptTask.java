package com.dev.kylesmith.wakeup.model.ServerInterface;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kylesmith on 1/15/15.
 */
public class AddApptTask extends AsyncTask<Object, String, String> {
    public AsyncResponse delegate = null;
    private String result;
    private String linekey;

    @Override
    protected String doInBackground(Object... params) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(ApiConstants.URL);

        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        entityBuilder.addTextBody(ApiConstants.KEY_PASSWORD, ApiConstants.PASSWORD);
        entityBuilder.addTextBody(ApiConstants.KEY_COMMAND, ApiConstants.COMMAND_ADD_APPT);
        entityBuilder.addTextBody(ApiConstants.KEY_ID, (String) params[0]);
        entityBuilder.addTextBody(ApiConstants.KEY_DATE, parseDate((Date)params[1]));

        HttpEntity entity = entityBuilder.build();

        post.setEntity(entity);

        try {
            HttpResponse response = client.execute(post);
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
            Log.i("HTTP RESPONSE", "The new linkey is: " + result);
            if(result.isEmpty()) return "";
            else{
                linekey = result;
                return result;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "";

    }

    @Override
    protected void onPostExecute(String s) {
        delegate.processFinish(result);
    }

    private String parseDate(Date date){
        return date.toString();
    }
}
