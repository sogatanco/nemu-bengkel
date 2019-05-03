package site.team2dev.nemubengkel;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

public class Fungsi {
    private Boolean isEmpty(EditText et){
        if(et.getText().toString().trim().length()>0)
            return false;

        return true;
    }
//    check empty edittext
    public void emptyChecker(EditText[] editTexts){
        for(int i=0;i<editTexts.length;i++){
            EditText editText=editTexts[i];
            if(isEmpty(editText)){
                editText.setError("can't be empty");
            }
        }
    }

//    check format email
    public void checkEmail(final EditText email){
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {

                }
                else{
                    email.setError("invalid Email address");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

//    check if has error
    public Boolean isHasError(EditText[] editTexts){
        for(int i=0;i<editTexts.length;i++){
            if(editTexts[i].getError()!=null)
                return true;
        }
        return false;
    }

//    check password length
    public void lengthPass(final EditText pass){
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pass.getText().toString().length()<5){
                    pass.setError("lest then 5 char");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


//    pass match
    public void matchPass(EditText pass1,EditText pass2){
        if(!pass1.getText().toString().equalsIgnoreCase(pass2.getText().toString())){
            pass2.setError("password not match");
        }
    }


//    getusername from token
    public String getUsername(String token){
        JWT jwt=new JWT(token);
        Claim datausername=jwt.getClaim("username");
        String username=datausername.asString();
        return username;
    }


//    is null
    public String isNull(String string){
        if(string.equals("null")){
            return "";
        }
        return string;
    }

}
