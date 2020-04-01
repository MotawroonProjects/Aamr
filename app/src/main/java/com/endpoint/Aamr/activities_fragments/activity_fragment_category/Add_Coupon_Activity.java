package com.endpoint.Aamr.activities_fragments.activity_fragment_category;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.endpoint.Aamr.R;
import com.endpoint.Aamr.activities_fragments.activity_home.client_home.activity.ClientHomeActivity;
import com.endpoint.Aamr.language.Language_Helper;
import com.endpoint.Aamr.models.UserModel;
import com.endpoint.Aamr.remote.Api;
import com.endpoint.Aamr.share.Common;
import com.endpoint.Aamr.singletone.UserSingleTone;
import com.endpoint.Aamr.tags.Tags;

import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_Coupon_Activity extends AppCompatActivity {
    private LinearLayout ll_back;
    private ImageView arrow;
    private EditText edt_coupon;
    private Button btn_use_coupon,btn_check;
    private ClientHomeActivity activity;
    private String current_language;
    private UserSingleTone userSingleTone;
    private UserModel userModel;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_coupon);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_up);

        initView();
    }

    private void initView()
    {
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();


        Paper.init(this);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

        arrow = findViewById(R.id.arrow);

        if (current_language.equals("ar"))
        {
            arrow.setImageResource(R.drawable.ic_right_arrow);
        }else
            {
                arrow.setImageResource(R.drawable.ic_left_arrow);

            }


        edt_coupon =findViewById(R.id.edt_coupon);
        btn_use_coupon =findViewById(R.id.btn_use_coupon);
        btn_check = findViewById(R.id.btn_check);

        edt_coupon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String coupon = edt_coupon.getText().toString().trim();
                if (coupon.length()>0)
                {
                    btn_check.setVisibility(View.VISIBLE);
                }else
                    {
                        btn_check.setVisibility(View.GONE);

                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ll_back = findViewById(R.id.ll_back);

        btn_use_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coupon = edt_coupon.getText().toString().trim();
                edt_coupon.setError(null);
                Common.CloseKeyBoard(activity,edt_coupon);
                SendCoupon(coupon,"check");

            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
finish();
            }
        });


    }
    private void checkData()
    {
        String coupon = edt_coupon.getText().toString().trim();
        if (!TextUtils.isEmpty(coupon))
        {
            edt_coupon.setError(null);
            Common.CloseKeyBoard(activity,edt_coupon);
            SendCoupon(coupon,"check");
        }else
            {
                edt_coupon.setError(getString(R.string.field_req));
            }
    }
    private void SendCoupon(String coupon, final String type)
    {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getCouponValue(userModel.getData().getUser_id(),type,coupon)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful())
                        {

                            if (type.equals("check"))
                            {
                                CreateAlertDialog(getString(R.string.coupon_found));
                            }else
                                {
                                    if (response.body()!=null)
                                    {
                                        CreateAlertDialog(getString(R.string.coupon_used));
                                        edt_coupon.setText("");
                                        updateUserData(response.body());
                                    }
                                }
                        }else
                            {
                                try {
                                    Log.e("error_code",response.code()+"_"+response.errorBody().string());
                                }catch (Exception e){}

                                if (response.code()==404)
                                {
                                    CreateAlertDialog(getString(R.string.coupon_not_found));
                                }else
                                    {
                                        Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();


                                    }


                            }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {

                        try {
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}


                    }
                });





    }
    private void updateUserData(UserModel userModel)
    {
        this.userModel = userModel;
        activity.updateUserDataProfile(userModel);
    }
    public  void CreateAlertDialog(String msg)
    {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_sign,null);
        Button doneBtn = view.findViewById(R.id.doneBtn);
        TextView tv_msg = view.findViewById(R.id.tv_msg);
        TextView tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.app_name));
        tv_msg.setText(msg);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }
}