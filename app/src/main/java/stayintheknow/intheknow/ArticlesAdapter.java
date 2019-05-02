package stayintheknow.intheknow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder>{

    private Context context;
    private List<Article> articles;

    public ArticlesAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creates row in our recycler view
        View view = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Binds data at given position into the view holder
        Article article = articles.get(position);
        holder.bind(article);
    }

    @Override
    public int getItemCount() {
        // Returns how many items are in our data set
        return articles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvDescription;
        private TextView tvAuthor;
        private ImageView ivImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            ivImage = itemView.findViewById(R.id.ivImage);
        }

        public void bind(Article article) {
            tvTitle.setText(article.getTitle());
            tvDescription.setText(article.getDescription());
            tvAuthor.setText(article.getAuthor().getName());
            ParseFile image = article.getImage();
            if(image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
        }
    }
}
