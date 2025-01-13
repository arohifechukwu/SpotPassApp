//package com.example.spotpassapp.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.spotpassapp.R;
//import com.example.spotpassapp.model.User;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.List;
//
//public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
//
//    private Context context;
//    private List<User> userList;
//    private DatabaseReference databaseRef;
//
//    public UserAdapter(Context context, List<User> userList) {
//        this.context = context;
//        this.userList = userList;
//        this.databaseRef = FirebaseDatabase.getInstance().getReference("users");
//    }
//
//    @NonNull
//    @Override
//    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
//        return new UserViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
//        User user = userList.get(position);
//
//        // Correctly set values based on the User model
//        holder.userName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
//        holder.userEmail.setText(user.getEmail());
//
//        // Delete User Logic
//        holder.deleteUserButton.setOnClickListener(v -> {
//            String userKey = user.getEmail().replace(".", "_");  // Use email as a key substitute
//            databaseRef.child(userKey).removeValue().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    Toast.makeText(context, "User deleted!", Toast.LENGTH_SHORT).show();
//                    userList.remove(position);
//                    notifyItemRemoved(position);
//                } else {
//                    Toast.makeText(context, "Failed to delete user.", Toast.LENGTH_SHORT).show();
//                }
//            });
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return userList.size();
//    }
//
//    public static class UserViewHolder extends RecyclerView.ViewHolder {
//        TextView userName, userEmail;
//        Button deleteUserButton;
//
//        public UserViewHolder(@NonNull View itemView) {
//            super(itemView);
//            userName = itemView.findViewById(R.id.userName);
//            userEmail = itemView.findViewById(R.id.userEmail);
//            deleteUserButton = itemView.findViewById(R.id.deleteUserButton);
//        }
//    }
//}




//package com.example.spotpassapp.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.spotpassapp.EditUserActivity;
//import com.example.spotpassapp.R;
//import com.example.spotpassapp.model.User;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import java.util.List;
//
//public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
//
//    private Context context;
//    private List<User> userList;
//    private DatabaseReference databaseRef;
//
//    public UserAdapter(Context context, List<User> userList) {
//        this.context = context;
//        this.userList = userList;
//        this.databaseRef = FirebaseDatabase.getInstance().getReference("users");
//    }
//
//    @NonNull
//    @Override
//    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
//        return new UserViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
//        User user = userList.get(position);
//
//        holder.userName.setText(user.getFirstName() + " " + user.getLastName());
//        holder.userEmail.setText(user.getEmail());
//
//        // Edit User
//        holder.editUserButton.setOnClickListener(v -> {
//            Intent intent = new Intent(context, EditUserActivity.class);
//            intent.putExtra("userId", user.getEmail());
//            context.startActivity(intent);
//        });
//
//        // Delete User
//        holder.deleteUserButton.setOnClickListener(v -> {
//            databaseRef.child(user.getEmail()).removeValue().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    Toast.makeText(context, "User deleted!", Toast.LENGTH_SHORT).show();
//                    userList.remove(position);
//                    notifyItemRemoved(position);
//                } else {
//                    Toast.makeText(context, "Failed to delete user.", Toast.LENGTH_SHORT).show();
//                }
//            });
//        });
//
//        // Disable/Enable User
//        holder.disableUserButton.setText(user.isDisabled() ? "Enable" : "Disable");
//        holder.disableUserButton.setOnClickListener(v -> {
//            databaseRef.child(user.getEmail()).child("disabled").setValue(!user.isDisabled())
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            user.setDisabled(!user.isDisabled());
//                            notifyItemChanged(position);
//                            Toast.makeText(context, user.isDisabled() ? "User disabled" : "User enabled", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return userList.size();
//    }
//
//    public static class UserViewHolder extends RecyclerView.ViewHolder {
//
//        TextView userName, userEmail;
//        Button editUserButton, deleteUserButton, disableUserButton;
//
//        public UserViewHolder(@NonNull View itemView) {
//            super(itemView);
//            userName = itemView.findViewById(R.id.userName);
//            userEmail = itemView.findViewById(R.id.userEmail);
//            editUserButton = itemView.findViewById(R.id.editUserButton);
//            deleteUserButton = itemView.findViewById(R.id.deleteUserButton);
//            disableUserButton = itemView.findViewById(R.id.disableUserButton);
//        }
//    }
//}




