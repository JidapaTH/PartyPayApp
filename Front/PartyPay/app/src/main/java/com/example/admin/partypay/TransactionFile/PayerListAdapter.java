package com.example.admin.partypay.TransactionFile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.partypay.CustomSpinnerAdapter;
import com.example.admin.partypay.R;
import com.example.admin.partypay.MemberFile.Member;

import java.util.ArrayList;

/**
 * Created by Admin on 7/28/2017.
 */

public class PayerListAdapter extends ArrayAdapter<PayerList> {


    private ArrayList<Member> mMembers;
    private int mListType; //0 = payer, 1 = receiver

    // All views in Member Activity (including TextWatcher)
    static class ViewHolder{
        private TextView payerText;
        private Spinner memberSpinner;
        private EditText amountEditText;
        private ImageView deleteImageView;
        private TextWatcher textWatcher;
    }

    public PayerListAdapter(Activity c, ArrayList<PayerList> payers, ArrayList<Member> memberList, int listType){
        super(c,0,payers);
        mMembers = memberList;
        mListType = listType;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View listItemViewTemp = convertView;
        if(listItemViewTemp == null) {
            listItemViewTemp = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_payer_add_transaction, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.payerText = (TextView) listItemViewTemp.findViewById(R.id.payer_text);
            viewHolder.memberSpinner = (Spinner) listItemViewTemp.findViewById(R.id.payer_spinner);
            viewHolder.deleteImageView = (ImageView) listItemViewTemp.findViewById(R.id.payer_delete_image);
            viewHolder.amountEditText = (EditText) listItemViewTemp.findViewById(R.id.payer_amount_edittext);
            listItemViewTemp.setTag(viewHolder);
        }

        final View listItemView = listItemViewTemp;

        final PayerList currentPayer = getItem(position);
        ViewHolder vHolder = (ViewHolder) listItemView.getTag();

        int pos = position+1;
        if(mListType == 0) {
            String s = "Payer#" + pos;
            vHolder.payerText.setText(s);
        }else{
            String s = "Receiver#" + pos;
            vHolder.payerText.setText(s);
        }
        if (vHolder.textWatcher != null)
            vHolder.amountEditText.removeTextChangedListener(vHolder.textWatcher);

        // Set member's name in amountEditText, Set TextWatcher to remember String when losing focus
        vHolder.textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String amount = s.toString();
                if (!amount.isEmpty()&& !amount.equals(".")) {
                    currentPayer.setAmount(Double.parseDouble(amount));
                } else {
                    currentPayer.setAmount(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        vHolder.amountEditText.addTextChangedListener(vHolder.textWatcher);
        String amount;
        if(currentPayer.getAmount() == 0) {
            amount = "";
        }else {
            amount = fmt(currentPayer.getAmount());
        }
        vHolder.amountEditText.setText(amount);
        //vHolder.amountEditText.setSelection(vHolder.amountEditText.getText().length());

        // Setup spinner with members' name
        final CustomSpinnerAdapter payerAdapter = new CustomSpinnerAdapter(getContext(), mMembers);
        vHolder.memberSpinner.setAdapter(payerAdapter);
        vHolder.memberSpinner.setSelection(currentPayer.getMemberPos());
        vHolder.memberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPayer.setMemberPos(position);
                currentPayer.setMemberName(mMembers.get(position).get_id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Setup delete ImageView
        if(position == 0){
            vHolder.deleteImageView.setVisibility(View.INVISIBLE);
        } else {
            vHolder.deleteImageView.setVisibility(View.VISIBLE);
        }
        vHolder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position < getCount()) {
                    ObjectAnimator anim = ObjectAnimator.ofFloat(listItemView, View.ALPHA, 0);
                    anim.setDuration(300);
                    listItemView.setHasTransientState(true);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            PayerListAdapter.this.remove(getItem(position));
                            PayerListAdapter.this.notifyDataSetChanged();
                            listItemView.setAlpha(1);
                            listItemView.setHasTransientState(false);
                        }
                    });
                    anim.start();
                } else {
                    Log.i("PayerListAdapter","Delete fail at position: "+position+" getCount :" + getCount());
                }
            }
        });
        return listItemView;
    }

    // If double has decimal place, keep it. If not, remove all decimal (xxx.0 -> xxx)
    public static String fmt(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }

}
