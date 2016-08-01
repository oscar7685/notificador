package com.casa.app.application;

/**
 * Created by Oscar on 28/05/2016.
 */
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class FriendsFragment extends Fragment {
    EditText editText;
    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String ip = appSharedPrefs.getString("Ip", "192.168.1.51");


        editText = (EditText) rootView.findViewById(R.id.username);
        editText.setText(""+ip);

        final Button button = (Button) rootView.findViewById(R.id.btn_signup);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String ipnueva = editText.getText().toString();
                SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                prefsEditor.putString("Ip", ""+ipnueva);
                prefsEditor.commit();
                Toast toast = Toast.makeText(getActivity(), "Ip cambiada exitosamente", Toast.LENGTH_SHORT);
                toast.show();
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}