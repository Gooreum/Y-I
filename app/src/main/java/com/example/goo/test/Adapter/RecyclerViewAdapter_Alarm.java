package com.example.goo.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.goo.test.Activity.Home.Content_Detail_Activity;
import com.example.goo.test.Item.Item_Career;
import com.example.goo.test.Item.Item_alarm;
import com.example.goo.test.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Goo on 2018-05-27.
 */

public class RecyclerViewAdapter_Alarm extends RecyclerView.Adapter<RecyclerViewAdapter_Alarm.MyViewHolder> {

    private List<Item_alarm> listItems;
    private Context context;


    //어댑터 생성자
    public RecyclerViewAdapter_Alarm(Context context, List<Item_alarm> listItems) {
        this.context = context;
        this.listItems = listItems;

    }


    //뷰홀더에 대한 클래스를 만든다.
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        //유저이름
        private TextView username,username_project_start,username_project_recruit,username_project_completed,username_project_like,username_project_join,
                username_project_reply,username_project_re_reply,username_project_share,username_project_recruit_cancel;
        //메세지
        private TextView new_project_message,project_start_message,project_recruit_message,project_completed_message,project_like_message,project_join_message,
                project_reply_message,project_re_reply_message,project_share_message,project_recruit_message_cancel;
        //작성시간
        private TextView history_new_project,history_project_start,history_project_recruit,history_project_completed,history_project_like,history_project_join,
                history_project_reply,history_project_re_reply,history_project_share,history_project_recruit_cancel;
        //유저 프로필 이미지
        private CircleImageView user_profile,user_profile_project_start,user_profile_project_recruit,user_profile_project_completed,
                user_profile_project_like,user_profile_project_join,user_profile_project_reply,user_profile_project_re_reply,user_profile_project_share,user_profile_project_recruit_cancel;
        //해당 매세지를 담고 있는 relative레이아웃
        private RelativeLayout relative_new_project,relative_project_start,relative_project_recruit,relative_project_completed,
                relative_project_like,relative_project_join,relative_project_reply,relative_project_re_reply,relative_project_share,relative_project_recruit_cancel;

        public MyViewHolder(View itemView) {
            super(itemView);




            //어떤 xml 요소를 뷰홀더로 잡을지 지정해줌.
            mCardView = itemView.findViewById(R.id.mCardView);
            //relativelayout 생성
            relative_new_project = itemView.findViewById(R.id.relative_new_project);
            relative_project_start = itemView.findViewById(R.id.relative_project_start);
            relative_project_recruit = itemView.findViewById(R.id.relative_project_recruit);
            relative_project_completed = itemView.findViewById(R.id.relative_project_completed);
            relative_project_like = itemView.findViewById(R.id.relative_project_like);
            relative_project_join = itemView.findViewById(R.id.relative_project_join);
            relative_project_reply = itemView.findViewById(R.id.relative_project_reply);
            relative_project_re_reply = itemView.findViewById(R.id.relative_project_re_reply);
            relative_project_share = itemView.findViewById(R.id.relative_project_share);
            relative_project_recruit_cancel = itemView.findViewById(R.id.relative_project_recruit_cancel);


            //유저 프로필 이미지
            user_profile = itemView.findViewById(R.id.user_profile);
            user_profile_project_start = itemView.findViewById(R.id.user_profile_project_start);
            user_profile_project_recruit = itemView.findViewById(R.id.user_profile_project_recruit);
            user_profile_project_recruit_cancel = itemView.findViewById(R.id.user_profile_project_recruit_cancel);
            user_profile_project_completed = itemView.findViewById(R.id.user_profile_project_completed);
            user_profile_project_like = itemView.findViewById(R.id.user_profile_project_like);
            user_profile_project_join = itemView.findViewById(R.id.user_profile_project_join);
            user_profile_project_reply = itemView.findViewById(R.id.user_profile_project_reply);
            user_profile_project_re_reply = itemView.findViewById(R.id.user_profile_project_re_reply);
            user_profile_project_share = itemView.findViewById(R.id.user_profile_project_share);


            //메세지
            new_project_message = itemView.findViewById(R.id.new_project_message);
            project_start_message = itemView.findViewById(R.id.project_start_message);
            project_recruit_message = itemView.findViewById(R.id.project_recruit_message);
            project_recruit_message_cancel = itemView.findViewById(R.id.project_recruit_message_cancel);
            project_completed_message = itemView.findViewById(R.id.project_completed_message);
            project_like_message = itemView.findViewById(R.id.project_like_message);
            project_join_message = itemView.findViewById(R.id.project_join_message);
            project_reply_message = itemView.findViewById(R.id.project_reply_message);
            project_re_reply_message = itemView.findViewById(R.id.project_re_reply_message);
            project_share_message = itemView.findViewById(R.id.project_share_message);

            //유저이름
            username = itemView.findViewById(R.id.username);
            username_project_start = itemView.findViewById(R.id.username_project_start);
            username_project_recruit = itemView.findViewById(R.id.username_project_recruit);
            username_project_recruit_cancel = itemView.findViewById(R.id.username_project_recruit_cancel);
            username_project_completed = itemView.findViewById(R.id.username_project_completed);
            username_project_like = itemView.findViewById(R.id.username_project_like);
            username_project_join = itemView.findViewById(R.id.username_project_join);
            username_project_reply = itemView.findViewById(R.id.username_project_reply);
            username_project_re_reply = itemView.findViewById(R.id.username_project_re_reply);
            username_project_share = itemView.findViewById(R.id.username_project_share);

            //알림시간
            history_new_project = itemView.findViewById(R.id.history_new_project);
            history_project_start = itemView.findViewById(R.id.history_project_start);
            history_project_recruit = itemView.findViewById(R.id.history_project_recruit);
            history_project_recruit_cancel = itemView.findViewById(R.id.history_project_recruit_cancel);
            history_project_completed = itemView.findViewById(R.id.history_project_completed);
            history_project_like = itemView.findViewById(R.id.history_project_like);
            history_project_join = itemView.findViewById(R.id.history_project_join);
            history_project_reply = itemView.findViewById(R.id.history_project_reply);
            history_project_re_reply = itemView.findViewById(R.id.history_project_re_reply);
            history_project_share = itemView.findViewById(R.id.history_project_share);


        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_alarm, parent, false);



        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item_alarm item = listItems.get(position);
        //새로운 프로젝트 작성,추가모집 ,프로젝트 완료, 좋아요,신청하기,댓글,대댓글,공유하기 여부에 따라 relativelayout을 보이게 할지 안보이게 할지 정한다.