//package com.example.spotpassapp.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.spotpassapp.EditUserActivity;
//import com.example.spotpassapp.R;
//import com.example.spotpassapp.model.User;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import java.util.List;
//
//public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
//
//    private Context context;
//    private List<User> userList;
//    private DatabaseReference databaseRef;
//
//    public UserAdapter(Context context, List<User> userList) {
//        this.context = context;
//        this.userList = userList;
//        this.databaseRef = FirebaseDatabase.getInstance().getReference("users");
//    }
//
//    @NonNull
//    @Override
//    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
//        return new UserViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
//        User user = userList.get(position);
//
//        holder.userName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
//        holder.userEmail.setText(user.getEmail());
//
//        // Edit User Button Click Listener
//        holder.editUserButton.setOnClickListener(v -> {
//            Intent intent = new Intent(context, EditUserActivity.class);
//            intent.putExtra("userEmail", user.getEmail());
//            context.startActivity(intent);
//        });
//
//        // Delete User Action
//        holder.deleteUserButton.setOnClickListener(v -> {
//            databaseRef.child(user.getEmail()).removeValue().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    Toast.makeText(context, "User deleted!", Toast.LENGTH_SHORT).show();
//                    userList.remove(position);
//                    notifyItemRemoved(position);
//                } else {
//                    Toast.makeText(context, "Failed to delete user.", Toast.LENGTH_SHORT).show();
//                }
//            });
//        });
//
//        // Enable/Disable User Action
//        holder.disableUserButton.setText(user.isDisabled() ? "Enable" : "Disable");
//        holder.disableUserButton.setOnClickListener(v -> {
//            boolean newStatus = !user.isDisabled();
//            databaseRef.child(user.getEmail()).child("disabled").setValue(newStatus)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            user.setDisabled(newStatus);
//                            notifyItemChanged(position);
//                            String message = newStatus ? "User disabled" : "User enabled";
//                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return userList.size();
//    }
//
//    public static class UserViewHolder extends RecyclerView.ViewHolder {
//
//        TextView userName, userEmail;
//        Button editUserButton, deleteUserButton, disableUserButton;
//
//        public UserViewHolder(@NonNull View itemView) {
//            super(itemView);
//            userName = itemView.findViewById(R.id.userName);
//            userEmail = itemView.findViewById(R.id.userEmail);
//            editUserButton = itemView.findViewById(R.id.editUserButton);
//            deleteUserButton = itemView.findViewById(R.id.deleteUserButton);
//            disableUserButton = itemView.findViewById(R.id.disableUserButton);
//        }
//    }
//}




//package com.example.spotpassapp.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.spotpassapp.EditUserActivity;
//import com.example.spotpassapp.R;
//import com.example.spotpassapp.model.User;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import java.util.List;
//
//import static com.example.spotpassapp.FirebaseUtils.encodeUserEmail;
//
//public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
//
//    private Context context;
//    private List<User> userList;
//    private DatabaseReference databaseRef;
//
//    public UserAdapter(Context context, List<User> userList) {
//        this.context = context;
//        this.userList = userList;
//        this.databaseRef = FirebaseDatabase.getInstance().getReference("users");
//    }
//
//    @NonNull
//    @Override
//    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
//        return new UserViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
//        User user = userList.get(position);
//
//        holder.userName.setText(user.getFirstName() + " " + user.getLastName());
//        holder.userEmail.setText(user.getEmail());
//
//        // Edit User Button Click Listener
//        holder.editUserButton.setOnClickListener(v -> {
//            Intent intent = new Intent(context, EditUserActivity.class);
//            intent.putExtra("userEmail", user.getEmail());
//            context.startActivity(intent);
//        });
//
//        // Delete User Button Click Listener
//        holder.deleteUserButton.setOnClickListener(v -> {
//            String encodedEmail = encodeUserEmail(user.getEmail());
//            databaseRef.child(encodedEmail).removeValue().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    Toast.makeText(context, "User deleted!", Toast.LENGTH_SHORT).show();
//                    userList.remove(position);
//                    notifyItemRemoved(position);
//                } else {
//                    Toast.makeText(context, "Failed to delete user.", Toast.LENGTH_SHORT).show();
//                }
//            });
//        });
//
//        // Enable/Disable User Button Click Listener
//        holder.disableUserButton.setText(user.isDisabled() ? "Enable" : "Disable");
//        holder.disableUserButton.setOnClickListener(v -> {
//            String encodedEmail = encodeUserEmail(user.getEmail());
//            boolean newStatus = !user.isDisabled();
//            databaseRef.child(encodedEmail).child("disabled").setValue(newStatus)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            user.setDisabled(newStatus);
//                            notifyItemChanged(position);
//                            String message = newStatus ? "User disabled" : "User enabled";
//                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return userList.size();
//    }
//
//    public static class UserViewHolder extends RecyclerView.ViewHolder {
//
//        TextView userName, userEmail;
//        Button editUserButton, deleteUserButton, disableUserButton;
//
//        public UserViewHolder(@NonNull View itemView) {
//            super(itemView);
//            userName = itemView.findViewById(R.id.userName);
//            userEmail = itemView.findViewById(R.id.userEmail);
//            editUserButton = itemView.findViewById(R.id.editUserButton);
//            deleteUserButton = itemView.findViewById(R.id.deleteUserButton);
//            disableUserButton = itemView.findViewById(R.id.disableUserButton);
//        }
//    }
//}




