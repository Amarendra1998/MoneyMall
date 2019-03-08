package com.example.computer.moneymall;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.computer.moneymall.Model.ItemClickListener;
import com.example.computer.moneymall.Model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Product extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView FindFriendsRecyclerList;
    private DatabaseReference UserRef;
    String retriveid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        UserRef = FirebaseDatabase.getInstance().getReference().child("Product");
       // retriveid = UserRef.push().getKey();
        FindFriendsRecyclerList = (RecyclerView)findViewById(R.id.recycle);
        FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(UserRef,Products.class).build();
        FirebaseRecyclerAdapter<Products,FindProductViewHolder>adapter = new FirebaseRecyclerAdapter<Products, FindProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindProductViewHolder holder, final int position, @NonNull Products model) {
                holder.userName.setText(model.getName());
                Picasso.get().load(model.getImage()).placeholder(R.drawable.ic_person_black_24dp).into(holder.profileImage);
                holder.Apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //String visit_user_id = getRef(position).getKey();
                        Intent intent = new Intent(Product.this,EmploymentDetail.class);
                        startActivity(intent);
                    }
                });
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Product.this,ProfileActivity.class);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public FindProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_layout,viewGroup,false);
                FindProductViewHolder viewHolder = new FindProductViewHolder(view);
                return viewHolder;
            }
        };
        FindFriendsRecyclerList.setAdapter(adapter);
        adapter.startListening();
    }
    public static class FindProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
    View.OnCreateContextMenuListener
    {
        TextView userName,userStatus;
        CircleImageView profileImage;
        Button Apply;
        private ItemClickListener itemClickListener;
        public FindProductViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_status);
            Apply = itemView.findViewById(R.id.apply);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            itemView.setOnClickListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }
}
