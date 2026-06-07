package com.netstress.ultimate;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText display;
    private StringBuilder expression = new StringBuilder();
    private String secretCode = "44+55+66";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = findViewById(R.id.editText);

        // Set click listeners for all buttons
        int[] buttonIds = {
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
            R.id.btn_add, R.id.btn_sub, R.id.btn_mul, R.id.btn_div,
            R.id.btn_clear, R.id.btn_eq
        };
        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(this::onButtonClick);
        }
    }

    private void onButtonClick(View v) {
        Button btn = (Button) v;
        String text = btn.getText().toString();

        if (text.equals("C")) {
            expression.setLength(0);
            display.setText("");
        } else if (text.equals("=")) {
            // Do nothing or just keep the expression as is (no calculation)
            // For the purpose of hiding, we don't need to compute.
            // We'll keep display unchanged.
        } else {
            expression.append(text);
            display.setText(expression.toString());
        }

        // Check for secret code
        if (expression.toString().equals(secretCode)) {
            Intent intent = new Intent(MainActivity.this, ControlActivity.class);
            startActivity(intent);
            expression.setLength(0);
            display.setText("");
        }
    }
}
