package cn.com.ths.wyyx.im;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This class echoes a string called from JavaScript.
 */
public class thsWyyxIM extends CordovaPlugin {
    public static final  String TAG = "thsWyyxIM-CordovaPlugin";
    private static thsWyyxIM instance;
    public thsWyyxIM() {
        instance = this;
    }
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
       // requestPermissions();
        Log.e(TAG,"initialize");
    }

//     @TargetApi(23)
//     private void requestPermissions() {
//         PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(cordova.getActivity(), new PermissionsResultAction() {
//             @Override
//             public void onGranted() {
// //				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
//             }

//             @Override
//             public void onDenied(String permission) {
//                 //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
//             }
//         });
//     }
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        // 登录即时通讯服务器
        if (action.equals("loginIM")) {
            //userName,pwd
            String userName = args.getString(0);
            String pwd = args.getString(1);
            this.loginIM( userName, pwd, callbackContext);
            return true;
         // 退出登录
        }else  if(action.equals("logout")){
            this.logout(callbackContext);
            return true;
        }else if(action.equals("isLoggedIn")){
        //    boolean isLoggedIn = DemoHelper.getInstance().isLoggedIn();
        //     callbackContext.success(isLoggedIn+"");
            return true;
        }else if(action.equals("enterRoom")){

            return true;
        }else if(action.equals("createChannel")){
            
            return true;
        }
        return false;
    }

    /**
     * 退出登录
     */
    private  void logout(CallbackContext callbackContext){

        callbackContext.success("success");
    }
    /**
     * 登录IM服务器
     */
    private void loginIM(String currentUsername, String currentPassword,CallbackContext callbackContext){
        
        callbackContext.success("success");
    }
   
  
}
