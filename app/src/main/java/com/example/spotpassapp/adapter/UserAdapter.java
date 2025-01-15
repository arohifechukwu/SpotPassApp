package com.example.spotpassapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.spotpassapp.EditUserActivity;
import com.example.spotpassapp.R;
import com.example.spotpassapp.model.User;
import com.example.spotpassapp.util.FirebaseUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final Context context;
    private final List<User> userList;
    private final DatabaseReference databaseRef;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        this.databaseRef = FirebaseDatabase.getInstance().getReference("users");
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
        holder.userEmail.setText(user.getEmail());

        holder.editUserButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditUserActivity.class);
            intent.putExtra("userEmail", user.getEmail());
            context.startActivity(intent);
        });

        holder.deleteUserButton.setOnClickListener(v -> {
            String emailKey = FirebaseUtils.encodeUserEmail(user.getEmail());
            databaseRef.child(emailKey).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    userList.remove(position);
                    notifyItemRemoved(position);
                    showToast("User deleted!");
                } else {
                    showToast("Failed to delete user.");
                }
            });
        });
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userEmail;
        Button editUserButton, deleteUserButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            editUserButton = itemView.findViewById(R.id.editUserButton);
            deleteUserButton = itemView.findViewById(R.id.deleteUserButton);
        }
    }
}