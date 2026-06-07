package com.netstress.ultimate;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class ControlActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        Button start = findViewById(R.id.startAttack);
        EditText target = findViewById(R.id.targetInput);
        start.setOnClickListener(v -> {
            String ip = target.getText().toString().trim();
            if (ip.isEmpty()) {
                Toast.makeText(this, "Enter target IP", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent service = new Intent(this, NetStressService.class);
            service.putExtra("target", ip);
            startService(service);
            Toast.makeText(this, "Attack started on " + ip, Toast.LENGTH_SHORT).show();
        });
    }
}
