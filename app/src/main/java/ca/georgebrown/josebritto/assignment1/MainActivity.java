package ca.georgebrown.josebritto.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText hoursInput;
    private EditText rateInput;
    private Button mainButton;
    private TextView payOutput;
    private TextView overtimeOutput;
    private TextView taxesOutput;
    private TextView totalDisplay;
    private View outputGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() != R.id.about_menu_item)
            return super.onOptionsItemSelected(item);
        Intent about = new Intent(this, AboutActivity.class);
        startActivity(about);
        return true;
    }




    private void initializeUI() {
        hoursInput = findViewById(R.id.hours_input);
        rateInput = findViewById(R.id.rate_input);
        mainButton = findViewById(R.id.button);
        payOutput = findViewById(R.id.pay_output);
        overtimeOutput = findViewById(R.id.overtime_output);
        taxesOutput = findViewById(R.id.taxes_output);
        totalDisplay = findViewById(R.id.totalpay);
        outputGroup = findViewById(R.id.outputLayout);
        outputGroup.setVisibility(View.INVISIBLE);

        Toolbar actionbar = findViewById(R.id.toolbar);
        setSupportActionBar(actionbar);

        rateInput.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus)
                outputGroup.setVisibility(View.INVISIBLE);
        });
        hoursInput.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus)
                outputGroup.setVisibility(View.INVISIBLE);
        });

        mainButton.setOnClickListener(v -> processOutput());
    }

    private void processOutput() {
        String rateValue = rateInput.getText().toString();
        String hoursValue = hoursInput.getText().toString();
        double rate, hours;
        try{
            hours = Double.parseDouble(hoursValue);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please fill out your hours first!", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            rate = Double.parseDouble(rateValue);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please fill out your hourly rate!", Toast.LENGTH_SHORT).show();
            return;
        }
        double overTimeHours = hours - 40;
        double regularHours = hours <= 40 ? hours : 40;
        if(overTimeHours < 0)
            overTimeHours = 0;
        double overtimePay = overTimeHours * rate * 1.5;
        double regularPay = regularHours * rate;
        double pay = overtimePay + regularPay;
        double tax = pay* 0.18;
        double totalPay = pay - tax;

        String payText = getString(R.string.pay_output_template).replace("XXX", String.format("%.2f", pay));
        String overtimePayText = getString(R.string.overtime_output_template).replace("XXX", String.format("%.2f", overtimePay));
        String taxesText = getString(R.string.taxes_output_template).replace("XXX", String.format("%.2f", tax));
        String totalText = "$" + String.format("%.2f", totalPay);

        payOutput.setText(payText);
        overtimeOutput.setText(overtimePayText);
        taxesOutput.setText(taxesText);
        totalDisplay.setText(totalText);
        hideKeyboardAndClearFocus();
        outputGroup.setVisibility(View.VISIBLE);
    }

    private void hideKeyboardAndClearFocus() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(hoursInput.getWindowToken(), 0);
        hoursInput.clearFocus();
        rateInput.clearFocus();
    }
}