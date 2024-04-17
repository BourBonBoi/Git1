package com.example.git1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;

    private TextView mTextViewSecond;
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textViewMain);
        mTextViewSecond = (TextView) findViewById(R.id.textViewContributions);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
    }
    public void onClickPr1(View view) {
        GitHubServicePr1 gitHubServicePr1 = GitHubServicePr1.retrofit.create(GitHubServicePr1.class);
        final Call<List<Contributor>> call = gitHubServicePr1.repoContributors("square", "picasso");

        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                final TextView textViewLogins = (TextView) findViewById(R.id.textViewMain);
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
                final TextView textView = (TextView) findViewById(R.id.textViewMain);
                textView.setText("Что-то пошло не так: " + throwable.getMessage());
            }
        });
    }
    public void onClickPr2(View view) {
        mTextView.setText("");
        mTextViewSecond.setText("");
        mProgressBar.setVisibility(View.VISIBLE);

        GitHubServicePr2 gitHubService = GitHubServicePr2.retrofit.create(GitHubServicePr2.class);
        final Call<User> call =
                gitHubService.getUser("alexanderklimov");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // response.isSuccessfull() is true if the response code is 2xx
                if (response.isSuccessful()) {
                    User user = response.body();

                    // Получаем json из github-сервера и конвертируем его в удобный вид
                    mTextView.setText("Аккаунт Github: " + user.getName() +
                            "\nСайт: " + user.getBlog() +
                            "\nКомпания: " + user.getCompany());

                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    int statusCode = response.code();

                    // handle request errors yourself
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mTextView.setText(errorBody.string());
                        mProgressBar.setVisibility(View.INVISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                mTextView.setText("Что-то пошло не так: " + throwable.getMessage());
            }
        });
    }
}