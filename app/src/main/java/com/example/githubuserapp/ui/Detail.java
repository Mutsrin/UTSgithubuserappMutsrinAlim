package com.example.githubuserapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.githubuserapp.R;
import com.example.githubuserapp.data.response.GithubUser;
import com.example.githubuserapp.data.retrofit.ApiConfig;
import com.example.githubuserapp.data.retrofit.ApiService;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        progressBar = findViewById(R.id.progressBar);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String username = extras.getString("username");
            if (username != null && !username.isEmpty()) {
                ApiService apiService = ApiConfig.getApiService();
                Call<GithubUser> userCall = apiService.getUser(username);

                TextView textView = findViewById(R.id.nama);
                TextView textView2 = findViewById(R.id.username);
                TextView textView3 = findViewById(R.id.bio);
                ImageView imageView = findViewById(R.id.gambar);

                showLoading(true);
                userCall.enqueue(new Callback<GithubUser>() {
                    @Override
                    public void onResponse(@NonNull Call<GithubUser> call, @NonNull Response<GithubUser> response) {
                        if (response.isSuccessful()){
                            showLoading(false);
                            GithubUser user = response.body();
                            if (user != null){
                                String name = "Name: " + user.getName();
                                String usernames = "Username: " + user.getUsername();
                                String bio = "Bio: " + user.getBio();
                                String gambar = user.getAvatarUrl();

                                textView.setText(name);
                                textView2.setText(usernames);
                                textView3.setText(bio);
                                Picasso.get().load(gambar).into(imageView);
                            }else {
                                Toast.makeText(Detail.this, "Failed to get user data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GithubUser> call, Throwable t) {
                        Toast.makeText(Detail.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Username is null or empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showLoading(Boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}