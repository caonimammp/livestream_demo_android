package cn.ucai.live.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.live.R;
import cn.ucai.live.data.restapi.LiveManager;
import cn.ucai.live.utils.I;
import cn.ucai.live.utils.MD5;

import static cn.ucai.live.utils.I.REQUEST_CODE_PICK_PIC;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.email)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.login_progress)
    ProgressBar loginProgress;
    @BindView(R.id.layout_group_icon)
    LinearLayout layoutGroupIcon;
    String avatarName;
    File avatarFile;
    @BindView(R.id.nick)
    EditText nick;
    @BindView(R.id.email_login_form)
    LinearLayout emailLoginForm;
    @BindView(R.id.login_form)
    ScrollView loginForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(username.getText()) || TextUtils.isEmpty(password.getText())) {
                    showToast("用户名和密码不能为空");
                    return;
                }
                final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("正在注册...");
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            boolean isScuccess = LiveManager.getInstance().register(username.getText().toString(), nick.getText().toString(),
                                    MD5.getMessageDigest(password.getText().toString()), avatarFile);
                            if(isScuccess){
                                EMClient.getInstance().createAccount(username.getText().toString(), MD5.getMessageDigest(password.getText().toString()));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pd.dismiss();
                                        showToast("注册成功");
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class).putExtra("username",username.getText().toString()));
                                        finish();
                                    }
                                });
                            }else {
                                LiveManager.getInstance().unRegister(username.getText().toString());
                            }
                        } catch (final HyphenateException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();
                                    showLongToast("注册失败：" + e.getMessage());
                                }
                            });
                        }
                    }
                }).start();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PICK_PIC:
                if (data == null || data.getData() == null) {
                    return;
                }
                startPhotoZoom(data.getData());
                break;
            case I.REQUEST_CODE_CUTTING:
                if (data != null) {
                    setPicToView(data);
                }
                break;
//            case I.REQUEST_CODE_PICK_CONTACT:
//                if (resultCode == RESULT_OK) {
//
//                }
//                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, I.REQUEST_CODE_CUTTING);
    }

    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(getResources(), photo);
            ivAvatar.setImageDrawable(drawable);
            saveBitmapFile(photo);
        }
    }

    private String getAvatarName() {
        avatarName = I.AVATAR_TYPE_GROUP_PATH + System.currentTimeMillis();
        return avatarName;
    }

    private void saveBitmapFile(Bitmap bitmap) {
        if (bitmap != null) {
            String imagePath = getAvatarPath(RegisterActivity.this, I.AVATAR_TYPE) + "/" + getAvatarName() + ".jpg";
            avatarFile = new File(imagePath);
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(avatarFile));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getAvatarPath(Context context, String path) {
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File folder = new File(dir, path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder.getAbsolutePath();
    }

    @OnClick(R.id.layout_group_icon)
    public void onClick() {
        uploadHeadPhoto();
    }

    private void uploadHeadPhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dl_title_upload_photo);
        builder.setItems(new String[]{getString(R.string.dl_msg_take_photo), getString(R.string.dl_msg_local_upload)},
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                Toast.makeText(RegisterActivity.this, getString(R.string.toast_no_support),
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(pickIntent, REQUEST_CODE_PICK_PIC);
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }
}
