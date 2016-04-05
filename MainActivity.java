package com.example.frede.homework6;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends ActionBarActivity implements FriendsFragment.OnFragmentInteractionListener, HobbiesF.OnFragmentInteractionListener {
    private FrameLayout fragmentContainer;
    private Button friendsButton, hobbieButton;
    private boolean friendsPressed, hobbiesPressed;
    private FragmentManager fm;
    private FriendsFragment friendZone;
    private HobbiesF hobbiesZone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        friendsPressed = hobbiesPressed = false;
        fragmentContainer = (FrameLayout)findViewById(R.id.FrameContainer);
        friendsButton = (Button)findViewById(R.id.friendsButton);
        hobbieButton = (Button)findViewById(R.id.hobbieButton);
        fm = getFragmentManager();
    }

    public void onFriendsPressed(View v){
        FragmentTransaction t = fm.beginTransaction();
        if(friendsPressed){
            friendsPressed = false;
            Fragment f = fm.findFragmentByTag("friendZone");
            t.remove(f);
            t.commit();
        }else{
            friendsPressed = true;
            if(friendZone == null) {
                friendZone = FriendsFragment.newInstance();
            }
            if(hobbiesPressed) {
                hobbiesPressed = false;
                t.replace(R.id.FrameContainer, friendZone, "friendZone");
                t.commit();
            }else {
                t.add(R.id.FrameContainer, friendZone, "friendZone");
                t.commit();
            }
        }
    }

    public void onHobbiesPressed(View view) {
        FragmentTransaction t = fm.beginTransaction();
        if(hobbiesPressed){
            hobbiesPressed = false;
            Fragment f = fm.findFragmentByTag("hobbiesZone");
            t.remove(f);
            t.commit();
        }else{
            hobbiesPressed = true;
            if(hobbiesZone == null){
                hobbiesZone = HobbiesF.newInstance();
            }
            if(friendsPressed){
                friendsPressed = false;
                t.replace(R.id.FrameContainer, hobbiesZone, "hobbiesZone" );
                t.commit();
            }else {
                t.add(R.id.FrameContainer, hobbiesZone, "hobbiesZone");
                t.commit();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
