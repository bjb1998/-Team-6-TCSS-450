package edu.uw.tcss450.group6App.ui.contacts.search;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.CornerFamily;

import java.util.List;

import edu.uw.tcss450.group6App.R;
import edu.uw.tcss450.group6App.databinding.FragmentChatMessageBinding;
import edu.uw.tcss450.group6App.databinding.FragmentContactCardBinding;
import edu.uw.tcss450.group6App.databinding.FragmentContactSearchBinding;
import edu.uw.tcss450.group6App.model.UserInfoViewModel;
import edu.uw.tcss450.group6App.ui.auth.signin.SignInFragmentDirections;

class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ContactViewHolder> {

    private final List<ContactInfo> mUsers;
    private ContactsSearchViewModel viewModel;

    public ContactsRecyclerViewAdapter(List<ContactInfo> users) {
        this.mUsers = users;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_card, parent, false), viewModel);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if(viewModel==null){
            viewModel=new ViewModelProvider((ViewModelStoreOwner) recyclerView.getContext()).get(ContactsSearchViewModel.class);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setContact(mUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private FragmentContactCardBinding binding;
        private ContactInfo mContact;
        private ContactsSearchViewModel viewModel;

        public ContactViewHolder(@NonNull View view, ContactsSearchViewModel model) {
            super(view);
            mView = view;
            binding = FragmentContactCardBinding.bind(view);
            viewModel = model;
        }

        @SuppressLint("SetTextI18n")
        void setContact(final ContactInfo contact) {
            mContact = contact;
            binding.textUsername.setText(contact.getUsername());
            binding.textName.setText(contact.getFName() + " " + contact.getLName());
            binding.buttonInvite.setOnClickListener(button ->
                    viewModel.connectContact(mContact.getEmail())
                    );
        }
    }
}
