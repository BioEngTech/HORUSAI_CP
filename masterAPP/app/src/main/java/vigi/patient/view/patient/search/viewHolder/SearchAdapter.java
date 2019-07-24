package vigi.patient.view.patient.search.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vigi.patient.R;
import vigi.patient.model.entities.CareProvider;
import vigi.patient.view.utils.recyclerView.EmptyRecyclerView;

public class SearchAdapter extends EmptyRecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {
    private List<CareProvider> list;
    private List<CareProvider> fullList;

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView name;
        TextView field;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cp_image);
            name = itemView.findViewById(R.id.cp_name);
            field = itemView.findViewById(R.id.cp_field);
        }
    }

    public SearchAdapter(List<CareProvider> list) {
        this.list = list;
        this.fullList = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_toolbar_search,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CareProvider currentCP = list.get(position);
        // holder.image.setImageDrawable(currentCP.getImage()); TODO set cp image
        //holder.field.setText(currentCP.getField); TODO add field parameter to Care Provider
        holder.name.setText(currentCP.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CareProvider> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CareProvider item : fullList) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            if (results.values != null){
                list.addAll((List) results.values);
            }
            notifyDataSetChanged();
        }
    };
}

