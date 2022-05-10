package edu.uw.tcss450.group6App.ui.chat.chatMenu;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group6App.R;
import edu.uw.tcss450.group6App.databinding.FragmentChatCardBinding;
import edu.uw.tcss450.group6App.ui.contacts.ContactsViewModel;

public class ChatMenuRecyclerViewAdapater extends RecyclerView.Adapter<ChatMenuRecyclerViewAdapater.ChatViewHolder> {

    private final List<ChatInfo> mChats;
    private ContactsViewModel viewModel;

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
            viewModel=new ViewModelProvider((ViewModelStoreOwner) recyclerView.getContext()).get(ContactsViewModel.class);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMenuRecyclerViewAdapater.ChatViewHolder holder, int position) {
        holder.setChat(mChats.get(position));
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private FragmentChatCardBinding binding;
        private ChatInfo mChat;
        private ContactsViewModel viewModel;

        public ChatViewHolder(@NonNull View view, ContactsViewModel model) {
            super(view);
            mView = view;
            binding = FragmentChatCardBinding.bind(view);
            viewModel = model;
        }

        @SuppressLint("SetTextI18n")
        void setChat(final ChatInfo chat) {
            mChat = chat;
            binding.textChatName.setText(chat.getName());
            binding.buttonEnterChat.setOnClickListener(button ->
                    Log.d("OPEN CHAT", "Opening chat...")
                    //viewModel.removeContact(mContact.getEmail())
            );
        }
    }
}
