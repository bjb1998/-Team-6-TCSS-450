package edu.uw.tcss450.group6App.ui.chat.chatMenu;

import static edu.uw.tcss450.group6App.MainActivity.currentUserInfo;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group6App.R;
import edu.uw.tcss450.group6App.databinding.FragmentChatCardBinding;
import edu.uw.tcss450.group6App.model.UserInfoViewModel;

public class ChatMenuRecyclerViewAdapater extends RecyclerView.Adapter<ChatMenuRecyclerViewAdapater.ChatViewHolder> {

    private final List<ChatInfo> mChats;
    private UserInfoViewModel viewModel;

    public ChatMenuRecyclerViewAdapater(List<ChatInfo> chats) {
        this.mChats = chats;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_card, parent, false), viewModel);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if(viewModel==null){
            viewModel= currentUserInfo;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMenuRecyclerViewAdapater.ChatViewHolder holder, int position) {
        holder.setChat(mChats.get(position));
        holder.binding.buttonEnterChat.setOnClickListener(button -> {
            viewModel.setCurrentChatId(holder.mChat.getChatId());
            Navigation.findNavController(holder.mView).navigate(
                    ChatMenuFragmentDirections.actionNavigationChatMenuToNavigationChat());
        });
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private FragmentChatCardBinding binding;
        private ChatInfo mChat;
        private UserInfoViewModel viewModel;

        public ChatViewHolder(@NonNull View view, UserInfoViewModel model) {
            super(view);
            mView = view;
            binding = FragmentChatCardBinding.bind(view);
            viewModel = model;
        }

        @SuppressLint("SetTextI18n")
        void setChat(final ChatInfo chat) {
            mChat = chat;
            binding.textChatName.setText(mChat.getName());
        }
    }
}
