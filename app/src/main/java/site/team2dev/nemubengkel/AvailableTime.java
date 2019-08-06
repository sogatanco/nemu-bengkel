package site.team2dev.nemubengkel;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class AvailableTime extends AppCompatActivity {
    private EditText start, finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_time);

        start=(EditText)findViewById(R.id.start);
        finish=(EditText)findViewById(R.id.finish);

        Intent intent=getIntent();
        start.setText(intent.getStringExtra("timestart"));
        finish.setText(intent.getStringExtra("timefinish"));
    }

    public void batal(View view) {
        finish();
    }



    private void showTime(EditText eText){
        int hour =00;
        int minutes = 00;
        // time picker dialog
        TimePickerDialog picker = new TimePickerDialog(AvailableTime.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        eText.setText(String.format("%02d:%02d", sHour, sMinute));
                    }
                }, hour, minutes, false);
        picker.show();
    }

    public void start(View view) {
        showTime(start);
    }

    public void finish(View view) {
        showTime(finish);
    }

    public void simpan(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("timestart",start.getText().toString());
        returnIntent.putExtra("timefinish",finish.getText().toString());
        setResult(AddBengkel.RESULT_OK,returnIntent);
        finish();
    }
}
