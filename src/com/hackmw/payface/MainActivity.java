package com.hackmw.payface;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;
import android.provider.MediaStore;

public class MainActivity extends Activity {
	protected final String VENDOR_ID = "TESTVENDOR1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void imageCapture(View v) {
    	TextView t = (TextView)findViewById(R.id.textView2);
    	t.setText("");
    	Intent myIntent = new Intent("com.google.zxing.client.android.SCAN");
    	myIntent.putExtra("SCAN_MODE", "QR_CODE_MODE");
    	myIntent.putExtra("SAVE_HISTORY", false);
    	startActivityForResult(myIntent, 0);
    }
    
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                    final String contents = data.getStringExtra("SCAN_RESULT"); //this is the result
                    TextView t = (TextView)findViewById(R.id.textView2);
//                    t.setText(contents);
                 // HTTP
                    String url = "http://payface.cfapps.io/requestPayment.json?userId=TESTMERCHANT1&paymentToken=" + contents + "&amountUSD=5000";
                    InputStream is = null;
                    String result = "";
            		try {	    	

            			AsyncHttpClient client = new AsyncHttpClient();
            			client.get(url, new AsyncHttpResponseHandler() {
            				TextView t = (TextView)findViewById(R.id.textView2);
            				
            			    @Override
            			    public void onStart() {
            			        // called before request is started
            			    }

            			    @Override
            			    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
            			        // called when response HTTP status is "200 OK"
            			    	JSONObject jsonObject = null;
            			    	String responseString = null;
								try {
									responseString = new String(response);
									Log.v("dbg", responseString);
									jsonObject = new JSONObject(responseString);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
            			    	String rtnMsg;
            			    	try {
									rtnMsg = jsonObject.get("text").toString();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									rtnMsg = "Unhandled Exception Encountered";
								}
            			    	if (rtnMsg.equals("CONFIRM")) {
            			    		rtnMsg = "Your request for payment from account " + contents + " has completed successfully";
            			    	} else {
            			    		rtnMsg = "Something fucked up -- Message: " + rtnMsg;
            			    	}
            			    	t.setText(rtnMsg);
            			    }

            			    @Override
            			    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
            			    	t.setText("Server request failed, please verify your connection status and try again.");
            			    }

            			    @Override
            			    public void onRetry(int retryNo) {
            			        // called when request is retried
            			    }
            			});
            			
            		} catch(Exception e) {
            			Log.v("dbg", "failed here: " + e.toString());
            			e.printStackTrace();
            		}    	
            } else 
            if (resultCode == RESULT_CANCELED) {
              // Handle cancel
            	
            }
        }
    }
    
    //

    /*
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
 