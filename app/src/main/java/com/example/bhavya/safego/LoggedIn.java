package com.example.bhavya.safego;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class LoggedIn extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String ip="192.168.1.12";
    private final static String port="6666";
    private String Email;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        Intent intent = getIntent();
        TextView username;
        TextView logout;
        ImageView profilePic;
        username = header.findViewById(R.id.username);
        profilePic = header.findViewById(R.id.profile_pic);
        logout = header.findViewById(R.id.logout);
        GoogleSignInAccount account = intent.getParcelableExtra("account");
        if (account != null) {
            if (account.getDisplayName() != null)
                username.setText(account.getDisplayName());
        android.net.Uri profileUrl = account.getPhotoUrl();
        new LoadProfilePic(profilePic).execute(String.valueOf(profileUrl));
        }

        TextView u2=findViewById(R.id.username);
        ImageView pic2=findViewById(R.id.profile_pic);
        TextView email=findViewById(R.id.email);

        if (account != null) {
            if (account.getDisplayName() != null)
                u2.setText(account.getDisplayName());
            email.setText(account.getEmail());
            Email=account.getEmail();
            android.net.Uri profileUrl = account.getPhotoUrl();
            new LoadProfilePic(pic2).execute(String.valueOf(profileUrl));
        }
        TextView isUnder18=findViewById(R.id.isUnder18);
        String IsUnder18=isUnder18(account.getPhotoUrl());
        if(IsUnder18.equals("1"))
            isUnder18.setVisibility(View.VISIBLE);
        try {
            setProfile();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Button updateProfile=findViewById(R.id.update);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("up","updating");
                try {
                    UpdateProfile();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }
    private String isUnder18(Uri photoUrl) {
        final StringBuffer response = new StringBuffer();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL("http://" + ip + ":" + port + "/isUnder18/"+Email);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.getResponseCode();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    conn.disconnect();
                }catch (Exception e)
                {
                    Log.d("error",e.toString());
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("under18",response.toString());
        return response.toString();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logged_in, menu);
        return true;
    }

    private void setProfile() throws JSONException {
        EditText DLno=findViewById(R.id.DLno);
        EditText numberPlate=findViewById(R.id.numPlate);
        EditText gender=findViewById(R.id.gender);
        EditText phoneNo=findViewById(R.id.phoneno);
        EditText age=findViewById(R.id.age);

        final StringBuffer response = new StringBuffer();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL("http://" + ip + ":" + port + "/profile/"+Email);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.getResponseCode();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    conn.disconnect();
                }catch (Exception e)
                {
                    Log.d("error",e.toString());
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("res",response.toString());
        JSONObject obj = new JSONObject(response.toString());
//        if(!obj.getString("DLno").equals("undefined"))
            DLno.setText((obj.getString("DLno")));
//        if(!obj.getString("licencePlateNo").equals("undefined"))
            numberPlate.setText((obj.getString("licencePlateNo")));
//        if(!obj.getString("gender").equals("undefined"))
            gender.setText((obj.getString("gender")));
//        if(!obj.getString("phoneNo").equals("undefined"))
            phoneNo.setText((obj.getString("phoneNo")));
//        if(!obj.getString("age").equals("undefined"))
            age.setText((obj.getString("age")));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent=new Intent(LoggedIn.this,LoggedOut.class);
                        startActivity(intent);
                    }
                });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void UpdateProfile() throws JSONException {
        EditText DLno=findViewById(R.id.DLno);
        EditText numberPlate=findViewById(R.id.numPlate);
        EditText gender=findViewById(R.id.gender);
        EditText phoneNo=findViewById(R.id.phoneno);
        EditText age=findViewById(R.id.age);

        final StringBuffer response=new StringBuffer();
        final String json = "{\"DLno\":\"" + DLno.getText() + "\",\"licencePlateNo\":\""+numberPlate.getText()+"\",\"gender\":\""+ gender.getText()+"\",\"age\":\""+age.getText()+"\",\"phoneNo\":\""+phoneNo.getText()+"\",\"email\":\""+Email+"\"}";
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://"+ip+":"+port+"/update");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    OutputStream os;
                    os = conn.getOutputStream();
                    os.write(json.getBytes());
                    os.flush();
                    os.close();
                    conn.getResponseCode();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    conn.disconnect();

                }catch (Exception e){
                    //error occured
                    Log.d("error",e.toString());
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(response.toString().equals("done")) {
          setProfile();
        }

    }
}