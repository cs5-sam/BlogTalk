package android.example.blogtalk.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.example.blogtalk.R;
import android.os.Bundle;
import android.widget.TextView;

public class TermsActivity extends AppCompatActivity {

    private TextView termsTitle;
    private TextView termsDescription;
    private TextView privacyDescription;
    private TextView privacyTitle;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        termsTitle = findViewById(R.id.terms_title);
        privacyTitle = findViewById(R.id.privacy_title);
        termsDescription = findViewById(R.id.terms_description);
        privacyDescription = findViewById(R.id.privacy_description);

        termsTitle.setText("Terms & Conditions");
        privacyTitle.setText("Privacy Policy");
        termsDescription.setText(getResources().getString(R.string.terms_description));
        privacyDescription.setText(getResources().getString(R.string.privacy_policy));
    }
}