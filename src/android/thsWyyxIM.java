package cn.com.ths.wyyx.im;
import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import com.netease.nim.chatroom.demo.base.util.string.MD5;
import com.netease.nim.chatroom.demo.education.activity.ChatRoomActivity;
import com.netease.nim.chatroom.demo.education.helper.PluginHelper;
import com.netease.nim.chatroom.demo.im.business.LogoutHelper;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PermissionHelper;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class echoes a string called from JavaScript.
 */
public class thsWyyxIM extends CordovaPlugin {
    public static final  String TAG = "thsWyyxIM-CordovaPlugin";
    private static thsWyyxIM instance;
    private CallbackContext callbackContext;
    private String action;
    private JSONArray args;

    public thsWyyxIM() {
        instance = this;
    }
    /**
     * 权限列表 android.permission.CAMERA
     */
    private String[] locPerArr = new String[] {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.VIBRATE,

    };
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        // requestPermissions();
        Log.e(TAG,"initialize");
    }

    /**
     * 检查权限并申请,权限没问题即可执行操作
     */
    private void promtForAction() {
        for (int i = 0, len = locPerArr.length; i < len; i++) {
            if (!PermissionHelper.hasPermission(this, locPerArr[i])) {
                PermissionHelper.requestPermission(this, i, locPerArr[i]);
                return;
            }
        }
        this.exeAction();
    }

    @Override
    public void onRequestPermissionResult(int requestCode,
                                          String[] permissions, int[] grantResults) throws JSONException {
        // TODO Auto-generated method stub
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                return;
            }
        }
        this.promtForAction();
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
        }else if(action.equals("enterRoom")||action.equals("createChannel")){ // 进入创建好的房间||根据创建好的房间，创建频道开启音视频
            this.action =action;
            this.args =args;
            this.callbackContext =callbackContext;
            this.promtForAction();
            return true;
        }else if(action.equals("createUser")){
            String account = args.getString(0);
            String nickName = args.getString(1);
            String password = args.getString(2);
            if(!account.equals("")&&!nickName.equals("")&&!password.equals("")){
                createUser(account,nickName,password,callbackContext);
            }
            return true;
        }
        return false;
    }

    /**
     * 执行action操作（由于进入房间需要申请权限，所以单独抽出）
     * @throws JSONException
     */
    private void exeAction(){
        if(this.action==null){
              return;
        }
        try {
            if (this.action.equals("enterRoom")) {
                String room_no = this.args.getString(0);
                if (!room_no.equals("")) {
                    enterRoom(room_no, false, this.callbackContext);
                }
            } else if (this.action.equals("createChannel")) {
                String room_no = args.getString(0);
                if (!room_no.equals("")) {
                    createChannel(room_no, this.callbackContext);
                }
            }
        } catch (JSONException e){
            Log.e(TAG,e.getMessage());
        }
    }

    /**
     * 退出登录
     */
    private  void logout(CallbackContext callbackContext){
        LogoutHelper.logout(cordova.getActivity(), false);
        callbackContext.success("success");
    }
    /**
     * 登录IM服务器
     */
    private void loginIM(String currentUsername, String currentPassword,CallbackContext callbackContext){
        final String account = currentUsername.toLowerCase();
        final String token = tokenFromPassword(currentPassword);
        PluginHelper.getInstance().loginIm(account, token, new PluginHelper.LognCallBack() {
            @Override
            public void loginRes(String res) {
                callbackContext.success(res);
            }
        });

    }

    //DEMO中使用 username 作为 NIM 的account ，md5(password) 作为 token
    //开发者需要根据自己的实际情况配置自身用户系统和 NIM 用户系统的关系
    private String tokenFromPassword(String password) {
        return MD5.getStringMD5(password);
    }
    /**
     * 进入房间
     * @param roomId 房间号
     * @param isCreate 是否为房间创建者
     */
    public void enterRoom(String roomId,boolean isCreate,CallbackContext callbackContext){
        ChatRoomActivity.start(cordova.getActivity(), roomId, isCreate);
        callbackContext.success("success");
    }

    /**
     * 创建会议频道
     */
    private void createChannel(final String roomId,CallbackContext callbackContext) {
        PluginHelper.getInstance().createRoom(roomId, cordova.getActivity(), new PluginHelper.CreateRoomCallBack() {
            @Override
            public void createRoomRes(String res) {
                callbackContext.success(res);
            }
        });
    }

    /**
     * 创建用户
     * @param account 用户
     * @param nickName 昵称
     * @param password 密码
     * @param callbackContext 回调
     */
    private void createUser(String account,String nickName,String password,CallbackContext callbackContext){
        PluginHelper.getInstance().createUser(account, nickName, password, new PluginHelper.CreateUserCallBack() {
            @Override
            public void createUserRes(String res) {
                callbackContext.success(res);
            }
        });
    }

}
