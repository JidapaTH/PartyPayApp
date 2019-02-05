package com.example.admin.partypay.MemberFile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.admin.partypay.EventActivityFile.SummaryBar;
import com.example.admin.partypay.R;

import java.util.ArrayList;

/**
 * Created by Admin on 6/29/2017.
 */

public class MemberAdapter extends ArrayAdapter<Member> {

    private boolean getEditTextFocus = false;
    private boolean isNewEvent;
    private ArrayList<SummaryBar> debtor;
    private ArrayList<SummaryBar> creditor;
    public final int STATUS_ACTIVE=0;
    public final int STATUS_INACTIVE=1;

    // All views in Member Activity (including TextWatcher)
    static class ViewHolder{
        private EditText nameEditText;
        private ImageView statusImageView;
        private ImageView editImageView;
        private ImageView deleteImageView;
        private TextWatcher textWatcher;
        private RelativeLayout parentView;
        //private Button button;
    }

    //Constructor method
    public MemberAdapter(Activity context, ArrayList<Member> members){
        super(context,0,members);
        isNewEvent = true;
    }

    //Constructor method
    public MemberAdapter(Activity context, ArrayList<Member> members, ArrayList<SummaryBar> d, ArrayList<SummaryBar> c){
        super(context,0,members);
        isNewEvent = false;
        debtor = d;
        creditor = c;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {

        Log.v("MemberAdapter","Position:"+position);
        View listItemViewTemp = convertView;
        if(listItemViewTemp == null) {
            listItemViewTemp = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_member, parent, false);
            //Inflate new views
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.parentView = (RelativeLayout) listItemViewTemp.findViewById(R.id.member_parent_view);
            viewHolder.nameEditText = (EditText) listItemViewTemp.findViewById(R.id.member_name_edittext);
            viewHolder.statusImageView = (ImageView) listItemViewTemp.findViewById(R.id.member_status_imageview);
            viewHolder.editImageView = (ImageView) listItemViewTemp.findViewById(R.id.member_edit_imageview);
            viewHolder.deleteImageView = (ImageView) listItemViewTemp.findViewById(R.id.member_delete_imageview);
            listItemViewTemp.setTag(viewHolder);
        }

        final View listItemView = listItemViewTemp;
        final Member currentMember = getItem(position);
        final ViewHolder vHolder = (ViewHolder) listItemView.getTag();
        // Remove any existing TextWatcher that will be keyed to the wrong ListItem
        if (vHolder.textWatcher != null)
            vHolder.nameEditText.removeTextChangedListener(vHolder.textWatcher);

        // Set member's name in editText, Set TextWatcher to remember String when losing focus
        vHolder.textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentMember.change_name(s.toString());
                Log.v("MemberAdapter","Text changed to: "+s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        vHolder.nameEditText.addTextChangedListener(vHolder.textWatcher);
        vHolder.nameEditText.setText(currentMember.get_name());
        vHolder.nameEditText.setSelection(vHolder.nameEditText.getText().length());
        if(position == 0){
            vHolder.nameEditText.setEnabled(false);
        } else {
            vHolder.nameEditText.setEnabled(true);
        }
        // Set member status image
        setStatusImageViewResource(vHolder.statusImageView,currentMember);
        // Toggle status when image is pressed
        vHolder.statusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView activeImageView = (ImageView) v.findViewById(R.id.member_status_imageview);
                if(currentMember.get_status()==STATUS_ACTIVE){
                    currentMember.change_status(STATUS_INACTIVE);
                } else {
                    currentMember.change_status(STATUS_ACTIVE);
                }
                setStatusImageViewResource(activeImageView,currentMember);
            }
        });

        // Set delete onClickListener with AlertDialog
        vHolder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this is not new event, check first if member still has debt/credit
                if(!isNewEvent) {
                    String name = "";
                    String found = "";
                    double amount=0;
                    for(int i =0;i<debtor.size();i++){
                        if(debtor.get(i).getMemberID()==currentMember.get_id()){
                            name = debtor.get(i).getMemberName();
                            amount = debtor.get(i).getPendingAmount();
                            found = "debts";
                        }
                    }
                    if(found.isEmpty()){
                        for(int i =0;i<creditor.size();i++){
                            if(creditor.get(i).getMemberID()==currentMember.get_id()){
                                name = creditor.get(i).getMemberName();
                                amount = creditor.get(i).getPendingAmount();
                                found = "credits";
                            }
                        }
                    }
                    if(!found.isEmpty()){
                        //There's still debt/creditor
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                        mBuilder.setTitle("This member cannot be deleted.");
                        mBuilder.setMessage("'"+name+"' still has "+found+" of "+amount+" baht."+" Please clear all "+found+" first.");
                        mBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alertDialog = mBuilder.create();
                        alertDialog.show();
                    } else {
                        deleteMember(currentMember, position, listItemView);
                    }
                }
            }
        });

        vHolder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showEditNameDialog(vHolder.nameEditText.getText().toString(), vHolder.nameEditText);
            }
        });

        //If it's new event, hide active button
        if (isNewEvent) {
            vHolder.statusImageView.setVisibility(View.GONE);
            vHolder.editImageView.setVisibility(View.GONE);
            vHolder.nameEditText.setEnabled(true);
        } else {

            vHolder.nameEditText.setEnabled(false);
            if(position==0){
                vHolder.statusImageView.setVisibility(View.INVISIBLE);
                vHolder.editImageView.setVisibility(View.INVISIBLE);
            } else {
                vHolder.statusImageView.setVisibility(View.VISIBLE);
                vHolder.editImageView.setVisibility(View.VISIBLE);
            }
        }
        //If it's first position, hide delete button
        if(position == 0) {
            vHolder.deleteImageView.setVisibility(View.INVISIBLE);
        } else {
            vHolder.deleteImageView.setVisibility(View.VISIBLE);
        }
        return listItemView;
    }

    //Use this to change dp unit to pixel unit
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    //Use this to set ImageResouce for member status
    private void setStatusImageViewResource (ImageView imageView, Member member){
        if(member.get_status()==STATUS_ACTIVE){
            // When active
            imageView.setImageResource(R.mipmap.ic_launcher_round);
        } else {
            // When inactive
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }

    public void deleteMember(Member currentMember, final int position, final View listItemView ){
        // Check if member name is empty, if false, show alert dialog before delete
        if(currentMember.get_name().trim().isEmpty()){
            ObjectAnimator anim = ObjectAnimator.ofFloat(listItemView,View.ALPHA,0);
            anim.setDuration(300);
            listItemView.setHasTransientState(true);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    MemberAdapter.this.remove(getItem(position));
                    MemberAdapter.this.notifyDataSetChanged();
                    listItemView.setAlpha(1);
                    listItemView.setHasTransientState(false);
                }
            });
            anim.start();
        } else {
            // Add command when delete button is pressed
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
            mBuilder.setMessage(getContext().getResources().getString(R.string.member_alert_message1)
                    + currentMember.get_name()
                    + getContext().getResources().getString(R.string.member_alert_message2));
            mBuilder.setPositiveButton(R.string.member_alert_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ObjectAnimator anim = ObjectAnimator.ofFloat(listItemView,View.ALPHA,0);
                    anim.setDuration(300);
                    listItemView.setHasTransientState(true);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            MemberAdapter.this.remove(getItem(position));
                            MemberAdapter.this.notifyDataSetChanged();
                            listItemView.setAlpha(1);
                            listItemView.setHasTransientState(false);
                        }
                    });
                    anim.start();
                    dialogInterface.dismiss();
                }
            });
            mBuilder.setNegativeButton(R.string.member_alert_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = mBuilder.create();
            alertDialog.show();
        }
    }

    public void showEditNameDialog(String name, final EditText editText) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_member_edit, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        edt.setText(name);
        edt.setSelection(edt.getText().length());
        
        dialogBuilder.setMessage("Edit this member's name");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                editText.setText(edt.getText().toString());
                //do something with edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

}