        //새로운 모집글이 있다면
        if (item.new_project == 1 && item.project_like == 0 && item.project_join == 0 && item.project_reply == 0 && item.project_re_reply == 0 &&item.cancel_more_recruiting == 0) {
            holder.relative_new_project.setVisibility(View.VISIBLE);
            holder.relative_project_start.setVisibility(View.GONE);
            holder.relative_project_recruit.setVisibility(View.GONE);
            holder.relative_project_recruit_cancel.setVisibility(View.GONE);
            holder.relative_project_completed.setVisibility(View.GONE);
            holder.relative_project_like.setVisibility(View.GONE);
            holder.relative_project_join.setVisibility(View.GONE);
            holder.relative_project_reply.setVisibility(View.GONE);
            holder.relative_project_re_reply.setVisibility(View.GONE);
            holder.relative_project_share.setVisibility(View.GONE);
            //유저이름 보이기
            holder.username.setText(item.username);
            //유저프로필 이미지 보이기
            Picasso.with(context).load(item.profile).error(R.mipmap.ic_launcher_round).into(holder.user_profile);
            //시간
            holder.history_new_project.setText(item.history);
            //친구가 새로운 모집글을 올렸다는 메세지를 누르게 되면 해당 게시글 상세보기 화면으로 넘어간다.
            holder.relative_new_project.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newProject(item.new_project_id,item.project_email);
                }
            });


        }//프로젝트를 시작했다면
        else if (item.project_start == 1 && item.project_recruiting == 0 && item.project_like == 0 && item.project_join == 0 && item.project_reply == 0 && item.project_re_reply == 0 &&item.cancel_more_recruiting == 0) {
            holder.relative_new_project.setVisibility(View.GONE);
            holder.relative_project_start.setVisibility(View.VISIBLE);
            holder.relative_project_recruit.setVisibility(View.GONE);
            holder.relative_project_recruit_cancel.setVisibility(View.GONE);
            holder.relative_project_completed.setVisibility(View.GONE);
            holder.relative_project_like.setVisibility(View.GONE);
            holder.relative_project_join.setVisibility(View.GONE);
            holder.relative_project_reply.setVisibility(View.GONE);
            holder.relative_project_re_reply.setVisibility(View.GONE);
            holder.relative_project_share.setVisibility(View.GONE);
            //유저이름 보이기
            holder.username_project_start.setText(item.username);
            //유저프로필 이미지 보이기
            Picasso.with(context).load(item.profile).error(R.mipmap.ic_launcher_round).into(holder.user_profile_project_start);
            //시간
            holder.history_project_start.setText(item.history);
            //친구가 프로젝트를 시작했다는 메세지를 누르게 되면 해당 게시글 상세보기 화면으로 넘어간다.
            holder.relative_project_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                startProject(item.project_start_id,item.project_email);
                }
            });
        }

        //프로젝트 추가모집중이라면
        else if (item.project_recruiting == 1 && item.project_start == 1 && item.project_like == 0 && item.project_join == 0 && item.project_reply == 0 && item.project_re_reply == 0 &&item.cancel_more_recruiting == 0) {
            holder.relative_new_project.setVisibility(View.GONE);
            holder.relative_project_start.setVisibility(View.GONE);
            holder.relative_project_recruit.setVisibility(View.VISIBLE);
            holder.relative_project_recruit_cancel.setVisibility(View.GONE);
            holder.relative_project_completed.setVisibility(View.GONE);
            holder.relative_project_like.setVisibility(View.GONE);
            holder.relative_project_join.setVisibility(View.GONE);
            holder.relative_project_reply.setVisibility(View.GONE);
            holder.relative_project_re_reply.setVisibility(View.GONE);
            holder.relative_project_share.setVisibility(View.GONE);
            //유저이름 보이기
            holder.username_project_recruit.setText(item.username);
            //유저프로필 이미지 보이기
            Picasso.with(context).load(item.profile).error(R.mipmap.ic_launcher_round).into(holder.user_profile_project_recruit);
            //시간
            holder.history_project_recruit.setText(item.history);
            //친구의 프로젝트를 추가모집하고 있다는 메세지를 누르게 되면 해당 게시글 상세보기 화면으로 넘어간다.
            holder.relative_project_recruit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                recruitingProject(item.project_recruiting_id,item.project_email);
                }
            });
        }
        //프로젝트 완료되었다면
        else if (item.project_completed == 1 && item.project_like == 0 && item.project_join == 0 && item.project_reply == 0 && item.project_re_reply == 0 &&item.cancel_more_recruiting == 0) {
            holder.relative_new_project.setVisibility(View.GONE);
            holder.relative_project_start.setVisibility(View.GONE);
            holder.relative_project_recruit.setVisibility(View.GONE);
            holder.relative_project_recruit_cancel.setVisibility(View.GONE);
            holder.relative_project_completed.setVisibility(View.VISIBLE);
            holder.relative_project_like.setVisibility(View.GONE);
            holder.relative_project_join.setVisibility(View.GONE);
            holder.relative_project_reply.setVisibility(View.GONE);
            holder.relative_project_re_reply.setVisibility(View.GONE);
            holder.relative_project_share.setVisibility(View.GONE);
            //유저이름 보이기
            holder.username_project_completed.setText(item.username);
            //유저프로필 이미지 보이기
            Picasso.with(context).load(item.profile).error(R.mipmap.ic_launcher_round).into(holder.user_profile_project_completed);
            //시간
            holder.history_project_completed.setText(item.history);

            //친구가 프로젝트를 완료했다는 메세지를 누르게 되면 해당 프로젝트 완료 상세보기 화면으로 넘어간다.
            holder.relative_project_completed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    completedProject(item.project_completed_id,item.project_email,item.project_url);
                }
            });
        }
        //좋아요를 눌렀다면
        else if (item.project_like == 1) {
            holder.relative_new_project.setVisibility(View.GONE);
            holder.relative_project_start.setVisibility(View.GONE);
            holder.relative_project_recruit.setVisibility(View.GONE);
            holder.relative_project_recruit_cancel.setVisibility(View.GONE);
            holder.relative_project_completed.setVisibility(View.GONE);
            holder.relative_project_like.setVisibility(View.VISIBLE);
            holder.relative_project_join.setVisibility(View.GONE);
            holder.relative_project_reply.setVisibility(View.GONE);
            holder.relative_project_re_reply.setVisibility(View.GONE);
            holder.relative_project_share.setVisibility(View.GONE);
            //유저이름 보이기
            holder.username_project_like.setText(item.username);
            //유저프로필 이미지 보이기
            Picasso.with(context).load(item.profile).error(R.mipmap.ic_launcher_round).into(holder.user_profile_project_like);
            //유저가 누구의 게시글에 좋아요를 눌렀습니다.
            holder.project_like_message.setText("님이 " + item.project_like_writer + "님의 게시글을 추천하였습니다.");
            //시간
            holder.history_project_like.setText(item.history);
            //친구의 추천 하였다는 메세지를 누르면 해당 게시글 상세보기 화면으로 넘어간다.
            holder.relative_project_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeProject(item.project_like_id,item.project_email,item.project_recruiting,item.project_start,item.project_completed);
                }
            });
        }


        //신청하기를 눌렀다면
        else if (item.project_join == 1) {
            holder.relative_new_project.setVisibility(View.GONE);
            holder.relative_project_start.setVisibility(View.GONE);
            holder.relative_project_recruit.setVisibility(View.GONE);
            holder.relative_project_recruit_cancel.setVisibility(View.GONE);
            holder.relative_project_completed.setVisibility(View.GONE);
            holder.relative_project_like.setVisibility(View.GONE);
            holder.relative_project_join.setVisibility(View.VISIBLE);
            holder.relative_project_reply.setVisibility(View.GONE);
            holder.relative_project_re_reply.setVisibility(View.GONE);
            holder.relative_project_share.setVisibility(View.GONE);
            //유저이름 보이기
            holder.username_project_join.setText(item.username);
            //유저프로필 이미지 보이기
            Picasso.with(context).load(item.profile).error(R.mipmap.ic_launcher_round).into(holder.user_profile_project_join);
            //유저가 누구의 게시글에 좋아요를 눌렀습니다.
            holder.project_join_message.setText("님이 " + item.project_join_writer + "님의 프로젝트에 참여하기를 원합니다.");
            //시간
            holder.history_project_join.setText(item.history);
            //친구의 프로젝트를 신청했다는 메세지를 누르게 되면 해당 게시글 상세보기 화면으로 넘어간다.
            holder.relative_project_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    joinProject(item.project_join_id,item.project_email,item.project_recruiting,item.project_start,item.project_completed);
                }
            });
        }

        //댓글을 달았다면
        else if (item.project_reply == 1) {
            holder.relative_new_project.setVisibility(View.GONE);
            holder.relative_project_start.setVisibility(View.GONE);
            holder.relative_project_recruit.setVisibility(View.GONE);
            holder.relative_project_recruit_cancel.setVisibility(View.GONE);
            holder.relative_project_completed.setVisibility(View.GONE);
            holder.relative_project_like.setVisibility(View.GONE);
            holder.relative_project_join.setVisibility(View.GONE);
            holder.relative_project_reply.setVisibility(View.VISIBLE);
            holder.relative_project_re_reply.setVisibility(View.GONE);
            holder.relative_project_share.setVisibility(View.GONE);
            //유저이름 보이기
            holder.username_project_reply.setText(item.username);
            //유저프로필 이미지 보이기
            Picasso.with(context).load(item.profile).error(R.mipmap.ic_launcher_round).into(holder.user_profile_project_reply);
            //시간
            holder.history_project_reply.setText(item.history);

            //댓글 메세지를 누르게 되면 해당 게시글 상세보기 화면으로 넘어가고 댓글 화면을 추가적으로 띄워준다.
            holder.relative_project_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    replyProject(item.project_reply_id,item.project_email,item.project_recruiting,item.project_start,item.project_completed);
                }
            });
        }
        //대-댓글을 달았다면
        else if (item.project_re_reply == 1) {
            holder.relative_new_project.setVisibility(View.GONE);
            holder.relative_project_start.setVisibility(View.GONE);
            holder.relative_project_recruit.setVisibility(View.GONE);
            holder.relative_project_recruit_cancel.setVisibility(View.GONE);
            holder.relative_project_completed.setVisibility(View.GONE);
            holder.relative_project_like.setVisibility(View.GONE);
            holder.relative_project_join.setVisibility(View.GONE);
            holder.relative_project_reply.setVisibility(View.GONE);
            holder.relative_project_re_reply.setVisibility(View.VISIBLE);
            holder.relative_project_share.setVisibility(View.GONE);
            //유저이름 보이기
            holder.username_project_re_reply.setText(item.username);
            //유저프로필 이미지 보이기
            Picasso.with(context).load(item.profile).error(R.mipmap.ic_launcher_round).into(holder.user_profile_project_re_reply);
            //시간
            holder.history_project_re_reply.setText(item.history);

            //대댓글을 누르면 해당 게시글 상세보기로 넘어가고 댓글 화면을 띄워준다.
            holder.relative_project_re_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    re_replyProject(item.project_re_reply_id,item.project_email,item.project_recruiting,item.project_start,item.project_completed);
                }
            });
        }
        //프로젝트를 공유하였다면
        else if ( item.project_share == 1) {
            holder.relative_new_project.setVisibility(View.GONE);
            holder.relative_project_start.setVisibility(View.GONE);
            holder.relative_project_recruit.setVisibility(View.GONE);
            holder.relative_project_recruit_cancel.setVisibility(View.GONE);
            holder.relative_project_completed.setVisibility(View.GONE);
            holder.relative_project_like.setVisibility(View.GONE);
            holder.relative_project_join.setVisibility(View.GONE);
            holder.relative_project_reply.setVisibility(View.GONE);
            holder.relative_project_re_reply.setVisibility(View.GONE);
            holder.relative_project_share.setVisibility(View.VISIBLE);
            //유저이름 보이기
            holder.username_project_share.setText(item.username);
            //유저프로필 이미지 보이기
            Picasso.with(context).load(item.profile).error(R.mipmap.ic_launcher_round).into(holder.user_profile_project_share);
            //유저가 누구의 게시글을 공유하였습니다. 눌렀습니다.
            holder.project_join_message.setText("님이 " + item.project_share_writer + "님의 프로젝트를 공유하였습니다.");
            //시간
            holder.history_project_share.setText(item.history);
            //공유하였다는 메세지를 누르게 되면 해당 게시글 상세보기 화면으로 넘어간다.
            holder.relative_project_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareProject(item.project_share_id,item.project_email,item.project_recruiting,item.project_start,item.project_completed);
                }
            });
        }
        //프로젝트 추가모집을 취소했다면
        else if(item.cancel_more_recruiting == 1 && item.project_start == 1){
            holder.relative_new_project.setVisibility(View.GONE);
            holder.relative_project_start.setVisibility(View.GONE);
            holder.relative_project_recruit.setVisibility(View.GONE);
            holder.relative_project_recruit_cancel.setVisibility(View.VISIBLE);
            holder.relative_project_completed.setVisibility(View.GONE);
            holder.relative_project_like.setVisibility(View.GONE);
            holder.relative_project_join.setVisibility(View.GONE);
            holder.relative_project_reply.setVisibility(View.GONE);
            holder.relative_project_re_reply.setVisibility(View.GONE);
            holder.relative_project_share.setVisibility(View.GONE);

            //유저이름 보이기
            holder.username_project_recruit_cancel.setText(item.username);
            //유저프로필 이미지 보이기
            Picasso.with(context).load(item.profile).error(R.mipmap.ic_launcher_round).into(holder.user_profile_project_recruit_cancel);
            //유저가 누구의 게시글을 공유하였습니다. 눌렀습니다.
            holder.project_join_message.setText("님이 프로젝트 추가모집을 취소하였습니다.");
            //시간
            holder.history_project_recruit_cancel.setText(item.history);
            //공유하였다는 메세지를 누르게 되면 해당 게시글 상세보기 화면으로 넘어간다.
            holder.relative_project_recruit_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelRecruit(item.project_start_id,item.project_email,item.project_recruiting,item.project_start,item.project_completed);
                }
            });
        }
    }
    @Override
    public int getItemCount() {

        return listItems.size();

    }

    //새로운 모집글 화면으로 넘어가기
    public void newProject(int project_id,String project_email){
        Intent intent = new Intent(context, Content_Detail_Activity.class);
        intent.putExtra("project_id",project_id);
        intent.putExtra("project_email",project_email);
        context.startActivity(intent);
    }

    //시작된 프로젝트 화면으로 넘어가기
    public void startProject(int project_id,String project_email){
        Intent intent = new Intent(context, Content_Detail_Activity.class);
        intent.putExtra("project_id",project_id);
        intent.putExtra("project_email",project_email);
        intent.putExtra("project_developing",1);
        context.startActivity(intent);
    }
    //추가모집중인 모집글 화면으로 넘어가기
    public void recruitingProject(int project_id,String project_email){
        Intent intent = new Intent(context, Content_Detail_Activity.class);
        intent.putExtra("project_id",project_id);
        intent.putExtra("project_email",project_email);
        intent.putExtra("project_recruting",1);
        intent.putExtra("project_developing",1);
        context.startActivity(intent);
    }

    //완료된 모집글 화면으로 넘어가기
    public void completedProject(int project_id,String project_email,String project_url){
        Intent intent = new Intent(context, Content_Detail_Activity.class);
        intent.putExtra("project_id",project_id);
        intent.putExtra("project_email",project_email);
        intent.putExtra("project_completed",1);
        intent.putExtra("project_url","https://github.com/tensorflow/tensorflow");
        context.startActivity(intent);
    }

    //추천누른 모집글 화면으로 넘어가기
    public void likeProject(int project_id,String project_email,int project_recruiting, int project_developing,int project_completed){
        Intent intent = new Intent(context, Content_Detail_Activity.class);
        intent.putExtra("project_id",project_id);
        intent.putExtra("project_email",project_email);
        intent.putExtra("project_developing",project_developing);
        intent.putExtra("project_recruiting",project_recruiting);
        intent.putExtra("project_completed",project_completed);
        if(project_recruiting == 1 && project_developing == 1){
            intent.putExtra("more_recruit_member",3);
        }
        System.out.println("왜 안넘어오냐고...recruiting : " + project_recruiting);
        System.out.println("왜 안넘어오냐고...developing : " + project_developing);
        System.out.println("왜 안넘어오냐고...completed :" + project_completed);
        context.startActivity(intent);
    }
    //신청하기 모집글 화면으로 넘어가기
    public void joinProject(int project_id,String project_email,int project_recruiting, int project_developing,int project_completed){
        Intent intent = new Intent(context, Content_Detail_Activity.class);
        intent.putExtra("project_id",project_id);
        intent.putExtra("project_email",project_email);
        intent.putExtra("alarm_join",true);
        intent.putExtra("project_developing",project_developing);
        intent.putExtra("project_recruiting",project_recruiting);
        intent.putExtra("project_completed",project_completed);
        if(project_recruiting == 1 && project_developing == 1){
            intent.putExtra("more_recruit_member",3);
        }
        System.out.println("왜 안넘어오냐고...recruiting : " + project_recruiting);
        System.out.println("왜 안넘어오냐고...developing : " + project_developing);
        System.out.println("왜 안넘어오냐고...completed :" + project_completed);
        context.startActivity(intent);
    }

    //댓글을 작성한 모집글 화면으로 넘어가기
    public void replyProject(int project_id,String project_email,int project_recruiting, int project_developing,int project_completed){
        Intent intent = new Intent(context, Content_Detail_Activity.class);
        intent.putExtra("project_id",project_id);
        intent.putExtra("project_email",project_email);
        intent.putExtra("alarm_reply",true);
        intent.putExtra("project_developing",project_developing);
        intent.putExtra("project_recruiting",project_recruiting);
        intent.putExtra("project_completed",project_completed);
        if(project_recruiting == 1 && project_developing == 1){
            intent.putExtra("more_recruit_member",3);
        }
        context.startActivity(intent);
    }

    //대댓글 작성한 모집글 화면으로 넘어가기
    public void re_replyProject(int project_id,String project_email,int project_recruiting, int project_developing,int project_completed){
        Intent intent = new Intent(context, Content_Detail_Activity.class);
        intent.putExtra("project_id",project_id);
        intent.putExtra("project_email",project_email);
        intent.putExtra("alarm_re_reply",true);
        intent.putExtra("project_developing",project_developing);
        intent.putExtra("project_recruiting",project_recruiting);
        intent.putExtra("project_completed",project_completed);
        if(project_recruiting == 1 && project_developing == 1){
            intent.putExtra("more_recruit_member",3);
        }
        context.startActivity(intent);
    }

    //공유하기한 게시글 화면으로 넘어가기
    public void shareProject(int project_id,String project_email,int project_recruiting, int project_developing,int project_completed){
        Intent intent = new Intent(context, Content_Detail_Activity.class);
        intent.putExtra("project_id",project_id);
        intent.putExtra("project_email",project_email);
        intent.putExtra("project_developing",project_developing);
        intent.putExtra("project_recruiting",project_recruiting);
        intent.putExtra("project_completed",project_completed);
        if(project_recruiting == 1 && project_developing == 1){
            intent.putExtra("more_recruit_member",3);
        }
        context.startActivity(intent);
    }
    //공유하기한 게시글 화면으로 넘어가기
    public void cancelRecruit(int project_id,String project_email,int project_recruiting, int project_developing,int project_completed){
        Intent intent = new Intent(context, Content_Detail_Activity.class);
        intent.putExtra("project_id",project_id);
        intent.putExtra("project_email",project_email);
        intent.putExtra("project_developing",project_developing);
        intent.putExtra("project_recruiting",project_recruiting);
        intent.putExtra("project_completed",project_completed);

        context.startActivity(intent);
    }

}
