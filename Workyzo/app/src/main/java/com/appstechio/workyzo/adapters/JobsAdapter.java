package com.appstechio.workyzo.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.activities.JobDetails_Activity;
import com.appstechio.workyzo.activities.Postjob_Activity;
import com.appstechio.workyzo.fragments.addexperience;
import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.models.Job;

import com.appstechio.workyzo.models.User;
import com.appstechio.workyzo.activities.Chatmessages_Activity;
import com.appstechio.workyzo.activities.Freelancerprofile_Activity;
import com.appstechio.workyzo.databinding.JobsRcvBinding;
import com.appstechio.workyzo.network.ApiClient;
import com.appstechio.workyzo.network.ApiService;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.JobsViewHolder> {

    private ArrayList<Job> Jobs_lists;



    public JobsAdapter(ArrayList<Job> jobPosts) {
        this.Jobs_lists = jobPosts;
    }


    @NonNull
    @Override
    public JobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JobsViewHolder(
                JobsRcvBinding.inflate(LayoutInflater.from(parent.getContext()), parent
                        , false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull JobsViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        holder.setData(Jobs_lists.get(position));


        //Click on Card view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.jobsRcvBinding.getRoot().getContext(), JobDetails_Activity.class);
                Job jobPost = Jobs_lists.get(position);
                intent.putExtra(Constants.KEY_JOB_PAYMENT, holder.jobsRcvBinding.EmployerJobSalarypaid.getText().toString());
                intent.putExtra(Constants.KEY_JOB, jobPost);
                holder.jobsRcvBinding.getRoot().getContext().startActivity(intent);
            }
        });


        //CLICK ON ITEM MENU BUTTON

        holder.jobsRcvBinding.JobpostedMenuBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                PopupMenu popup = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    popup = new PopupMenu(holder.jobsRcvBinding.JobpostedMenuBtn.getContext(),
                            holder.jobsRcvBinding.JobpostedMenuBtn, Gravity.NO_GRAVITY, R.attr.actionOverflowMenuStyle, 0);
                }

