package pro.valentinlacour.actuandroid;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    public JSONArray jsonArray;
    private LayoutInflater mInflater;

    public ArticleAdapter(Context context, JSONArray jsonArray) {
        mInflater = LayoutInflater.from(context);
        this.jsonArray = jsonArray;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Inflate an item view.
        View mItemView = mInflater.inflate(R.layout.article_list_item, parent, false);
        return new ArticleViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        try {
            JSONObject jsonObject = (JSONObject) jsonArray.get(position);
            TextView title = holder.articleListItem.findViewById(R.id.articleListItemTitle);
            TextView category = holder.articleListItem.findViewById(R.id.articleListItemDescription);
            title.setText(jsonObject.getString("title"));
            category.setText(jsonObject.getString("category"));
            holder.articleListItem.setTag( jsonObject);
        } catch (JSONException e) {

        }

    }

    @Override
    public int getItemCount() {
       return jsonArray.length();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ArticleAdapter mAdapter;
        private ConstraintLayout articleListItem;

        public ArticleViewHolder(View itemView, ArticleAdapter adapter) {
            super(itemView);
            articleListItem = (ConstraintLayout) itemView.findViewById(R.id.articleListItem);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ArticleActivity.class);
            JSONObject jsonObject = (JSONObject) v.getTag();
            try {
                intent.putExtra("id", jsonObject.getString("id"));
            } catch (JSONException e) {

            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);
        }

    }

}
