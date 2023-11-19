package com.UndefinedParticle.moreandlessfunctionality;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.UndefinedParticle.moreandlessfunctionality.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ShowMoreAndLessTextView showMoreAndLessTextView;
    String textContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showMoreAndLessTextView = new ShowMoreAndLessTextView(this);

        binding.setText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textContent = binding.editTextText.getText().toString();
                binding.textView.setText(textContent);
                binding.editTextText.setText("");

                showMoreAndLessTextView.setResizableText(binding.textView, textContent, 3, true, null);
            }
        });
    }
}