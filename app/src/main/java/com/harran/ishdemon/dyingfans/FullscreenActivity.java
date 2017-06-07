package com.harran.ishdemon.dyingfans;
import android.content.ActivityNotFoundException;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.app.ProgressDialog;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.q42.android.scrollingimageview.ScrollingImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import com.wang.avi.AVLoadingIndicatorView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import android.widget.RelativeLayout.LayoutParams;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;




public class FullscreenActivity extends AppCompatActivity {

    ActionBar actionbar;
    final Context context = this;
    TextView textview;
    String ExternalFontPath;
    Typeface FontLoaderTypeface;
    LayoutParams layoutparams;
    TextView tv;
    private AVLoadingIndicatorView avi;
    FrameLayout frameLayout;
    String url="https://steamdb.info/api/GetGraph/?type=concurrent_week&appid=239140.json";
    ProgressDialog dialog;
    int firstitem=0;

    public void onComposeAction(MenuItem mi) {
        // handle click here
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.action_bar_dialog);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);

        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        MenuItem Info =(MenuItem)findViewById(R.id.info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);



        ActionBarTitleGravity();
        avi = (AVLoadingIndicatorView) findViewById(R.id.load);
        //dialog = new ProgressDialog(this);
        // dialog.setMessage("Connecting to Harran's database");
        //dialog.show();
        YoYo.with(Techniques.Wobble).duration(2000).repeat(10).playOn(findViewById(R.id.imageView10));
        YoYo.with(Techniques.Wobble).duration(1000).repeat(999).playOn(findViewById(R.id.imageView8));
        YoYo.with(Techniques.Hinge).duration(6000).repeat(999).playOn(findViewById(R.id.imageView7));
        final Handler autoUpdateHandler;
        autoUpdateHandler = new Handler();
        autoUpdateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //start the following method every 1 second.
                update();
                autoUpdateHandler.postDelayed(this, 60000);

            }
        }, 5000);


        tv = (TextView) findViewById(R.id.fullscreen_content);
        ScrollingImageView scrollingBackground = (ScrollingImageView) findViewById(R.id.scrolling_background);
        scrollingBackground.stop();
        scrollingBackground.start();

       LayoutInflater inflater = getLayoutInflater();
        View toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        final Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastLayout);
        toast.show();
        startAnim();

    }


    private void ActionBarTitleGravity() {
        // TODO Auto-generated method stub

        actionbar = getSupportActionBar();

        textview = new TextView(getApplicationContext());

        layoutparams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        textview.setLayoutParams(layoutparams);

        textview.setText("DYING FANS");

        textview.setTextColor(Color.WHITE);

        //textview.setGravity(Gravity.CENTER);

        textview.setTextSize(27);
        // Assests folder font folder path
        ExternalFontPath = "fonts/Rogan_Bold.otf";

        // Load Typeface font url String ExternalFontPath
        FontLoaderTypeface = Typeface.createFromAsset(getAssets(), ExternalFontPath);
        textview.setTypeface(FontLoaderTypeface);

        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        actionbar.setCustomView(textview);

    }



    void update() {
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LayoutInflater inflater = getLayoutInflater();
                View toastLayout2 = inflater.inflate(R.layout.custom_toast2, (ViewGroup) findViewById(R.id.custom_toast_layout));
                final Toast toast2 = new Toast(getApplicationContext());
                toast2.setDuration(Toast.LENGTH_LONG);
                toast2.setView(toastLayout2);
                toast2.show();
                //Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
                //dialog.dismiss();
                stopAnim();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(FullscreenActivity.this);
        rQueue.add(request);

    }

    void parseJsonData(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONObject data = object.getJSONObject("data");
            JSONArray Values = data.getJSONArray("values");
            int lastitem = Values.getInt(Values.length()-1);
            //tv.setText( Integer.toString(lastitem));
            animateTextView(firstitem,lastitem,tv);
            firstitem = lastitem;


        }  catch (JSONException e) {
            e.printStackTrace();
        }
        //dialog.dismiss();
        stopAnim();



    }
    void startAnim(){
        avi.show();
        // or avi.smoothToShow();
    }
    void stopAnim(){
        avi.hide();
        // or avi.smoothToHide();
    }


    public void animateTextView(int initialValue, int finalValue, final TextView  textview) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(1500);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                textview.setText(valueAnimator.getAnimatedValue().toString());

            }
        });
        valueAnimator.start();

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void sendToSteam(View view) {
        String url = "http://steamcommunity.com/id/ishDEMON/"; // You could have this at the top of the class as a constant, or pass it in as a method variable, if you wish to send to multiple websites
        Intent i = new Intent(Intent.ACTION_VIEW); // Create a new intent - stating you want to 'view something'
        i.setData(Uri.parse(url));  // Add the url data (allowing android to realise you want to open the browser)
        startActivity(i); // Go go go!
    }
    public void sendTomail(View view) {
        String mailto = "mailto:ishdemon10@gmail.com";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));

        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
//TO
        }
    }




}
