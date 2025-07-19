package com.yusufwdn.cryptowatcher.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yusufwdn.cryptowatcher.R;
import com.yusufwdn.cryptowatcher.model.Coin;

import java.text.DecimalFormat;
import java.util.List;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinViewHolder> {
    private List<Coin> coinList;
    private Context context;
    private OnItemClickListener listener;

    public CoinAdapter(List<Coin> coinList, Context context) {
        this.coinList = coinList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(String coinId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Method ini dipanggil saat RecyclerView perlu membuat ViewHolder baru
    // ViewHolder adalah object yang "memegang" satu item tampilan (satu baris koin)
    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Membuat view baru dari layout item_coin.xml
        View view = LayoutInflater.from(context).inflate(R.layout.item_coin, parent, false);
        return new CoinViewHolder(view);
    }

    // Method ini dipanggil untuk menampilkan data pada ViewHolder tertentu.
    @Override
    public void onBindViewHolder(@NonNull CoinViewHolder holder, int position) {
        // Mengambil data koin pada posisi tertentu
        Coin coin = coinList.get(position);
        // Memanggil method di ViewHolder untuk mengisi data ke tampilan
        holder.bind(coin);
    }

    // Method ini mengembalikan jumlah total item di dalam list.
    @Override
    public int getItemCount() {
        return coinList.size();
    }

    // Method untuk mengupdate data di adapter dan memberi tahu RecyclerView untuk refresh
    public void filterList(List<Coin> newCoinList) {
        this.coinList.clear();
        this.coinList.addAll(newCoinList);
        notifyDataSetChanged();
    }

    class CoinViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCoinIcon;
        TextView tvCoinName, tvCoinSymbol, tvCoinPrice, tvCoinPriceChange;

        public CoinViewHolder(@NonNull View itemView) {
            super(itemView);

            // Menghubungkan variabel ID view dari layout
            ivCoinIcon = itemView.findViewById(R.id.ivCoinIcon);
            tvCoinName = itemView.findViewById(R.id.tvCoinName);
            tvCoinSymbol = itemView.findViewById(R.id.tvCoinSymbol);
            tvCoinPrice = itemView.findViewById(R.id.tvCoinPrice);
            tvCoinPriceChange = itemView.findViewById(R.id.tvCoinPriceChange);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();

                // Cek dulu position yang didapatkan itu valid atau tidak
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(coinList.get(position).getId());
                }
            });
        }

        void bind(Coin coin) {
            tvCoinName.setText(coin.getName());
            tvCoinSymbol.setText(coin.getSymbol());

            // Set format harga
            DecimalFormat df = new DecimalFormat("$#,##0.00");
            tvCoinPrice.setText(df.format(coin.getCurrentPrice()));

            // Set format persentase dan warna
            DecimalFormat p_df = new DecimalFormat("0.00'%'");
            String priceChangeString = p_df.format(coin.getPriceChangePercentage24h());
            tvCoinPriceChange.setText(priceChangeString);

            // Jika minus, warna merah dan sebaliknya hijau
            if (coin.getPriceChangePercentage24h() < 0) {
                tvCoinPriceChange.setTextColor(Color.RED);
            } else {
                tvCoinPriceChange.setTextColor(Color.GREEN);
            }

            // Menggunakan Glide untuk memuat gambar dari URL ke ImageView
            Glide.with(context).load(coin.getImageUrl()).into(ivCoinIcon);
        }
    }
}
