package com.example.quanlybanhang.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybanhang.Interface.ItemClickListener;
import com.example.quanlybanhang.model.EventBus.SuaXoaEvent;
import com.example.quanlybanhang.model.EventBus.SuaXoaEventUser;
import com.example.quanlybanhang.model.User;
import com.example.quanlybanhang.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    Context context;
    List<User> listUser;

    public UserAdapter(Context context, List<User> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {
        User user = listUser.get(position);
        holder.itemuser_ten.setText(user.getName());
        holder.itemuser_email.setText(user.getEmail());
        holder.itemuser_pass.setText(user.getPass());
        holder.itemuser_sdt.setText(user.getSdt());
        holder.itemuser_chucvu.setText(user.getChucvu());


        holder.setItemClickListener((view, position1, isLongClick) -> {
            if (!isLongClick) {

            } else {

                EventBus.getDefault().postSticky(new SuaXoaEventUser(user));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {
        TextView itemuser_ten, itemuser_email, itemuser_sdt, itemuser_chucvu,itemuser_pass;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemuser_ten = itemView.findViewById(R.id.itemuser_ten);
            itemuser_email = itemView.findViewById(R.id.itemuser_email);
            itemuser_pass = itemView.findViewById(R.id.itemuser_pass);
            itemuser_sdt = itemView.findViewById(R.id.itemuser_sdt);
            itemuser_chucvu = itemView.findViewById(R.id.itemuser_chucvu);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            //contextMenu.add(0, 0, getAdapterPosition(), "Sửa");
            //contextMenu.add(0, 1, getAdapterPosition(), "Xóa");
            contextMenu.add(0, 0, getAdapterPosition(), "Sửa");
            if (!"admin".equalsIgnoreCase(listUser.get(getAdapterPosition()).getChucvu())) {
                contextMenu.add(0, 1, getAdapterPosition(), "Xóa");
            }
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), true);
            return false;
        }
    }
}
