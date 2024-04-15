package com.example.git1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick(View view) {
        GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);
        final Call<List<Contributor>> call =
                gitHubService.repoContributors("square", "picasso");

        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                final TextView textViewLogins = (TextView) findViewById(R.id.textViewLogins);
                final TextView textViewContributions = (TextView) findViewById(R.id.textViewContributions);
                StringBuilder loginsText = new StringBuilder();
                StringBuilder contributionsText = new StringBuilder();
                List<Contributor> contributors = response.body();
                for (Contributor contributor : contributors) {
                    loginsText.append(contributor.getLogin()).append("\n");
                    contributionsText.append(contributor.getContributions()).append("\n");
                }
                textViewLogins.setText(loginsText.toString());
                textViewContributions.setText(contributionsText.toString());
            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable throwable) {
                final TextView textView = (TextView) findViewById(R.id.textViewLogins);
                textView.setText("Что-то пошло не так: " + throwable.getMessage());
            }
        });
    }
}