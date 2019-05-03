//package site.team2dev.nemubengkel;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.List;
//
//public class BengkelAdapter extends RecyclerView.Adapter<BengkelAdapter.ViewHolder> {
//
//    private Context context;
//    private List<Bengkel> list;
//
//    public BengkelAdapter(Context context, List<Bengkel> list){
//        this.context=context;
//        this.list=list;
//    }
//
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view= LayoutInflater.from(context).inflate(R.layout.list_bengkel_item, viewGroup, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
//        Bengkel bengkel=list.get(position);
//
//        viewHolder.textNamaBengkel.setText(bengkel.getNamaBengkel());
//        viewHolder.textKategori.setText(""+bengkel.getKategori());
//        viewHolder.textApprove.setText(""+bengkel.getApprove());
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView textNamaBengkel, textKategori, textApprove;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            textNamaBengkel=itemView.findViewById(R.id.main_nama);
//            textKategori=itemView.findViewById(R.id.main_kategori);
//            textApprove=itemView.findViewById(R.id.main_approve);
//        }
//    }
//}
