package edu.sfsu.cs.orange.ocr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.googlecode.tesseract.android.TessBaseAPI;

import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class SendResultAsyncTask extends AsyncTask<String, String, String> {
	private static final String TAG = OcrInitAsyncTask.class.getSimpleName();
	
	private CaptureActivity activity;

	SendResultAsyncTask(CaptureActivity activity) {
	    this.activity = activity;
	}
	
	@Override
	protected String doInBackground(String... uri) {
		HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
        	String url = uri[0] + "?res=" + URLEncoder.encode(uri[1], "utf-8");
        	HttpGet httpget = new HttpGet(url);
            response = httpclient.execute(httpget);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                response.getEntity().getContent().close();
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return responseString;
	}
	
	@Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result != null) {
	        Toast toast = Toast.makeText(activity, result, Toast.LENGTH_SHORT);
	        toast.setGravity(Gravity.TOP, 0, 0);
	        toast.show();
        }else {
        	Toast toast = Toast.makeText(activity, "request faild.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }

}
