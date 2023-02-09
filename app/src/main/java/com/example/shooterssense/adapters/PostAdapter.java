package com.example.shooterssense.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shooterssense.R;
import com.example.shooterssense.fragments.ComunidadFragment;
import com.example.shooterssense.model.Post;
import com.example.shooterssense.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> implements View.OnClickListener {
   //TERMINADO-REVISADO
    public static final String CLAVE_POST = "POST";
    Context mContext;
    List<Post> mPosts;
    FirebaseUser firebaseUser;

    private View.OnClickListener listener;

    public PostAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        v.setOnClickListener(this);
        PostAdapter.PostViewHolder ivh = new PostAdapter.PostViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post = mPosts.get(position);

        Glide.with(mContext).load(post.getImageUrl())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher))
                .into(holder.postImage);

        if (post.getDescripcion().equals("")) {
            holder.tituloAyuda.setVisibility(View.GONE);
        } else {
            holder.tituloAyuda.setVisibility(View.VISIBLE);
            holder.tituloAyuda.setText(post.getTitulo());
        }

        loopInfo(holder.imageProfile, holder.username, post.getPublisher());

        isLikes(post.getId(), holder.likePost);
        nrLikes(holder.contadorLikes, post.getId());
        getComments(post.getId(), holder.commentsCounter);

        holder.likePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.likePost.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getId())
                            .child(firebaseUser.getUid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Comentarios").child(post.getId())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.contadorLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ComunidadFragment.class);
                i.putExtra(CLAVE_POST, post.getId());
                mContext.startActivity(i);
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ComentarioAdapter.class);
                i.putExtra("POST_ID", post.getId());
                i.putExtra("PUBLISHER_ID", post.getPublisher());
                mContext.startActivity(i);
            }
        });


    }

    private void loopInfo(final ImageView imageProfile,
                          final TextView username,
                          final String userId) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario user = snapshot.getValue(Usuario.class);
                Glide.with(mContext).load(user.getFotoPerfilUrl()).into(imageProfile);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        ImageView imageProfile;
        TextView username;
        ImageView postImage;
        TextView tituloAyuda;

        ImageView likePost;
        TextView contadorLikes;

        ImageView comment;
        TextView commentsCounter;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.imageProfile);
            username = itemView.findViewById(R.id.username);
            postImage = itemView.findViewById(R.id.postImage);
            tituloAyuda = itemView.findViewById(R.id.tituloAyuda);

            likePost = itemView.findViewById(R.id.likePost);
            contadorLikes = itemView.findViewById(R.id.contadorLikes);

            comment = itemView.findViewById(R.id.comment);
            commentsCounter = itemView.findViewById(R.id.commentsCounter);
        }
    }

    private void getComments(String postid, TextView comments) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comentarios").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.setText(snapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isLikes(String postid, ImageView imageView) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_like); //Boton de likes cargado
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_like_start); //boton de like descargado
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Contar número de likes
    private void nrLikes(final TextView likes, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes")
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