//package com.example.spotpassapp.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.spotpassapp.EditUserActivity;
//import com.example.spotpassapp.R;
//import com.example.spotpassapp.model.User;
//import com.example.spotpassapp.util.FirebaseUtils;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import java.util.List;
//
//public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
//
//    private Context context;
//    private List<User> userList;
//    private DatabaseReference databaseRef;
//
//    public UserAdapter(Context context, List<User> userList) {
//        this.context = context;
//        this.userList = userList;
//        this.databaseRef = FirebaseDatabase.getInstance().getReference("users");
//    }
//
//    @NonNull
//    @Override
//    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
//        return new UserViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
//        User user = userList.get(position);
//
//        holder.userName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
//        holder.userEmail.setText(user.getEmail());
//
//        // Edit User
//        holder.editUserButton.setOnClickListener(v -> {
//            Intent intent = new Intent(context, EditUserActivity.class);
//            intent.putExtra("userEmail", FirebaseUtils.encodeUserEmail(user.getEmail()));
//            context.startActivity(intent);
//        });
//
//        // Delete User
//        holder.deleteUserButton.setOnClickListener(v -> {
//            String emailKey = FirebaseUtils.encodeUserEmail(user.getEmail());
//
//            databaseRef.child(emailKey).removeValue().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    userList.remove(position);
//                    notifyItemRemoved(position);
//                    Toast.makeText(context, "User deleted!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "Failed to delete user.", Toast.LENGTH_SHORT).show();
//                }
//            });
//        });
//
//// Disable/Enable User
//        holder.disableUserButton.setText(user.isDisabled() ? "Enable" : "Disable");
//        holder.disableUserButton.setOnClickListener(v -> {
//            String emailKey = FirebaseUtils.encodeUserEmail(user.getEmail());
//            boolean newStatus = !user.isDisabled();
//
//            databaseRef.child(emailKey).child("disabled").setValue(newStatus)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            user.setDisabled(newStatus);
//                            notifyItemChanged(position);
//                            String message = newStatus ? "User disabled" : "User enabled";
//                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(context, "Action failed.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return userList.size();
//    }
//
//    public static class UserViewHolder extends RecyclerView.ViewHolder {
//
//        TextView userName, userEmail;
//        Button editUserButton, deleteUserButton, disableUserButton;
//
//        public UserViewHolder(@NonNull View itemView) {
//            super(itemView);
//            userName = itemView.findViewById(R.id.userName);
//            userEmail = itemView.findViewById(R.id.userEmail);
//            editUserButton = itemView.findViewById(R.id.editUserButton);
//            deleteUserButton = itemView.findViewById(R.id.deleteUserButton);
//            disableUserButton = itemView.findViewById(R.id.disableUserButton);
//        }
//    }
//}



