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
    private boolean lastWasEq = false;
    private String secretCode = "44+55+66";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = findViewById(R.id.editText);

        // All button click listeners
        int[] digitIds = {R.id.btn_0,R.id.btn_1,R.id.btn_2,R.id.btn_3,R.id.btn_4,
                          R.id.btn_5,R.id.btn_6,R.id.btn_7,R.id.btn_8,R.id.btn_9};
        for (int id : digitIds) {
            findViewById(id).setOnClickListener(v -> append(((Button)v).getText().toString()));
        }
        findViewById(R.id.btn_add).setOnClickListener(v -> appendOp("+"));
        findViewById(R.id.btn_sub).setOnClickListener(v -> appendOp("-"));
        findViewById(R.id.btn_mul).setOnClickListener(v -> appendOp("*"));
        findViewById(R.id.btn_div).setOnClickListener(v -> appendOp("/"));
        findViewById(R.id.btn_clear).setOnClickListener(v -> clear());
        findViewById(R.id.btn_eq).setOnClickListener(v -> evaluate());
    }

    private void append(String s) {
        if (lastWasEq) { expression.setLength(0); lastWasEq = false; }
        expression.append(s);
        display.setText(expression.toString());
        checkSecret();
    }

    private void appendOp(String op) {
        if (expression.length() == 0) return;
        if (lastWasEq) { expression.setLength(0); lastWasEq = false; }
        char last = expression.charAt(expression.length()-1);
        if (last == '+' || last == '-' || last == '*' || last == '/') {
            expression.setCharAt(expression.length()-1, op.charAt(0));
        } else {
            expression.append(op);
        }
        display.setText(expression.toString());
        checkSecret();
    }

    private void clear() {
        expression.setLength(0);
        display.setText("");
        lastWasEq = false;
    }

    private void evaluate() {
        if (expression.length() == 0) return;
        try {
            String expr = expression.toString();
            double result = new ExpressionEvaluator().evaluate(expr);
            display.setText(String.valueOf(result));
            expression.setLength(0);
            expression.append(result);
            lastWasEq = true;
        } catch (Exception e) {
            display.setText("Error");
            expression.setLength(0);
        }
    }

    private void checkSecret() {
        if (expression.toString().equals(secretCode)) {
            Intent intent = new Intent(MainActivity.this, ControlActivity.class);
            startActivity(intent);
            expression.setLength(0);
            display.setText("");
        }
    }

    // Simple expression evaluator (supports + - * /)
    class ExpressionEvaluator {
        double evaluate(String expr) {
            // Remove spaces
            expr = expr.replaceAll("\\s", "");
            // Simple evaluator using operator precedence
            // For simplicity, we use a basic implementation
            return new Object() {
                int pos = -1, ch;
                void next() { ch = (++pos < expr.length()) ? expr.charAt(pos) : -1; }
                double parse() {
                    next();
                    double x = parseTerm();
                    while (ch == '+' || ch == '-') {
                        char op = (char) ch;
                        next();
                        double y = parseTerm();
                        if (op == '+') x += y;
                        else x -= y;
                    }
                    return x;
                }
                double parseTerm() {
                    double x = parseFactor();
                    while (ch == '*' || ch == '/') {
                        char op = (char) ch;
                        next();
                        double y = parseFactor();
                        if (op == '*') x *= y;
                        else x /= y;
                    }
                    return x;
                }
                double parseFactor() {
                    if (ch == '(') {
                        next();
                        double x = parse();
                        if (ch == ')') next();
                        return x;
                    }
                    StringBuilder sb = new StringBuilder();
                    while ((ch >= '0' && ch <= '9') || ch == '.') {
                        sb.append((char)ch);
                        next();
                    }
                    return Double.parseDouble(sb.toString());
                }
            }.parse();
        }
    }
}
