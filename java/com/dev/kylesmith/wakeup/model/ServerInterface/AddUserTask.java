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
public class AddUserTask extends AsyncTask<Object, String, Integer> {
    private String result;
    private int userId;
    public AsyncResponse delegate = null;

    @Override
    protected Integer doInBackground(Object... params) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(ApiConstants.URL);

        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        entityBuilder.addTextBody(ApiConstants.KEY_PASSWORD, ApiConstants.PASSWORD);
        entityBuilder.addTextBody(ApiConstants.KEY_COMMAND, ApiConstants.COMMAND_ADD_USER);
        entityBuilder.addTextBody(ApiConstants.KEY_PHONE_NUM, (String)params[1]);
        entityBuilder.addBinaryBody(ApiConstants.KEY_PICTURE, (byte[])params[0]);

        HttpEntity entity = entityBuilder.build();

        post.setEntity(entity);

        try {
            HttpResponse response = client.execute(post);
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
            Log.i("HTTP RESPONSE", "The new user id is: " + result);
            if(result.isEmpty()) return 0;
            else{
                userId = Integer.parseInt(result);
                return userId;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;

    }

    @Override
    protected void onPostExecute(Integer id) {
        delegate.processFinish(userId);
    }
}