//                TextView Noexp = (TextView) ((Activity)holder.jobsRcvBinding.getRoot().getContext()).findViewById(R.id.Noexperiencelabel);
                //  TextView ViewMore_Experience = (TextView) ((Activity)holder.jobsRcvBinding.getRoot().getContext()).findViewById(R.id.Viewmore_Experiences);
                // RecyclerView Experience_RCV = (RecyclerView) ((Activity)holder.jobsRcvBinding.getRoot().getContext()).findViewById(R.id.Experienceprofile_RCV);
                // FragmentContainerView Experience_frame = (FragmentContainerView) ((Activity)holder.jobsRcvBinding.getRoot().getContext()).findViewById(R.id.addexperienceframe);
                Activity activity = unwrap(view.getContext());
                ImageView no_data = (ImageView)activity.findViewById(R.id.Nodata_Image);
                RecyclerView Jobs_RCV = (RecyclerView) activity.findViewById(R.id.MyJobsPosted_RCV);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_edit:
                                //Constants.UPDATE_FLAG = true;
                                Intent intent = new Intent(holder.jobsRcvBinding.getRoot().getContext(), Postjob_Activity.class);
                                Job jobPost = Jobs_lists.get(position);
                                intent.putExtra(Constants.KEY_JOB_PAYMENT, holder.jobsRcvBinding.EmployerJobSalarypaid.getText().toString());
                                intent.putExtra(Constants.KEY_JOB, jobPost);
                                Constants.EDITPOST_FLAG = true;
                                Constants.step = 0;
                                holder.jobsRcvBinding.getRoot().getContext().startActivity(intent);

                                return true;

                            case R.id.action_delete:

                                AlertDialog.Builder builder = new AlertDialog.Builder(holder.jobsRcvBinding.getRoot().getContext());
                                builder.setTitle("Are you sure about this ?");


                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        PreferenceManager preferenceManager = new PreferenceManager(holder.jobsRcvBinding.getRoot().getContext());
                                        int Clicked_position =  holder.getBindingAdapterPosition();
                                       // Constants.Experience_Map =  Experience_list;



                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection(Constants.KEY_COLLECTION_JOBS).document(Jobs_lists.get(Clicked_position).getJob_ID())
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(holder.jobsRcvBinding.getRoot().getContext(), "Job is Deleted Successfully", Toast.LENGTH_LONG).show();
                                                        //Constants.Experience_Map.remove(Clicked_position);
                                                        Jobs_lists.remove(Clicked_position);
                                                        notifyItemRemoved(Clicked_position);
                                                        notifyItemRangeChanged(Clicked_position,Jobs_lists.size());

                                                        if (Jobs_lists.size() == 0 ){
                                                            no_data.setVisibility(View.VISIBLE);
                                                            Jobs_RCV.setVisibility(View.GONE);
                                                        }else {
                                                            JobsAdapter jobsAdapter;
                                                            jobsAdapter =  new JobsAdapter(Jobs_lists);
                                                            Jobs_RCV.setAdapter(jobsAdapter);
                                                            no_data.setVisibility(View.GONE);
                                                            Jobs_RCV.setVisibility(View.VISIBLE);
                                                        }

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                       // Log.w(TAG, "Error deleting document", e);
                                                    }
                                                });

                                        //Reload The RecyclerView Adapter


                                    }

                                });

                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                       dialogInterface.dismiss();
                                    }
                                });


                                AlertDialog ad = builder.create();
                                ad.show();

                                return true;

                            default:
                                return false;
                        }
                    }
                });

                /*  The below code in try catch is responsible to display icons*/
                try {
                    Field[] fields = popup.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popup);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // here you can inflate your menu
                popup.inflate(R.menu.recyclerview_menu);


                popup.show();
            }
        });
    }





    private static Activity unwrap(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        return (Activity) context;
    }

    public void filterList(ArrayList<Job> filteredlist) {
        Jobs_lists = filteredlist;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return Jobs_lists.size();
    }


    public static class JobsViewHolder extends RecyclerView.ViewHolder {

        private final JobsRcvBinding jobsRcvBinding;


        public JobsViewHolder(JobsRcvBinding jobsRcvBinding1) {
            super(jobsRcvBinding1.getRoot());
            this.jobsRcvBinding = jobsRcvBinding1;
        }

        void setData(Job jobPost) {



            jobsRcvBinding.EmployerJobTitle.setText(jobPost.getTitle());
            jobsRcvBinding.EmployerjobTasksdescription.setText(jobPost.getDescription());
            jobsRcvBinding.EmployerJobPoststarttime.setText(new StringBuilder().append("Posted ").append(jobPost.getCreatedDate()).toString());

            if (jobsRcvBinding.EmployerjobTasksdescription.length() > 80) {
                jobsRcvBinding.textView2.setVisibility(View.VISIBLE);
            } else {
                jobsRcvBinding.textView2.setVisibility(View.GONE);
            }

            jobsRcvBinding.textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    jobsRcvBinding.EmployerjobTasksdescription.setMaxLines(100);
                    jobsRcvBinding.textView2.setVisibility(View.GONE);
                }
            });

            ArrayList<String> skills = jobPost.getSkillsRequired();
            int i = 0;
            if (skills == null) {
                // jobsRcvBinding.employerjobRequiredSkills.setVisibility(View.GONE);
            } else {
                // jobsRcvBinding.employerjobRequiredSkills.setVisibility(View.VISIBLE);

                for (int a = 0; a < skills.size(); a++) {
                    Chip chip = new Chip(jobsRcvBinding.getRoot().getContext());
                    chip.setText(skills.get(a).toString());
                    chip.setSingleLine(true);
                    chip.setTextSize(14);
                    jobsRcvBinding.SkillsChipGroup.addView(chip);

                }


                Map<String, Object> payment = jobPost.getBudget();
                String Type = null;
                Object MinBudget = null;
                Object MaxBudget = null;

                Type = (String) payment.get(Constants.KEY_JOB_PAYMENT_TYPE);
                MinBudget =  payment.get(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET);
                MaxBudget = payment.get(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET);


                if (Type.equals("Fixed price")) {
                    jobsRcvBinding.EmployerJobSalarypaid.setText(new StringBuilder().append(MinBudget).append(" - ").append(MaxBudget).append(" USD").toString());
                } else {
                    jobsRcvBinding.EmployerJobSalarypaid.setText(new StringBuilder().append(MinBudget).append(" - ").append(MaxBudget).append(" USD per hour").toString());

                }

                if(jobPost.getProposals() != null && jobPost.getProposals().size() != 0){
                    jobsRcvBinding.employerjobBids.setText(new StringBuilder().append(jobPost.getProposals().size()).append(" Bids").toString());
                }else {
                    jobsRcvBinding.employerjobBids.setText(new StringBuilder().append("No Bids").toString());

                }

                if (Constants.MYJOBS_FLAG) {
                    jobsRcvBinding.HiredFreelancerLabel.setVisibility(View.VISIBLE);
                    jobsRcvBinding.hiredFreelancerTxt.setVisibility(View.VISIBLE);
                    if (Constants.HIREDJOB_FLAG) {
                        jobsRcvBinding.HiredFreelancerLabel.setVisibility(View.GONE);
                        jobsRcvBinding.hiredFreelancerTxt.setVisibility(View.GONE);
                        jobsRcvBinding.bidImage.setVisibility(View.GONE);
                        jobsRcvBinding.employerjobBids.setVisibility(View.GONE);
                        jobsRcvBinding.JobpostedMenuBtn.setVisibility(View.GONE);
                        jobsRcvBinding.CompleteSwitch.setVisibility(View.GONE);

                    } else {
                        jobsRcvBinding.bidImage.setVisibility(View.VISIBLE);
                        jobsRcvBinding.employerjobBids.setVisibility(View.VISIBLE);
                        jobsRcvBinding.HiredFreelancerLabel.setVisibility(View.VISIBLE);
                        jobsRcvBinding.hiredFreelancerTxt.setVisibility(View.VISIBLE);
                        jobsRcvBinding.CompleteSwitch.setVisibility(View.VISIBLE);
                        jobsRcvBinding.JobpostedMenuBtn.setVisibility(View.VISIBLE);

                        if (jobPost.getCompleted().equals(true)) {
                            jobsRcvBinding.CompleteSwitch.setChecked(true);
                            jobsRcvBinding.CompleteSwitch.setEnabled(false);
                            jobsRcvBinding.CompleteSwitch.setText("Completed");
                            jobsRcvBinding.JobpostedMenuBtn.setVisibility(View.GONE);

                        }else {
                            jobsRcvBinding.CompleteSwitch.setChecked(false);
                            jobsRcvBinding.CompleteSwitch.setEnabled(true);
                            jobsRcvBinding.CompleteSwitch.setText("Uncompleted");
                            jobsRcvBinding.JobpostedMenuBtn.setVisibility(View.VISIBLE);
                        }

                        jobsRcvBinding.CompleteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                                if (b) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(jobsRcvBinding.getRoot().getContext());
                                    builder.setTitle("Do you confirm that this job has been completed successfully?");


                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //jobsRcvBinding.CompleteSwitch.setTextOn("Yes"); // displayed text of the Switch whenever it is in checked or on state
                                            PreferenceManager preferenceManager = new PreferenceManager(jobsRcvBinding.getRoot().getContext());
                                            FirebaseFirestore database = FirebaseFirestore.getInstance();
                                            DocumentReference documentReference =
                                                    database.collection(Constants.KEY_COLLECTION_JOBS).document(jobPost.getJob_ID()
                                                    );


                                            documentReference.update(Constants.KEY_COMPLETED, true)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(jobsRcvBinding.getRoot().getContext(), "Job is Completed", Toast.LENGTH_LONG).show();
                                                            jobPost.setCompleted(true);

                                                            if (jobPost.getCompleted().equals(true)) {
                                                                jobsRcvBinding.CompleteSwitch.setEnabled(false);
                                                                jobsRcvBinding.CompleteSwitch.setText("Completed");
                                                                jobsRcvBinding.JobpostedMenuBtn.setVisibility(View.GONE);

                                                            } else {
                                                                jobsRcvBinding.CompleteSwitch.setEnabled(true);
                                                                jobsRcvBinding.CompleteSwitch.setText("Uncompleted");
                                                                jobsRcvBinding.JobpostedMenuBtn.setVisibility(View.VISIBLE);
                                                            }


                                                        }
                                                    })

                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(jobsRcvBinding.getRoot().getContext(), "Error", Toast.LENGTH_LONG).show();

                                                        }
                                                    });



                                        }




                                    });

                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            jobsRcvBinding.CompleteSwitch.setChecked(false);
                                        }

                                    });
                                    AlertDialog ad = builder.create();
                                    ad.show();

                                }else {
                                    jobsRcvBinding.CompleteSwitch.setText("Uncompleted"); // displayed text of the Switch whenever it is in unchecked i.e. off state
                                }
                            }
                        });





                        if (jobPost.getHired_Freelancer() == null || jobPost.getHired_Freelancer().equals("")) {
                            jobsRcvBinding.hiredFreelancerTxt.setText("None");
                            jobsRcvBinding.CompleteSwitch.setVisibility(View.GONE);
                        } else {
                            jobsRcvBinding.hiredFreelancerTxt.setText(jobPost.getHired_Freelancer());
                            jobsRcvBinding.CompleteSwitch.setVisibility(View.VISIBLE);
                        }
                    }


                } else {
                    jobsRcvBinding.HiredFreelancerLabel.setVisibility(View.GONE);
                    jobsRcvBinding.hiredFreelancerTxt.setVisibility(View.GONE);
                    jobsRcvBinding.JobpostedMenuBtn.setVisibility(View.GONE);
                    jobsRcvBinding.CompleteSwitch.setVisibility(View.GONE);

                }

            }
        }

        private Bitmap getUserImage(String encodedImage) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }


    }
}









