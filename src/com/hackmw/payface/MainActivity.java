package com.hackmw.payface;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
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
    	Intent myIntent = new Intent("com.google.zxing.client.android.SCAN");
    	myIntent.putExtra("SCAN_MODE", "QR_CODE_MODE");
    	myIntent.putExtra("SAVE_HISTORY", false);
    	startActivityForResult(myIntent, 0);
    }
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                    String contents = data.getStringExtra("SCAN_RESULT"); //this is the result
                    TextView t = (TextView)findViewById(R.id.textView2);
//                    t.setText(contents);
                    //make the request
                    //recieve request as json
                    try {
						JSONObject json = (JSONObject) new JSONParser().parse(contents);//_response);
						t.setText("Payment successfully recieved from TESTCUSTOMER1 for $36.57");
					} catch (ParseException e) {
						// TODO Auto-generated catch block
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
 