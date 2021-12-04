package com.example.radiologi.admin.formAddData;

import static com.example.radiologi.admin.formAddData.FormAddDataActivity.DATE;
import static com.example.radiologi.admin.formAddData.FormAddDataActivity.GENDER;
import static com.example.radiologi.admin.formAddData.FormAddDataActivity.NAME;
import static com.example.radiologi.admin.formAddData.FormAddDataActivity.NO_RECORD;
import static com.example.radiologi.admin.formAddData.FormAddDataActivity.NO_REG;
import static com.example.radiologi.admin.formAddData.FormAddDataActivity.PHOTO;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.radiologi.R;
import com.example.radiologi.databinding.DialogFragmentConfirmBinding;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ConfirmDialogFragment extends DialogFragment {

    private DialogFragmentConfirmBinding binding;
    private ConfirmDialogListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = DialogFragmentConfirmBinding.inflate(inflater);

        Bundle args = getArguments();
        if (args != null){
            populateDataToReview(args);
        }

        builder.setView(binding.getRoot())
                .setPositiveButton(R.string.okay, (dialog, which) -> {
                    listener.onDialogPositiveClick(ConfirmDialogFragment.this);
                })
                .setNegativeButton(R.string.not, (dialog, which) -> {
                    listener.onDialogNegativeClick(ConfirmDialogFragment.this);
                    Objects.requireNonNull(ConfirmDialogFragment.this.getDialog()).cancel();
                });

        return builder.create();
    }

    private void populateDataToReview(Bundle args){
        binding.tvNoReg.setText(args.getString(NO_REG, ""));
        binding.tvNoRecord.setText(args.getString(NO_RECORD, ""));
        binding.tvName.setText(args.getString(NAME, ""));
        binding.tvDate.setText(args.getString(DATE, ""));
        binding.tvGender.setText(args.getString(GENDER, ""));

        String photos = args.getString(PHOTO,"");
        Uri uriPhotos = Uri.parse(photos);
        binding.ivFoto.setImageURI(uriPhotos);

        /*Picasso.get()
                .load(args.getString(PHOTO, ""))
                .placeholder(R.drawable.ic_upload)
                .into(binding.ivFoto);*/
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ConfirmDialogListener) context;
        }catch (ClassCastException exception){
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public interface ConfirmDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

}