//package com.example.spotpassapp.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.spotpassapp.EditUserActivity;
//import com.example.spotpassapp.R;
//import com.example.spotpassapp.model.User;
//import com.example.spotpassapp.util.FirebaseUtils;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import java.util.List;
//
//public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
//
//    private Context context;
//    private List<User> userList;
//    private DatabaseReference databaseRef;
//
//    public UserAdapter(Context context, List<User> userList) {
//        this.context = context;
//        this.userList = userList;
//        this.databaseRef = FirebaseDatabase.getInstance().getReference("users");
//    }
//
//    @NonNull
//    @Override
//    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
//        return new UserViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
//        User user = userList.get(position);
//
//        holder.userName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
//        holder.userEmail.setText(user.getEmail());
//
//        // Edit User Button Click
//        holder.editUserButton.setOnClickListener(v -> {
//            Intent intent = new Intent(context, EditUserActivity.class);
//            intent.putExtra("userEmail", FirebaseUtils.encodeUserEmail(user.getEmail()));
//            context.startActivity(intent);
//        });
//
//        // Delete User Button Click
//        holder.deleteUserButton.setOnClickListener(v -> {
//            String emailKey = FirebaseUtils.encodeUserEmail(user.getEmail());
//            databaseRef.child(emailKey).removeValue().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    FirebaseAuth auth = FirebaseAuth.getInstance();
//                    FirebaseUser currentUser = auth.getCurrentUser();
//
//                    if (currentUser != null && currentUser.getEmail().equals(user.getEmail())) {
//                        currentUser.delete().addOnCompleteListener(authTask -> {
//                            if (authTask.isSuccessful()) {
//                                userList.remove(position);
//                                notifyItemRemoved(position);
//                                Toast.makeText(context, "User deleted!", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(context, "Failed to delete from Auth.", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    } else {
//                        userList.remove(position);
//                        notifyItemRemoved(position);
//                        Toast.makeText(context, "User deleted from Database.", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(context, "Failed to delete user.", Toast.LENGTH_SHORT).show();
//                }
//            });
//        });
//
//        // Disable/Enable User
//        holder.disableUserButton.setText(user.isDisabled() ? "Enable" : "Disable");
//        holder.disableUserButton.setOnClickListener(v -> {
//            String emailKey = FirebaseUtils.encodeUserEmail(user.getEmail());
//            boolean newStatus = !user.isDisabled();
//
//            databaseRef.child(emailKey).child("disabled").setValue(newStatus)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            user.setDisabled(newStatus);
//                            notifyItemChanged(position);
//                            String message = newStatus ? "User disabled" : "User enabled";
//                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(context, "Action failed.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return userList.size();
//    }
//
//    public static class UserViewHolder extends RecyclerView.ViewHolder {
//
//        TextView userName, userEmail;
//        Button editUserButton, deleteUserButton, disableUserButton;
//
//        public UserViewHolder(@NonNull View itemView) {
//            super(itemView);
//            userName = itemView.findViewById(R.id.userName);
//            userEmail = itemView.findViewById(R.id.userEmail);
//            editUserButton = itemView.findViewById(R.id.editUserButton);
//            deleteUserButton = itemView.findViewById(R.id.deleteUserButton);
//            disableUserButton = itemView.findViewById(R.id.disableUserButton);
//        }
//    }
//}




//package com.example.spotpassapp.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.spotpassapp.EditUserActivity;
//import com.example.spotpassapp.R;
//import com.example.spotpassapp.model.User;
//import com.example.spotpassapp.util.FirebaseUtils;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.List;
//
//public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
//
//    private Context context;
//    private List<User> userList;
//    private DatabaseReference databaseRef;
//
//    public UserAdapter(Context context, List<User> userList) {
//        this.context = context;
//        this.userList = userList;
//        this.databaseRef = FirebaseDatabase.getInstance().getReference("users");
//    }
//
//    @NonNull
//    @Override
//    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
//        return new UserViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
//        User user = userList.get(position);
//
//        holder.userName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
//        holder.userEmail.setText(user.getEmail());
//
//        // Edit User
//        holder.editUserButton.setOnClickListener(v -> {
//            Intent intent = new Intent(context, EditUserActivity.class);
//            intent.putExtra("userEmail", FirebaseUtils.encodeUserEmail(user.getEmail()));
//            context.startActivity(intent);
//        });
//
//        // Delete User
//        // Delete User
//        holder.deleteUserButton.setOnClickListener(v -> {
//            String emailKey = FirebaseUtils.encodeUserEmail(user.getEmail());
//
//            databaseRef.child(emailKey).removeValue().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    userList.remove(position);
//                    notifyItemRemoved(position);
//                    Toast.makeText(context, "User deleted!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "Failed to delete user.", Toast.LENGTH_SHORT).show();
//                }
//            });
//        });
//
//
//        // Disable/Enable User
//        holder.disableUserButton.setText(user.isDisabled() ? "Enable" : "Disable");
//        holder.disableUserButton.setOnClickListener(v -> {
//            String emailKey = FirebaseUtils.encodeUserEmail(user.getEmail());
//            boolean newStatus = !user.isDisabled();
//
//            databaseRef.child(emailKey).child("disabled").setValue(newStatus)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            user.setDisabled(newStatus);
//                            notifyItemChanged(position);
//                            String message = newStatus ? "User disabled" : "User enabled";
//                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(context, "Action failed.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return userList.size();
//    }
//
//    public static class UserViewHolder extends RecyclerView.ViewHolder {
//
//        TextView userName, userEmail;
//        Button editUserButton, deleteUserButton, disableUserButton;
//
//        public UserViewHolder(@NonNull View itemView) {
//            super(itemView);
//            userName = itemView.findViewById(R.id.userName);
//            userEmail = itemView.findViewById(R.id.userEmail);
//            editUserButton = itemView.findViewById(R.id.editUserButton);
//            deleteUserButton = itemView.findViewById(R.id.deleteUserButton);
//            disableUserButton = itemView.findViewById(R.id.disableUserButton);
//        }
//    }
//}




