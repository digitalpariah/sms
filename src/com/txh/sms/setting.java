/**
 * date:013-06-02
 * settings */
package com.txh.sms;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;

public class setting extends PreferenceActivity {
    /** Called when the activity is first created. */
    private txApplication tx;
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);    
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent(this,MainActivity.class));
			anim();
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
    
    private void anim(){
		tx = (txApplication)getApplication();
		String anim = tx.getAnim();
		if(anim.equals("fade")){
			overridePendingTransition(R.anim.fade, R.anim.hold); 
		}
		else if(anim.equals("zoom")){
			overridePendingTransition(R.anim.my_scale_action,R.anim.my_alpha_action);
		}
		else if(anim.equals("roll")){
			overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
		}
		else if(anim.equals("iphone")){
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
		else if(anim.equals("Staggered")){
			overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);
		}
		else if(anim.equals("unfold")){
			overridePendingTransition(R.anim.unfold_enter,R.anim.unfold_exit);
		}
		//default is ubuntu style
		else{
			overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
		}
	}
}