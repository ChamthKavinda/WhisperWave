package com.chama.whisperwave.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chama.whisperwave.adapters.UsersAdapter;
import com.chama.whisperwave.databinding.ActivityUsersBinding;
import com.chama.whisperwave.listeners.UserListener;
import com.chama.whisperwave.models.User;
import com.chama.whisperwave.utilities.Constants;
import com.chama.whisperwave.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends BaseActivity implements UserListener {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        searchUser();
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    private void searchUser() {
        showEnterUsernameMessage();
        binding.layoutSend.setOnClickListener(v -> {
            getUsers(binding.inputUserName.getText().toString());
        });

    }

    private void getUsers(String SearchUserName) {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                            User user = new User();
                            user.username = queryDocumentSnapshot.getString(Constants.KEY_USER_NAME);
                            if (user.username.equals(SearchUserName)) {
                                user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                                user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                                user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                                user.id = queryDocumentSnapshot.getId();
                                users.add(user);
                                binding.textErrorMessage.setVisibility(View.INVISIBLE);
                            }
                        }
                        if (users.size() > 0) {
                            UsersAdapter usersAdapter = new UsersAdapter(users, this);
                            binding.userRecyclerView.setAdapter(usersAdapter);
                            binding.userRecyclerView.setVisibility(View.VISIBLE);
                        }
                        else {
                            showErrorMessage();
                        }
                    }
                    else {
                        showErrorMessage();
                    }
                });
    }

    private void showEnterUsernameMessage() {
        binding.textErrorMessage.setText(String.format("%s", "Enter username"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s", "No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}