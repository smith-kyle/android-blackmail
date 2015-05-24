package com.dev.kylesmith.wakeup.model.ServerInterface;

import android.os.AsyncTask;

import com.google.android.gms.common.api.Api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by kylesmith on 1/18/15.
 */
public class DeleteApptTask extends AsyncTask<String, Integer, Object> {
    private boolean success = false;
    public AsyncResponse delegate = null;

    @Override
    protected Object doInBackground(String... params) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(ApiConstants.URL);

        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entityBuilder.addTextBody(ApiConstants.KEY_PASSWORD, ApiConstants.PASSWORD);
        entityBuilder.addTextBody(ApiConstants.KEY_COMMAND, ApiConstants.COMMAND_DELETE_APPT);
        entityBuilder.addTextBody(ApiConstants.KEY_LINEKEY, (String)params[0]);

        HttpEntity entity = entityBuilder.build();
        post.setEntity(entity);

        try{
            HttpResponse response = client.execute(post);
            HttpEntity httpEntity = response.getEntity();
            success = Boolean.parseBoolean(EntityUtils.toString(httpEntity));
        }catch (Exception e){e.printStackTrace();}

        return success;
    }

    @Override
    protected void onPostExecute(Object b) {
        delegate.processFinish(success);
    }
}
