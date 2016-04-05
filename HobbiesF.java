package com.example.frede.homework6;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HobbiesF.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HobbiesF#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HobbiesF extends android.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private final String FILENAME = "properties.xml";
    private Button hobbieChange;
    private EditText hobbieInput;
    private Properties p;
    private Activity activity;
    private TextView hobbieView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HobbiesFrangment.
     */
    // TODO: Rename and change types and number of parameters
    public static HobbiesF newInstance() {
        HobbiesF fragment = new HobbiesF();
        Bundle args = new Bundle();
        return fragment;
    }

    public HobbiesF() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hobbies, container, false);
        hobbieInput = (EditText)v.findViewById(R.id.hobbieInput);
        hobbieChange = (Button) v.findViewById(R.id.hobbieChange);
        hobbieView = (TextView) v.findViewById(R.id.hobbieChange);

        activity = getActivity();
        File file = new File(activity.getFilesDir(), FILENAME );
        p = new Properties();

        hobbieChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeHobbie();
            }
        });
        try {
            if(file.exists()) {
                FileInputStream reader = activity.openFileInput(FILENAME);
                p.loadFromXML(reader);
                String s = p.getProperty("hobbie");
                if(s.isEmpty()) {
                    hobbieInput.setHint("Your hobbie here.");
                    hobbieChange.setText("Apply");
                    hobbieView.setText("Unknown");
                }else {
                    hobbieView.setText(s);
                }
                reader.close();
            }else{
                hobbieView.setText("Unknown");
                hobbieInput.setHint("Your hobbie here.");
                hobbieChange.setText("Apply");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return v;
    }

    private void changeHobbie() {
        String hobbie = hobbieInput.getText().toString();
        try {
            FileOutputStream writer = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            if(!hobbie.isEmpty()){
                p.setProperty("hobbie", hobbie);
                p.storeToXML(writer, null);
                hobbieView.setText(hobbie);
                hobbieInput.setText("");
                writer.close();
                Toast.makeText(getActivity(), "Hobbie Saved", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(activity, "Please insert a hobbie to continue.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            // mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}