package com.crystal.administrator.scanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.administrator.scanner.util.ApiCallback;
import com.crystal.administrator.scanner.util.BaseHandler;
import com.crystal.administrator.scanner.util.HttpUIDelegate;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.encoding.EncodingHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.EnumSet;
import java.util.Scanner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.id.candidatesArea;
import static android.R.id.message;
import static android.R.id.primary;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 0;
    private static final int CONNECT_SUCESS = 2;
    //清除信息后延迟一秒开始扫描
    private static final long DELAY_TIME = 1000;
    Button scannerQRCode, generateQRCode;
    ImageView qrcodeImg;
    private Button bt_clear = null;
    //自行车编号
    private TextView tv_lock_num = null;
    //开锁状态
    private TextView tv_lock_state = null;
    private String UNLOCK_URL = "https://bicycle.lekongyun.com/api/v1/send";
    private String KEY_SITN = "sign";
    private String KEY_IMEI = "imei";
    private String CMD = "UNLOCK";
    private String APPID= "7765219646";
    //相关的请求数据
    private String URL_VERBOSE  = "cmd=" +
            CMD +
            "&state=userid%3A42853&notify_url=http%3A%2F%2Fapi.example.net%2Finterface%2Fget_notify.php&appid=" +
            APPID;
    private  String TOLEN = "20a34ac384";

    private int CONECT_FAILED=0;
    //缺少必填参数
    private final String LACK_PAR = "4000";
    //验证出错,检查请求中的sign参数
    private final String SIGN_ERR = "4003";
    //参数异常,具体异常见msg参数描述
    private final String PARM_E = "5000";
    //设备未找到,该设备未连接服务器
    private final String NO_FIND = "6000";
    //设备不在线,请稍后重试
    private final String NO_ONLINE = "6001";
    //请求设备该指令超时,请稍后重试
    private final String OUT_OF_TIME = "6002";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scannerQRCode = (Button) findViewById(R.id.qrcode_dencode);
        generateQRCode = (Button) findViewById(R.id.qrcode_encode);
        qrcodeImg = (ImageView) findViewById(R.id.qrcode_img);
        tv_lock_num = (TextView) findViewById(R.id.tv_bike_num);
        tv_lock_state = (TextView) findViewById(R.id.tv_lock_state);
        bt_clear = (Button) findViewById(R.id.bt_clear);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                goToScan();
            }
        };
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清除信息
                tv_lock_num.setText("_");
                tv_lock_state.setText("_");
                //跳转到扫码界面
                handler.sendEmptyMessageDelayed(0,DELAY_TIME);
            }
        });
        scannerQRCode.setOnClickListener(this);
        generateQRCode.setOnClickListener(this);
    }

    /**
     * 跳转到扫描界面
     */
    private void goToScan() {
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qrcode_dencode: //扫描
               goToScan();
                break;
            case R.id.qrcode_encode: //生成
                try {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.head);
                    Bitmap www = EncodingHandler.createQRCode("www", 600, 600, bitmap);
                    qrcodeImg.setImageBitmap(www);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            scanResult = scanResult.substring(scanResult.length()-10,scanResult.length());
            //显示单车编号
            tv_lock_num.setText(scanResult);
            //准备请求参数
            final String param = scanResult;

            final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    //失败说明
                    String statement = "";
                    if(msg.what ==CONECT_FAILED){
                        String result = (String) msg.obj;
                        switch (result){
                            case LACK_PAR:
                                //缺少参数
                                statement=getText(R.string.lack_par).toString();
                                break;
                            case SIGN_ERR:
                                //sign参数异常
                                statement = getText(R.string.sign_err).toString();
                                break;
                            case NO_FIND:
                                //设备信息未找到
                                statement = getText(R.string.no_find).toString();
                                break;
                            case NO_ONLINE:
                                //设备不在线
                                statement = getText(R.string.no_online).toString();
                                break;
                            case OUT_OF_TIME:
                                //连接超时
                                statement = getText(R.string.out_of_time).toString();
                                break;
                            default:
                                break;
                        }
                        tv_lock_state.setText(getText(R.string.unlock_failde).toString()+result+"  "+result+statement);
                    }else if(msg.what==CONNECT_SUCESS){
                        tv_lock_state.setText(getText(R.string.unlock_sucess).toString());
                    }
                }
            };
            String sign = APPID+TOLEN+param+CMD;
            sign= MD5.getMd5Php(sign);
            //请求地址
            StringBuffer stringbuffer = new StringBuffer(UNLOCK_URL);
            stringbuffer.append("?");
            //添加不变的参数
            stringbuffer.append(URL_VERBOSE).append("&");
            //添加sign
            stringbuffer.append(KEY_SITN).append("=").append(sign).append("&");
            stringbuffer.append(KEY_IMEI).append("=").append(param);
            //创建okhttp对象
            OkHttpClient mOkHttpClient = new OkHttpClient();
            //创建一个Request
            final Request request = new Request.Builder()
                    .url(stringbuffer.toString())
                    .build();
            //new call
            Call call = mOkHttpClient.newCall(request);
            ApiCallback<GetPicModel> callback = new ApiCallback<GetPicModel>() {
                @Override
                protected void onApiCallback(HttpResponse response, GetPicModel data) {
                    super.onApiCallback(response, data);
                    if(response.isSuccessful()){
                        GetPicModel getPicModel = (GetPicModel) data;
                        if(getPicModel.code.equals("2000")){
                            handler.obtainMessage(CONNECT_SUCESS,getPicModel.code).sendToTarget();
                        }else {
                            if(getPicModel.code.equals(PARM_E)){
                                //参数异常,需要mag查看详情
                                handler.obtainMessage(CONECT_FAILED,getPicModel.code+getPicModel.codeMsg).sendToTarget();
                            }else {
                                handler.obtainMessage(CONECT_FAILED,getPicModel.code).sendToTarget();
                            }
                        }

                    }
                }
            };
            final ApiHttpCallbackWarpper<GetPicModel> warpperCallback = new ApiHttpCallbackWarpper(GetPicModel.class, getApplicationContext(), new HttpUIDelegate() {
                @Override
                public void showDialog(String message) {

                }

                @Override
                public void hideDialog(HttpResponse response, String message) {

                }

                @Override
                public void end(HttpResponse response) {

                }

                @Override
                public BaseHandler getResultHandler() {
                    return new BaseHandler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            return false;
                        }
                    });
                }
            }, "", callback);
            //发送开锁请求
            call.enqueue(new OkHttpCallbackWrapper(warpperCallback));
        }
    }



}
