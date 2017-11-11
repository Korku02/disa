package iitd.messfeeback.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Forgotpassword extends AppCompatActivity implements View.OnClickListener {

    public static final String PASSWORD="password";

    private Button buttonUpdatepassword;
    private EditText editTextNewPassword, editTextNewRePassword;
    private TextView nootptext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);


        editTextNewPassword = (EditText) findViewById(R.id.editTextNewPassword);
        editTextNewRePassword = (EditText) findViewById(R.id.editTextNewRePassword);
        buttonUpdatepassword = (Button) findViewById(R.id.buttonUpdatepassword);
        nootptext  = (TextView) findViewById(R.id.nootptext);

        nootptext.setOnClickListener(this);
        buttonUpdatepassword.setOnClickListener(this);
    }

    private boolean validate() {
        boolean temp=true;
        String pass= editTextNewPassword.getText().toString();
        String cpass= editTextNewRePassword.getText().toString();
        if(!pass.equals(cpass)){
            Toast.makeText(this,"Password Not matching",Toast.LENGTH_SHORT).show();
            temp=false;
        }
        return temp;
    }

    @Override
    public void onClick(View view) {
        if(view == nootptext){
            finish();
            //TODO OTP sending link
        }
        if(view == buttonUpdatepassword){
            validate();
            String errorMessage = "You better remember this.";
            Toast.makeText(this,errorMessage,Toast.LENGTH_LONG ).show();
            finish();
            startActivity(new Intent(this, userlogin.class));
        }
    }
}
