package edu.uw.tcss450.group6App.ui.contacts;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group6App.R;
import edu.uw.tcss450.group6App.databinding.FragmentContactCardBinding;
import edu.uw.tcss450.group6App.databinding.FragmentContactVerifiedBinding;
import edu.uw.tcss450.group6App.ui.contacts.search.ContactInfo;

class ContactsVerifiedRecyclerViewAdapter extends RecyclerView.Adapter<ContactsVerifiedRecyclerViewAdapter.ContactViewHolder> {

    private final List<ContactInfo> mUsers;
    private ContactsViewModel viewModel;

    public ContactsVerifiedRecyclerViewAdapter(List<ContactInfo> users) {
        this.mUsers = users;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_verified, parent, false), viewModel);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if(viewModel==null){
            viewModel=new ViewModelProvider((ViewModelStoreOwner) recyclerView.getContext()).get(ContactsViewModel.class);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsVerifiedRecyclerViewAdapter.ContactViewHolder holder, int position) {
        holder.setContact(mUsers.get(position));
        holder.binding.buttonChat.setOnClickListener(button ->
                viewModel.createChat(holder.mContact.getEmail())
        );
        holder.binding.buttonRemove.setOnClickListener(button -> {
            holder.viewModel.removeContact(holder.mContact.getEmail());
            mUsers.remove(position);
            this.notifyDataSetChanged();

        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private FragmentContactVerifiedBinding binding;
        private ContactInfo mContact;
        private ContactsViewModel viewModel;

        public ContactViewHolder(@NonNull View view, ContactsViewModel model) {
            super(view);
            mView = view;
            binding = FragmentContactVerifiedBinding.bind(view);
            viewModel = model;
        }

        @SuppressLint("SetTextI18n")
        void setContact(final ContactInfo contact) {
            mContact = contact;
            binding.textUsername.setText(contact.getUsername());
            binding.textName.setText(contact.getFName() + " " + contact.getLName());
        }
    }
}
