package project.mapobed.notbuk.Home.Fragment.HelpMe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import project.mapobed.notbuk.R;

public class HelpMeActivity extends AppCompatActivity {
    private TextView text;
    private ImageButton info;
    private Button proceed;
    private ConstraintLayout co_pay;
    private final int UPI_PAYMENT = 1;
    private TextInputLayout amount_help,message_help;
    private Uri uri;
    private String note, name = "Debopam Roy", upiId = "debo.roy79@okicici";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_me);
        proceed = findViewById(R.id.pay_me_button);
        message_help=findViewById(R.id.message_paid);
        co_pay = findViewById(R.id.co_pay);
        info = findViewById(R.id.security_pay);
        text = findViewById(R.id.security_text);
        amount_help = findViewById(R.id.amount_paid);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount_help.getEditText().getText().toString().trim().length()>1) {
                    amount_help.setErrorEnabled(false);
                    if (message_help.getEditText().getText().toString().trim().length()>1)
                        note=message_help.getEditText().getText().toString().trim();
                    else
                        note="Donation from Notbuk.";
                    payUsingUpi(amount_help.getEditText().getText().toString().trim(), upiId, name, note);
                } else {

                    amount_help.setError(getString(R.string.amount));
                    amount_help.requestFocus();
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
    }

    private void snackbarShow(Object text) {
        Snackbar snackbar = Snackbar.make(co_pay, text.toString(), Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.firstColor));
        snackbar.setTextColor(getResources().getColor(R.color.secondColor));
        snackbar.show();
    }

    void payUsingUpi(String amount, String upiId, String name, String note) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(HelpMeActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
            default:
                Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        String str = data.get(0);
        Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
        String paymentCancel = "";
        if (str == null) str = "discard";
        String status = "";
        String approvalRefNo = "";
        String response[] = str.split("&");
        for (int i = 0; i < response.length; i++) {
            String equalStr[] = response[i].split("=");
            if (equalStr.length >= 2) {
                if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                    status = equalStr[1].toLowerCase();
                } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                    approvalRefNo = equalStr[1];
                }
            } else {
                paymentCancel = "Payment cancelled by user.";
            }
        }

        if (status.equals("success")) {
            //Code to handle successful transaction here.
            Toast.makeText(HelpMeActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
            Log.d("UPI", "responseStr: " + approvalRefNo);
        } else if ("Payment cancelled by user.".equals(paymentCancel)) {
            Toast.makeText(HelpMeActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(HelpMeActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
        }
    }


}