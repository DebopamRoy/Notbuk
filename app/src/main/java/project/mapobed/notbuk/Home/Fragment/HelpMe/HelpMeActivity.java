package project.mapobed.notbuk.Home.Fragment.HelpMe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import project.mapobed.notbuk.R;

public class HelpMeActivity extends AppCompatActivity implements PaymentBottomSheet.BottomSheetButtonListener {
    private TextView text;
    private ImageButton info;
    private Button proceed;
    private ConstraintLayout co_pay;
    private static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    private static final String PAYTM_PACKAGE_NAME = "net.one97.paytm";
    private static final String PHONE_PAY_PACKAGE_NAME = "com.phonepe.app";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    int PAYTM_PAY_REQUEST_CODE = 231;
    int PHONEPAY_PAY_REQUEST_CODE = 312;
    private TextInputLayout amount;
    private Uri uri;
    private String amt, dest_name, upiId = "debo.roy79@okicici";
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_me);
        proceed = findViewById(R.id.pay_me_button);
        co_pay = findViewById(R.id.co_pay);
        info = findViewById(R.id.security_pay);
        text = findViewById(R.id.security_text);
        amount = findViewById(R.id.amount_paid);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(amount.getEditText().getText().toString().trim()) >= 10 &&Integer.parseInt(amount.getEditText().getText().toString().trim())<=8000) {
                    amount.setErrorEnabled(false);
                    amt = amount.getEditText().getText().toString().trim();
                } else {

                    amount.setError(getString(R.string.amount));
                    amount.requestFocus();
                    return;

                }

            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getVisibility() == View.VISIBLE)
                    text.setVisibility(View.INVISIBLE);
                else
                    text.setVisibility(View.VISIBLE);
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentBottomSheet paymentBottomSheet = new PaymentBottomSheet();
                paymentBottomSheet.show(getSupportFragmentManager(), "Payment_bottom_sheet");
            }
        });
    }


    private static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private static Uri getUpiPaymentUri(String name, String upiId, String t_note, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", t_note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String status = null;
        if (requestCode == GOOGLE_PAY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            status = data.getStringExtra("Status").toLowerCase();
            if (status.equals("success")) {
                snackbarShow(getString(R.string.thank_you));
            } else {
                snackbarShow(R.string.payment_failed);
            }
        } else if (requestCode == PAYTM_PAY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            status = data.getStringExtra("Status").toLowerCase();
            if (status.equals("success")) {
                snackbarShow(getString(R.string.thank_you));

            } else {
                snackbarShow(R.string.payment_failed);
            }
        } else if (requestCode == PHONEPAY_PAY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            status = data.getStringExtra("Status").toLowerCase();
            if (status.equals("success")) {
                snackbarShow(getString(R.string.thank_you));

            } else {
                snackbarShow(R.string.payment_failed);
            }
        } else {
            snackbarShow(R.string.please_try_again);
        }
    }


    private void payWithGPay() {
        if (isAppInstalled(this, GOOGLE_PAY_PACKAGE_NAME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        } else {
            snackbarShow(getString(R.string.google_pay));
        }
    }

    private void payWithPaytm() {
        if (isAppInstalled(this, PAYTM_PACKAGE_NAME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(PAYTM_PACKAGE_NAME);
            startActivityForResult(intent, PAYTM_PAY_REQUEST_CODE);
        } else {
            snackbarShow(getString(R.string.paytm));
        }
    }

    private void payWithPhonePay() {
        if (isAppInstalled(this, PHONE_PAY_PACKAGE_NAME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(PHONE_PAY_PACKAGE_NAME);
            startActivityForResult(intent, PHONEPAY_PAY_REQUEST_CODE);
        } else {
            snackbarShow(getString(R.string.phone_pay));
        }
    }

    @Override
    public void buttonClicked(String text) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            dest_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if (text.equals("Paytm")) {
            uri = getUpiPaymentUri(dest_name, upiId, getString(R.string.support_for_app), amt);
            payWithPaytm();
        }
        if (text.equals("PhonePay")) {
            uri = getUpiPaymentUri(dest_name, upiId, getString(R.string.support_for_app), amt);
            payWithPhonePay();
        }
        if (text.equals("GPay")) {
            uri = getUpiPaymentUri(dest_name, upiId, getString(R.string.support_for_app), amt);
            payWithGPay();
        }
    }

    private void snackbarShow(Object text) {
        Snackbar snackbar = Snackbar.make(co_pay, text.toString(), Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.firstColor));
        snackbar.setTextColor(getResources().getColor(R.color.secondColor));
        snackbar.show();
    }
}