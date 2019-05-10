//package stayintheknow.intheknow;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import java.util.List;
//
//public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
//
//    private Context context;
//    private List<Articles> nytarticles;
//
//    public Adapter(Context context, List<Articles> articles) {
//        this.context = context;
//        this.nytarticles = articles;
//    }
//
//    @NonNull
//    @Override
//    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Articles article = nytarticles.get(position);
//        holder.bind(article);
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return nytarticles.size();
//    }
//
//    class ViewHolder extends RecyclerView.ViewHolder {
//
//        private TextView tvTitle;
//        private TextView tvDescription;
//        private TextView tvAuthor;
//        private ImageView ivImage;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            tvTitle = itemView.findViewById(R.id.tvTitle);
//            tvDescription = itemView.findViewById(R.id.tvDescription);
//            tvAuthor = itemView.findViewById(R.id.tvAuthor);
//            ivImage = itemView.findViewById(R.id.ivImage);
//        }
//
//        public void bind(Articles article) {
//            tvTitle.setText(article.getTitle());
//            tvDescription.setText(article.getDescription());
//            tvAuthor.setText(article.getAuhor());
//            Glide.with(context).load(article.getPicture_url()).into(ivImage);
//
//        }
//    }
//}
