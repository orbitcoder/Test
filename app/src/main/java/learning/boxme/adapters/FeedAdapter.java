package learning.boxme.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import learning.boxme.DetailActivity;
import learning.boxme.R;
import learning.boxme.pojo.UserEvent;
import learning.boxme.utils.JsonUtils;

/**
 * Created by bhupendrarawat on 18/12/16.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {


    List<UserEvent> userEventData;
    Activity context;


    public FeedAdapter( Activity con) {

        context = con;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserEvent userEvent = userEventData.get(position);
        holder.name.setText(userEvent.getActor().getLogin());
        holder.content.setText("type: " + userEvent.getType());
        holder.repo.setText("repo: " + userEvent.getRepo().getName());
        String imageUrl = userEvent.getActor().getAvatarUrl();
        Log.i("TEST", "" + JsonUtils.jsonify(userEvent.getActor()));

        Picasso.with(context).load(imageUrl).into(holder.user_image);

    }

    @Override
    public int getItemCount() {
        return userEventData.size();
    }

    public void setData(List<UserEvent> userEvents) {
        userEventData =  null;

        userEventData=userEvents;
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView user_image;
        TextView name, content, repo;


        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.uname);
            content = (TextView) itemView.findViewById(R.id.content_text);
            repo = (TextView) itemView.findViewById(R.id.repo);
            user_image = (ImageView) itemView.findViewById(R.id.userimage);


        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, DetailActivity.class);
            UserEvent userEvent = userEventData.get(getAdapterPosition());
            intent.putExtra("REPO", userEvent.getRepo().getUrl());
            intent.putExtra("NAME", userEvent.getRepo().getName());
            intent.putExtra("Image", userEvent.getActor().getAvatarUrl());
            Pair<View, String> p1 = Pair.create((View) repo, "repo");
            Pair<View, String> p2 = Pair.create((View) user_image, "userImage");
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(context, p1, p2);
            context.startActivity(intent, options.toBundle());

        }
    }
}
