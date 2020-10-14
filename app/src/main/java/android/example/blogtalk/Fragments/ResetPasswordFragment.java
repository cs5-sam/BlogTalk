package android.example.blogtalk.Fragments;

import android.content.Intent;
import android.example.blogtalk.Activities.LoginActivity;
import android.example.blogtalk.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LinearLayout linearLayout;
    private Button resetPasswordBtn;
    private TextView goBackBtn;
    private EditText resetEmailEditText;
    private ProgressBar resetBar;
    private FrameLayout parentFrameLayout;
    private FirebaseAuth auth;

    private String mParam1;
    private String mParam2;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    public static ResetPasswordFragment newInstance(String param1, String param2) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
                             Bundle savedInstanceState) {// Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        resetEmailEditText = view.findViewById(R.id.forgot_email);
        goBackBtn = view.findViewById(R.id.go_back);
        linearLayout = view.findViewById(R.id.linear_layout_1);
        resetPasswordBtn = view.findViewById(R.id.forgot_btn);
        resetBar = view.findViewById(R.id.reset_progressBar);

        auth = FirebaseAuth.getInstance();
//
//        parentFrameLayout = getActivity().findViewById(R.id.register_frame_layout);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetBar.setVisibility(View.VISIBLE);
                resetPasswordBtn.setVisibility(View.INVISIBLE);

                if(resetEmailEditText.getText().toString().equals("")){

                    resetBar.setVisibility(View.INVISIBLE);
                    resetPasswordBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "PLease fill all fields", Toast.LENGTH_SHORT).show();

                }
                else{

                    auth.sendPasswordResetEmail(resetEmailEditText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        resetBar.setVisibility(View.INVISIBLE);
                                        resetPasswordBtn.setVisibility(View.VISIBLE);
                                        linearLayout.setVisibility(View.VISIBLE);
                                        Toast.makeText(getActivity(), "Email sent successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                    else{

                                        resetBar.setVisibility(View.INVISIBLE);
                                        resetPasswordBtn.setVisibility(View.VISIBLE);
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            resetBar.setVisibility(View.INVISIBLE);
                            resetPasswordBtn.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment();
            }
        });
    }

    private void setFragment() {
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_left);
//        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
//        fragmentTransaction.commit();
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }
}