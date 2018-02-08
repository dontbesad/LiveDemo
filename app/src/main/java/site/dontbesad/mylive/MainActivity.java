package site.dontbesad.mylive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alivc.live.pusher.AlivcAudioAACProfileEnum;
import com.alivc.live.pusher.AlivcFpsEnum;
import com.alivc.live.pusher.AlivcLivePushCameraTypeEnum;
import com.alivc.live.pusher.AlivcLivePushConfig;
import com.alivc.live.pusher.AlivcLivePusher;
import com.alivc.live.pusher.AlivcPreviewOrientationEnum;
import com.alivc.live.pusher.AlivcQualityModeEnum;
import com.alivc.live.pusher.AlivcResolutionEnum;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public String TAG = "MainActivity";
    private boolean pushing;

    private AlivcLivePushConfig mPushConf;
    private AlivcResolutionEnum resolution = AlivcResolutionEnum.RESOLUTION_360P;
    private AlivcQualityModeEnum quality = AlivcQualityModeEnum.QM_RESOLUTION_FIRST;
    private AlivcPreviewOrientationEnum orientation = AlivcPreviewOrientationEnum.ORIENTATION_PORTRAIT;
    private AlivcFpsEnum fps = AlivcFpsEnum.FPS_30;
    private AlivcAudioAACProfileEnum aac = AlivcAudioAACProfileEnum.AAC_LC;

    private SurfaceView surface;
    private AlivcLivePusher mPusher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setPushConfig();

        this.mainLogic();
    }

    private void setPushConfig() {
        this.mPushConf = new AlivcLivePushConfig();

        this.mPushConf.setResolution(this.resolution);
        this.mPushConf.setQualityMode(this.quality);
        this.mPushConf.setTargetVideoBitrate(1200);
        this.mPushConf.setMinVideoBitrate(400);
        this.mPushConf.setInitialVideoBitrate(800);
        this.mPushConf.setBeautyOn(false);
        this.mPushConf.setPreviewOrientation(this.orientation);
        this.mPushConf.setFps(this.fps);
        this.mPushConf.setAudioProfile(this.aac);
        this.mPushConf.setCameraType(AlivcLivePushCameraTypeEnum.CAMERA_TYPE_BACK);

    }

    private void mainLogic() {
        this.pushing = false;
        surface = (SurfaceView) findViewById(R.id.surface);
        surface.getHolder().addCallback(this.surfaceCallback);

        Button btnStart = (Button) findViewById(R.id.btn_start);
        Button btnStop = (Button) findViewById(R.id.btn_stop);
        Button btnSwitch = (Button) findViewById(R.id.btn_switch);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnSwitch.setOnClickListener(this);

        mPusher = new AlivcLivePusher();
        mPusher.init(this, this.mPushConf);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                this.mStartPush();
                break;
            case R.id.btn_stop:
                this.mStopPush();
                break;
            case R.id.btn_switch:
                this.mSwitchCamera();
                break;
            default:
                Log.d(this.TAG, "按钮error");
        }
    }

    private void mStartPush() {
        if (this.pushing == true) {
            Toast.makeText(this, "已经在推流中", Toast.LENGTH_SHORT).show();
            return ;
        }
        this.pushing = true;
        String url = "rtmp://120.77.241.49:1935/live/home";
        Toast.makeText(this, "开始推流", Toast.LENGTH_SHORT).show();
        this.mPusher.startPush(url);
    }

    private void mStopPush() {
        if (this.pushing == false) {
            Toast.makeText(this, "推流已经停止了", Toast.LENGTH_SHORT).show();
            return ;
        }
        this.mPusher.stopPush();
        this.pushing = false;
    }

    private void mSwitchCamera() {
        this.mPusher.switchCamera();
    }

    private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            //surfaceHolder.setKeepScreenOn(true);
            MainActivity.this.mPusher.startPreview(MainActivity.this.surface);
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };
}