//package com.example.spotpassapp.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.spotpassapp.EditUserActivity;
//import com.example.spotpassapp.R;
//import com.example.spotpassapp.model.User;
//import com.example.spotpassapp.util.FirebaseUtils;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.List;
//
//public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
//
//    private Context context;
//    private List<User> userList;
//    private DatabaseReference databaseRef;
//
//    public UserAdapter(Context context, List<User> userList) {
//        this.context = context;
//        this.userList = userList;
//        this.databaseRef = FirebaseDatabase.getInstance().getReference("users");
//    }
//
//    @NonNull
//    @Override
//    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
//        return new UserViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
//        User user = userList.get(position);
//
//        holder.userName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
//        holder.userEmail.setText(user.getEmail());
//
//        // Edit User
//        holder.editUserButton.setOnClickListener(v -> {
//            Intent intent = new Intent(context, EditUserActivity.class);
//            intent.putExtra("userEmail", FirebaseUtils.encodeUserEmail(user.getEmail()));
//            context.startActivity(intent);
//        });
//
//        // Delete User
//        holder.deleteUserButton.setOnClickListener(v -> deleteUser(user, position));
//
//        // Disable/Enable User
//        holder.disableUserButton.setText(user.isDisabled() ? "Enable" : "Disable");
//        holder.disableUserButton.setOnClickListener(v -> toggleUserStatus(user, position));
//    }
//
//    private void deleteUser(User user, int position) {
//        String emailKey = FirebaseUtils.encodeUserEmail(user.getEmail());
//
//        databaseRef.child(emailKey).removeValue().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                FirebaseAuth auth = FirebaseAuth.getInstance();
//                auth.fetchSignInMethodsForEmail(user.getEmail()).addOnCompleteListener(fetchTask -> {
//                    if (fetchTask.isSuccessful() && fetchTask.getResult().getSignInMethods().size() > 0) {
//                        auth.signInWithEmailAndPassword(user.getEmail(), "DEFAULT_PASSWORD")
//                                .addOnCompleteListener(signInTask -> {
//                                    if (signInTask.isSuccessful()) {
//                                        FirebaseUser firebaseUser = auth.getCurrentUser();
//                                        if (firebaseUser != null) {
//                                            firebaseUser.delete().addOnCompleteListener(deleteTask -> {
//                                                if (deleteTask.isSuccessful()) {
//                                                    userList.remove(position);
//                                                    notifyItemRemoved(position);
//                                                    Toast.makeText(context, "User deleted from Authentication!", Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                        }
//                                    }
//                                });
//                    } else {
//                        Toast.makeText(context, "User not found in Authentication!", Toast.LENGTH_SHORT).show();
//                        userList.remove(position);
//                        notifyItemRemoved(position);
//                    }
//                });
//            }
//        });
//    }
//
//    private void toggleUserStatus(User user, int position) {
//        String emailKey = FirebaseUtils.encodeUserEmail(user.getEmail());
//        boolean newStatus = !user.isDisabled();
//
//        databaseRef.child(emailKey).child("disabled").setValue(newStatus)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        user.setDisabled(newStatus);
//                        notifyItemChanged(position);
//                        String message = newStatus ? "User disabled" : "User enabled";
//                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    @Override
//    public int getItemCount() {
//        return userList.size();
//    }
//
//    public static class UserViewHolder extends RecyclerView.ViewHolder {
//
//        TextView userName, userEmail;
//        Button editUserButton, deleteUserButton, disableUserButton;
//
//        public UserViewHolder(@NonNull View itemView) {
//            super(itemView);
//            userName = itemView.findViewById(R.id.userName);
//            userEmail = itemView.findViewById(R.id.userEmail);
//            editUserButton = itemView.findViewById(R.id.editUserButton);
//            deleteUserButton = itemView.findViewById(R.id.deleteUserButton);
//            disableUserButton = itemView.findViewById(R.id.disableUserButton);
//        }
//    }
//}








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